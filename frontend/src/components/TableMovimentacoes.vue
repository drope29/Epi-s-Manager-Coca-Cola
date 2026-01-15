<script setup>
import { ref, onMounted, computed, watch, nextTick } from 'vue';
import axios from 'axios';
import { VueSignaturePad } from 'vue-signature-pad';
import FormularioEntregaEpi from './FormularioEntregaEpi.vue';
import Swal from 'sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';
import { S3Client, PutObjectCommand } from "@aws-sdk/client-s3";

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

// --- ESTADOS DA ASSINATURA ---
const isSignatureModalOpen = ref(false);
const itemToSign = ref(null);
const signaturePadRef = ref(null);

const s3Client = new S3Client({
    region: "us-east-1",
    endpoint: "http://localhost:4566",
    credentials: {
        accessKeyId: "test",
        secretAccessKey: "test"
    },
    forcePathStyle: true 
});
const BUCKET_NAME = "assinaturas-bucket";

// --- Carregamento Inicial ---
onMounted(async () => {
    await fetchMovimentacoes();
    await fetchEpis();
});

// --- Funções API ---
async function fetchMovimentacoes() {
    loading.value = true;
    try {
        const token = localStorage.getItem('token');
        if (!token) {
            Swal.fire('Erro', 'Token não encontrado. Faça login novamente.', 'error');
            return;
        }

        const response = await fetch(`${backUrl}/api/movimentacao/`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            if (response.status === 401) {
                Swal.fire('Sessão Expirada', 'Faça login novamente.', 'warning');
            }
            console.warn(`Erro na API: ${response.status}`);
            movimentacoes.value = [];
        } else {
            const allData = await response.json();
            const idColab = props.colaborador.funcionarioId || props.colaborador.id;

            const filteredData = allData.filter(item => {
                const itemFuncId = item.funcionario?.id || item.funcionario?.funcionarioId;
                return itemFuncId === idColab;
            });

            movimentacoes.value = filteredData.map(item => ({ ...item, epi: item.epi || {} }));
        }
    } catch (error) {
        console.error('Erro ao carregar movimentações:', error);
        movimentacoes.value = [];
        Swal.fire('Erro', 'Não foi possível carregar as movimentações.', 'error');
    } finally {
        loading.value = false;
    }
}

async function fetchEpis() {
    try {
        const token = localStorage.getItem('token');
        const response = await axios.get(`${backUrl}/api/epis/`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        epis.value = response.data;
    } catch (error) {
        console.error("Erro ao buscar EPIs:", error);
    }
}

// --- Helpers de Formatação ---
const formatarDataDisplay = (dataStr) => {
    if (!dataStr) return "";
    let date;
    try {
        if (dataStr.includes('-')) {
            const dateParts = dataStr.split('T')[0].split('-');
            date = new Date(Date.UTC(parseInt(dateParts[0]), parseInt(dateParts[1]) - 1, parseInt(dateParts[2])));
        } else if (dataStr.includes('/')) {
            const dateParts = dataStr.split('/');
            date = new Date(Date.UTC(parseInt(dateParts[2]), parseInt(dateParts[1]) - 1, parseInt(dateParts[0])));
        } else {
            date = new Date(dataStr);
        }
        if (isNaN(date.getTime())) return dataStr;
        return date.toLocaleDateString('pt-BR', { timeZone: 'UTC' });
    } catch (e) {
        return dataStr;
    }
};

const formatarDataInput = (dataStr) => {
    if (!dataStr) return "";
    let date;
    try {
        if (dataStr.includes('-')) {
            const dateParts = dataStr.split('T')[0].split('-');
            date = new Date(Date.UTC(parseInt(dateParts[0]), parseInt(dateParts[1]) - 1, parseInt(dateParts[2])));
        } else if (dataStr.includes('/')) {
            const dateParts = dataStr.split('/');
            date = new Date(Date.UTC(parseInt(dateParts[2]), parseInt(dateParts[1]) - 1, parseInt(dateParts[0])));
        } else {
            date = new Date(dataStr);
        }
        if (isNaN(date.getTime())) return '';
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

const filteredEpisInEdit = computed(() => {
    const query = editSearchQuery.value.toLowerCase().trim();
    if (!query) {
        return epis.value;
    }
    return epis.value.filter(epi => {
        const descricao = epi.descricao ? epi.descricao.toLowerCase() : '';
        const ca = epi.codigoAutenticacao ? epi.codigoAutenticacao.toString().toLowerCase() : '';
        return descricao.includes(query) || ca.includes(query);
    });
});

const editedCaDisplay = computed(() => {
    if (!editingItemId.value || !editFormData.value.epiId) return '';
    const selectedEpi = epis.value.find(e => (e.id || e.epiId) == editFormData.value.epiId);
    return selectedEpi?.codigoAutenticacao || '';
});


// --- Funções de Edição Inline ---
function startEdit(item) {
    const idCorreto = item.movimentacaoId || item.id;
    editingItemId.value = idCorreto;

    const epiIdInicial = item.epi?.id || item.epi?.epiId || null;

    editFormData.value = {
        id: idCorreto,
        dataEntrega: formatarDataInput(item.dataEntrega),
        epiId: epiIdInicial,
        status: item.status || '',
        dataProximaEntrega: formatarDataInput(item.dataProximaEntrega),
        quantidade: item.quantidade || 1,
        recebedor: item.recebedor || '',
        cod_entrega: item.cod_entrega || '',
        cod_devolucao: item.cod_devolucao || '',
        funcionarioId: item.funcionario?.id || props.colaborador.funcionarioId || props.colaborador.id,
        caDisplay: item.epi?.codigoAutenticacao || '',
        assinatura: item.assinatura || null
    };

    if (item.epi && item.epi.descricao) {
        editSearchQuery.value = `${item.epi.descricao} (CA: ${item.epi.codigoAutenticacao || 'N/A'})`;
    } else {
        editSearchQuery.value = '';
    }

    isEditDropdownOpen.value = false;
    if (epis.value.length === 0) fetchEpis();
}

function selectEpiInEdit(epi) {
    const idDoEpi = epi.id || epi.epiId;
    editFormData.value.epiId = idDoEpi;
    editSearchQuery.value = `${epi.descricao} (CA: ${epi.codigoAutenticacao})`;
    isEditDropdownOpen.value = false;
}

async function saveEdit() {
    if (!editFormData.value.epiId) { Swal.fire('Atenção', 'Selecione um EPI da lista.', 'warning'); return; }

    const quantidadeNum = parseInt(editFormData.value.quantidade, 10);
    if (isNaN(quantidadeNum) || quantidadeNum <= 0) { Swal.fire('Atenção', 'Quantidade inválida.', 'warning'); return; }

    loading.value = true;

    const payloadBackend = {
        dataEntrega: editFormData.value.dataEntrega || null,
        epiId: editFormData.value.epiId,
        funcionarioId: editFormData.value.funcionarioId,
        status: editFormData.value.status || null,
        quantidade: quantidadeNum,
        dataProximaEntrega: editFormData.value.dataProximaEntrega || null,
        assinatura: editFormData.value.assinatura
    };

    try {
        const token = localStorage.getItem('token');
        const response = await axios.put(`${backUrl}/api/movimentacao/${editingItemId.value}`, payloadBackend, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.status >= 200 && response.status < 300) {
            const updatedItemFromServer = response.data || {};
            const index = movimentacoes.value.findIndex(m => (m.movimentacaoId || m.id) === editingItemId.value);

            if (index !== -1) {
                const itemAtual = movimentacoes.value[index];
                const epiAtualizado = epis.value.find(e => (e.id || e.epiId) == editFormData.value.epiId) || {};

                movimentacoes.value[index] = {
                    ...itemAtual,
                    ...editFormData.value,
                    epi: epiAtualizado,
                    dataEntrega: editFormData.value.dataEntrega ? new Date(editFormData.value.dataEntrega + 'T00:00:00Z').toISOString() : null,
                    dataProximaEntrega: editFormData.value.dataProximaEntrega ? new Date(editFormData.value.dataProximaEntrega + 'T00:00:00Z').toISOString() : null,
                    ...updatedItemFromServer,
                };
            }
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

watch(() => editFormData.value.epiId, (newEpiId) => {
    if (editingItemId.value) {
        const selectedEpi = epis.value.find(e => (e.id || e.epiId) == newEpiId);
        editFormData.value.caDisplay = selectedEpi?.codigoAutenticacao || '';
    }
});

function handleDeleteEpi(item) {
    const nomeEpi = item.epi?.descricao || 'Item Desconhecido';
    const idParaExcluir = item.movimentacaoId || item.id;

    if (!idParaExcluir) {
        Swal.fire('Erro', 'ID da movimentação não encontrado.', 'error');
        return;
    }

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
                const token = localStorage.getItem('token');
                const response = await fetch(`${backUrl}/api/movimentacao/${idParaExcluir}`, {
                    method: 'DELETE',
                    headers: { 'Authorization': `Bearer ${token}` }
                });

                if (!response.ok) {
                    if (response.status === 401) throw new Error('Sessão Expirada (401)');
                    const errorData = await response.text();
                    throw new Error(`Falha ao excluir: ${response.status} ${errorData}`);
                }
                movimentacoes.value = movimentacoes.value.filter(m => (m.movimentacaoId || m.id) !== idParaExcluir);
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

function openFormEntrega() { isFormEntregaVisible.value = true; }
function closeFormEntrega() { isFormEntregaVisible.value = false; fetchMovimentacoes(); }
function handleItemAdicionado(itemSalvo) { movimentacoes.value.unshift({ ...itemSalvo, epi: itemSalvo.epi || {} }); closeFormEntrega(); }
function handleItemAtualizado(itemAtualizado) {
    const idKey = itemAtualizado.movimentacaoId ? 'movimentacaoId' : 'id';
    const index = movimentacoes.value.findIndex(m => m[idKey] === itemAtualizado[idKey]);
    if (index !== -1) {
        const newList = [...movimentacoes.value];
        newList[index] = { ...itemAtualizado, epi: itemAtualizado.epi || {} };
        movimentacoes.value = newList;
    }
}

const getNomeUnidade = (unidade) => {
    if (unidade && unidade.toLowerCase().includes('coca-cola')) return "Coca-Cola FEMSA Brasil";
    return unidade || "Não Informado";
};

function openEditDropdown() { isEditDropdownOpen.value = true; }
function closeEditDropdown() {
    setTimeout(() => {
        const focusedElement = document.activeElement;
        if (!focusedElement?.closest('.edit-combobox-dropdown')) {
            isEditDropdownOpen.value = false;
        }
    }, 150);
}

// =========================================================
// === LÓGICA DE ASSINATURA =================================
// =========================================================

function abrirPad(item) {
    itemToSign.value = item;
    isSignatureModalOpen.value = true;

    setTimeout(() => {
        if (signaturePadRef.value) {
            signaturePadRef.value.resizeCanvas();
        }
    }, 50);
}

function closeSignatureModal() {
    isSignatureModalOpen.value = false;
    itemToSign.value = null;
}

function limparAssinatura() {
    if (signaturePadRef.value) {
        signaturePadRef.value.clearSignature();
    }
}

const dataURLtoUint8Array = (dataURL) => {
    const arr = dataURL.split(',');
    const bstr = atob(arr[1]);
    let n = bstr.length;
    const u8arr = new Uint8Array(n);
    while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
    }
    return u8arr;
};

async function salvarAssinatura() {
    if (!signaturePadRef.value) return;

    // 1. Pega assinatura em PNG (Base64)
    const { isEmpty, data } = signaturePadRef.value.saveSignature("image/png");

    if (isEmpty) {
        Swal.fire('Atenção', 'Por favor, assine antes de salvar.', 'warning');
        return;
    }

    if (!itemToSign.value) return;

    loading.value = true;
    const idItem = itemToSign.value.movimentacaoId || itemToSign.value.id;

    try {
        // --- 2. UPLOAD DIRETO PRO S3 (LOCALSTACK) ---
        const fileName = `assinatura_${idItem}_${Date.now()}.png`;

        // CONVERSÃO CORRIGIDA: Usa Uint8Array em vez de Blob
        const fileBuffer = dataURLtoUint8Array(data);

        const command = new PutObjectCommand({
            Bucket: BUCKET_NAME,
            Key: fileName,
            Body: fileBuffer, // Enviando bytes puros
            ContentType: "image/png",
            ACL: 'public-read'
        });

        await s3Client.send(command);

        // --- 3. GERA URL DO ARQUIVO ---
        const imageUrl = `http://localhost:4566/${BUCKET_NAME}/${fileName}`;
        console.log("Upload S3 Sucesso! URL:", imageUrl);

        // --- 4. ENVIA APENAS A URL PARA O BACKEND ---
        const token = localStorage.getItem('token');

        const payload = {
            dataEntrega: itemToSign.value.dataEntrega,
            epiId: itemToSign.value.epi?.id || itemToSign.value.epi?.epiId,
            funcionarioId: props.colaborador.funcionarioId || props.colaborador.id,
            status: itemToSign.value.status,
            quantidade: itemToSign.value.quantidade,
            dataProximaEntrega: itemToSign.value.dataProximaEntrega,
            recebedor: itemToSign.value.recebedor,
            cod_entrega: itemToSign.value.cod_entrega,
            cod_devolucao: itemToSign.value.cod_devolucao,
            assinatura: imageUrl // Enviando o link
        };

        const response = await axios.put(`${backUrl}/api/movimentacao/${idItem}`, payload, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            }
        });

        if (response.status >= 200 && response.status < 300) {
            const index = movimentacoes.value.findIndex(m => (m.movimentacaoId || m.id) === idItem);
            if (index !== -1) {
                movimentacoes.value[index].assinatura = imageUrl;
            }
            Swal.fire('Sucesso', 'Assinatura salva com sucesso!', 'success');
            closeSignatureModal();
        } else {
            throw new Error('Falha ao salvar link da assinatura no banco');
        }

    } catch (error) {
        console.error("Erro no processo de assinatura:", error);
        // Mostra o erro real se for do S3
        const msg = error.message || 'Não foi possível salvar a assinatura.';
        Swal.fire('Erro', msg, 'error');
    } finally {
        loading.value = false;
    }
}

</script>

<template>
    <div @click.self="$emit('close')"
        class="fixed inset-0 z-40 bg-black bg-opacity-70 flex justify-center items-center p-4 transition-opacity duration-300">
        <div class="bg-white rounded-lg shadow-2xl w-[1400px] max-h-[90vh] overflow-hidden flex flex-col animate-fade-in">

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
                        <div class="flex-1 min-w-[200px]"><strong>Unidade:</strong> {{
                            getNomeUnidade(colaborador.unidade) }}</div>
                    </div>
                    <div class="flex flex-wrap space-x-4">
                        <div class="flex-1 min-w-[300px]"><strong>Função:</strong> {{ colaborador.funcao.nome || 'N/A'
                        }}</div>
                        <div class="flex-1 min-w-[200px]"><strong>Setor:</strong> {{ colaborador.setor || 'N/A' }}</div>
                    </div>
                    <div class="flex flex-wrap space-x-4 mt-1">
                        <div class="flex-1 min-w-[300px]"><strong>RE/CPF:</strong> {{ colaborador.re || 'N/A' }}</div>
                        <div class="flex-1 min-w-[200px]"><strong>Data de Admissão:</strong> {{
                            (colaborador.data_admissao || colaborador.dataAdmissao) ?
                            formatarDataDisplay(colaborador.data_admissao || colaborador.dataAdmissao) :
                            '____/____/______' }}</div>
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
                                <th class="border border-black p-1 w-[90px] header-cell">DATA ENTREGA</th>
                                <th class="border border-black p-1 w-[50px] header-cell">QTDE</th>
                                <th class="border border-black p-1 header-cell">DESCRIÇÃO DO EPI</th>
                                <th class="border border-black p-1 w-[70px] header-cell">CA</th>
                                <th class="border border-black p-1 w-[160px] header-cell">ASSINATURA</th>
                                <th class="border border-black p-1 w-[100px] header-cell">DATA DEVOLUÇÃO</th>
                                <th class="border border-black p-1 w-[100px] header-cell">RECEBEDOR</th>
                                <th class="border border-black p-1 w-[100px] header-cell">COD ENTREGA</th>
                                <th class="border border-black p-1 w-[100px] header-cell">COD DEVOLUÇÃO</th>
                                <th class="border border-black p-1 w-[80px] header-cell">AÇÕES</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="item in movimentacoes" :key="item.movimentacaoId || item.id"
                                class="h-9 hover:bg-gray-50"
                                :class="{ 'bg-yellow-100': editingItemId === (item.movimentacaoId || item.id) }">

                                <td class="border border-black p-0 text-center align-middle">
                                    <input v-if="editingItemId === (item.movimentacaoId || item.id)" type="date"
                                        v-model="editFormData.dataEntrega"
                                        class="w-full h-full p-1 text-center input-edit" />
                                    <span v-else class="p-1 block">{{ formatarDataDisplay(item.dataEntrega) }}</span>
                                </td>

                                <td class="border border-black p-0 text-center align-middle">
                                    <input v-if="editingItemId === (item.movimentacaoId || item.id)" type="number" min="1"
                                        v-model.number="editFormData.quantidade"
                                        class="w-full h-full p-1 text-center input-edit" />
                                    <span v-else class="p-1 block">{{ item.quantidade || 1 }}</span>
                                </td>

                                <td class="border border-black p-0 relative align-middle">
                                    <div v-if="editingItemId === (item.movimentacaoId || item.id)"
                                        class="relative w-full h-full">
                                        <input type="text" v-model="editSearchQuery" @focus="openEditDropdown"
                                            @blur="closeEditDropdown" placeholder="Buscar ou Selecionar EPI..."
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
                                                    :key="epi.id || epi.epiId" @mousedown.prevent="selectEpiInEdit(epi)"
                                                    class="px-3 py-1 text-sm text-gray-800 cursor-pointer hover:bg-blue-100">
                                                    {{ epi.descricao }} (CA: {{ epi.codigoAutenticacao }})
                                                </li>
                                            </ul>
                                        </div>
                                    </div>
                                    <span v-else class="p-1 block">{{ item.epi?.descricao || 'N/A' }}</span>
                                </td>

                                <td class="border border-black p-1 text-center align-middle">
                                    <span v-if="editingItemId === (item.movimentacaoId || item.id)">{{ editedCaDisplay
                                    }}</span>
                                    <span v-else>{{ item.epi?.codigoAutenticacao || 'N/A' }}</span>
                                </td>

                                <td class="text-center align-middle border border-black p-2 bg-gray-50">
                                    <template v-if="item.assinatura">
                                        <div class="flex flex-col items-center justify-center w-full h-full">
                                            <img :src="item.assinatura"
                                                class="h-16 w-auto max-w-full object-contain bg-white rounded-md border border-gray-300 p-1"
                                                alt="Assinatura" />
                                        </div>
                                    </template>
                                    <template v-else>
                                        <button
                                            class="bg-green-600 hover:bg-green-700 text-white font-bold text-sm py-2 px-6 rounded shadow uppercase tracking-wide transition-all hover:scale-105"
                                            @click="abrirPad(item)">
                                            ASSINAR
                                        </button>
                                    </template>
                                </td>
                                <td class="border border-black p-0 text-center align-middle">
                                    <input v-if="editingItemId === (item.movimentacaoId || item.id)" type="date"
                                        v-model="editFormData.dataProximaEntrega"
                                        class="w-full h-full p-1 text-center input-edit" />
                                    <span v-else class="p-1 block">{{ formatarDataDisplay(item.dataProximaEntrega)
                                    }}</span>
                                </td>

                                <td class="border border-black p-0 text-center align-middle">
                                    <input v-if="editingItemId === (item.movimentacaoId || item.id)" type="text"
                                        v-model="editFormData.recebedor" class="w-full h-full p-1 text-center input-edit" />
                                    <span v-else class="p-1 block">{{ item.recebedor || '' }}</span>
                                </td>

                                <td class="border border-black p-0 text-center align-middle">
                                    <input v-if="editingItemId === (item.movimentacaoId || item.id)" type="text"
                                        pattern="[0-9]*" inputmode="numeric" v-model="editFormData.cod_entrega"
                                        class="w-full h-full p-1 text-center input-edit" />
                                    <span v-else class="p-1 block">{{ item.cod_entrega || '' }}</span>
                                </td>

                                <td class="border border-black p-0 text-center align-middle">
                                    <input v-if="editingItemId === (item.movimentacaoId || item.id)" type="text"
                                        pattern="[0-9]*" inputmode="numeric" v-model="editFormData.cod_devolucao"
                                        class="w-full h-full p-1 text-center input-edit" />
                                    <span v-else class="p-1 block">{{ item.cod_devolucao || '' }}</span>
                                </td>

                                <td class="border border-black p-1 text-center whitespace-nowrap align-middle">
                                    <div v-if="editingItemId === (item.movimentacaoId || item.id)"
                                        class="flex justify-center items-center gap-1">
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

                            <tr v-for="n in linhasEmBranco" :key="`blank-${n}`" class="h-9">
                                <td v-for="i in 10" :key="i" class="border border-black"></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
    </div>

    <FormularioEntregaEpi v-if="isFormEntregaVisible" :funcionarioId="colaborador.funcionarioId || colaborador.id"
        :itemParaEditar="null" @close="closeFormEntrega" @itemAdicionado="handleItemAdicionado"
        @itemAtualizado="handleItemAtualizado" />

    <div v-if="isSignatureModalOpen" class="fixed inset-0 z-50 bg-black bg-opacity-80 flex justify-center items-center p-4">

        <div class="bg-white rounded-lg shadow-xl w-full max-w-5xl flex flex-col">
            <div class="p-4 border-b flex justify-between items-center bg-gray-100 rounded-t-lg">
                <h3 class="text-2xl font-bold text-gray-800">Assinatura Digital</h3>
                <button @click="closeSignatureModal" class="text-gray-500 hover:text-red-500">
                    <svg class="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12">
                        </path>
                    </svg>
                </button>
            </div>

            <div class="p-6 flex flex-col items-center justify-center bg-white">
                <p class="text-lg text-gray-600 mb-2 font-medium">Assine no quadro abaixo:</p>
                <div
                    class="border-2 border-dashed border-gray-400 rounded w-full h-96 bg-gray-50 cursor-crosshair shadow-inner">
                    <VueSignaturePad width="100%" height="100%" ref="signaturePadRef"
                        :options="{ penColor: 'black', minWidth: 2, maxWidth: 4 }" />
                </div>
            </div>

            <div class="p-6 border-t flex justify-between bg-gray-50 rounded-b-lg">
                <button @click="limparAssinatura"
                    class="px-6 py-3 bg-gray-300 text-gray-700 rounded-md hover:bg-gray-400 text-base font-semibold transition-colors">
                    Limpar
                </button>
                <div class="flex gap-4">
                    <button @click="closeSignatureModal"
                        class="px-6 py-3 bg-red-100 text-red-600 rounded-md hover:bg-red-200 text-base font-semibold transition-colors">
                        Cancelar
                    </button>
                    <button @click="salvarAssinatura" :disabled="loading"
                        class="px-8 py-3 bg-gradient-to-r from-green-500 to-emerald-500 text-white rounded-md hover:opacity-90 text-lg font-bold shadow-md transition-all duration-200 disabled:opacity-50 disabled:cursor-not-allowed">
                        {{ loading ? 'Salvando...' : 'Salvar Assinatura' }}
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
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

td {
    white-space: normal;
    vertical-align: top;
    word-wrap: break-word;
}

th.header-cell {
    white-space: normal;
    word-wrap: break-word;
    vertical-align: middle;
    text-align: center;
}

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
    line-height: normal;
    font-size: 0.875rem;
    padding: 0.25rem 0.5rem;
}

.input-edit:focus {
    border-color: #3b82f6;
    box-shadow: 0 0 0 2px rgba(59, 130, 246, 0.4);
}

td select.input-edit {
    padding-right: 1.5rem;
}

td input[type="date"].input-edit {
    padding: 0.15rem 0.5rem;
    vertical-align: middle;
}

td input[type=number]::-webkit-inner-spin-button,
td input[type=number]::-webkit-outer-spin-button {
    -webkit-appearance: none;
    margin: 0;
}

td input[type=number] {
    -moz-appearance: textfield;
}

.edit-combobox-dropdown {
    z-index: 20;
}</style>