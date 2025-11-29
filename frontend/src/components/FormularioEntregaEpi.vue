<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';
import Swal from 'sweetalert2';

// --- Props e Emits ---
const props = defineProps({
    funcionarioId: {
        type: [Number, String],
        required: true
    },
    itemParaEditar: {
        type: Object,
        default: null
    }
});
const emit = defineEmits(['close', 'itemAdicionado', 'itemAtualizado']);

// --- Configuração ---
const backUrl = import.meta.env.VITE_BACKEND_URL;
const loading = ref(false);
const epis = ref([]); // Lista completa de EPIs
const isDropdownOpen = ref(false);
const searchQuery = ref('');

const isEditMode = computed(() => !!props.itemParaEditar);

// --- Estado do formulário ---
const form = ref({
    dataEntrega: new Date().toISOString().split('T')[0],
    epiId: null,
    funcionarioId: props.funcionarioId,
    status: 'ASSINADO'
});

// --- Filtro de EPIs ---
const filteredEpis = computed(() => {
    const query = searchQuery.value.toLowerCase().trim();
    if (!query) {
        return epis.value;
    }

    return epis.value.filter(epi => {
        const descricao = (epi.descricao || '').toLowerCase();
        const ca = (epi.codigoAutenticacao?.toString() || '').toLowerCase();
        return descricao.includes(query) || ca.includes(query);
    });
});

// --- Valor exibido no input (controle do combobox) ---
const displayValue = computed({
    get() {
        if (isDropdownOpen.value) {
            return searchQuery.value;
        }
        if (form.value.epiId) {
            const epi = epis.value.find(e => e.epiId === form.value.epiId);
            return epi ? `${epi.descricao} (CA: ${epi.codigoAutenticacao})` : '';
        }
        return '';
    },
    set(value) {
        searchQuery.value = value;
        if (!value) {
            form.value.epiId = null;
        }
    }
});

// --- Busca inicial de EPIs ---
onMounted(() => {
    fetchEpis();

    if (isEditMode.value) {
        const item = props.itemParaEditar;

        // Formatação da data (robusta)
        let dataFormatada = item.dataEntrega;
        try {
            const date = new Date(item.dataEntrega);
            if (!isNaN(date.getTime())) {
                dataFormatada = date.toISOString().split('T')[0];
            } else if (typeof item.dataEntrega === 'string' && item.dataEntrega.includes('/')) {
                const [dia, mes, ano] = item.dataEntrega.split('/');
                dataFormatada = `${ano}-${mes.padStart(2, '0')}-${dia.padStart(2, '0')}`;
            }
        } catch (e) {
            console.warn('Falha ao formatar data:', e);
        }

        // ✅ CORREÇÃO PRINCIPAL: usa item.epi.epiId, não .id
        form.value = {
            dataEntrega: dataFormatada,
            epiId: item.epi?.epiId ?? null,
            funcionarioId: props.funcionarioId,
            status: item.status || 'ASSINADO'
        };
    }
});

// --- Carregar lista de EPIs ---
async function fetchEpis() {
    try {
        const response = await axios.get(`${backUrl}/api/epis/`);
        epis.value = response.data;
    } catch (error) {
        console.error("Erro ao buscar EPIs:", error);
        Swal.fire('Erro', 'Não foi possível carregar a lista de EPIs.', 'error');
    }
}

// --- Seleção no dropdown ---
function selectEpi(epi) {
    form.value.epiId = epi.epiId;
    isDropdownOpen.value = false;
    searchQuery.value = '';
}

function openDropdown() {
    isDropdownOpen.value = true;
    searchQuery.value = '';
}

function closeDropdown() {
    setTimeout(() => {
        isDropdownOpen.value = false;
    }, 200);
}

// --- Salvar (criar ou atualizar) ---
async function handleSave() {
    if (loading.value) return;

    if (!form.value.dataEntrega || !form.value.epiId) {
        Swal.fire('Atenção', 'Preencha a Data de Entrega e selecione um EPI.', 'warning');
        return;
    }

    loading.value = true;

    const payload = {
        dataEntrega: form.value.dataEntrega,
        epi: form.value.epiId, // ✅ backend espera campo "epi" com valor do epiId
        funcionario: form.value.funcionarioId,
        status: form.value.status
    };

    try {
        if (isEditMode.value) {
            const itemId = props.itemParaEditar.id;
            const response = await axios.put(`${backUrl}/api/movimentacao/${itemId}`, payload);
            Swal.fire('Salvo!', 'O item foi atualizado com sucesso.', 'success');
            emit('itemAtualizado', response.data);
        } else {
            const response = await axios.post(`${backUrl}/api/movimentacao/`, payload);
            Swal.fire('Salvo!', 'O item foi adicionado com sucesso.', 'success');
            emit('itemAdicionado', response.data);
        }

        emit('close');

    } catch (error) {
        console.error("Erro ao salvar:", error);
        const errorMsg = error.response?.data?.message || 'Não foi possível salvar o item.';
        Swal.fire('Erro!', errorMsg, 'error');
    } finally {
        loading.value = false;
    }
}
</script>

<template>
    <div class="fixed inset-0 bg-black bg-opacity-50 backdrop-blur-sm flex justify-center items-center z-50 p-4">
        <div class="mx-auto w-full max-w-2xl bg-white rounded-xl shadow-lg flex flex-col">
            <div class="px-6 py-4 flex justify-between items-center border-b">
                <h2 class="font-bold text-2xl sm:text-4xl">
                    {{ isEditMode ? 'Editar Item' : 'Adicionar Item' }}
                </h2>
                <div @click="emit('close')" style="cursor: pointer" class="text-gray-600">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M6 18L18 6M6 6l12 12" />
                    </svg>
                </div>
            </div>

            <form @submit.prevent="handleSave" class="flex-grow flex flex-col justify-between">
                <div class="p-6">
                    <div class="w-full mx-auto space-y-6">
                        <!-- Combobox de EPI -->
                        <div class="relative">
                            <label for="epiCombobox" class="block text-base font-medium text-gray-600 ml-1 mb-1">
                                Selecione o EPI
                            </label>
                            <input
                                id="epiCombobox"
                                type="text"
                                :value="displayValue"
                                @input="displayValue = $event.target.value; isDropdownOpen = true"
                                @focus="openDropdown"
                                @blur="closeDropdown"
                                placeholder="-- Escolha ou busque um EPI --"
                                required
                                class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100 focus:placeholder-gray-500"
                            />
                            <div
                                v-if="isDropdownOpen"
                                class="absolute z-10 w-full mt-1 bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-y-auto"
                            >
                                <ul class="py-1">
                                    <li v-if="!searchQuery" class="px-3 py-2 text-sm text-gray-500">
                                        Mostrando os 10 primeiros. Digite para buscar...
                                    </li>
                                    <li
                                        v-if="filteredEpis.length === 0 && searchQuery"
                                        class="px-3 py-2 text-sm text-gray-500"
                                    >
                                        Nenhum resultado para "{{ searchQuery }}"
                                    </li>
                                    <li
                                        v-for="epi in (searchQuery ? filteredEpis : filteredEpis.slice(0, 10))"
                                        :key="epi.epiId"
                                        @mousedown="selectEpi(epi)"
                                        class="px-3 py-2 text-lg text-gray-800 cursor-pointer hover:bg-blue-100"
                                    >
                                        {{ epi.descricao }} (CA: {{ epi.codigoAutenticacao }})
                                    </li>
                                </ul>
                            </div>
                        </div>

                        <!-- Data de Entrega -->
                        <div>
                            <label for="dataEntregaInput" class="block text-base font-medium text-gray-600 ml-1 mb-1">
                                Data de Entrega
                            </label>
                            <input
                                id="dataEntregaInput"
                                type="date"
                                v-model="form.dataEntrega"
                                required
                                class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100 text-gray-700"
                            />
                        </div>
                    </div>
                </div>

                <!-- Botão de Ação -->
                <div class="text-center p-6 border-t">
                    <button
                        type="submit"
                        :disabled="loading"
                        class="
                            font-bold text-white text-xl
                            px-12 sm:px-20 py-3 rounded-md 
                            bg-gradient-to-r from-green-500 to-emerald-500
                            transition-all duration-300 ease-out
                            hover:-translate-y-1 hover:shadow-lg
                        "
                        :class="{ 'opacity-70 cursor-not-allowed': loading }"
                    >
                        {{ loading ? 'Salvando...' : (isEditMode ? 'Salvar Alterações' : 'Salvar Item') }}
                    </button>
                </div>
            </form>
        </div>
    </div>
</template>