<script setup>
import AnimacaoCaminhao from './components/AnimacaoCaminhao.vue';
</script>

<template>
  <div class="flex items-center justify-center h-screen bg-gray-100 gap-8">
    <button class="
      flex-shrink-0 isolate flex items-center relative overflow-hidden cursor-pointer bg-transparent
      border-4 border-yellow-400
      py-6 px-20
      text-yellow-400 text-[22px] font-bold uppercase no-underline
      transition-all duration-1000
      hover:text-white
      before:content-[''] before:absolute before:top-0 before:left-[-40px]
      before:h-full before:w-0
      before:bg-yellow-400 before:-z-10
      before:skew-x-[45deg]
      before:transition-all before:duration-1000
      hover:before:w-[160%]
    ">
      Lista Epi's
    </button>

    <button class="
      flex-shrink-0 isolate flex items-center relative overflow-hidden cursor-pointer bg-transparent
      border-4 border-black
      py-6 px-8
      text-black text-[20px] font-bold uppercase no-underline
      transition-all duration-1000
      hover:text-white
      before:content-[''] before:absolute before:top-0 before:left-[-40px]
      before:h-full before:w-0
      before:bg-black before:-z-10
      before:skew-x-[45deg]
      before:transition-all before:duration-1000
      hover:before:w-[160%]
    ">
      Lista Colaboradores
    </button>
  </div>

  <AnimacaoCaminhao />
</template>