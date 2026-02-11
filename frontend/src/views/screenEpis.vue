<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios'; // <--- Importante para o teste de segurança
import FormularioModalEpi from '../components/formEpi.vue';
import AnimacaoCaminhao from '../components/AnimacaoCaminhao.vue';
import TableEpis from '../components/tableEpi.vue';

const backUrl = import.meta.env.VITE_BACKEND_URL;
const isModalVisible = ref(false);
const tableKey = ref(0);
const epiParaEditar = ref(null);

// --- TRAVA DE SEGURANÇA AO ENTRAR NA TELA ---
onMounted(async () => {
  try {
    const token = localStorage.getItem('token');
    if (token) {
      // Tenta buscar a lista de EPIs (ou qualquer rota leve).
      // Se der erro 401, o interceptador do main.js assume e redireciona.
      await axios.get(`${backUrl}/api/epis/`, {
        headers: { Authorization: `Bearer ${token}` }
      });
    }
  } catch (error) {
    // O erro é silencioso aqui, pois o main.js já vai abrir o Modal e redirecionar
  }
});

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

  <FormularioModalEpi v-if="isModalVisible" :epi="epiParaEditar" @close="closeModal" @epiAdicionado="refreshTable"
    @epiAtualizado="refreshTable" />

  <TableEpis :key="tableKey" @open-epi-modal="openAddModal" @open-edit-epi-modal="openEditModal" />
</template>

<style scoped></style>