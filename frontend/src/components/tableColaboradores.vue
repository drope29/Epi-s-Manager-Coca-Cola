<script lang="js">
import { defineComponent } from 'vue';

export default defineComponent({
  data() {
    return {
      colaboradores: []
    };
  },

  async mounted() {
    try {
      const response = await fetch('http://localhost:8080/api/funcionarios/');

      if (!response.ok) {
        throw new Error('Erro ao buscar colaboradores');
      }

      const data = await response.json();
      this.colaboradores = data;
    } catch (error) {
      console.error('Erro ao carregar colaboradores:', error);
    }
  }
});
</script>

<template>
  <div>
    <main class="p-4 sm:p-8">
      <div class="overflow-x-auto relative shadow-md sm:rounded-lg">
        <table class="w-full text-sm text-left text-gray-500">
          <thead class="text-xs text-gray-700 uppercase bg-gray-100">
            <tr>
              <th scope="col" class="px-6 py-3">RE</th>
              <th scope="col" class="px-6 py-3">Nome</th>
              <th scope="col" class="px-6 py-3">Cargo</th>
              <th scope="col" class="px-6 py-3">Turno</th>
            </tr>
          </thead>

          <tbody>
            <tr v-if="colaboradores.length === 0">
              <td colspan="4" class="px-6 py-4 text-center text-gray-500">
                Nenhum colaborador cadastrado.
              </td>
            </tr>

            <tr
              v-for="colaborador in colaboradores"
              :key="colaborador.id"
              class="bg-white border-b hover:bg-gray-50"
            >
              <td class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
                {{ colaborador.re }}
              </td>
              <td class="px-6 py-4">{{ colaborador.nome }}</td>
              <td class="px-6 py-4">{{ colaborador.funcao }}</td>
              <td class="px-6 py-4">{{ colaborador.turno || "Não Informado" }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </main>
  </div>
</template>
