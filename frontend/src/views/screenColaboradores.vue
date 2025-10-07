<script setup>
import { ref } from 'vue';
import FormularioModal from '../components/formColaboradores.vue';
import AnimacaoCaminhao from '../components/AnimacaoCaminhao.vue';
import TableColaboradores from '../components/tableColaboradores.vue';

const isModalVisible = ref(false);

function openModal() {
    isModalVisible.value = true;
}
function closeModal() {
    isModalVisible.value = false;
}
</script>

<template>
    <AnimacaoCaminhao />

    <FormularioModal v-if="isModalVisible" @close="closeModal" />

    <TableColaboradores @open-add-modal="openModal" />
</template>

<style scoped></style>