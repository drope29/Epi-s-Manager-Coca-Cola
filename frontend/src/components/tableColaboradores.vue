<script setup>
import { ref, onMounted, nextTick } from 'vue';
import DataTable from 'datatables.net-vue3';
import DataTablesCore from 'datatables.net-dt';
import Responsive from 'datatables.net-responsive-dt';
import Buttons from 'datatables.net-buttons-dt';
import Swal from 'sweetalert2';

import 'datatables.net-dt/css/dataTables.dataTables.min.css';
import 'datatables.net-responsive-dt/css/responsive.dataTables.min.css';
import 'sweetalert2/dist/sweetalert2.min.css';
import ButtonAdd from './buttonAdd.vue';

const backUrl = import.meta.env.VITE_BACKEND_URL;
const emit = defineEmits(['open-add-modal', 'open-edit-modal']);

DataTable.use(DataTablesCore);
DataTable.use(Responsive);
DataTable.use(Buttons);

const globalSearch = ref('');
const dtRef = ref(null);

const columns = [
  { data: 're', title: 'RE', className: 'text-center', render: data => data == 0 ? "Não Informado" : data },
  { data: 'nome', title: 'Nome', className: 'text-center' },
  { data: 'funcao', title: 'Cargo', className: 'text-center' },
  { data: 'unidade', title: 'Unidade', className: 'text-center', render: (_d, _t, row) => row.unidade || "Não Informado" },
  { data: 'genero', title: 'Gênero', className: 'text-center', render: (_d, _t, row) => row.genero || "Não Informado" },
  { data: null, title: 'Turno', className: 'text-center', render: (_d, _t, row) => row.turno || "Não Informado" },
  {
    data: null,
    title: 'Ações',
    orderable: false,
    searchable: false,
    className: 'text-center',
    render: (_d, _t, row) => {

      const editBtn = `
        <button
          data-action="edit"
          data-id="${row.id}" 
          class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-3 rounded text-xs transition-colors duration-200"
          title="Editar">
          Editar
        </button>`;

      const deleteBtn = `
        <button
          data-action="delete"
          data-id="${row.id}"
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
  autoWidth: false,
  searching: true,
  paging: true,
  info: true,
  lengthChange: true,
  pageLength: 10,
  dom: '<"datatable-header"l>rt<"datatable-footer"ip>',
  language: {
    lengthMenu: "Mostrar _MENU_ entradas",
    zeroRecords: "Nenhum colaborador encontrado.",
    info: "Mostrando de _START_ até _END_ de _TOTAL_ registros",
    infoEmpty: "Mostrando 0 registros",
    infoFiltered: "(filtrado de _MAX_ registros)",
    paginate: { first: "Primeiro", last: "Último", next: "Próximo", previous: "Anterior" },
    emptyTable: "Nenhum Colaborador Cadastrado"
  },
};

const colaboradores = ref([]);

onMounted(async () => {
  try {
    const response = await fetch(`${backUrl}/api/funcionarios/`);
    if (!response.ok) throw new Error('Erro ao buscar colaboradores');
    colaboradores.value = await response.json();
    await nextTick();
    if (dtRef.value && globalSearch.value) dtRef.value.dt.search(globalSearch.value).draw();
  } catch (error) { console.error(error); }
});

const applySearch = () => { if (dtRef.value) dtRef.value.dt.search(globalSearch.value).draw(); }

const handleTableClick = (event) => {
  const target = event.target.closest('button[data-action]');
  if (!target) return;

  const action = target.dataset.action;
  const id = target.dataset.id;
  const colaborador = colaboradores.value.find(c => c.id.toString() === id);

  if (!colaborador) return;

  if (action === 'edit') {
    handleEdit(colaborador);
  } else if (action === 'delete') {
    handleDelete(colaborador);
  }
};

const handleEdit = (colaborador) => {
  console.log('EDITAR:', colaborador);
  emit('open-edit-modal', colaborador);
};

const handleDelete = (colaborador) => {
  Swal.fire({
    title: 'Você tem certeza?',
    text: `Deseja realmente excluir ${colaborador.nome} (RE: ${colaborador.re})? Esta ação não pode ser desfeita.`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#d33',
    cancelButtonColor: '#3085d6',
    confirmButtonText: 'Sim, excluir!',
    cancelButtonText: 'Cancelar'
  }).then(async (result) => {
    if (result.isConfirmed) {
      try {
        const response = await fetch(`${backUrl}/api/funcionarios/${colaborador.id}`, {
          method: 'DELETE',
        });

        if (!response.ok) {
          throw new Error('Falha ao excluir colaborador');
        }

        colaboradores.value = colaboradores.value.filter(c => c.id !== colaborador.id);

        Swal.fire(
          'Excluído!',
          `${colaborador.nome} foi excluído com sucesso.`,
          'success'
        );

      } catch (error) {
        console.error('Erro ao excluir:', error);
        Swal.fire(
          'Erro!',
          'Não foi possível excluir o colaborador. Tente novamente.',
          'error'
        );
      }
    }
  });
};
</script>

<template>
  <div>
    <header class="bg-gray-800 text-white p-4 flex items-center space-x-4">
      <div class="relative w-full">
        <input type="text" placeholder="Procurar por nome, RE, cargo..."
          class="bg-gray-700 text-white rounded-lg py-2 pl-10 pr-4 focus:outline-none focus:ring-2 focus:ring-blue-500 w-full"
          v-model="globalSearch" @input="applySearch" />
        <svg class="w-5 h-5 text-gray-400 absolute left-3 top-1/2 transform -translate-y-1/2" fill="none"
          stroke="currentColor" viewBox="0 0 24 24">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
            d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
        </svg>
      </div>
      <ButtonAdd @add-clicked="$emit('open-add-modal')" />
    </header>

    <main class="p-4 sm:p-8">
      <div class="overflow-x-auto relative shadow-md sm:rounded-lg bg-white" @click="handleTableClick">
        <DataTable :columns="columns" :data="colaboradores" :options="options" ref="dtRef"
          class="w-full text-sm text-gray-700" />
      </div>
    </main>
  </div>
</template>

<style scoped>
:deep(.dataTables_wrapper) {
  width: 100%;
  padding: 0 !important;
}

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
}

:deep(div.dataTables_wrapper div.dataTables_paginate .paginate_button.current) {
  background-color: #2563eb;
  color: white;
  border-color: #2563eb;
  font-weight: 600;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
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