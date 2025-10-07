<script setup>
import { ref, onMounted, nextTick } from 'vue';
import DataTable from 'datatables.net-vue3';
import DataTablesCore from 'datatables.net-dt';

import Responsive from 'datatables.net-responsive-dt';
import Buttons from 'datatables.net-buttons-dt';
import 'datatables.net-responsive-dt/css/responsive.dataTables.css';
import ButtonAdd from './buttonAdd.vue';

// --- CORREÇÃO 1: Defina o evento que este componente vai emitir ---
const emit = defineEmits(['open-epi-modal']);

DataTable.use(DataTablesCore);
DataTable.use(Responsive);
DataTable.use(Buttons);

const globalSearch = ref('');
const dtRef = ref(null);

const columns = [
  {
    data: 'codigo',
    title: 'CA',
    className: 'px-6 py-4 font-medium text-gray-900 whitespace-nowrap text-center w-1/2',
    render: function(data, type, row) {
      return data == 0 ? "Não Informado" : data;
    }
  },
  {
    data: 'descricao',
    title: 'Descrição',
    className: 'px-6 py-4 font-medium text-gray-900 whitespace-nowrap text-center w-1/2'
  },
];


const options = {
  responsive: true,
  searching: true,
  paging: true,
  info: true,
  lengthChange: true,
  pageLength: 10,
  dom: 'lrtip',
  language: {
    lengthMenu: "Mostrar _MENU_ entradas por página",
    zeroRecords: "Nenhum colaborador encontrado.",
    info: "Mostrando de _START_ até _END_ de _TOTAL_ registros",
    infoEmpty: "Mostrando 0 até 0 de 0 registros",
    infoFiltered: "(filtrado de _MAX_ registros no total)",
    paginate: {
      first: "Primeiro",
      last: "Último",
      next: "Próximo",
      previous: "Anterior"
    }
  },
};

const epis = ref([]);

onMounted(async () => {
  try {
    const response = await fetch('http://localhost:8080/api/epis/');
    if (!response.ok) {
      throw new Error('Erro ao buscar colaboradores');
    }
    const data = await response.json();
    epis.value = data;

    await nextTick();
    if (dtRef.value && globalSearch.value) {
      const dt = dtRef.value.dt;
      if (dt) {
        dt.search(globalSearch.value).draw();
      }
    }

  } catch (error) {
    console.error('Erro ao carregar epis:', error);
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
        <input
          type="text"
          placeholder="Procurar"
          class="bg-gray-700 text-white rounded-lg py-2 pl-10 pr-4 focus:outline-none focus:ring-2 focus:ring-blue-500 w-full"
          v-model="globalSearch"
          @input="applySearch"
        />
        <svg
          class="w-5 h-5 text-gray-400 absolute left-3 top-1/2 transform -translate-y-1/2"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
          ></path>
        </svg>
      </div>
      <ButtonAdd @add-clicked="$emit('open-epi-modal')" />
    </header>

    <main class="p-4 sm:p-8">
      <div class="overflow-x-auto relative shadow-md sm:rounded-lg">
        <DataTable
          :columns="columns"
          :data="epis"
          :options="options"
          ref="dtRef"
          class="w-full text-sm text-left text-gray-500"
        >
          <thead class="text-xs text-gray-700 uppercase bg-gray-100">
            <tr>
              <th scope="col" class="px-6 py-3">CA</th>
              <th scope="col" class="px-6 py-3">Descrição</th>
            </tr>
          </thead>
          </DataTable>
        
        <div v-if="epis.length === 0" class="text-center py-4 text-gray-500">
          Nenhum epi cadastrado.
        </div>
      </div>
    </main>
  </div>
</template>