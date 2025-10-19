<script setup>
import { reactive, computed, onMounted } from 'vue';
import axios from 'axios';
import Swal from 'sweetalert2';
import 'sweetalert2/dist/sweetalert2.min.css';

const props = defineProps({
  colaborador: {
    type: Object,
    default: null 
  }
});

const emit = defineEmits(['close', 'colaboradorAdicionado', 'colaboradorAtualizado']);
const backUrl = import.meta.env.VITE_BACKEND_URL;

const form = reactive({
  nome: '',
  sobrenome: '',
  unidade: '',
  re: '',
  cargo: '',
  turno: '',
  genero: '',
});

const errors = reactive({
  nome: null,
  sobrenome: null,
  unidade: null,
  re: null,
  cargo: null,
  turno: null,
  genero: null,
});

const isEditMode = computed(() => !!props.colaborador);

onMounted(() => {
  if (isEditMode.value) {
    const nomeCompleto = props.colaborador.nome.split(' ');
    form.nome = nomeCompleto.shift(); 
    form.sobrenome = nomeCompleto.join(' '); 

    form.unidade = props.colaborador.unidade;
    form.re = props.colaborador.re;
    form.cargo = props.colaborador.funcao; 
    form.turno = props.colaborador.turno;
    form.genero = props.colaborador.genero;
  }
});

function validateForm() {
  Object.keys(errors).forEach(key => errors[key] = null);
  let formValido = true;

  if (!form.nome.trim()) { errors.nome = "Campo Obrigatório"; formValido = false; }
  if (!form.sobrenome.trim()) { errors.sobrenome = "Campo Obrigatório"; formValido = false; }
  if (!form.unidade) { errors.unidade = "Campo Obrigatório"; formValido = false; }
  if (!form.re) { errors.re = "Campo Obrigatório"; formValido = false; }
  if (!form.cargo.trim()) { errors.cargo = "Campo Obrigatório"; formValido = false; }
  if (!form.turno) { errors.turno = "Campo Obrigatório"; formValido = false; }
  if (!form.genero) { errors.genero = "Campo Obrigatório"; formValido = false; }
  
  return formValido;
}

async function registrarColaborador() {
  try {
    const response = await axios.post(`${backUrl}/api/funcionarios/`, {
      nome: `${form.nome} ${form.sobrenome}`,
      funcao: form.cargo,
      re: form.re,
      unidade: form.unidade,
      turno: form.turno,
      genero: form.genero,
    });

    if (response.status === 201) {
      Swal.fire(
        'Registrado!',
        'O colaborador foi registrado com sucesso.',
        'success'
      );
      emit('colaboradorAdicionado');
    }
  } catch (error) {
    console.error("Erro ao registrar colaborador:", error);
    Swal.fire(
      'Erro!',
      'Não foi possível registrar o colaborador. Verifique o console.',
      'error'
    );
  }
}

async function atualizarColaborador() {
  try {

    const response = await axios.put(`${backUrl}/api/funcionarios/${props.colaborador.id}`, {
      nome: `${form.nome} ${form.sobrenome}`,
      funcao: form.cargo,
      re: form.re,
      unidade: form.unidade,
      turno: form.turno,
      genero: form.genero,
    });

    if (response.status === 200) {
      Swal.fire(
        'Atualizado!',
        'O colaborador foi atualizado com sucesso.',
        'success'
      );
      emit('colaboradorAtualizado'); 
    }
  } catch (error) {
    console.error("Erro ao atualizar colaborador:", error);
    Swal.fire(
      'Erro!',
      'Não foi possível atualizar o colaborador. Verifique o console.',
      'error'
    );
  }
}

async function handleSubmit() {
  if (validateForm()) {
    if (isEditMode.value) {
      await atualizarColaborador();
    } else {
      await registrarColaborador();
    }
  } else {
    console.log("Formulário inválido.");
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
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M6 18L18 6M6 6l12 12"></path>
          </svg>
        </div>
      </div>

      <form @submit.prevent="handleSubmit" class="flex-grow flex flex-col justify-between overflow-y-auto">

        <div class="p-6">
          <div class="w-full max-w-2xl mx-auto space-y-6">
            <div class="flex flex-col sm:flex-row gap-4">
              <div class="flex-1">
                <input type="text" placeholder="Nome" v-model="form.nome"
                  class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100 focus:placeholder-gray-500" />
                <p v-if="errors.nome" class="text-red-500 text-sm mt-1">{{ errors.nome }}</p>
              </div>
              <div class="flex-1">
                <input type="text" placeholder="Sobrenome" v-model="form.sobrenome"
                  class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100 focus:placeholder-gray-500" />
                <p v-if="errors.sobrenome" class="text-red-500 text-sm mt-1">{{ errors.sobrenome }}</p>
              </div>
            </div>
            <div>
              <select v-model="form.unidade"
                class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100">
                <option disabled value="">Selecione uma unidade</option>
                <option>ANTONIO CARLOS</option>
                <option>BLUMENAU</option>
              </select>
              <p vV-if="errors.unidade" class="text-red-500 text-sm mt-1">{{ errors.unidade }}</p>
            </div>
            
            <div>
              <input type="number" placeholder="RE" v-model="form.re"
                :disabled="isEditMode"
                class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100 disabled:bg-gray-200 disabled:cursor-not-allowed" />
              <p v-if="errors.re" class="text-red-500 text-sm mt-1">{{ errors.re }}</p>
            </div>
            
            <div>
              <input type="text" placeholder="Cargo" v-model="form.cargo"
                class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100" />
              <p v-if="errors.cargo" class="text-red-500 text-sm mt-1">{{ errors.cargo }}</p>
            </div>
            <div>
              <select v-model="form.turno"
                class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100">
                <option disabled value="">Turno</option>
                <option>MANHA</option>
                <option>TARDE</option>
                <option>NOITE</option>
              </select>
              <p v-if="errors.turno" class="text-red-500 text-sm mt-1">{{ errors.turno }}</p>
            </div>
            <div>
              <div class="text-gray-600 text-base">Gênero</div>
              <div class="mt-2 flex space-x-4">
                <label for="male"
                  class="flex-1 flex justify-between items-center rounded-md px-4 py-3 border border-gray-400 cursor-pointer">
                  <span class="text-lg">Homem</span>
                  <input type="radio" id="male" v-model="form.genero" value="homem" name="gender" class="h-5 w-5" />
                </label>
                <label for="female"
                  class="flex-1 flex justify-between items-center rounded-md px-4 py-3 border border-gray-400 cursor-pointer">
                  <span class="text-lg">Mulher</span>
                  <input type="radio" id="female" v-model="form.genero" value="mulher" name="gender" class="h-5 w-5" />
                </label>
              </div>
              <p v-if="errors.genero" class="text-red-500 text-sm mt-1">{{ errors.genero }}</p>
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
                  hover:-translate-y-1 hover:shadow-lg hover:shadow-green-500/40">
            {{ isEditMode ? 'Salvar Alterações' : 'Registrar' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>