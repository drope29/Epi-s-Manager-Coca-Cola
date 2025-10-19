<script setup>
import AnimacaoCaminhao from '../components/AnimacaoCaminhao.vue';
</script>

<template>
  <div class="relative flex flex-col md:flex-row items-center justify-center h-screen gap-8 homepage-background">

    <router-link to="/epis" class="
    flex-shrink-0 isolate flex items-center relative overflow-hidden cursor-pointer bg-transparent
    rounded-lg border-4 border-red-600 py-6 px-20
    text-red-600 text-[22px] font-bold uppercase no-underline
    transition-all duration-1000 hover:text-white
    before:content-[''] before:absolute before:top-0 before:left-[-40px]
    before:h-full before:w-0 before:bg-red-600 before:-z-10
    before:skew-x-[45deg] before:transition-all before:duration-1000
    hover:before:w-[160%]
  ">
      Lista Epi's
    </router-link>

    <router-link to="/colaboradores" class="
    flex-shrink-0 isolate flex items-center relative overflow-hidden cursor-pointer bg-transparent
    rounded-lg border-4 border-black py-6 px-8
    text-black text-[20px] font-bold uppercase no-underline
    transition-all duration-1000 hover:text-white
    before:content-[''] before:absolute before:top-0 before:left-[-40px]
    before:h-full before:w-0 before:bg-black before:-z-10
    before:skew-x-[45deg] before:transition-all before:duration-1000
    hover:before:w-[160%]
  ">
      Lista Colaboradores
    </router-link>
  </div>

  <AnimacaoCaminhao />
</template>

<style scoped>
.homepage-background {
  position: relative;
  isolation: isolate;
}

.homepage-background::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url('/logo-femsa.png');
  background-color: #f8f8f8;
  background-size: 200px auto;   
  background-repeat: no-repeat;
  background-position: left bottom;
}
</style>