<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue';
import DataTable from 'datatables.net-vue3';
import DataTablesCore from 'datatables.net-dt';
import Responsive from 'datatables.net-responsive-dt';
import Buttons from 'datatables.net-buttons-dt';
import Swal from 'sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';

import 'datatables.net-dt/css/dataTables.dataTables.min.css';
import 'datatables.net-responsive-dt/css/responsive.dataTables.min.css';
import ButtonAdd from './buttonAdd.vue';

const backUrl = import.meta.env.VITE_BACKEND_URL;
const emit = defineEmits(['open-epi-modal', 'open-edit-epi-modal']);

DataTable.use(DataTablesCore);
DataTable.use(Responsive);
DataTable.use(Buttons);

const globalSearch = ref('');
const dtRef = ref(null);
let tableInstance = null; // referência ao dataTables instance (jQuery DataTable)

// colunas: note que agora data-id usa row.epiId || row.id
const columns = [
  {
    data: 'codigoAutenticacao',
    title: 'CA',
    className: 'text-center',
    render: (data) => data || "Não Informado"
  },
  {
    data: 'descricao',
    title: 'Descrição',
    className: 'text-center'
  },
  {
    data: 'codigoCompra',
    title: 'Cod. Compra',
    className: 'text-center',
    render: (data) => data == 0 ? "Não Informado" : data
  },
  {
    data: 'dataValidade',
    title: 'Validade',
    className: 'text-center',
    render: (data) => data == null ? "Não Informado" : data
  },
  {
    data: null,
    title: 'Ações',
    orderable: false,
    searchable: false,
    className: 'text-center',
    render: (_d, _t, row) => {
      // usa epiId se existir, senão id
      const dataId = row.epiId ?? row.id ?? '';
      const editBtn = `
        <button
          data-action="edit"
          data-id="${dataId}"
          class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-3 rounded text-xs transition-colors duration-200"
          title="Editar">
          Editar
        </button>`;

      const deleteBtn = `
        <button
          data-action="delete"
          data-id="${dataId}"
          class="bg-red-500 hover:bg-red-700 text-white font-bold py-1 px-3 rounded text-xs transition-colors duration-200"
          title="Excluir">
          Excluir
        </button>`;

      return `<div class="flex justify-center gap-2">${editBtn} ${deleteBtn}</div>`;
    }
  }
];

const options = {
  responsive: true,
  searching: true,
  paging: true,
  info: true,
  lengthChange: true,
  pageLength: 10,
  dom: '<"datatable-header"l>rt<"datatable-footer"ip>',
  language: {
    lengthMenu: "Mostrar _MENU_ entradas",
    zeroRecords: "Nenhum EPI encontrado.",
    info: "Mostrando de _START_ até _END_ de _TOTAL_ registros",
    infoEmpty: "Mostrando 0 registros",
    infoFiltered: "(filtrado de _MAX_ registros)",
    paginate: { first: "Primeiro", last: "Último", next: "Próximo", previous: "Anterior" },
    emptyTable: "Nenhum Epi Cadastrado"
  },
};

const epis = ref([]);

// handler de editar
const handleEdit = (epi) => {
  console.log('EDITAR EPI:', epi);
  emit('open-edit-epi-modal', epi);
};

// handler de excluir (já com SweetAlert)
const handleDelete = (epi) => {
  Swal.fire({
    title: 'Você tem certeza?',
    text: `Deseja realmente excluir o EPI "${epi.descricao}" (CA: ${epi.codigoCompra})?`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#d33',
    cancelButtonColor: '#3085d6',
    confirmButtonText: 'Sim, excluir!',
    cancelButtonText: 'Cancelar'
  }).then(async (result) => {
    if (result.isConfirmed) {
      try {
        const response = await fetch(`${backUrl}/api/epis/${epi.id ?? epi.epiId}`, {
          method: 'DELETE',
        });

        if (!response.ok) {
          throw new Error('Falha ao excluir EPI');
        }

        // remove da lista local
        epis.value = epis.value.filter(e => {
          // compara ambos os campos possíveis
          const a = e.id?.toString?.() ?? e.id;
          const b = e.epiId?.toString?.() ?? e.epiId;
          const target = epi.id?.toString?.() ?? epi.epiId?.toString?.();
          return !(a === target || b === target);
        });

        Swal.fire('Excluído!', 'O EPI foi excluído com sucesso.', 'success');

      } catch (error) {
        console.error('Erro ao excluir EPI:', error);
        Swal.fire('Erro!', 'Não foi possível excluir o EPI. Tente novamente.', 'error');
      }
    }
  });
};

onMounted(async () => {
  try {
    console.log("URL do backend:", `${backUrl}/api/epis/`);
    const response = await fetch(`${backUrl}/api/epis/`);
    if (!response.ok) throw new Error('Erro ao buscar EPI');
    const data = await response.json();
    epis.value = data;

    if (data.length > 0) {
      console.log('Dados do primeiro EPI (da API):', data[0]);
    }

    await nextTick();

    // se quiser aplicar busca inicial
    if (dtRef.value && globalSearch.value) dtRef.value.dt.search(globalSearch.value).draw();

    // pega instância do DataTable (é um objeto jQuery DataTable)
    // dtRef.value.dt é a instância exposta pelo datatables.net-vue3
    tableInstance = dtRef.value?.dt;

    // se existir, registra listener direto no DataTables (delegation)
    if (tableInstance && typeof tableInstance.on === 'function') {
      // use function() para que "this" seja o elemento clicado (ou podemos usar event.currentTarget)
      tableInstance.on('click', 'button[data-action]', function (e) {
        // this é o botão que foi clicado
        const btn = this;
        const action = btn.getAttribute('data-action');
        const id = btn.getAttribute('data-id');

        if (!id) {
          console.warn('botão com data-id vazio');
          return;
        }

        // procura pelo EPI: compara com epiId ou id (string safe)
        const epi = epis.value.find(ep => {
          const epId = ep.epiId?.toString?.();
          const normalId = ep.id?.toString?.();
          return (epId === id) || (normalId === id);
        });

        if (!epi) {
          console.warn('EPI não encontrado para id', id);
          return;
        }

        if (action === 'edit') {
          handleEdit(epi);
        } else if (action === 'delete') {
          handleDelete(epi);
        }
      });
    } else {
      console.warn('Não foi possível recuperar tableInstance para adicionar o listener.');
    }

  } catch (error) {
    console.error(error);
  }
});

onUnmounted(() => {
  if (tableInstance && typeof tableInstance.off === 'function') {
    tableInstance.off('click', 'button[data-action]');
  }
});

const applySearch = () => {
  if (dtRef.value) dtRef.value.dt.search(globalSearch.value).draw();
}
</script>

<template>
  <div>
    <header class="bg-gray-800 text-white p-4 flex items-center space-x-4">
      <div class="relative w-full">
        <input type="text" placeholder="Procurar por CA ou Descrição..."
          class="bg-gray-700 text-white rounded-lg py-2 pl-10 pr-4 focus:outline-none focus:ring-2 focus:ring-blue-500 w-full"
          v-model="globalSearch" @input="applySearch" />
        <svg class="w-5 h-5 text-gray-400 absolute left-3 top-1/2 transform -translate-y-1/2" fill="none"
          stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
            d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
        </svg>
      </div>
      <ButtonAdd @add-clicked="$emit('open-epi-modal')" />
    </header>

    <main class="p-4 sm:p-8">
      <div class="overflow-x-auto relative shadow-md sm:rounded-lg bg-white">
        <!-- sem @click aqui -->
        <DataTable :columns="columns" :data="epis" :options="options" ref="dtRef" class="w-full text-sm text-gray-700">
        </DataTable>
      </div>
    </main>
  </div>
</template>

<style scoped>
:deep(.dataTables_wrapper) {
  width: 100%;
  padding: 0 !important;
}

/* (mantive o seu CSS original — não alterei visual) */

:deep(.dataTables_filter) {
  display: none;
}

:deep(.datatable-header),
:deep(.datatable-footer) {
  padding: 1rem;
  border-color: rgb(229, 231, 235);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

:deep(table.dataTable thead th),
:deep(table.dataTable tbody td) {
  padding: 0.75rem 1rem !important;
  text-align: center !important;
  vertical-align: middle !important;
  font-weight: 500 !important;
  color: rgb(55, 65, 81) !important;
}

:deep(table.dataTable thead th) {
  border-bottom: 2px solid rgb(229, 231, 235) !important;
  background-color: rgb(243, 244, 246) !important;
}

:deep(table.dataTable tbody tr) {
  border-bottom: 1px solid rgb(229, 231, 235) !important;
  transition: background-color 0.2s ease-in-out;
}

:deep(table.dataTable tbody tr:nth-child(even)) {
  background-color: rgb(249, 250, 251);
}

:deep(table.dataTable tbody tr:hover) {
  background-color: rgb(219, 234, 254);
}

:deep(table.dataTable tbody tr button) {
  cursor: pointer;
}

:deep(div.dataTables_wrapper div.dataTables_paginate .paginate_button) {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0.5rem 0.75rem;
  margin: 0 0.25rem;
  border-radius: 0.5rem;
  color: rgb(55, 65, 81);
  border: 1px solid rgb(229, 231, 235);
  background-color: white;
  cursor: pointer;
  transition: all 0.2s ease-in-out;
  font-weight: 500;
}

:deep(div.dataTables_wrapper div.dataTables_paginate .paginate_button:hover) {
  background-color: rgb(229, 231, 235);
  border-color: rgb(203, 213, 225);
  color: rgb(30, 41, 59);
  transform: translateY(-2px);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  cursor: pointer;
}

:deep(div.dataTables_wrapper div.dataTables_paginate .paginate_button.current) {
  background-color: #2563eb;
  /* azul */
  color: white;
  border-color: #2563eb;
  font-weight: 600;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  cursor: pointer;
}

:deep(div.dataTables_wrapper div.dataTables_paginate .paginate_button.current:hover) {
  background-color: #1e40af;
  border-color: #1e40af;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  cursor: pointer;
}

:deep(div.dataTables_wrapper div.dataTables_paginate .first,
  div.dataTables_wrapper div.dataTables_paginate .last,
  div.dataTables_wrapper div.dataTables_paginate .previous,
  div.dataTables_wrapper div.dataTables_paginate .next) {
  font-weight: 500;
  cursor: pointer;
}
</style>