<script setup>
import { reactive } from 'vue';

const emit = defineEmits(['close']);

const form = reactive({
    item: '',
    tamanho: '',
    codigo: '',
});

const errors = reactive({
    item: null,
    tamanho: null,
});

function registrarEPI() {
    errors.item = null;
    errors.tamanho = null;

    let formValido = true;

    if (!form.item.trim()) {
        errors.item = "Campo Obrigatório";
        formValido = false;
    }

    if (!form.tamanho.trim()) {
        errors.tamanho = "Campo Obrigatório";
        formValido = false;
    }

    if (formValido) {
        console.log("EPI Registrado:", form);
    } else {
        console.log("Formulário inválido.");
    }
}
</script>

<template>
    <div class="fixed inset-0 bg-black bg-opacity-50 backdrop-blur-sm flex justify-center items-center z-40 p-4">

        <div
            class="mx-auto w-full max-w-2xl h-full sm:h-auto sm:min-h-[700px] sm:max-h-[90vh] bg-white rounded-xl shadow-lg drop-shadow-md flex flex-col">

            <div class="px-6 py-4 flex justify-between items-center border-b">
                <h2 class="flex font-bold items-center text-2xl sm:text-4xl">Adicionar EPI's</h2>
                <div @click="emit('close')" style="cursor: pointer" class="text-gray-600">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M6 18L18 6M6 6l12 12">
                        </path>
                    </svg>
                </div>
            </div>

            <form @submit.prevent="registrarEPI" class="flex-grow flex flex-col justify-between">

                <div class="flex-grow flex items-center p-6">
                    <div class="w-full mx-auto space-y-6">
                        <div>
                            <input type="text" placeholder="Item" v-model="form.item"
                                class="flex-1 w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100 focus:placeholder-gray-500" />
                            <p v-if="errors.item" class="text-red-500 text-sm mt-1">{{ errors.item }}</p>
                        </div>

                        <div>
                            <input type="text" placeholder="Tamanho" v-model="form.tamanho"
                                class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100 focus:placeholder-gray-500" />
                            <p v-if="errors.tamanho" class="text-red-500 text-sm mt-1">{{ errors.tamanho }}</p>
                        </div>

                        <div>
                            <input type="text" placeholder="Codigo(Não Obrigatorio)" v-model="form.codigo"
                                class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100 focus:placeholder-gray-500" />
                        </div>
                    </div>
                </div>

                <div class="text-center p-6 border-t">
                    <button type="submit" class="
                        font-bold text-white text-xl
                        px-12 sm:px-20 py-3 rounded-md 
                        bg-gradient-to-r from-green-500 to-emerald-500
                        bg-[length:200%_auto] bg-[position:0%_0%] hover:bg-[position:100%_0%]
                        transition-all duration-500 ease-out
                        hover:-translate-y-1 hover:shadow-lg hover:shadow-green-500/40
                        ">
                        Registrar
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>