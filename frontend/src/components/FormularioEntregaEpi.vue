<script setup>
import { ref } from 'vue';
import axios from 'axios';
import Swal from 'sweetalert2';

// 1. DEFINIR PROPS E EMITS
const props = defineProps({
    colaboradorId: {
        type: [Number, String],
        required: true
    }
});
const emit = defineEmits(['close', 'itemAdicionado']);

const backUrl = import.meta.env.VITE_BACKEND_URL;
const loading = ref(false);

// 2. FORMULÁRIO ATUALIZADO
const form = ref({
    data_entrega: new Date().toISOString().split('T')[0], // Padrão: Hoje
    quantidade: 1,
    descricao: '',
    ca: '',
    assinatura: '', // ATUALIZADO: Inicia vazio
    cod_entrega: 1, // Padrão: 'EPI Novo' (agora oculto)
    funcionario_id: props.colaboradorId // ID do colaborador
});

// NOVA FUNÇÃO para o botão Assinar
function handleAssinar() {
    console.log('Botão Assinar clicado!');
    // TODO: Implementar a lógica do vue-pad aqui
    // Ex: abrir modal de assinatura e no final:
    // form.value.assinatura = 'Assinatura Digital Capturada';
    Swal.fire('Em breve!', 'Aqui você abrirá o modal de assinatura.', 'info');
}

async function handleSave() {
    if (loading.value) return;

    // Validação (removido cod_entrega, adicionado assinatura)
    if (!form.value.descricao.trim() || !form.value.data_entrega || form.value.quantidade <= 0) {
        Swal.fire('Atenção', 'Preencha Descrição, Data de Entrega e Quantidade.', 'warning');
        return;
    }

    // Você pode querer validar a assinatura aqui
    // if (!form.value.assinatura) {
    //   Swal.fire('Atenção', 'A assinatura é obrigatória.', 'warning');
    //   return;
    // }

    loading.value = true;

    try {
        const response = await axios.post(`${backUrl}/api/movimentacoes`, form.value);

        if (response.status >= 200 && response.status < 300) {
            Swal.fire('Salvo!', 'O item foi adicionado com sucesso.', 'success');
            emit('itemAdicionado', response.data);
            emit('close'); // Fecha este modal
        }
    } catch (error) {
        console.error("Erro ao salvar movimentação:", error);
        Swal.fire('Erro!', 'Não foi possível salvar o item.', 'error');
    } finally {
        loading.value = false;
    }
}
</script>

<template>
    <div class="fixed inset-0 bg-black bg-opacity-50 backdrop-blur-sm flex justify-center items-center z-50 p-4">
        <div class="mx-auto w-full max-w-2xl bg-white rounded-xl shadow-lg flex flex-col">

            <div class="px-6 py-4 flex justify-between items-center border-b">
                <h2 class="font-bold text-2xl sm:text-4xl">Adicionar Item</h2>
                <div @click="emit('close')" style="cursor: pointer" class="text-gray-600">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M6 18L18 6M6 6l12 12">
                        </path>
                    </svg>
                </div>
            </div>

            <form @submit.prevent="handleSave" class="flex-grow flex flex-col justify-between">
                <div class="p-6">
                    <div class="w-full mx-auto space-y-6">

                        <div>
                            <input type="text" placeholder="Descrição do Item" v-model="form.descricao" required
                                class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100 focus:placeholder-gray-500" />
                        </div>

                        <div>
                            <input type="text" placeholder="C.A. (Não Obrigatório)" v-model="form.ca"
                                class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100 focus:placeholder-gray-500" />
                        </div>

                        <div>
                            <label for="dataValidade" class="block text-base font-medium text-gray-600 ml-1 mb-1">Data de
                                Entrega</label>
                            <input id="dataValidade" type="date" v-model="form.data_entrega" required
                                class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100 text-gray-700" />
                        </div>

                        <div>
                            <input type="number" placeholder="Quantidade" v-model="form.quantidade" min="1" required
                                class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100 focus:placeholder-gray-500" />
                        </div>

                        <div>
                            <label for="assinatura"
                                class="block text-base font-medium text-gray-600 ml-1 mb-1">Assinatura</label>
                            <div class="flex items-center gap-4">
                                <input type="text" id="assinatura" placeholder="Aguardando assinatura..."
                                    v-model="form.assinatura" readonly
                                    class="flex-1 ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-200 text-gray-700 cursor-not-allowed" />
                                <button type="button" @click="handleAssinar"
                                    class="bg-green-500 hover:bg-green-600 text-white font-bold py-3 px-6 rounded-md text-lg transition-all duration-300">
                                    Assinar
                                </button>
                            </div>
                        </div>

                    </div>
                </div>

                <div class="text-center p-6 border-t">
                    <button type="submit" :disabled="loading" class="
                        font-bold text-white text-xl
                        px-12 sm:px-20 py-3 rounded-md 
                        bg-gradient-to-r from-green-500 to-emerald-500
                        transition-all duration-300 ease-out
                        hover:-translate-y-1 hover:shadow-lg" :class="{ 'opacity-70 cursor-not-allowed': loading }">
                        {{ loading ? 'Salvando...' : 'Salvar Item' }}
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>