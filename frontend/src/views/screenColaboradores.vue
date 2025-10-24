<script setup>
import { ref } from 'vue';
import FormularioModal from '../components/formColaboradores.vue';
import AnimacaoCaminhao from '../components/AnimacaoCaminhao.vue';
import TableColaboradores from '../components/tableColaboradores.vue';

// 1. IMPORTAR o seu componente de movimentações
import TableMovimentacoes from '../components/TableMovimentacoes.vue';

const tableKey = ref(0);

// --- State para Modal de Adicionar/Editar ---
const isModalVisible = ref(false);
const colaboradorParaEditar = ref(null);

// 2. ADICIONAR state para o modal de movimentações
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

// 3. ADICIONAR funções para controlar o modal de movimentações
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