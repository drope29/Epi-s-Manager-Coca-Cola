<template>
    <div class="min-h-screen w-full flex items-center justify-center bg-[#f8f8f8]">
        <div class="max-w-lg w-full relative flex flex-col p-10 rounded-2xl text-black bg-white shadow-lg">

            <div class="text-3xl font-bold mb-4 text-[#1e0e4b] text-center">
                Gerencie a entrega de seus Epi's
            </div>

            <div class="text-lg mb-6 text-center text-[#1e0e4b]">
                Entre na sua conta para continuar
            </div>

            <form @submit.prevent="login" class="flex flex-col gap-6">
                <div class="block relative">
                    <label for="login" class="block text-gray-600 cursor-text text-lg font-medium mb-2">
                        Login
                    </label>
                    <input type="text" id="login" v-model="loginField" required
                        class="rounded-lg border border-gray-300 text-lg w-full text-black appearance-none block h-14 m-0 px-4 focus:ring-2 ring-offset-2 ring-gray-900 outline-0" />
                </div>

                <div class="block relative">
                    <label for="password" class="block text-gray-600 cursor-text text-lg font-medium mb-2">
                        Senha
                    </label>
                    <input type="password" id="password" v-model="password" required
                        class="rounded-lg border border-gray-300 text-lg w-full text-black appearance-none block h-14 m-0 px-4 focus:ring-2 ring-offset-2 ring-gray-900 outline-0" />
                </div>

                <button type="submit"
                    class="bg-blue-600 w-[180px] m-auto py-3 rounded-lg text-white text-[18px] font-bold transition-all duration-300 hover:bg-blue-800 hover:scale-[1.015]">
                    Entrar
                </button>
            </form>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const backUrl = import.meta.env.VITE_BACKEND_URL;

const loginField = ref("");
const password = ref("");

// --- Redireciona se já estiver logado ---
onMounted(() => {
    const token = localStorage.getItem('token');
    if (token) {
        router.push({ name: "Home" });
    }
});

async function login() {
    try {
        // 1. Limpeza total antes de começar
        localStorage.clear();

        const response = await fetch(`${backUrl}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                username: loginField.value,
                password: password.value
            })
        });

        if (!response.ok) {
            if (response.status === 401) throw new Error('Usuário ou senha incorretos!');
            throw new Error(`Erro no servidor: ${response.status}`);
        }

        const data = await response.json();
        let tokenReal = null;

        // 2. Lógica para encontrar o token
        if (data && typeof data === 'string') {
            tokenReal = data;
        } else if (data.token) {
            if (typeof data.token === 'string') {
                tokenReal = data.token;
            } else if (data.token.token) {
                tokenReal = data.token.token;
            } else if (data.token.accessToken) {
                tokenReal = data.token.accessToken;
            }
        } else if (data.accessToken) {
            tokenReal = data.accessToken;
        } else if (data.Authorization) {
            tokenReal = data.Authorization;
        }

        // 3. Verificação e Salvamento
        if (tokenReal && typeof tokenReal === 'string' && tokenReal.length > 10) {
            if (tokenReal.startsWith("Bearer ")) {
                tokenReal = tokenReal.replace("Bearer ", "");
            }

            localStorage.setItem('token', tokenReal);
            router.push({ name: "Home" });
        } else {
            // Aqui usamos alert pois é um erro de sistema que impede o login
            alert("Erro de sistema: Token de autenticação inválido.");
        }

    } catch (error) {
        // --- ALTERAÇÃO DE SEGURANÇA ---
        // Não logamos o objeto 'error' completo para evitar vazamento de dados no console
        console.error("Falha na autenticação.");

        // Exibimos a mensagem para o usuário saber o que houve
        alert(error.message || "Erro ao tentar fazer login.");
    }
}
</script>