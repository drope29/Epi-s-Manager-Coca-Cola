<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import axios from 'axios';
import FormularioEntregaEpi from './FormularioEntregaEpi.vue'; // Certifique-se que o caminho está correto
import Swal from 'sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';
import VueSignaturePad from 'vue-signature-pad';


// --- Props e Emits ---
const props = defineProps({
    colaborador: { type: Object, required: true }
});
defineEmits(['close']);

const backUrl = import.meta.env.VITE_BACKEND_URL;
const movimentacoes = ref([]);
const totalLinhasVisuais = 20;

// --- Estados para Edição Inline ---
const editingItemId = ref(null);
const editFormData = ref({});
const epis = ref([]);
const loading = ref(false);
const isEditDropdownOpen = ref(false);
const editSearchQuery = ref('');

// --- Estados do Modal ---
const isFormEntregaVisible = ref(false);

// --- Carregamento Inicial ---
onMounted(async () => {
    await fetchMovimentacoes();
    await fetchEpis();
});

// --- Funções API ---
async function fetchMovimentacoes() {
    loading.value = true;
    try {
        const response = await fetch(`${backUrl}/api/movimentacao/`); // Busca todas
        if (!response.ok) {
            console.warn(`API ${response.url} não encontrada ou falhou: ${response.status}`);
            movimentacoes.value = [];
        } else {
            const allData = await response.json();
            // Filtra pelo ID do colaborador (assumindo item.funcionario.id)
            const filteredData = allData.filter(item => item.funcionario && item.funcionario.id === props.colaborador.id);
            movimentacoes.value = filteredData.map(item => ({ ...item, epi: item.epi || {} }));
        }
    } catch (error) {
        console.error('Erro ao carregar e filtrar movimentações:', error);
        movimentacoes.value = [];
        Swal.fire('Erro', 'Não foi possível carregar as movimentações.', 'error');
    } finally {
        loading.value = false;
    }
}

async function fetchEpis() {
    try {
        const response = await axios.get(`${backUrl}/api/epis/`);
        epis.value = response.data;
    } catch (error) {
        console.error("Erro ao buscar EPIs:", error);
    }
}

// --- Helpers de Formatação (CORRIGIDOS) ---
const formatarDataDisplay = (dataStr) => {
    if (!dataStr) return "";

    let date;
    try {
        // Caso 1: Formato ISO (YYYY-MM-DD or YYYY-MM-DDTHH:mm:ss)
        if (dataStr.includes('-')) {
            const dateParts = dataStr.split('T')[0].split('-'); // ["YYYY", "MM", "DD"]
            date = new Date(Date.UTC(
                parseInt(dateParts[0]), // YYYY
                parseInt(dateParts[1]) - 1, // MM (0-indexed)
                parseInt(dateParts[2])  // DD
            ));

            // Caso 2: Formato Brasileiro (DD/MM/YYYY)
        } else if (dataStr.includes('/')) {
            const dateParts = dataStr.split('/'); // ["DD", "MM", "YYYY"]
            date = new Date(Date.UTC(
                parseInt(dateParts[2]), // YYYY
                parseInt(dateParts[1]) - 1, // MM (0-indexed)
                parseInt(dateParts[0])  // DD
            ));

            // Caso 3: Tentar a sorte com o construtor padrão
        } else {
            date = new Date(dataStr);
        }

        if (isNaN(date.getTime())) {
            return dataStr; // Retorna o original se for "Invalid Date"
        }

        // Formata para DD/MM/YYYY
        return date.toLocaleDateString('pt-BR', { timeZone: 'UTC' });

    } catch (e) {
        return dataStr;
    }
};

const formatarDataInput = (dataStr) => {
    if (!dataStr) return "";

    let date;
    try {
        // Caso 1: Formato ISO (YYYY-MM-DD or YYYY-MM-DDTHH:mm:ss)
        if (dataStr.includes('-')) {
            const dateParts = dataStr.split('T')[0].split('-'); // ["YYYY", "MM", "DD"]
            date = new Date(Date.UTC(
                parseInt(dateParts[0]),
                parseInt(dateParts[1]) - 1,
                parseInt(dateParts[2])
            ));
        }
        // Caso 2: Formato Brasileiro (DD/MM/YYYY)
        else if (dataStr.includes('/')) {
            const dateParts = dataStr.split('/'); // ["DD", "MM", "YYYY"]
            date = new Date(Date.UTC(
                parseInt(dateParts[2]),
                parseInt(dateParts[1]) - 1,
                parseInt(dateParts[0])
            ));
        }
        // Caso 3: Tentar a sorte
        else {
            date = new Date(dataStr);
        }

        if (isNaN(date.getTime())) return '';

        // Formata para YYYY-MM-DD
        const year = date.getUTCFullYear();
        const month = (date.getUTCMonth() + 1).toString().padStart(2, '0');
        const day = date.getUTCDate().toString().padStart(2, '0');
        return `${year}-${month}-${day}`;

    } catch (e) {
        return '';
    }
};

// --- Computed Properties ---
const linhasEmBranco = computed(() => {
    return Math.max(0, totalLinhasVisuais - movimentacoes.value.length);
});

// Filtro para o combobox de edição inline
const filteredEpisInEdit = computed(() => {
    const query = editSearchQuery.value.toLowerCase().trim();
    if (!query) {
        return epis.value; // Template limita a 10
    }
    return epis.value.filter(epi => {
        const descricao = epi.descricao ? epi.descricao.toLowerCase() : '';
        const ca = epi.codigoAutenticacao ? epi.codigoAutenticacao.toString().toLowerCase() : '';
        return descricao.includes(query) || ca.includes(query);
    });
});

// Texto exibido no input do combobox de edição
const editDisplayValue = computed({
    get() {
        if (isEditDropdownOpen.value) {
            return editSearchQuery.value;
        }
        if (editFormData.value.epiId) {
            const epi = epis.value.find(e => e.id === editFormData.value.epiId);
            return epi ? `${epi.descricao} (CA: ${epi.codigoAutenticacao})` : '';
        }
        return '';
    },
    set(value) {
        editSearchQuery.value = value;
        // Não limpa epiId aqui automaticamente ao digitar
    }
});


// Computed para o CA na edição
const editedCaDisplay = computed(() => {
    if (!editingItemId.value || !editFormData.value.epiId) return '';
    const selectedEpi = epis.value.find(e => e.id === editFormData.value.epiId);
    return selectedEpi?.codigoAutenticacao || '';
});


// --- Funções de Edição Inline ---
function startEdit(item) {
    editingItemId.value = item.id;
    editFormData.value = {
        id: item.id,
        dataEntrega: formatarDataInput(item.dataEntrega),
        epiId: item.epi?.id || null,
        status: item.status || '',
        dataProximaEntrega: formatarDataInput(item.dataProximaEntrega),
        quantidade: item.quantidade || 1,
        recebedor: item.recebedor || '',
        cod_entrega: item.cod_entrega || '',
        cod_devolucao: item.cod_devolucao || '',
        funcionarioId: item.funcionario?.id || props.colaborador.id,
        caDisplay: item.epi?.codigoAutenticacao || ''
    };
    editSearchQuery.value = '';
    isEditDropdownOpen.value = false;
    if (epis.value.length === 0) fetchEpis();
}

async function saveEdit() {
    // Validações...
    if (!editFormData.value.epiId) { Swal.fire('Atenção', 'Selecione um EPI.', 'warning'); return; }
    const quantidadeNum = parseInt(editFormData.value.quantidade, 10);
    if (isNaN(quantidadeNum) || quantidadeNum <= 0) { Swal.fire('Atenção', 'Quantidade inválida.', 'warning'); return; }
    const codEntregaNum = parseInt(editFormData.value.cod_entrega, 10);
    if (editFormData.value.cod_entrega && isNaN(codEntregaNum)) { Swal.fire('Atenção', 'Cod Entrega inválido.', 'warning'); return; }
    const codDevolucaoNum = parseInt(editFormData.value.cod_devolucao, 10);
    if (editFormData.value.cod_devolucao && isNaN(codDevolucaoNum)) { Swal.fire('Atenção', 'Cod Devolução inválido.', 'warning'); return; }

    loading.value = true;

    // Payload para o backend (campos que existem)
    const payloadBackend = {
        dataEntrega: editFormData.value.dataEntrega || null,
        epiId: editFormData.value.epiId,
        funcionarioId: editFormData.value.funcionarioId,
        status: editFormData.value.status || null,
        quantidade: quantidadeNum,
        dataProximaEntrega: editFormData.value.dataProximaEntrega || null,
    };

    try {
        const response = await axios.put(`${backUrl}/api/movimentacao/${editingItemId.value}`, payloadBackend, {
            headers: { 'Content-Type': 'application/json' }
        });

        if (response.status >= 200 && response.status < 300) {
            const updatedItemFromServer = response.data || {};

            // Atualiza item local mesclando dados
            const finalUpdatedItem = {
                ...movimentacoes.value.find(m => m.id === editingItemId.value),
                ...editFormData.value,
                epi: epis.value.find(e => e.id === editFormData.value.epiId) || {},
                dataEntrega: editFormData.value.dataEntrega ? new Date(editFormData.value.dataEntrega + 'T00:00:00Z').toISOString() : null,
                dataProximaEntrega: editFormData.value.dataProximaEntrega ? new Date(editFormData.value.dataProximaEntrega + 'T00:00:00Z').toISOString() : null,
                ...updatedItemFromServer,
            };

            handleItemAtualizado(finalUpdatedItem);
            Swal.fire('Salvo!', 'Alterações salvas.', 'success');
            cancelEdit();
        } else {
            throw new Error(`Erro ${response.status}: ${response.statusText}`);
        }
    } catch (error) {
        console.error('Erro ao salvar edição:', error);
        const errorMsg = error.response?.data?.message || error.message || 'Não foi possível salvar.';
        Swal.fire('Erro!', errorMsg, 'error');
    } finally {
        loading.value = false;
    }
}


function cancelEdit() {
    editingItemId.value = null;
    editFormData.value = {};
    editSearchQuery.value = '';
    isEditDropdownOpen.value = false;
}

// Watcher para CA
watch(() => editFormData.value.epiId, (newEpiId) => {
    if (editingItemId.value) {
        const selectedEpi = epis.value.find(e => e.id === newEpiId);
        editFormData.value.caDisplay = selectedEpi?.codigoAutenticacao || '';
    }
});

// --- Função Excluir ---
function handleDeleteEpi(item) {
    const nomeEpi = item.epi?.descricao || 'Item Desconhecido';
    Swal.fire({
        title: 'Você tem certeza?',
        text: `Deseja realmente excluir: ${nomeEpi}?`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Sim, excluir!',
        cancelButtonText: 'Cancelar'
    }).then(async (result) => {
        if (result.isConfirmed) {
            loading.value = true;
            try {
                const response = await fetch(`${backUrl}/api/movimentacao/${item.id}`, { method: 'DELETE' });
                if (!response.ok) {
                    const errorData = await response.text();
                    throw new Error(`Falha ao excluir: ${response.status} ${errorData}`);
                }
                movimentacoes.value = movimentacoes.value.filter(m => m.id !== item.id);
                Swal.fire('Excluído!', `'${nomeEpi}' foi excluído.`, 'success');
            } catch (error) {
                console.error('Erro ao excluir:', error);
                Swal.fire('Erro!', `Não foi possível excluir. ${error.message || ''}`, 'error');
            } finally {
                loading.value = false;
            }
        }
    });
}

// --- Funções do Modal ---
function openFormEntrega() { isFormEntregaVisible.value = true; }
function closeFormEntrega() { isFormEntregaVisible.value = false; fetchMovimentacoes(); }
function handleItemAdicionado(itemSalvo) { movimentacoes.value.unshift({ ...itemSalvo, epi: itemSalvo.epi || {} }); closeFormEntrega(); }
function handleItemAtualizado(itemAtualizado) {
    const index = movimentacoes.value.findIndex(m => m.id === itemAtualizado.id);
    if (index !== -1) {
        const newList = [...movimentacoes.value];
        newList[index] = { ...itemAtualizado, epi: itemAtualizado.epi || {} };
        movimentacoes.value = newList;
    }
}

// --- Função Auxiliar ---
const getNomeUnidade = (unidade) => {
    if (unidade && unidade.toLowerCase().includes('coca-cola')) return "Coca-Cola FEMSA Brasil";
    return unidade || "Não Informado";
};

// --- Handlers do Combobox de Edição ---
function selectEpiInEdit(epi) {
    editFormData.value.epiId = epi.id;
    isEditDropdownOpen.value = false;
    editSearchQuery.value = '';
    const currentDisplay = editDisplayValue.value; // Força re-render do display
}


function openEditDropdown() {
    isEditDropdownOpen.value = true;
    editSearchQuery.value = '';
}

function closeEditDropdown() {
    setTimeout(() => {
        const focusedElement = document.activeElement;
        if (!focusedElement?.closest('.edit-combobox-dropdown')) {
            isEditDropdownOpen.value = false;
        }
    }, 150);
}

// --- Vue-pad ---

const showSignatureModal = ref(false);
const assinaturaAtual = ref(null);
const assinaturaRowIndex = ref(null);

</script>

<template>
    <div @click.self="$emit('close')"
        class="fixed inset-0 z-40 bg-black bg-opacity-70 flex justify-center items-center p-4 transition-opacity duration-300">
        <div class="bg-white rounded-lg shadow-2xl w-[1400px] max-h-[90vh] overflow-hidden flex flex-col animate-fade-in">

            <!-- Cabeçalho do Modal -->
            <header class="bg-gray-100 p-4 flex justify-between items-center border-b border-gray-300">
                <h2 class="text-xl font-semibold text-gray-800">
                    Ficha de Controle de EPIs {{ loading ? '(Carregando...)' : '' }}
                </h2>
                <button @click="$emit('close')" class="text-gray-500 hover:text-red-600" title="Fechar">
                    <svg class="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                            d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"></path>
                    </svg>
                </button>
            </header>

            <main class="p-6 overflow-y-auto" style="font-family: Arial, sans-serif;">
                <!-- Cabeçalho da Ficha -->
                <div class="flex justify-between items-center border-b-2 border-black pb-2 mb-4">
                    <img src="/logo-femsa.png" alt="Coca-Cola FEMSA Brasil" class="h-10"
                        onerror="this.style.display='none'">
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
                        <div class="flex-1 min-w-[300px]"><strong>Função:</strong> {{ colaborador.funcao.nome || 'N/A' }}
                        </div>
                        <div class="flex-1 min-w-[200px]"><strong>Setor:</strong> {{ colaborador.setor || 'N/A' }}</div>
                    </div>
                    <div class="flex flex-wrap space-x-4 mt-1">
                        <div class="flex-1 min-w-[300px]"><strong>RE/CPF:</strong> {{ colaborador.re || 'N/A' }}</div>

                        <!-- 
                          ######################################################
                          # CORREÇÃO AQUI: Verifica 'data_admissao' e 'dataAdmissao'
                          ######################################################
                        -->
                        <div class="flex-1 min-w-[200px]"><strong>Data de Admissão:</strong> {{ (colaborador.data_admissao
                            || colaborador.dataAdmissao) ?
                            formatarDataDisplay(colaborador.data_admissao || colaborador.dataAdmissao) : '____/____/______'
                        }}</div>

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

                <!-- Tabela com Edição Inline -->
                <div class="overflow-x-auto">
                    <table class="min-w-full border-collapse border border-black text-sm">
                        <thead class="bg-gray-100">
                            <!-- 
                                    ######################################################
                                    # CORREÇÃO CABEÇALHO (THEAD)
                                    ######################################################
                                -->
                            <tr class="text-center font-bold">
                                <!-- Ajuste nas larguras e adicionado 'whitespace-normal' para permitir quebra -->
                                <th class="border border-black p-1 w-[90px] header-cell">DATA ENTREGA</th>
                                <th class="border border-black p-1 w-[50px] header-cell">QTDE</th>
                                <th class="border border-black p-1 header-cell">DESCRIÇÃO DO EPI</th>
                                <!-- Largura flexível -->
                                <th class="border border-black p-1 w-[70px] header-cell">CA</th>
                                <th class="border border-black p-1 w-[100px] header-cell">ASSINATURA</th>
                                <th class="border border-black p-1 w-[100px] header-cell">DATA DEVOLUÇÃO</th>
                                <th class="border border-black p-1 w-[100px] header-cell">RECEBEDOR</th>
                                <th class="border border-black p-1 w-[100px] header-cell">COD ENTREGA</th>
                                <th class="border border-black p-1 w-[100px] header-cell">COD DEVOLUÇÃO</th>
                                <th class="border border-black p-1 w-[80px] header-cell">AÇÕES</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="item in movimentacoes" :key="item.id" class="h-9 hover:bg-gray-50"
                                :class="{ 'bg-yellow-100': editingItemId === item.id }">

                                <!-- Data Entrega -->
                                <td class="border border-black p-0 text-center align-top">
                                    <input v-if="editingItemId === item.id" type="date" v-model="editFormData.dataEntrega"
                                        class="w-full h-full p-1 text-center input-edit" />
                                    <span v-else class="p-1 block">{{ formatarDataDisplay(item.dataEntrega) }}</span>
                                </td>

                                <!-- Quantidade -->
                                <td class="border border-black p-0 text-center align-top">
                                    <input v-if="editingItemId === item.id" type="number" min="1"
                                        v-model.number="editFormData.quantidade"
                                        class="w-full h-full p-1 text-center input-edit" />
                                    <span v-else class="p-1 block">{{ item.quantidade || 1 }}</span>
                                </td>

                                <!-- Descrição (Combobox EPI) -->
                                <td class="border border-black p-0 relative align-top">
                                    <div v-if="editingItemId === item.id" class="relative w-full h-full">
                                        <input type="text" :value="editDisplayValue"
                                            @input="editDisplayValue = $event.target.value; isEditDropdownOpen = true"
                                            @focus="openEditDropdown" @blur="closeEditDropdown"
                                            placeholder="Buscar ou Selecionar EPI..."
                                            class="w-full h-full p-1 input-edit bg-white" />
                                        <div v-if="isEditDropdownOpen"
                                            class="absolute left-0 right-0 z-20 mt-1 bg-white border border-gray-300 rounded-md shadow-lg max-h-48 overflow-y-auto edit-combobox-dropdown">
                                            <ul class="py-1">
                                                <li v-if="!editSearchQuery" class="px-3 py-2 text-xs text-gray-500">
                                                    Mostrando 10 primeiros...
                                                </li>
                                                <li v-if="filteredEpisInEdit.length === 0 && editSearchQuery"
                                                    class="px-3 py-2 text-xs text-gray-500">
                                                    Nenhum resultado para "{{ editSearchQuery }}"
                                                </li>
                                                <li v-for="epi in (editSearchQuery ? filteredEpisInEdit : filteredEpisInEdit.slice(0, 10))"
                                                    :key="epi.id" @mousedown.prevent="selectEpiInEdit(epi)"
                                                    class="px-3 py-1 text-sm text-gray-800 cursor-pointer hover:bg-blue-100">
                                                    {{ epi.descricao }} (CA: {{ epi.codigoAutenticacao }})
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                    <span v-else class="p-1 block">{{ item.epi?.descricao || 'N/A' }}</span>
                                </td>


                                <!-- CA (Automático) -->
                                <td class="border border-black p-1 text-center align-top">
                                    <span v-if="editingItemId === item.id">{{ editedCaDisplay }}</span>
                                    <span v-else>{{ item.epi?.codigoAutenticacao || 'N/A' }}</span>
                                </td>

                                <!-- Assinatura (Status) -->
                                <td class="text-center align-middle">
                                    <template v-if="row.assinatura">
                                        <img :src="row.assinatura" class="h-10 mx-auto" />
                                    </template>

                                    <template v-else>
                                        <button class="bg-green-600 text-white px-3 py-1 rounded" @click="abrirPad(index)">
                                            Assinar
                                        </button>
                                    </template>
                                </td>

                                <!-- Data Devolução -->
                                <td class="border border-black p-0 text-center align-top">
                                    <input v-if="editingItemId === item.id" type="date"
                                        v-model="editFormData.dataProximaEntrega"
                                        class="w-full h-full p-1 text-center input-edit" />
                                    <span v-else class="p-1 block">{{ formatarDataDisplay(item.dataProximaEntrega) }}</span>
                                </td>

                                <!-- Recebedor -->
                                <td class="border border-black p-0 text-center align-top">
                                    <input v-if="editingItemId === item.id" type="text" v-model="editFormData.recebedor"
                                        class="w-full h-full p-1 text-center input-edit" />
                                    <span v-else class="p-1 block">{{ item.recebedor || '' }}</span>
                                </td>

                                <!-- Código Entrega -->
                                <td class="border border-black p-0 text-center align-top">
                                    <input v-if="editingItemId === item.id" type="text" pattern="[0-9]*" inputmode="numeric"
                                        v-model="editFormData.cod_entrega"
                                        class="w-full h-full p-1 text-center input-edit" />
                                    <span v-else class="p-1 block">{{ item.cod_entrega || '' }}</span>
                                </td>

                                <!-- Código Devolução -->
                                <td class="border border-black p-0 text-center align-top">
                                    <input v-if="editingItemId === item.id" type="text" pattern="[0-9]*" inputmode="numeric"
                                        v-model="editFormData.cod_devolucao"
                                        class="w-full h-full p-1 text-center input-edit" />
                                    <span v-else class="p-1 block">{{ item.cod_devolucao || '' }}</span>
                                </td>

                                <!-- Ações -->
                                <td class="border border-black p-1 text-center whitespace-nowrap align-top">
                                    <div v-if="editingItemId === item.id" class="flex justify-center items-center gap-1">
                                        <button @click="saveEdit()" title="Salvar" :disabled="loading"
                                            class="text-green-600 hover:text-green-800 p-1 disabled:opacity-50">
                                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M5 13l4 4L19 7"></path>
                                            </svg>
                                        </button>
                                        <button @click="cancelEdit()" title="Cancelar" :disabled="loading"
                                            class="text-red-600 hover:text-red-800 p-1 disabled:opacity-50">
                                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M6 18L18 6M6 6l12 12"></path>
                                            </svg>
                                        </button>
                                    </div>
                                    <div v-else class="flex justify-center items-center gap-1">
                                        <button @click="startEdit(item)" title="Editar" :disabled="loading"
                                            class="text-blue-600 hover:text-blue-800 p-1 disabled:opacity-50">
                                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.536L16.732 3.732z">
                                                </path>
                                            </svg>
                                        </button>
                                        <button @click="handleDeleteEpi(item)" title="Excluir" :disabled="loading"
                                            class="text-red-600 hover:text-red-800 p-1 disabled:opacity-50">
                                            <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16">
                                                </path>
                                            </svg>
                                        </button>
                                    </div>
                                </td>
                            </tr>

                            <!-- Linhas em branco -->
                            <tr v-for="n in linhasEmBranco" :key="`blank-${n}`" class="h-9">
                                <td v-for="i in 10" :key="i" class="border border-black"></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </main>

        </div>
    </div>

    <!-- Modal para Adicionar Item -->
    <FormularioEntregaEpi
        v-if="isFormEntregaVisible"
        :funcionarioId="colaborador.funcionarioId"
        :itemParaEditar="null"
        @close="closeFormEntrega"
        @itemAdicionado="handleItemAdicionado"
        @itemAtualizado="handleItemAtualizado"
    />
</template>

<style scoped>
/* Estilos existentes */
@keyframes fade-in {
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
}

table {
    width: 100%;
    table-layout: fixed;
}

/* Ajuste no TD: Permitir quebra, mas evitar overflow se nowrap for forçado em outro lugar */
td {
    white-space: normal;
    vertical-align: top;
    word-wrap: break-word;
}

/* Permite quebra */
/* Ajuste no TH (cabeçalho) */
th.header-cell {
    white-space: normal;
    /* Permite quebra de linha no cabeçalho */
    word-wrap: break-word;
    /* Força quebra se necessário */
    vertical-align: middle;
    /* Centraliza verticalmente o texto */
    text-align: center;
    /* Centraliza horizontalmente */
}


/* Classe comum para inputs de edição */
.input-edit {
    box-sizing: border-box;
    border: 1px solid #93c5fd;
    border-radius: 0.25rem;
    outline: none;
    background-color: #eff6ff;
    transition: border-color 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
    display: block;
    width: 100%;
    height: 2.25rem;
    /* ~h-9 */
    line-height: normal;
    font-size: 0.875rem;
    /* text-sm */
    padding: 0.25rem 0.5rem;
    /* p-1 px-2 */
}

.input-edit:focus {
    border-color: #3b82f6;
    box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.4);
}

/* Ajustes específicos para inputs na tabela */
td select.input-edit {
    padding-right: 1.5rem;
}

td input[type="date"].input-edit {
    padding: 0.15rem 0.5rem;
    vertical-align: middle;
}


/* Remove setas do input number */
td input[type=number]::-webkit-inner-spin-button,
td input[type=number]::-webkit-outer-spin-button {
    -webkit-appearance: none;
    margin: 0;
}

td input[type=number] {
    -moz-appearance: textfield;
}

/* Estilo para dropdown do combobox */
.edit-combobox-dropdown {
    z-index: 20;
}
</style>
