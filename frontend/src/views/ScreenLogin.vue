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

                <!-- Campo Login -->
                <div class="block relative">
                    <label for="login" class="block text-gray-600 cursor-text text-lg font-medium mb-2">
                        Login
                    </label>

                    <input type="text" id="login" v-model="loginField" class="rounded-lg border border-gray-300 text-lg w-full
            text-black appearance-none block h-14 m-0 px-4
            focus:ring-2 ring-offset-2 ring-gray-900 outline-0" />
                </div>

                <!-- Campo Senha -->
                <div class="block relative">
                    <label for="password" class="block text-gray-600 cursor-text text-lg font-medium mb-2">
                        Senha
                    </label>

                    <input type="password" id="password" v-model="password" class="rounded-lg border border-gray-300 text-lg w-full
            text-black appearance-none block h-14 m-0 px-4
            focus:ring-2 ring-offset-2 ring-gray-900 outline-0" />
                </div>

                <!-- Botão com hover -->
                <button type="submit" class="bg-blue-600 w-[180px] m-auto py-3 rounded-lg text-white text-[18px] font-bold
  transition-all duration-300 ease-out transform
  hover:bg-blue-800 hover:scale-[1.015] hover:shadow-md hover:brightness-105">
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

const loginField = ref("");
const password = ref("");

function login() {
    router.push({ name: "Home" });
}
</script>
