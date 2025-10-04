<script>
export default {
  data() {
    return {
      epis: [] 
    }
  },

  async mounted() {
    try {
      const response = await fetch('http://localhost:8080/api/epis')

      if (!response.ok) {
        throw new Error('Erro ao buscar EPIs')
      }

      const data = await response.json()

      this.epis = data

    } catch (error) {
      console.error('Erro ao carregar EPIs:', error)
    }
  }
}
</script>

<template>
  <div>
    <main class="p-4 sm:p-8">
      <div class="overflow-x-auto relative shadow-md sm:rounded-lg">
        <table class="w-full text-sm text-left text-gray-500">
          <thead class="text-xs text-gray-700 uppercase bg-gray-100">
            <tr>
              <th scope="col" class="px-6 py-3">Código</th>
              <th scope="col" class="px-6 py-3">Item</th>
              <th scope="col" class="px-6 py-3">CA</th>
              <th scope="col" class="px-6 py-3">Tamanho</th>
              <th scope="col" class="px-6 py-3">
                <span class="sr-only">Ações</span>
              </th>
            </tr>
          </thead>

          <tbody>
            <tr v-if="epis.length === 0">
              <td colspan="4" class="px-6 py-4 text-center text-gray-500">
                Nenhum EPI cadastrado.
              </td>
            </tr>

            <tr
              v-for="epi in epis"
              :key="epi.id"
              class="bg-white border-b hover:bg-gray-50"
            >
              <th
                scope="row"
                class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap"
              >
                {{ epi.item }}
              </th>
              <td class="px-6 py-4">
                {{ epi.tamanho }}
              </td>
              <td class="px-6 py-4">
                {{ epi.codigo }}
              </td>
              <td class="px-6 py-4 text-right">
                <a href="#" class="font-medium text-blue-600 hover:underline">Editar</a>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </main>
  </div>
</template>
