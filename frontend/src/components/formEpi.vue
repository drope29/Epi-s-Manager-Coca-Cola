<script setup>
import { reactive, onMounted, computed } from 'vue';
import axios from 'axios';
import Swal from 'sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';

const props = defineProps({
  epi: {
    type: Object,
    default: null
  }
});

const emit = defineEmits(['close', 'epiAdicionado', 'epiAtualizado']);
const backUrl = import.meta.env.VITE_BACKEND_URL;

const form = reactive({
  descricao: '',
  codigoCompra: '',
  codigoAutenticacao: '',
  dataValidade: '',
});

const errors = reactive({
  descricao: null,
  codigoCompra: null,
  codigoAutenticacao: null,
  dataValidade: null,
});

const isEditMode = computed(() => !!props.epi);

onMounted(() => {
  if (isEditMode.value) {
    form.descricao = props.epi.descricao;
    form.codigoCompra = props.epi.codigoCompra;
    form.codigoAutenticacao = props.epi.codigoAutenticacao || '';

    // CORREÇÃO: Validação robusta da data para evitar RangeError
    if (props.epi.dataValidade) {
      try {
        const dateObj = new Date(props.epi.dataValidade);
        if (!isNaN(dateObj.getTime())) {
          form.dataValidade = dateObj.toISOString().split('T')[0];
        } else {
          // Tenta formato DD/MM/YYYY se necessário
          form.dataValidade = '';
        }
      } catch (e) {
        console.warn("Data inválida ignorada:", props.epi.dataValidade);
        form.dataValidade = '';
      }
    }
  }
});

function validateForm() {
  Object.keys(errors).forEach(key => errors[key] = null);
  let formValido = true;

  if (!form.descricao.trim()) {
    errors.descricao = "Campo Obrigatório";
    formValido = false;
  }

  if (!form.codigoCompra) {
    errors.codigoCompra = "Campo Obrigatório";
    formValido = false;
  }

  // Validade não é obrigatória em alguns casos, mas se você quiser obrigar:
  if (!form.dataValidade) {
    errors.dataValidade = "Campo Obrigatório";
    formValido = false;
  }

  return formValido;
}

function createPayload() {
  return {
    descricao: form.descricao,
    codigoCompra: form.codigoCompra || 0,
    codigoAutenticacao: form.codigoAutenticacao || '',
    dataValidade: form.dataValidade || null,
  };
}

async function registrarEPI() {
  try {
    const token = localStorage.getItem('token'); // 1. Recupera Token
    const payload = createPayload();

    const response = await axios.post(`${backUrl}/api/epis/`, payload, {
      headers: { Authorization: `Bearer ${token}` } // 2. Envia Token
    });

    if (response.status >= 200 && response.status < 300) {
      Swal.fire('Registrado!', 'O EPI foi registrado com sucesso.', 'success');
      emit('epiAdicionado');
    }
  } catch (error) {
    console.error("Erro ao registrar EPI:", error);
    Swal.fire('Erro!', 'Não foi possível registrar o EPI.', 'error');
  }
}

async function atualizarEPI() {
  try {
    const token = localStorage.getItem('token'); // 1. Recupera Token
    const payload = createPayload();

    // CORREÇÃO: Garante o ID correto (epiId ou id)
    const idParaAtualizar = props.epi.epiId ?? props.epi.id;

    const response = await axios.put(`${backUrl}/api/epis/${idParaAtualizar}`, payload, {
      headers: { Authorization: `Bearer ${token}` } // 2. Envia Token
    });

    if (response.status >= 200 && response.status < 300) {
      Swal.fire('Atualizado!', 'O EPI foi atualizado com sucesso.', 'success');
      emit('epiAtualizado');
    } else {
      throw new Error(`Status inesperado: ${response.status}`);
    }
  } catch (error) {
    console.error("Erro ao atualizar EPI:", error);
    Swal.fire('Erro!', 'Não foi possível atualizar o EPI.', 'error');
  }
}

async function handleSubmit() {
  if (isEditMode.value) {
    await atualizarEPI();
  } else {
    if (validateForm()) {
      await registrarEPI();
    }
  }
}
</script>

<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 backdrop-blur-sm flex justify-center items-center z-40 p-4">
    <div class="mx-auto w-full max-w-2xl bg-white rounded-xl shadow-lg flex flex-col">

      <div class="px-6 py-4 flex justify-between items-center border-b">
        <h2 class="font-bold text-2xl sm:text-4xl">{{ isEditMode ? 'Editar EPI' : "Adicionar EPI" }}</h2>
        <div @click="emit('close')" style="cursor: pointer" class="text-gray-600">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
        </div>
      </div>

      <form @submit.prevent="handleSubmit" class="flex-grow flex flex-col justify-between">
        <div class="p-6">
          <div class="w-full mx-auto space-y-6">
            <div>
              <input type="text" placeholder="Descrição do Item" v-model="form.descricao"
                class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100 focus:placeholder-gray-500" />
              <p v-if="errors.descricao" class="text-red-500 text-sm mt-1">{{ errors.descricao }}</p>
            </div>

            <div>
              <input type="number" placeholder="CA (Não Obrigatório)" v-model="form.codigoAutenticacao"
                class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100 focus:placeholder-gray-500" />
              <p v-if="errors.codigoAutenticacao" class="text-red-500 text-sm mt-1">{{ errors.codigoAutenticacao }}</p>
            </div>

            <div>
              <input type="text" placeholder="Código de Compra" v-model="form.codigoCompra"
                class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100 focus:placeholder-gray-500" />
              <p v-if="errors.codigoCompra" class="text-red-500 text-sm mt-1">{{ errors.codigoCompra }}</p>
            </div>

            <div>
              <label for="dataValidade" class="block text-base font-medium text-gray-600 ml-1 mb-1">Data de
                Validade</label>
              <input id="dataValidade" type="date" v-model="form.dataValidade"
                class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100 text-gray-700" />
              <p v-if="errors.dataValidade" class="text-red-500 text-sm mt-1">{{ errors.dataValidade }}</p>
            </div>

          </div>
        </div>

        <div class="text-center p-6 border-t">
          <button type="submit" class="
                            font-bold text-white text-xl
                            px-12 sm:px-20 py-3 rounded-md 
                            bg-gradient-to-r from-green-500 to-emerald-500
                            transition-all duration-300 ease-out
                            hover:-translate-y-1 hover:shadow-lg">
            {{ isEditMode ? 'Salvar Alterações' : 'Registrar' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>