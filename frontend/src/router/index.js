import { createRouter, createWebHistory } from 'vue-router'

import Home from '../views/Home.vue'
import ScreenEpis from '../views/screenEpis.vue'
import ScreenColaboradores from '../views/screenColaboradores.vue'
import ScreenLogin from '../views/ScreenLogin.vue'

const routes = [
    {
        path: '/',
        name: 'Login',
        component: ScreenLogin
    },
    {
        path: '/home',
        name: 'Home',
        component: Home
    },
    {
        path: '/epis',
        name: 'Epis',
        component: ScreenEpis
    },
    {
        path: '/colaboradores',
        name: 'Colaboradores',
        component: ScreenColaboradores
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router
