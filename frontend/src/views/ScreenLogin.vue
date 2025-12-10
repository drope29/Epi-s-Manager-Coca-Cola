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
import { ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

const backUrl = import.meta.env.VITE_BACKEND_URL;

const loginField = ref("");
const password = ref("");

async function login() {
    try {
        // Limpa qualquer token antigo para evitar conflitos
        localStorage.removeItem('token');

        const response = await fetch(`${backUrl}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                username: loginField.value,
                password: password.value
            })
        });

        if (!response.ok) {
            throw new Error('Usuário ou senha incorretos!');
        }

        const data = await response.json();

        // LÓGICA CORRIGIDA: Verifica se o token está aninhado ou direto
        let tokenReal = null;

        if (data.token && typeof data.token === 'string') {
            tokenReal = data.token;
        } else if (data.token && data.token.token) {
            tokenReal = data.token.token; // O caso do seu backend
        } else if (data.access_token) {
            tokenReal = data.access_token;
        }

        if (tokenReal) {
            localStorage.setItem('token', tokenReal);
            router.push({ name: "Home" });
        } else {
            console.error("Erro: Token não encontrado na resposta", data);
            alert("Erro ao autenticar: Token inválido.");
        }

    } catch (error) {
        console.error("Erro no login:", error);
        alert(error.message || "Erro ao tentar fazer login.");
    }
}
</script>