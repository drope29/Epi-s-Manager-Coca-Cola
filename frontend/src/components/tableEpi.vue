<script setup>
import { ref, onMounted, nextTick } from 'vue';
import DataTable from 'datatables.net-vue3';
import DataTablesCore from 'datatables.net-dt';
import Responsive from 'datatables.net-responsive-dt';
import Buttons from 'datatables.net-buttons-dt';

import 'datatables.net-dt/css/dataTables.dataTables.min.css';
import 'datatables.net-responsive-dt/css/responsive.dataTables.min.css';
import ButtonAdd from './buttonAdd.vue';

const backUrl = import.meta.env.VITE_BACKEND_URL;
const emit = defineEmits(['open-epi-modal']);

DataTable.use(DataTablesCore);
DataTable.use(Responsive);
DataTable.use(Buttons);

const globalSearch = ref('');
const dtRef = ref(null);

const columns = [
  {
    data: 'codigoCompra',
    title: 'CA',
    className: 'text-center',
    render: (data) => data == 0 ? "Não Informado" : data
  },
  {
    data: 'descricao',
    title: 'Descrição',
    className: 'text-center'
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
    paginate: { first: "Primeiro", last: "Último", next: "Próximo", previous: "Anterior" }
  },
};

const epis = ref([]);

onMounted(async () => {
  try {
    const response = await fetch(`${backUrl}/api/epis/`);
    if (!response.ok) throw new Error('Erro ao buscar EPI');
    epis.value = await response.json();
    await nextTick();
    if (dtRef.value && globalSearch.value) dtRef.value.dt.search(globalSearch.value).draw();
  } catch (error) {
    console.error(error);
  }
});

const applySearch = () => {
  if (dtRef.value) dtRef.value.dt.search(globalSearch.value).draw();
}
</script>

<template>
  <div>
    <!-- Header com busca e botão -->
    <header class="bg-gray-800 text-white p-4 flex items-center space-x-4">
      <div class="relative w-full">
        <input type="text" placeholder="Procurar"
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

    <!-- Tabela -->
    <main class="p-4 sm:p-8">
      <div class="overflow-x-auto relative shadow-md sm:rounded-lg bg-white">
        <DataTable :columns="columns" :data="epis" :options="options" ref="dtRef" class="w-full text-sm text-gray-700">
          <thead>
            <tr>
              <th>CA</th>
              <th>Descrição</th>
            </tr>
          </thead>
        </DataTable>

        <div v-if="epis.length === 0" class="text-center py-10 text-gray-500">
          Nenhum EPI cadastrado.
        </div>
      </div>
    </main>
  </div>
</template>

<style scoped>
/* Ajuste geral do DataTable */
:deep(.dataTables_wrapper) {
  width: 100%;
  padding: 0 !important;
}

/* Esconde o filtro padrão */
:deep(.dataTables_filter) {
  display: none;
}

/* Header e Footer */
:deep(.datatable-header),
:deep(.datatable-footer) {
  padding: 1rem;
  border-color: rgb(229, 231, 235);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* Cabeçalho e células da tabela */
:deep(table.dataTable thead th),
:deep(table.dataTable tbody td) {
  padding: 0.75rem 1rem !important;
  text-align: center !important;
  vertical-align: middle !important;
  font-weight: 500 !important;
  color: rgb(55, 65, 81) !important;
}

/* Cabeçalho */
:deep(table.dataTable thead th) {
  border-bottom: 2px solid rgb(229, 231, 235) !important;
  background-color: rgb(243, 244, 246) !important;
}

/* Linhas da tabela */
:deep(table.dataTable tbody tr) {
  border-bottom: 1px solid rgb(229, 231, 235) !important;
  transition: background-color 0.2s ease-in-out;
}

/* Zebra stripes */
:deep(table.dataTable tbody tr:nth-child(even)) {
  background-color: rgb(249, 250, 251);
}

/* Hover nas linhas */
:deep(table.dataTable tbody tr:hover) {
  background-color: rgb(219, 234, 254);
  /* azul claro */
  cursor: pointer;
}

/* Paginação estilizada */
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

/* Hover de todos os botões */
:deep(div.dataTables_wrapper div.dataTables_paginate .paginate_button:hover) {
  background-color: rgb(229, 231, 235);
  border-color: rgb(203, 213, 225);
  color: rgb(30, 41, 59);
  transform: translateY(-2px);
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  cursor: pointer;
}

/* Botão ativo (número da página atual) */
:deep(div.dataTables_wrapper div.dataTables_paginate .paginate_button.current) {
  background-color: #2563eb;
  /* azul */
  color: white;
  border-color: #2563eb;
  font-weight: 600;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  cursor: pointer;
}

/* Hover no botão ativo */
:deep(div.dataTables_wrapper div.dataTables_paginate .paginate_button.current:hover) {
  background-color: #1e40af;
  /* azul mais escuro */
  border-color: #1e40af;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  cursor: pointer;
}

/* Espaçamento adicional para botões especiais (Primeiro, Último, Anterior, Próximo) */
:deep(div.dataTables_wrapper div.dataTables_paginate .first,
  div.dataTables_wrapper div.dataTables_paginate .last,
  div.dataTables_wrapper div.dataTables_paginate .previous,
  div.dataTables_wrapper div.dataTables_paginate .next) {
  font-weight: 500;
  cursor: pointer;
}
</style>
