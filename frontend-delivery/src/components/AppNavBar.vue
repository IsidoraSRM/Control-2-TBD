<script>
import { authService } from '../services/authService';

export default {
    name: 'NavBar',
    data() {
        return {
            menuOpen: false,
            isLoggedIn: false,
            userRole: ''
        }
    },
    created() {
        this.checkAuthStatus();
    },
    methods: {
        toggleMenu() {
            this.menuOpen = !this.menuOpen;
        },
        checkAuthStatus() {
            this.isLoggedIn = authService.isAuthenticated();
            this.userRole = authService.getUserRole();
        },
        logout() {
            authService.logout();
            this.checkAuthStatus();
            this.$router.push('/login');
        }
    }
}
</script>

<template>
    <nav>
        <router-link 
            :to="isLoggedIn && userRole === 'CLIENTE' ? '/homeClient' : '/'" 
            class="title"
        >
            <img src="@/assets/DocDelivery.png" alt="Logo" class="nav-logo">
            DocDelivery
        </router-link>
        
        <div class="menu" @click="toggleMenu">
            <span></span>
            <span></span>
            <span></span>   
        </div>
        
        <ul :class="{ open: menuOpen }">
            <!-- Opciones para cliente -->
            <template v-if="isLoggedIn && userRole === 'CLIENTE'">
                <li>
                    <router-link to="/homeClient" active-class="active">Inicio</router-link>
                </li>
                <li>
                    <router-link to="/client" active-class="active">Realizar un Pedido</router-link>
                </li>
            </template>

            <!-- Opciones para admin -->
            <li v-if="isLoggedIn && userRole === 'ADMIN'">
                <router-link to="/admin" active-class="active">Panel Admin</router-link>
            </li>

            <!-- Opciones para trabajador -->
            <li v-if="isLoggedIn && userRole === 'TRABAJADOR'">
                <router-link to="/trabajador" active-class="active">Panel Trabajador</router-link>
            </li>

            <!-- Menú usuario -->
            <template v-if="isLoggedIn">

                <li>
                    <a href="#" @click.prevent="logout">Cerrar Sesión</a>
                </li>
            </template>

            
            <li v-if="!isLoggedIn">
                <router-link to="/" active-class="active">Inicio</router-link>
            </li>
            
            <li v-if="!isLoggedIn">
                <router-link to="/login" active-class="active">Iniciar Sesión</router-link>
            </li>


        </ul>
    </nav>
</template>

<style scoped>
/* Estilos base */
nav {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #125A6C;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    z-index: 1000;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    padding: 1rem 1rem;

}

.title {
    font-size: 1.5rem;
    font-weight: bolder;
    text-decoration: none;
    color: #E2DCD2;
    display: flex;
    align-items: center;
    gap: 8px;
}

.nav-logo {
    height: 30px;
    width: auto;
}

ul {
    display: flex;
    gap: 0.5rem;
    margin: 0;
    padding-right: 20px;
    list-style: none;
}

li a {
    display: block;
    text-decoration: none;
    color: #E2DCD2;
    font-weight: bold;
    padding: 0.5rem 1rem;
    border-radius: 0.5rem;
    transition: background-color 0.3s;
}

li a:not(.active):hover {
    background-color: #B55529;
}

/* Menú hamburguesa */
.menu {
    display: none;
    cursor: pointer;
    flex-direction: column;
    justify-content: space-between;
    width: 2.25rem;
    height: 2rem;
    position: relative;
    z-index: 1001;
}

.menu span {
    height: 0.4rem;
    width: 100%;
    background-color: #E2DCD2;
    border-radius: 0.2rem;
    transition: all 0.3s;
}

/* Responsive */
@media (max-width: 768px) {
    .menu {
        display: flex;
    }
    
    ul {
        display: none;
        flex-direction: column;
        position: absolute;
        top: 100%;
        left: 0;
        right: 0;
        background-color: #125A6C;
        padding: 1rem;
    }
    
    ul.open {
        display: flex;
    }
    
    li {
        width: 100%;
        text-align: center;
    }
}
</style>