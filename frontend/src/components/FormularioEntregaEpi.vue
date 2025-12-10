<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import axios from 'axios';
import Swal from 'sweetalert2';

// --- Props e Emits ---
const props = defineProps({
    funcionarioId: { type: [Number, String], required: true },
    itemParaEditar: { type: Object, default: null }
});
const emit = defineEmits(['close', 'itemAdicionado', 'itemAtualizado']);

// --- Configuração ---
const backUrl = import.meta.env.VITE_BACKEND_URL;
const loading = ref(false);
const epis = ref([]);
const isDropdownOpen = ref(false);
const searchQuery = ref(''); // Texto que aparece no input

const isEditMode = computed(() => !!props.itemParaEditar);

const form = ref({
    dataEntrega: new Date().toISOString().split('T')[0],
    epiId: null,
    funcionarioId: props.funcionarioId,
    status: 'ASSINADO'
});

// --- Filtro de EPIs ---
const filteredEpis = computed(() => {
    const query = searchQuery.value.toLowerCase().trim();
    if (!query) return epis.value;
    return epis.value.filter(epi => {
        const descricao = (epi.descricao || '').toLowerCase();
        const ca = (epi.codigoAutenticacao?.toString() || '').toLowerCase();
        return descricao.includes(query) || ca.includes(query);
    });
});

// --- Inicialização ---
onMounted(async () => {
    await fetchEpis(); // Espera carregar os EPIs primeiro

    if (isEditMode.value) {
        const item = props.itemParaEditar;

        // Formatar Data
        let dataFormatada = item.dataEntrega;
        try {
            if (item.dataEntrega.includes('T')) dataFormatada = item.dataEntrega.split('T')[0];
        } catch (e) { }

        // Recuperar ID do EPI
        const currentEpiId = item.epi?.epiId || item.epi?.id || null;

        // Tenta achar o nome do EPI na lista carregada para preencher o input
        const epiEncontrado = epis.value.find(e => (e.epiId || e.id) == currentEpiId);

        if (epiEncontrado) {
            searchQuery.value = `${epiEncontrado.descricao} (CA: ${epiEncontrado.codigoAutenticacao})`;
        } else if (item.epi?.descricao) {
            // Fallback se não achou na lista mas tem descrição
            searchQuery.value = item.epi.descricao;
        }

        form.value = {
            dataEntrega: dataFormatada,
            epiId: currentEpiId,
            funcionarioId: props.funcionarioId,
            status: item.status || 'ASSINADO'
        };
    }
});

// --- API ---
async function fetchEpis() {
    try {
        const token = localStorage.getItem('token');
        const response = await axios.get(`${backUrl}/api/epis/`, {
            headers: { Authorization: `Bearer ${token}` }
        });
        epis.value = response.data;
    } catch (error) {
        console.error("Erro EPIs:", error);
    }
}

// --- Lógica do Combobox (CORRIGIDA) ---

function openDropdown() {
    isDropdownOpen.value = true;
    // Se o usuário clicar para abrir e já tiver algo selecionado, 
    // opcionalmente limpamos para ele buscar outro. 
    // Se preferir manter o texto, comente a linha abaixo:
    if (form.value.epiId) searchQuery.value = '';
}

function closeDropdown() {
    // Timeout para dar tempo do clique na lista acontecer antes de fechar
    setTimeout(() => { isDropdownOpen.value = false; }, 200);
}

function handleInput(event) {
    searchQuery.value = event.target.value;
    isDropdownOpen.value = true;
    // Se digitou, limpa o ID selecionado anteriormente para forçar nova escolha
    form.value.epiId = null;
}

function selectEpi(epi) {
    console.log("Selecionado:", epi); // Debug no console

    // 1. Salva o ID
    form.value.epiId = epi.epiId || epi.id;

    // 2. Atualiza o texto do input visualmente
    searchQuery.value = `${epi.descricao} (CA: ${epi.codigoAutenticacao})`;

    // 3. Fecha a lista
    isDropdownOpen.value = false;
}

// --- Salvar ---
async function handleSave() {
    if (loading.value) return;

    // Validação extra: O campo texto tem algo, mas nenhum ID foi vinculado?
    if (searchQuery.value && !form.value.epiId) {
        Swal.fire('Atenção', 'Você digitou um EPI mas não clicou na lista para selecionar. Por favor, selecione uma opção.', 'warning');
        return;
    }

    if (!form.value.dataEntrega || !form.value.epiId) {
        Swal.fire('Atenção', 'Preencha a data e selecione um EPI.', 'warning');
        return;
    }

    loading.value = true;

    const payload = {
        dataEntrega: form.value.dataEntrega,
        epi: form.value.epiId, // Envia apenas o ID
        funcionario: form.value.funcionarioId,
        status: form.value.status
    };

    try {
        const token = localStorage.getItem('token');
        const config = { headers: { Authorization: `Bearer ${token}` } };

        if (isEditMode.value) {
            const itemId = props.itemParaEditar.movimentacaoId || props.itemParaEditar.id;
            const response = await axios.put(`${backUrl}/api/movimentacao/${itemId}`, payload, config);
            emit('itemAtualizado', response.data);
            Swal.fire('Sucesso', 'Atualizado!', 'success');
        } else {
            const response = await axios.post(`${backUrl}/api/movimentacao/`, payload, config);
            emit('itemAdicionado', response.data);
            Swal.fire('Sucesso', 'Adicionado!', 'success');
        }
        emit('close');
    } catch (error) {
        console.error(error);
        Swal.fire('Erro', 'Falha ao salvar.', 'error');
    } finally {
        loading.value = false;
    }
}
</script>

<template>
    <div class="fixed inset-0 bg-black bg-opacity-50 backdrop-blur-sm flex justify-center items-center z-50 p-4">
        <div class="mx-auto w-full max-w-2xl bg-white rounded-xl shadow-lg flex flex-col" style="min-height: 400px;">
            <div class="px-6 py-4 flex justify-between items-center border-b">
                <h2 class="font-bold text-2xl">
                    {{ isEditMode ? 'Editar Item' : 'Adicionar Item' }}
                </h2>
                <div @click="emit('close')" class="cursor-pointer text-gray-600 hover:text-red-500">
                    <svg class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M6 18L18 6M6 6l12 12" />
                    </svg>
                </div>
            </div>

            <form @submit.prevent="handleSave" class="flex-grow flex flex-col p-6 space-y-6">

                <div class="relative">
                    <label class="block text-base font-medium text-gray-600 mb-1">Selecione o EPI</label>

                    <input type="text" :value="searchQuery" @input="handleInput" @focus="openDropdown" @blur="closeDropdown"
                        placeholder="Digite para buscar..."
                        class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-50 focus:ring-blue-500 focus:bg-white"
                        autocomplete="off" />

                    <div v-if="isDropdownOpen"
                        class="absolute z-50 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-xl max-h-60 overflow-y-auto">
                        <ul class="py-1">
                            <li v-if="filteredEpis.length === 0" class="px-3 py-2 text-gray-500">
                                Nenhum EPI encontrado.
                            </li>

                            <li v-for="epi in filteredEpis" :key="epi.id || epi.epiId" @mousedown.prevent="selectEpi(epi)"
                                class="px-3 py-2 text-lg text-gray-800 cursor-pointer hover:bg-blue-100 border-b border-gray-100 last:border-0">
                                {{ epi.descricao }} <span class="text-sm text-gray-500">(CA: {{ epi.codigoAutenticacao
                                }})</span>
                            </li>
                        </ul>
                    </div>
                </div>

                <div>
                    <label class="block text-base font-medium text-gray-600 mb-1">Data de Entrega</label>
                    <input type="date" v-model="form.dataEntrega" required
                        class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-50 focus:ring-blue-500" />
                </div>

                <div class="mt-auto pt-4 border-t flex justify-center">
                    <button type="submit" :disabled="loading"
                        class="font-bold text-white text-xl px-12 py-3 rounded-md bg-green-600 hover:bg-green-700 transition shadow-lg disabled:opacity-50">
                        {{ loading ? 'Salvando...' : 'Salvar' }}
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>