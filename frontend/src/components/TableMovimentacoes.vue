<script setup>
import { ref, onMounted, computed } from 'vue';
// 1. IMPORTAR O NOVO FORMULÁRIO DE ENTREGA
import FormularioEntregaEpi from './FormularioEntregaEpi.vue';

// --- Props e Emits existentes ---
const props = defineProps({
    colaborador: {
        type: Object,
        required: true
    }
});
defineEmits(['close']);

const backUrl = import.meta.env.VITE_BACKEND_URL;
const movimentacoes = ref([]);
const totalLinhasVisuais = 20;

// 2. STATE PARA O NOVO MODAL DE ENTREGA
const isFormEntregaVisible = ref(false);

// --- Lógica de busca de dados (existente) ---
onMounted(async () => {
    try {
        // ATENÇÃO: Verifique se este é seu endpoint para buscar as movimentações
        const response = await fetch(`${backUrl}/api/movimentacoes/funcionario/${props.colaborador.id}`);

        if (!response.ok) {
            console.warn("API não encontrada, usando dados simulados.");
            movimentacoes.value = [
                { id: 1, data_entrega: '20/10/2025', quantidade: 1, descricao: 'Capacete de Segurança', ca: '12345', assinatura: 'Assinado', data_devolucao: '', recebedor: '', cod_entrega: 1, cod_devolucao: '' },
                { id: 2, data_entrega: '20/10/2025', quantidade: 2, descricao: 'Luva de Malha', ca: '67890', assinatura: 'Assinado', data_devolucao: '', recebedor: '', cod_entrega: 1, cod_devolucao: '' },
            ];
        } else {
            movimentacoes.value = await response.json();
        }

    } catch (error) {
        console.error("Erro ao buscar movimentações:", error);
        movimentacoes.value = [
            { id: 1, data_entrega: '20/10/2025', quantidade: 1, descricao: 'Capacete de Segurança', ca: '12345', assinatura: 'Assinado', data_devolucao: '', recebedor: '', cod_entrega: 1, cod_devolucao: '' },
            { id: 2, data_entrega: '20/10/2025', quantidade: 2, descricao: 'Luva de Malha', ca: '67890', assinatura: 'Assinado', data_devolucao: '', recebedor: '', cod_entrega: 1, cod_devolucao: '' },
        ];
    }
});

const linhasEmBranco = computed(() => {
    return Math.max(0, totalLinhasVisuais - movimentacoes.value.length);
});

// --- Funções para os botões da tabela (existentes) ---
function handleEditEpi(item) {
    console.log("EDITAR EPI:", item);
    // TODO: Aqui você abriria o FormularioEntregaEpi em modo de edição
    alert(`Editar: ${item.descricao}`);
}

function handleDeleteEpi(item) {
    console.log("EXCLUIR EPI:", item);
    if (confirm(`Deseja realmente excluir: ${item.descricao}?`)) {
        // TODO: Adicionar 'fetch DELETE' para a API
        movimentacoes.value = movimentacoes.value.filter(m => m.id !== item.id);
    }
}

// 3. FUNÇÕES PARA CONTROLAR O NOVO MODAL DE ENTREGA
function openFormEntrega() {
    isFormEntregaVisible.value = true;
}

function closeFormEntrega() {
    isFormEntregaVisible.value = false;
}

/**
 * Esta função é chamada quando o evento 'itemAdicionado' é disparado
 * pelo FormularioEntregaEpi.vue
 */
function handleItemAdicionado(itemSalvo) {
    // Adiciona o novo item (retornado pela API) no início da lista
    movimentacoes.value.unshift(itemSalvo);
    // Não precisa fechar o modal aqui, o FormularioEntregaEpi já faz isso
}


// --- Função auxiliar (existente) ---
const getNomeUnidade = (unidade) => {
    if (unidade && unidade.toLowerCase().includes('coca-cola')) {
        return "Coca-Cola FEMSA Brasil";
    }
    return unidade || "Não Informado";
};
</script>

<template>
    <div @click.self="$emit('close')"
        class="fixed inset-0 z-40 bg-black bg-opacity-70 flex justify-center items-center p-4 transition-opacity duration-300">
        <div class="bg-white rounded-lg shadow-2xl w-[1400px] max-h-[90vh] overflow-hidden flex flex-col animate-fade-in">

            <header class="bg-gray-100 p-4 flex justify-between items-center border-b border-gray-300">
                <h2 class="text-xl font-semibold text-gray-800">
                    Ficha de Controle de EPIs
                </h2>
                <button @click="$emit('close')" class="text-gray-500 hover:text-red-600" title="Fechar">
                    <svg class="w-10 h-10" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                </button>
            </header>

            <main class="p-6 overflow-y-auto" style="font-family: Arial, sans-serif;">

                <div class="flex justify-between items-center border-b-2 border-black pb-2 mb-4">
                    <img src="/public/logo-femsa.png" alt="Coca-Cola FEMSA Brasil" class="h-10">
                    <span class="text-sm font-bold uppercase text-right">
                        Controle de Fornecimento de Uniformes e<br>Equipamentos de Proteção Individual
                    </span>
                </div>

                <div class="border-b-2 border-black pb-1 mb-2 text-sm">
                    <div class="flex flex-wrap space-x-4 mb-1">
                        <div class="flex-1 min-w-[300px]"><strong>Nome:</strong> {{ colaborador.nome || 'N/A' }}</div>
                        <div class="flex-1 min-w-[200px]"><strong>Unidade:</strong> {{ getNomeUnidade(colaborador.unidade)
                        }}</div>
                    </div>
                    <div class="flex flex-wrap space-x-4">
                        <div class="flex-1 min-w-[300px]"><strong>Função:</strong> {{ colaborador.funcao || 'N/A' }}</div>
                        <div class="flex-1 min-w-[200px]"><strong>Setor:</strong> {{ colaborador.unidade || 'N/A' }}</div>
                    </div>
                    <div class="flex flex-wrap space-x-4 mt-1">
                        <div class="flex-1 min-w-[300px]"><strong>RE/CPF:</strong> {{ colaborador.re || 'N/A' }}</div>
                        <div class="flex-1 min-w-[200px]"><strong>Data de Admissão:</strong> {{ '____/____/______' }}</div>
                    </div>
                </div>

                <div class="text-sm space-y-1 my-3">
                    <p>DECLARO que recebi os EPI's/Uniforme ABAIXO RELACIONADO(S)...</p>
                    <p>COMPROMETO-ME ainda a apresentar para troca todo equipamento...</p>
                    <p>DECLARO ainda, que fui instruído da sua correta utilização.</p>
                    <p>POR SER VERDADE, SUBSCREVO-ME ABAIXO.</p>
                </div>

                <div class="text-sm font-semibold mb-2">
                    <p>Código de Entrega: (1 - EPI novo) (2 - EPI Higienizado)</p>
                    <p>Código de devolução: (1 - Troca por tempo de uso) (2 - Perda) (3 - Furto) (4 - Mau uso) (5 -
                        Esquecimento)</p>
                </div>

                <div class="flex justify-end mb-4">
                    <button @click="openFormEntrega"
                        class="bg-blue-600 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-lg shadow-md transition-all duration-200 flex items-center gap-2">
                        <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"
                            xmlns="http://www.w3.org/2000/svg">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                d="M12 6v6m0 0v6m0-6h6m-6 0H6"></path>
                        </svg>
                        Adicionar Item
                    </button>
                </div>

                <div class="overflow-x-auto">
                    <table class="min-w-full border-collapse border border-black text-sm">
                        <thead class="bg-gray-100">
                            <tr class="text-center font-bold">
                                <td class="border border-black p-1">Data Entrega</td>
                                <td class="border border-black p-1">Qtde</td>
                                <td class="border border-black p-1">Descrição do EPI</td>
                                <td class="border border-black p-1">CA</td>
                                <td class="border border-black p-1">Assinatura</td>
                                <td class="border border-black p-1">Data Devolução</td>
                                <td class="border border-black p-1">Recebedor</td>
                                <td class="border border-black p-1">Código de Entrega</td>
                                <td class="border border-black p-1">Código de Devolução</td>
                                <td class="border border-black p-1">Ações</td>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="item in movimentacoes" :key="item.id" class="h-8 hover:bg-gray-100">
                                <td class="border border-black p-1 text-center">{{ item.data_entrega }}</td>
                                <td class="border border-black p-1 text-center">{{ item.quantidade }}</td>
                                <td class="border border-black p-1">{{ item.descricao }}</td>
                                <td class="border border-black p-1 text-center">{{ item.ca }}</td>
                                <td class="border border-black p-1 text-center">{{ item.assinatura }}</td>
                                <td class="border border-black p-1 text-center">{{ item.data_devolucao }}</td>
                                <td class="border border-black p-1 text-center">{{ item.recebedor }}</td>
                                <td class="border border-black p-1 text-center">{{ item.cod_entrega }}</td>
                                <td class="border border-black p-1 text-center">{{ item.cod_devolucao }}</td>

                                <td class="border border-black p-1 text-center whitespace-nowrap">
                                    <button @click="handleEditEpi(item)" title="Editar"
                                        class="text-blue-600 hover:text-blue-800 p-2">
                                        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.536L16.732 3.732z">
                                            </path>
                                        </svg>
                                    </button>
                                    <button @click="handleDeleteEpi(item)" title="Excluir"
                                        class="text-red-600 hover:text-red-800 p-2">
                                        <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16">
                                            </path>
                                        </svg>
                                    </button>
                                </td>
                            </tr>
                            <tr v-for="n in linhasEmBranco" :key="`blank-${n}`" class="h-8">
                                <td class="border border-black"></td>
                                <td class="border border-black"></td>
                                <td class="border border-black"></td>
                                <td class="border border-black"></td>
                                <td class="border border-black"></td>
                                <td class="border border-black"></td>
                                <td class="border border-black"></td>
                                <td class="border border-black"></td>
                                <td class="border border-black"></td>
                                <td class="border border-black"></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </main>

        </div>
    </div>
<FormularioEntregaEpi v-if="isFormEntregaVisible" :colaboradorId="colaborador.id" @close="closeFormEntrega"
    @itemAdicionado="handleItemAdicionado" /></template>

<style scoped>@keyframes fade-in {
    from {
        opacity: 0;
        transform: scale(0.95);
    }

    to {
        opacity: 1;
        transform: scale(1);
    }
}

.animate-fade-in {
    animation: fade-in 0.2s ease-out;
}</style>