<script setup>
import { ref } from 'vue';
import FormularioModal from '../components/formColaboradores.vue';
import AnimacaoCaminhao from '../components/AnimacaoCaminhao.vue';
import TableColaboradores from '../components/tableColaboradores.vue';

const isModalVisible = ref(false);
const tableKey = ref(0);

const colaboradorParaEditar = ref(null);

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

function refreshTable() {
  tableKey.value++;
  closeModal();
}
</script>

<template>
  <AnimacaoCaminhao />

  <FormularioModal 
    v-if="isModalVisible" 
    :colaborador="colaboradorParaEditar"
    @close="closeModal" 
    @colaboradorAdicionado="refreshTable"
    @colaboradorAtualizado="refreshTable" 
  />

  <TableColaboradores 
    :key="tableKey" 
    @open-add-modal="openAddModal" 
    @open-edit-modal="openEditModal" 
  />
</template>

<style scoped></style>