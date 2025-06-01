import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../pages/HomeView.vue'
import AuthView from '../pages/AuthView.vue'
import HowWorkView from '../pages/HowWork.vue'
import ClientView from '../pages/ClientView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/login',
      name: 'login',
      component: AuthView
    },
    {
      path: '/how-works',
      name: 'how-it-works',
      component: HowWorkView
    },
    {
      path: '/client',
      name: 'client',
      component: ClientView,
      meta: { requiresAuth: true, role: 'CLIENTE' }
    }
  ]
})

// Middleware de autenticación
router.beforeEach((to, from, next) => {
  const user = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : null
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)
  const requiresRole = to.matched.find(record => record.meta.role)

  console.log('user:', user);
  console.log('user.role:', user?.role);
  console.log('requiresRole:', requiresRole?.meta?.role);

  if (requiresAuth && !user) {
    next('/login')
  } else if (requiresRole) {
    // Permitir si el rol empieza con 'C' (por ejemplo, 'CLIENTE' o 'C')
    const userRole = (user?.role || '').toUpperCase();
    const requiredRole = (requiresRole.meta.role || '').toUpperCase();
    if (!userRole.startsWith(requiredRole[0])) {
      next('/')
    } else {
      next()
    }
  } else {
    next()
  }
})


export default router 