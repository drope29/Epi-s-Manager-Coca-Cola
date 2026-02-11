<script setup>
import { ref, onMounted } from 'vue'; // <--- Adicionei onMounted
import axios from 'axios'; // <--- Importante para o teste de segurança
import FormularioModal from '../components/formColaboradores.vue';
import AnimacaoCaminhao from '../components/AnimacaoCaminhao.vue';
import TableColaboradores from '../components/tableColaboradores.vue';
import TableMovimentacoes from '../components/TableMovimentacoes.vue';

const backUrl = import.meta.env.VITE_BACKEND_URL;
const tableKey = ref(0);

// --- TRAVA DE SEGURANÇA AO ENTRAR NA TELA ---
onMounted(async () => {
  try {
    const token = localStorage.getItem('token');
    if (token) {
      // Tenta buscar algo leve (ex: lista de funções) para validar o token.
      // Se der erro 401, o interceptador do main.js assume e redireciona.
      await axios.get(`${backUrl}/api/funcoes/`, {
        headers: { Authorization: `Bearer ${token}` }
      });
    }
  } catch (error) {
    // O erro é silencioso aqui, pois o main.js já vai abrir o Modal e redirecionar
  }
});

// --- State para Modal de Adicionar/Editar ---
const isModalVisible = ref(false);
const colaboradorParaEditar = ref(null);

// --- State para o modal de movimentações ---
const isMovimentacoesModalVisible = ref(false);
const colaboradorParaMovimentacoes = ref(null);

// --- Funções Modal Adicionar/Editar ---
function openAddModal() {
  colaboradorParaEditar.value = null;
  isModalVisible.value = true;
}

function openEditModal(colaborador) {
  colaboradorParaEditar.value = { ...colaborador };
  isModalVisible.value = true;
}

function closeModal() {
  isModalVisible.value = false;
  colaboradorParaEditar.value = null;
}

// --- Funções para controlar o modal de movimentações ---
function openMovimentacoesModal(colaborador) {
  colaboradorParaMovimentacoes.value = colaborador;
  isMovimentacoesModalVisible.value = true;
}

function closeMovimentacoesModal() {
  isMovimentacoesModalVisible.value = false;
  colaboradorParaMovimentacoes.value = null;
}

// --- Função de Refresh ---
function refreshTable() {
  tableKey.value++;
  closeModal(); // Fecha o modal de edição/adição
}
</script>

<template>
  <AnimacaoCaminhao />

  <FormularioModal v-if="isModalVisible" :colaborador="colaboradorParaEditar" @close="closeModal"
    @colaboradorAdicionado="refreshTable" @colaboradorAtualizado="refreshTable" />

  <TableMovimentacoes v-if="isMovimentacoesModalVisible" :colaborador="colaboradorParaMovimentacoes"
    @close="closeMovimentacoesModal" />

  <TableColaboradores :key="tableKey" @open-add-modal="openAddModal" @open-edit-modal="openEditModal"
    @open-movimentacoes-modal="openMovimentacoesModal" />
</template>

<style scoped></style>