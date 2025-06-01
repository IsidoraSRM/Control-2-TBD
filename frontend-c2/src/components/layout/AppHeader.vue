<template>
  <header class="header">
    <div class="header-content">
      <!-- Logo/Branding -->
      <div class="logo">
        <svg class="logo-icon" width="32" height="32" viewBox="0 0 32 32">
          <path d="M16 4L4 10v12l12 6 12-6V10L16 4z" fill="none" stroke="var(--text-primary)" stroke-width="2"/>
          <path d="M16 4v12l12-6M4 10l12 6" stroke="var(--text-primary)" stroke-width="2"/>
        </svg>
        <span class="logo-text">TBD Control</span>
      </div>

      <!-- Menú hamburguesa para móvil -->
      <button class="menu-toggle" @click="isMenuOpen = !isMenuOpen" :class="{ active: isMenuOpen }">
        <span></span>
        <span></span>
        <span></span>
      </button>

      <!-- Navegación -->
      <nav class="nav" :class="{ 'nav-open': isMenuOpen }">
        <ul class="nav-list">
          <li><router-link to="/" class="nav-link" active-class="active">Home</router-link></li>
          
          <li>
            <router-link to="/how-works" class="nav-link" data-tooltip="Gestión de tareas y más">
              ¿Como funciona?
            </router-link>
          </li>

        </ul>

        <!-- Botones de acción -->
        <div class="auth-buttons">
          <template v-if="isLoggedIn">
            <button class="btn btn-secondary" @click="logout">Cerrar sesión</button>
          </template>
          <template v-else>
            <router-link to="/login" class="btn btn-primary">Registro/Login</router-link>
          </template>
        </div>

      </nav>
    </div>
  </header>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'

const isMenuOpen = ref(false)
const isDarkMode = ref(true)
const router = useRouter()

const isLoggedIn = computed(() => {
  const user = localStorage.getItem('user')
  return !!user
})

const logout = () => {
  localStorage.removeItem('user')
  router.push('/login')
}

const toggleTheme = () => {
  isDarkMode.value = !isDarkMode.value
  // Aquí iría la lógica para cambiar el tema
}
</script>

<style scoped>
.header {
  background: var(--bg-secondary);
  border-bottom: 1px solid var(--border-blue);
  padding: 1rem 2rem;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 100;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* Logo */
.logo {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.logo-text {
  color: var(--text-primary);
  font-size: 1.5rem;
  font-weight: 600;
  transition: text-shadow 0.3s ease;
}

.logo:hover .logo-text {
  text-shadow: 0 0 8px var(--blue-neon);
}

/* Navegación */
.nav {
  display: flex;
  align-items: center;
  gap: 2rem;
}

.nav-list {
  display: flex;
  gap: 1.5rem;
  list-style: none;
}

.nav-link {
  color: var(--text-primary);
  text-decoration: none;
  padding: 0.5rem;
  transition: all 0.3s ease;
  position: relative;
}

.nav-link::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 0;
  height: 2px;
  background: var(--primary-blue);
  transition: width 0.3s ease;
}

.nav-link:hover::after,
.nav-link.active::after {
  width: 100%;
}

/* Botones */
.auth-buttons {
  display: flex;
  gap: 1rem;
}

.btn {
  padding: 0.5rem 1.5rem;
  border-radius: 4px;
  font-weight: 500;
  transition: all 0.3s ease;
  cursor: pointer;
}

.btn-primary {
  background: var(--primary-blue);
  color: var(--text-primary);
  border: none;
}

.btn-primary:hover {
  background: var(--primary-blue-hover);
  box-shadow: 0 0 12px var(--blue-glow);
}

.btn-secondary {
  background: transparent;
  color: var(--text-primary);
  border: 1px solid var(--border-blue);
}

.btn-secondary:hover {
  box-shadow: 0 0 8px var(--blue-glow);
}

/* Controles opcionales */
.optional-controls {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.language-select {
  background: transparent;
  color: var(--text-secondary);
  border: 1px solid var(--border-blue);
  padding: 0.3rem;
  border-radius: 4px;
}

.theme-toggle {
  background: transparent;
  border: none;
  color: var(--text-primary);
  cursor: pointer;
  padding: 0.5rem;
}

/* Menú hamburguesa */
.menu-toggle {
  display: none;
  flex-direction: column;
  justify-content: space-between;
  width: 30px;
  height: 21px;
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 0;
  z-index: 10;
}

.menu-toggle span {
  width: 100%;
  height: 3px;
  background-color: var(--text-primary);
  transition: all 0.3s ease;
}

/* Tooltip */
.nav-link[data-tooltip] {
  position: relative;
}

.nav-link[data-tooltip]::before {
  content: attr(data-tooltip);
  position: absolute;
  bottom: -30px;
  left: 50%;
  transform: translateX(-50%);
  padding: 0.5rem;
  background: var(--card-bg);
  color: var(--text-primary);
  border-radius: 4px;
  font-size: 0.875rem;
  white-space: nowrap;
  opacity: 0;
  visibility: hidden;
  transition: all 0.3s ease;
}

.nav-link[data-tooltip]:hover::before {
  opacity: 1;
  visibility: visible;
}

/* Responsive */
@media (max-width: 768px) {
  .menu-toggle {
    display: flex;
  }

  .nav {
    position: fixed;
    top: 0;
    right: -100%;
    height: 100vh;
    width: 80%;
    max-width: 400px;
    background: var(--bg-secondary);
    flex-direction: column;
    padding: 5rem 2rem;
    transition: right 0.3s ease;
  }

  .nav.nav-open {
    right: 0;
  }

  .nav-list {
    flex-direction: column;
    align-items: center;
  }

  .auth-buttons {
    flex-direction: column;
    width: 100%;
  }

  .optional-controls {
    margin-top: 2rem;
  }

  .menu-toggle.active span:nth-child(1) {
    transform: translateY(9px) rotate(45deg);
  }

  .menu-toggle.active span:nth-child(2) {
    opacity: 0;
  }

  .menu-toggle.active span:nth-child(3) {
    transform: translateY(-9px) rotate(-45deg);
  }
}
</style>
