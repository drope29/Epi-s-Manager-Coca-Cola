<script setup>
import { ref } from 'vue';
import FormularioModalEpi from '../components/formEpi.vue';
import Search from '../components/Search.vue';
import AnimacaoCaminhao from '../components/AnimacaoCaminhao.vue';
import buttonAdd from '../components/buttonAdd.vue';
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
    <div class="flex flex-col md:flex-row items-center gap-6 bg-gray-200 p-4">
        <Search />
        <buttonAdd @click="openModal" />
    </div>

    <AnimacaoCaminhao />

    
    <FormularioModalEpi v-if="isModalVisible" @close="closeModal" />
    
    <tableEpis />
    
</template>

<style scoped></style>