<script setup>
import { ref } from 'vue';
import FormularioModal from '../components/formColaboradores.vue';
import Search from '../components/Search.vue';
import AnimacaoCaminhao from '../components/AnimacaoCaminhao.vue';
import buttonAdd from '../components/buttonAdd.vue';

const isModalVisible = ref(false);

function openModal() {
    isModalVisible.value = true;
}
function closeModal() {
    isModalVisible.value = false;
}
</script>

<template>
    <div class="flex items-center gap-6 bg-gray-200 p-4">
        <Search />

        <buttonAdd @click="openModal" />
    </div>

    <AnimacaoCaminhao />

    <FormularioModal v-if="isModalVisible" @close="closeModal" />
</template>

<style scoped></style>