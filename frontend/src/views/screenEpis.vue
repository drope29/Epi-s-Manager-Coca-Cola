<script setup>
import { ref } from 'vue';
import FormularioModalEpi from '../components/formEpi.vue';
import AnimacaoCaminhao from '../components/AnimacaoCaminhao.vue';
import tableEpis from '../components/tableEpi.vue';

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
    
    <FormularioModalEpi v-if="isModalVisible" @close="closeModal" /> 
    <tableEpis />
    
</template>

<style scoped></style>