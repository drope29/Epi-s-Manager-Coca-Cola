<script setup>
import { ref } from 'vue';
import FormularioModal from '../components/formColaboradores.vue';
import AnimacaoCaminhao from '../components/AnimacaoCaminhao.vue';
import TableColaboradores from '../components/tableColaboradores.vue';

const isModalVisible = ref(false);
const tableKey = ref(0);

function openModal() {
    isModalVisible.value = true;
}
function closeModal() {
    isModalVisible.value = false;
}
function refreshTable() {
    tableKey.value++;
}
</script>

<template>
    <AnimacaoCaminhao />

    <FormularioModal v-if="isModalVisible" @close="closeModal" @colaboradorAdicionado="refreshTable" />

    <TableColaboradores :key="tableKey" @open-add-modal="openModal" />
</template>

<style scoped></style>