<script lang="js">
import { defineComponent } from 'vue';

export default defineComponent({
    data() {
        return {
            colaboradores: []
        };
    }
});
</script>


<template>
    <div>
        <main class="p-4 sm:p-8">
            <div class="overflow-x-auto relative shadow-md sm:rounded-lg">
                <table class="w-full text-sm text-left text-gray-500">

                    <thead class="text-xs text-gray-700 uppercase bg-gray-50">
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
                                Nenhum Colaborador cadastrado.
                            </td>
                        </tr>

                        <tr v-for="colaborador in colaboradores" :key="colaborador.id" class="bg-white border-b hover:bg-gray-50">
                            <th scope="row" class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap">
                                {{ colaborador.item }}
                            </th>
                            <td class="px-6 py-4">
                                {{ colaborador.tamanho }}
                            </td>
                            <td class="px-6 py-4">
                                {{ colaborador.codigo }}
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