<script setup>
import { ref, onMounted, nextTick } from 'vue';
import DataTable from 'datatables.net-vue3';
import DataTablesCore from 'datatables.net-dt';
import Responsive from 'datatables.net-responsive-dt';
import Buttons from 'datatables.net-buttons-dt';

// Importação dos estilos
import 'datatables.net-dt/css/dataTables.dataTables.min.css';
import 'datatables.net-responsive-dt/css/responsive.dataTables.min.css';

import ButtonAdd from './buttonAdd.vue';

// ✅ Linha adicionada para definir o evento que este componente emite
const emit = defineEmits(['open-add-modal']);

// Inicializa o DataTables e suas extensões
DataTable.use(DataTablesCore);
DataTable.use(Responsive);
DataTable.use(Buttons);

const globalSearch = ref('');
const dtRef = ref(null);

const columns = [
  {
    data: 're',
    title: 'RE',
    className: 'px-6 py-4 font-medium text-gray-900 whitespace-nowrap',
    render: function (data) {
      return data === 0 ? "Não Informado" : data;
    }
  },
  { data: 'nome', title: 'Nome', className: 'px-6 py-4' },
  { data: 'funcao', title: 'Cargo', className: 'px-6 py-4' },
  {
    data: null,
    title: 'Turno',
    className: 'px-6 py-4',
    render: function (_data, _type, row) {
      return row.turno || "Não Informado";
    }
  },
];

const options = {
  responsive: true,
  searching: false,
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
    paginate: {
      first: "Primeiro",
      last: "Último",
      next: "Próximo",
      previous: "Anterior"
    }
  },
};

const colaboradores = ref([]);

onMounted(async () => {
  try {
    const response = await fetch('http://localhost:8080/api/funcionarios/');
    if (!response.ok) {
      throw new Error('Erro ao buscar colaboradores');
    }
    const data = await response.json();
    colaboradores.value = data;

    await nextTick();
    if (dtRef.value && globalSearch.value) {
      const dt = dtRef.value.dt;
      if (dt) {
        dt.search(globalSearch.value).draw();
      }
    }
  } catch (error) {
    console.error('Erro ao carregar colaboradores:', error);
  }
});

const applySearch = () => {
  if (dtRef.value) {
    const dt = dtRef.value.dt;
    if (dt) {
      dt.search(globalSearch.value).draw();
    }
  }
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
          stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
            d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"></path>
        </svg>
      </div>
      <ButtonAdd @add-clicked="$emit('open-add-modal')" />
    </header>

    <main class="p-4 sm:p-8">
      <div class="bg-white shadow-md sm:rounded-lg overflow-hidden">
        <DataTable :columns="columns" :data="colaboradores" :options="options" ref="dtRef"
          class="w-full text-sm text-left text-gray-500">
          <thead>
            <tr>
              <th scope="col" class="px-6 py-3">RE</th>
              <th scope="col" class="px-6 py-3">Nome</th>
              <th scope="col" class="px-6 py-3">Cargo</th>
              <th scope="col" class="px-6 py-3">Turno</th>
            </tr>
          </thead>
        </DataTable>

        <div v-if="colaboradores.length === 0" class="text-center py-10 text-gray-500">
          Nenhum colaborador cadastrado.
        </div>
      </div>
    </main>
  </div>
</template>

<style scoped>
:deep(.dataTables_wrapper) {
  padding: 0 !important;
}

:deep(.dataTables_filter) {
  display: none;
}

:deep(.datatable-header) {
  padding: 1rem;
  border-bottom: 1px solid rgb(229, 231, 235);
}

:deep(.datatable-footer) {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem;
  border-top: 1px solid rgb(229, 231, 235);
}

:deep(table.dataTable thead th) {
  font-weight: 600 !important;
  color: rgb(55, 65, 81) !important;
  border-bottom: 1px solid rgb(229, 231, 235) !important;
  background-color: transparent !important;
}

:deep(table.dataTable tbody tr:last-child) {
  border-bottom: none !important;
}

:deep(div.dataTables_wrapper div.dataTables_length label) {
  font-weight: 500;
  color: rgb(55, 65, 81);
  display: flex;
  align-items: center;
  font-size: 0.875rem;
}

:deep(div.dataTables_wrapper div.dataTables_length select) {
  width: 5rem;
  margin-left: 0.5rem;
  padding: 0.375rem 2.5rem 0.375rem 0.5rem;
  border: 1px solid rgb(209, 213, 219);
  border-radius: 0.5rem;
  box-shadow: 0 1px 2px 0 rgb(0 0 0 / 0.05);
  background-color: white;
  background-repeat: no-repeat;
  -webkit-appearance: none;
  appearance: none;
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' fill='none' viewBox='0 0 20 20'%3e%3cpath stroke='%236b7280' stroke-linecap='round' stroke-linejoin='round' stroke-width='1.5' d='M6 8l4 4 4-4'/%3e%3c/svg%3e");
  background-position: right 0.5rem center;
  background-size: 1.5em 1.5em;
}

:deep(div.dataTables_wrapper div.dataTables_length select:focus) {
  outline: 2px solid transparent;
  outline-offset: 2px;
  border-color: #3b82f6;
}

:deep(div.dataTables_wrapper div.dataTables_info) {
  color: rgb(75, 85, 99);
  font-size: 0.875rem;
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
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
  font-size: 0.875rem;
  min-width: 2.5rem;
  text-align: center;
  white-space: nowrap;
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
}

:deep(div.dataTables_wrapper div.dataTables_paginate .paginate_button:not(.disabled):hover) {
  background-color: rgb(243, 244, 246);
  color: rgb(31, 41, 55);
  border-color: rgb(209, 213, 219);
  box-shadow: 0 2px 4px -1px rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06);
  transform: translateY(-1px);
}

:deep(div.dataTables_wrapper div.dataTables_paginate .paginate_button.current) {
  background-color: #2563eb;
  color: white;
  border-color: #2563eb;
  font-weight: 600;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -2px rgba(0, 0, 0, 0.1);
  transform: translateY(-1px);
}

:deep(div.dataTables_wrapper div.dataTables_paginate .paginate_button.current:hover) {
  background-color: #1d4ed8;
  border-color: #1d4ed8;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.15), 0 2px 4px -2px rgba(0, 0, 0, 0.1);
}

:deep(div.dataTables_wrapper div.dataTables_paginate .paginate_button.disabled) {
  opacity: 0.5;
  cursor: not-allowed;
  pointer-events: none;
  transform: none;
}

:deep(div.dataTables_wrapper div.dataTables_paginate .paginate_button.first,
  div.dataTables_wrapper div.dataTables_paginate .paginate_button.last,
  div.dataTables_wrapper div.dataTables_paginate .paginate_button.previous,
  div.dataTables_wrapper div.dataTables_paginate .paginate_button.next) {
  min-width: auto;
  padding: 0.5rem 0.75rem;
  font-weight: 500;
  letter-spacing: 0.025em;
}

:deep(div.dataTables_wrapper div.dataTables_paginate .paginate_button:not(.disabled):active) {
  transform: translateY(0px);
  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
}

:deep(table.dataTable thead .sorting),
:deep(table.dataTable thead .sorting_asc),
:deep(table.dataTable thead .sorting_desc) {
  background-image: none !important;
}
</style>