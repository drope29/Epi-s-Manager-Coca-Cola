<script setup>
import { reactive, computed, onMounted, ref } from 'vue';
import axios from 'axios';
import Swal from 'sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';
import { useRouter } from 'vue-router'; // <--- IMPORTANTE: Importar o router

const props = defineProps({
  colaborador: {
    type: Object,
    default: null
  }
});

const emit = defineEmits(['close', 'colaboradorAdicionado', 'colaboradorAtualizado']);
const backUrl = import.meta.env.VITE_BACKEND_URL;
const router = useRouter(); // <--- Instância do router

// --------------------------------------------
// FUNÇÃO AUXILIAR: TRATAMENTO DE SESSÃO
// --------------------------------------------
function handleSessionExpired() {
  Swal.fire({
    title: 'Sessão Expirada',
    text: 'Faça login novamente para continuar.',
    icon: 'warning',
    confirmButtonText: 'Ok',
    confirmButtonColor: '#3085d6',
    allowOutsideClick: false // Impede fechar clicando fora
  }).then((result) => {
    if (result.isConfirmed) {
      // 1. Remove o token inválido
      localStorage.removeItem('token');
      // 2. Redireciona para o login (rota /)
      // Usamos window.location.href para garantir que limpe qualquer estado residual
      window.location.href = '/';
    }
  });
}

// --------------------------------------------
// LISTA DE FUNÇÕES (CARGOS) E PESQUISA
// --------------------------------------------
const funcoes = ref([]);
const searchQuery = ref('');
const showDropdown = ref(false);

async function carregarFuncoes() {
  try {
    const token = localStorage.getItem('token');
    const response = await axios.get(`${backUrl}/api/funcoes/`, {
      headers: { Authorization: `Bearer ${token}` }
    });
    funcoes.value = response.data;
  } catch (e) {
    console.error("Erro ao carregar funções", e);
    // --- LÓGICA DE SESSÃO EXPIRADA ---
    if (e.response && e.response.status === 401) {
      handleSessionExpired(); // Chama a função que redireciona
    } else {
      funcoes.value = [];
    }
  }
}

const cargosFiltrados = computed(() => {
  if (!funcoes.value) return [];
  return funcoes.value
    .filter(f => f.nome.toLowerCase() !== 'admin')
    .filter(f => f.nome.toLowerCase().includes(searchQuery.value.toLowerCase()))
    .slice(0, 10);
});

function selecionarCargo(cargo) {
  form.cargo = cargo.id;
  searchQuery.value = cargo.nome;
  showDropdown.value = false;
  errors.cargo = null;
}

// === ESTADO DO FORMULÁRIO ===
const form = reactive({
  nome: '',
  sobrenome: '',
  unidade: '',
  re: '',
  cargo: '',
  setor: '',
  data_admissao: '',
  turno: '',
  genero: '',
});

// === ESTADO DE ERROS ===
const errors = reactive({
  nome: null,
  sobrenome: null,
  unidade: null,
  re: null,
  cargo: null,
  setor: null,
  data_admissao: null,
  turno: null,
  genero: null,
});

const isEditMode = computed(() => !!props.colaborador);

onMounted(async () => {
  await carregarFuncoes();

  if (isEditMode.value) {
    const nomeCompleto = (props.colaborador.nome || '').split(' ');
    form.nome = nomeCompleto.shift() || '';
    form.sobrenome = nomeCompleto.join(' ') || '';

    form.re = props.colaborador.re || '';
    form.unidade = props.colaborador.unidade || '';
    form.turno = props.colaborador.turno || '';
    form.genero = props.colaborador.genero || '';
    form.setor = props.colaborador.setor || '';

    if (props.colaborador.funcao) {
      form.cargo = props.colaborador.funcao.id;
      const cargoEncontrado = funcoes.value.find(f => f.id === form.cargo);
      if (cargoEncontrado) {
        searchQuery.value = cargoEncontrado.nome;
      }
    }

    if (props.colaborador.dataAdmissao) {
      try {
        if (props.colaborador.dataAdmissao.includes('T')) {
          form.data_admissao = props.colaborador.dataAdmissao.split('T')[0];
        } else {
          form.data_admissao = props.colaborador.dataAdmissao;
        }
      } catch (e) {
        form.data_admissao = '';
      }
    }
  }
});

function validateForm() {
  Object.keys(errors).forEach(key => errors[key] = null);
  let valid = true;

  if (!form.nome.trim()) { errors.nome = "Campo Obrigatório"; valid = false; }
  if (!form.sobrenome.trim()) { errors.sobrenome = "Campo Obrigatório"; valid = false; }
  if (!form.unidade) { errors.unidade = "Campo Obrigatório"; valid = false; }
  if (!form.re) { errors.re = "Campo Obrigatório"; valid = false; }
  if (!form.cargo) { errors.cargo = "Campo Obrigatório"; valid = false; }
  if (!form.turno) { errors.turno = "Campo Obrigatório"; valid = false; }
  if (!form.genero) { errors.genero = "Campo Obrigatório"; valid = false; }
  if (!form.setor.trim()) { errors.setor = "Campo Obrigatório"; valid = false; }
  if (!form.data_admissao) { errors.data_admissao = "Campo Obrigatório"; valid = false; }

  return valid;
}

// === REGISTRAR (POST) ===
async function registrarColaborador() {
  try {
    if (!validateForm()) return;

    const token = localStorage.getItem('token');

    const payload = {
      nome: `${form.nome.trim()} ${form.sobrenome.trim()}`,
      funcao: form.cargo,
      re: form.re,
      unidade: form.unidade,
      turno: form.turno,
      genero: form.genero,
      setor: form.setor,
      dataAdmissao: form.data_admissao
    };

    const response = await axios.post(`${backUrl}/api/funcionarios/`, payload, {
      headers: { Authorization: `Bearer ${token}` }
    });

    if (response.status === 201) {
      Swal.fire('Registrado!', 'Colaborador salvo com sucesso.', 'success');
      emit('colaboradorAdicionado');
    }
  } catch (error) {
    console.error("Erro no POST:", error);

    // --- LÓGICA DE SESSÃO EXPIRADA ---
    if (error.response && error.response.status === 401) {
      handleSessionExpired();
      return;
    }

    const msg = error.response?.data?.message || 'Falha ao registrar.';
    Swal.fire('Erro!', msg, 'error');
  }
}

// === ATUALIZAR (PUT) ===
async function atualizarColaborador() {
  try {
    const token = localStorage.getItem('token');

    let idCorreto = props.colaborador.funcionarioId ||
      props.colaborador.id ||
      props.colaborador.FuncionarioId;

    if (!idCorreto) {
      Swal.fire('Erro', 'ID não encontrado.', 'error');
      return;
    }

    const payload = {
      nome: `${form.nome} ${form.sobrenome}`,
      funcao: form.cargo,
      re: form.re,
      unidade: form.unidade,
      turno: form.turno,
      genero: form.genero,
      setor: form.setor,
      dataAdmissao: form.data_admissao
    };

    const response = await axios.put(`${backUrl}/api/funcionarios/${idCorreto}`, payload, {
      headers: { Authorization: `Bearer ${token}` }
    });

    if (response.status === 204 || response.status === 200) {
      Swal.fire('Atualizado!', 'O colaborador foi atualizado com sucesso.', 'success');
      emit('colaboradorAtualizado');
    }
  } catch (error) {
    console.error("Erro ao atualizar (PUT):", error);

    // --- LÓGICA DE SESSÃO EXPIRADA ---
    if (error.response && error.response.status === 401) {
      handleSessionExpired();
      return;
    }

    Swal.fire('Erro!', 'Não foi possível atualizar.', 'error');
  }
}

async function handleSubmit() {
  if (validateForm()) {
    if (isEditMode.value) {
      await atualizarColaborador();
    } else {
      await registrarColaborador();
    }
  }
}
</script>

<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 backdrop-blur-sm flex justify-center items-center z-40 p-4">

    <div
      class="mx-auto w-full max-w-4xl h-full sm:h-auto sm:max-h-[90vh] bg-white rounded-xl shadow-lg drop-shadow-md flex flex-col">

      <div class="px-6 py-4 flex justify-between items-center border-b">
        <h2 class="flex font-bold items-center text-2xl sm:text-4xl">
          {{ isEditMode ? 'Editar Colaborador' : 'Adicionar Colaborador' }}
        </h2>

        <div @click="emit('close')" style="cursor: pointer" class="text-gray-600">
          <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </div>
      </div>

      <form @submit.prevent="handleSubmit" class="flex-grow flex flex-col justify-between overflow-y-auto">

        <div class="p-6">
          <div class="w-full max-w-2xl mx-auto space-y-6">

            <div class="flex flex-col sm:flex-row gap-4">
              <div class="flex-1">
                <input type="text" placeholder="Nome" v-model="form.nome"
                  class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 bg-gray-100" />
                <p v-if="errors.nome" class="text-red-500 text-sm mt-1">{{ errors.nome }}</p>
              </div>

              <div class="flex-1">
                <input type="text" placeholder="Sobrenome" v-model="form.sobrenome"
                  class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 bg-gray-100" />
                <p v-if="errors.sobrenome" class="text-red-500 text-sm mt-1">{{ errors.sobrenome }}</p>
              </div>
            </div>

            <div>
              <select v-model="form.unidade" class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 bg-gray-100">
                <option disabled value="">Selecione uma unidade</option>
                <option value="Antonio Carlos">ANTONIO CARLOS</option>
                <option value="Blumenau">BLUMENAU</option>
              </select>
              <p v-if="errors.unidade" class="text-red-500 text-sm mt-1">{{ errors.unidade }}</p>
            </div>

            <div>
              <input type="text" placeholder="RE" v-model="form.re" :disabled="isEditMode"
                class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 bg-gray-100 disabled:bg-gray-200" />
              <p v-if="errors.re" class="text-red-500 text-sm mt-1">{{ errors.re }}</p>
            </div>

            <div class="relative">
              <input type="text" placeholder="Pesquise ou selecione um cargo..." v-model="searchQuery"
                @focus="showDropdown = true" @input="form.cargo = ''"
                class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 bg-gray-100" />

              <div class="absolute inset-y-0 right-0 flex items-center px-2 pointer-events-none">
                <svg class="w-5 h-5 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7"></path>
                </svg>
              </div>

              <ul v-if="showDropdown && cargosFiltrados.length > 0"
                class="absolute z-50 w-full bg-white border border-gray-300 rounded-md shadow-lg max-h-60 overflow-y-auto mt-1">
                <li v-for="f in cargosFiltrados" :key="f.id" @click="selecionarCargo(f)"
                  class="px-4 py-2 hover:bg-green-100 cursor-pointer text-gray-700 border-b border-gray-100 last:border-0">
                  {{ f.nome }}
                </li>
              </ul>

              <div v-if="showDropdown && cargosFiltrados.length === 0"
                class="absolute z-50 w-full bg-white border border-gray-300 rounded-md shadow-lg p-2 text-gray-500 text-sm mt-1">
                Nenhum cargo encontrado.
              </div>

              <div v-if="showDropdown" @click="showDropdown = false" class="fixed inset-0 z-40 bg-transparent"></div>

              <p v-if="errors.cargo" class="text-red-500 text-sm mt-1">{{ errors.cargo }}</p>
            </div>
            <div>
              <input type="text" placeholder="Setor" v-model="form.setor"
                class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 bg-gray-100" />
              <p v-if="errors.setor" class="text-red-500 text-sm mt-1">{{ errors.setor }}</p>
            </div>

            <div>
              <label for="dataAdmissaoInput" class="text-gray-600">Data de Admissão</label>
              <input id="dataAdmissaoInput" type="date" v-model="form.data_admissao"
                class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 bg-gray-100" />
              <p v-if="errors.data_admissao" class="text-red-500 text-sm mt-1">{{ errors.data_admissao }}</p>
            </div>

            <div>
              <select v-model="form.turno" class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 bg-gray-100">
                <option disabled value="">Turno</option>
                <option value="manha">MANHA</option>
                <option value="tarde">TARDE</option>
                <option value="noite">NOITE</option>
              </select>
              <p v-if="errors.turno" class="text-red-500 text-sm mt-1">{{ errors.turno }}</p>
            </div>

            <div>
              <div class="text-gray-600 text-base">Gênero</div>
              <div class="mt-2 flex space-x-4">
                <label class="flex-1 flex justify-between items-center px-4 py-3 border rounded-md">
                  <span>Homem</span>
                  <input type="radio" v-model="form.genero" value="Masculino" />
                </label>

                <label class="flex-1 flex justify-between items-center px-4 py-3 border rounded-md">
                  <span>Mulher</span>
                  <input type="radio" v-model="form.genero" value="Feminino" />
                </label>
              </div>
              <p v-if="errors.genero" class="text-red-500 text-sm mt-1">{{ errors.genero }}</p>
            </div>

          </div>
        </div>

        <div class="text-center p-6 border-t">
          <button type="submit"
            class="font-bold text-white text-xl px-12 py-3 rounded-md bg-gradient-to-r from-green-500 to-emerald-500">
            {{ isEditMode ? 'Salvar Alterações' : 'Registrar' }}
          </button>
        </div>

      </form>
    </div>
  </div>
</template>