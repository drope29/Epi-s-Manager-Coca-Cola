import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import Swal from 'sweetalert2'
import 'sweetalert2/dist/sweetalert2.min.css'

// --- 2. CONFIGURAÇÃO DO INTERCEPTOR GLOBAL ---
// Isso faz com que TODAS as requisições do sistema passem por aqui
axios.interceptors.response.use(
    (response) => {
        // Se a resposta do backend for sucesso, deixa passar normal
        return response;
    },
    (error) => {
        // Se der erro, verificamos se é o erro 401 (Sessão Expirada / Não Autorizado)
        if (error.response && error.response.status === 401) {

            // Verifica se já não tem um alerta aberto (para não abrir 10 ao mesmo tempo)
            // Se não tiver alerta visível, mostramos um novo
            if (!document.querySelector('.swal2-container')) {
                Swal.fire({
                    title: 'Sessão Expirada',
                    text: 'Sua conexão expirou. Por favor, faça login novamente.',
                    icon: 'warning',
                    confirmButtonText: 'Ok',
                    confirmButtonColor: '#3085d6',
                    allowOutsideClick: false, // Obriga o usuário a clicar no botão
                    allowEscapeKey: false
                }).then((result) => {
                    if (result.isConfirmed) {
                        // 1. Limpa o token estragado
                        localStorage.removeItem('token');

                        // 2. Força o navegador a ir para a tela de login (raiz)
                        window.location.href = '/';
                    }
                });
            }
        }

        // Retorna o erro para caso algum componente queira tratar algo específico
        return Promise.reject(error);
    }
);

const app = createApp(App)

app.use(router)

app.mount('#app')