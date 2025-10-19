<script setup>
import { ref } from 'vue';
import FormularioModalEpi from '../components/formEpi.vue';
import AnimacaoCaminhao from '../components/AnimacaoCaminhao.vue';
import TableEpis from '../components/tableEpi.vue';

const isModalVisible = ref(false);
const tableKey = ref(0);

const epiParaEditar = ref(null);

function openAddModal() {
  epiParaEditar.value = null;
  isModalVisible.value = true;
}

function openEditModal(epi) {
  epiParaEditar.value = { ...epi };
  isModalVisible.value = true;
}

function closeModal() {
  isModalVisible.value = false;
  epiParaEditar.value = null;
}

function refreshTable() {
  tableKey.value++;
  closeModal();
}
</script>

<template>
  <AnimacaoCaminhao />

  <FormularioModalEpi 
    v-if="isModalVisible" 
    :epi="epiParaEditar"
    @close="closeModal"
    @epiAdicionado="refreshTable"
    @epiAtualizado="refreshTable"
  />

  <TableEpis 
    :key="tableKey"
    @open-epi-modal="openAddModal" 
    @open-edit-epi-modal="openEditModal"
  />
</template>

<style scoped></style>