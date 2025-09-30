<script setup>
const emit = defineEmits(['close']);
</script>

<template>
    <div class="fixed inset-0 bg-black bg-opacity-50 backdrop-blur-sm flex justify-center items-center z-40">
        <div class="mx-auto w-[800px] h-[900px] bg-white rounded-xl shadow-lg drop-shadow-md flex flex-col">
            <div class="px-6 py-4 flex justify-between">
                <div>
                    <h2 class="flex font-bold items-center text-4xl">Adicionar EPI's</h2>
                </div>
                <div @click="emit('close')" style="cursor: pointer" class="text-gray-600">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="3" d="M6 18L18 6M6 6l12 12">
                        </path>
                    </svg>
                </div>
            </div>
            <hr class="bg-gray-600" />

            <div class="px-6 pt-3 pb-6 flex-grow flex flex-col justify-center items-center">
                <div class="w-full max-w-2xl space-y-6">

                    <div class="space-x-4 flex">
                        <input type="text" placeholder="Nome"
                            class="flex-1 ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100 focus:placeholder-gray-500" />
                    </div>
                    <div>
                        <input type="text" placeholder="Codigo"
                            class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100 focus:placeholder-gray-500" />
                    </div>
                    <div>
                        <input type="" placeholder=""
                            class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100 focus:placeholder-gray-500" />
                    </div>
                    <div>
                        <input type="" placeholder=""
                            class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100 focus:placeholder-gray-500" />
                    </div>
                    <div>
                        <input type="" placeholder=""
                            class="w-full ring-1 ring-gray-400 rounded-md text-lg px-3 py-3 outline-none bg-gray-100 focus:placeholder-gray-500" />
                    </div>
                    <div class="text-center pt-6">
                        <button class="
      font-bold text-white text-xl
      px-20 py-3 rounded-md 
      
      bg-gradient-to-r from-green-500 to-emerald-500
      bg-[length:200%_auto]
      bg-[position:0%_0%]
      hover:bg-[position:100%_0%]

      transition-all duration-500 ease-out
      hover:-translate-y-1
      hover:shadow-lg hover:shadow-green-500/40
    ">
                            Registrar
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>