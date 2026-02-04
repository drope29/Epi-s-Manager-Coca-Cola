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
        // NOTA: Esta rota NÃO tem 'requiresAuth', pois é pública.
    },
    {
        path: '/home',
        name: 'Home',
        component: Home,
        meta: { requiresAuth: true } // <--- PROTEGIDA
    },
    {
        path: '/epis',
        name: 'Epis',
        component: ScreenEpis,
        meta: { requiresAuth: true } // <--- PROTEGIDA
    },
    {
        path: '/colaboradores',
        name: 'Colaboradores',
        component: ScreenColaboradores,
        meta: { requiresAuth: true } // <--- PROTEGIDA
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

// --- O PORTEIRO (Navigation Guard) ---
router.beforeEach((to, from, next) => {
    // 1. Verifica se a rota exige autenticação
    const requiresAuth = to.matched.some(record => record.meta.requiresAuth);

    // 2. Tenta pegar o token do bolso
    const token = localStorage.getItem('token');

    // 3. Lógica de Redirecionamento
    if (requiresAuth && !token) {
        // Se a rota é protegida e NÃO tem token -> Manda pro Login
        next('/');
    } else if (to.path === '/' && token) {
        // (Opcional) Se tentar ir pro Login mas JÁ TEM token -> Manda pra Home
        next('/home');
    } else {
        // Tudo certo -> Pode passar
        next();
    }
});

export default router