<template>
  <div class="background-effects">
    <div class="floating-particles"></div>
    <div class="wave-animation"></div>
  </div>
  <div class="login-container">
    <div class="login-card" ref="cardRef">
      <h2>{{ isLogin ? 'Iniciar sesión' : 'Registrarse' }}</h2>
      <form @submit.prevent="isLogin ? handleLogin() : handleRegister()">
        <template v-if="isLogin">
          <div class="input-group">
            <input v-model="loginData.correo" type="email" id="login-correo" required />
            <label for="login-correo">Correo electrónico</label>
          </div>
          <div class="input-group">
            <input v-model="loginData.contrasena" type="password" id="login-contrasena" required />
            <label for="login-contrasena">Contraseña</label>
          </div>
        </template>
        <template v-else>
          <div class="input-group">
            <input v-model="registerData.nombreUsuario" type="text" id="register-nombreUsuario" required />
            <label for="register-nombreUsuario">Nombre de usuario</label>
          </div>
          <div class="input-group">
            <input v-model="registerData.correo" type="email" id="register-correo" required />
            <label for="register-correo">Correo electrónico</label>
          </div>
          <div class="input-group">
            <input v-model="registerData.contrasena" type="password" id="register-contrasena" required />
            <label for="register-contrasena">Contraseña</label>
          </div>
          <div class="map-section">
            <label>Selecciona tu ubicación en el mapa</label>
            <div class="map-container" style="margin-bottom: 1rem;">
              <l-map
                :zoom="mapZoom"
                :center="marker || mapCenter"
                style="height: 250px; width: 100%;"
                @click="onMapClick"
              >
                <l-tile-layer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
                <l-marker
                  v-if="marker"
                  :lat-lng="marker"
                  :icon="getMarkerIcon('pending')"
                />
              </l-map>
            </div>
            <div v-if="registerData.latitud !== null && registerData.longitud !== null" class="coordinates-info" style="text-align:center; margin-bottom: 1rem;">
              <span>Latitud: {{ registerData.latitud?.toFixed(6) }}</span> |
              <span>Longitud: {{ registerData.longitud?.toFixed(6) }}</span>
            </div>
          </div>
        </template>
        <button 
          :class="['login-button', { loading, 'evasive-button': shouldButtonBeEvasive }]" 
          type="submit"
          ref="loginButtonRef"
          @mouseenter="handleButtonMouseEnter"
          @mouseleave="handleButtonMouseLeave"
        >
          {{ loading ? '' : (isLogin ? 'Iniciar Sesión' : 'Registrarse') }}
        </button>
      </form>
      <div v-if="error" class="error-message">{{ error }}</div>
      <div class="links">
        <a href="#" @click.prevent="isLogin = !isLogin">
          {{ isLogin ? 'Registrarse' : 'Ya tienes cuenta? Inicia sesión' }}
        </a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
import { LMap, LTileLayer, LMarker } from '@vue-leaflet/vue-leaflet';
import 'leaflet/dist/leaflet.css';
import { getMarkerIcon } from '../utils/mapIcons';

const router = useRouter();
const isLogin = ref(true);
const loading = ref(false);
const error = ref('');
const cardRef = ref(null);
const loginButtonRef = ref(null);

const API_URL = import.meta.env.VITE_API_URL;

const loginData = ref({
  correo: '',
  contrasena: ''
});

const registerData = ref({
  nombreUsuario: '',
  correo: '',
  contrasena: '',
  fechaRegistro: new Date().toISOString(),
  rol: 'CLIENTE',
  latitud: null,
  longitud: null
});

const mapCenter = ref([-33.45, -70.65]);
const mapZoom = ref(13);
const marker = ref(null);

function onMapClick(e) {
  const { lat, lng } = e.latlng || e;
  registerData.value.latitud = lat;
  registerData.value.longitud = lng;
  marker.value = [lat, lng];
}

// Computed para determinar si el botón debe ser evasivo
const shouldButtonBeEvasive = computed(() => {
  if (!isLogin.value) return false; // Solo para login, no para registro
  return !loginData.value.correo || !loginData.value.contrasena;
});

// Función para manejar cuando el mouse entra al botón
const handleButtonMouseEnter = () => {
  if (shouldButtonBeEvasive.value && loginButtonRef.value) {
    moveButtonAway();
  }
};

// Función para mover el botón lejos del mouse
const moveButtonAway = () => {
  const button = loginButtonRef.value;
  if (!button) return;
  
  // Generar posición aleatoria dentro del contenedor
  const container = button.closest('.login-card');
  const containerRect = container.getBoundingClientRect();
  const buttonRect = button.getBoundingClientRect();
  
  // Calcular límites para que el botón no se salga del contenedor
  const maxX = containerRect.width - buttonRect.width - 50; // 60px de margen
  const maxY = containerRect.height - buttonRect.height - 50;
  
  const randomX = Math.random() * maxX - maxX / 2;
  const randomY = Math.random() * maxY - maxY / 2;
  
  // Aplicar transformación
  button.style.transform = `translate(${randomX}px, ${randomY}px) scale(0.9)`;
  button.style.transition = 'transform 0.2s ease-out';
  
  // Volver a la posición original después de un tiempo
  setTimeout(() => {
    if (shouldButtonBeEvasive.value) {
      button.style.transform = 'translate(0, 0) scale(1)';
    }
  }, 1000);
};

// Función para manejar cuando el mouse sale del botón
const handleButtonMouseLeave = () => {
  if (loginButtonRef.value && !shouldButtonBeEvasive.value) {
    loginButtonRef.value.style.transform = 'translate(0, 0) scale(1)';
  }
};

const handleLogin = async () => {
  if (!loginData.value.correo || !loginData.value.contrasena) {
    error.value = 'Completa todos los campos para iniciar sesión.';
    return;
  }
  error.value = '';
  loading.value = true;
  try {
    const response = await axios.post(`${API_URL}/auth/login`, loginData.value);
    localStorage.setItem('user', JSON.stringify(response.data));
    error.value = '¡Login exitoso!';
    // Redirigir según el rol
    if (response.data.role === 'ADMIN') {
      router.push('/admin');
    } else {
      router.push('/client');
    }
  } catch (err) {
    error.value = err.response?.data || 'Error al iniciar sesión';
  } finally {
    loading.value = false;
  }
};

const handleRegister = async () => {
  if (!registerData.value.nombreUsuario || !registerData.value.correo || !registerData.value.contrasena || registerData.value.latitud === null || registerData.value.longitud === null) {
    error.value = 'Completa todos los campos para registrarte.';
    return;
  }
  error.value = '';
  loading.value = true;
  try {
    const response = await axios.post(`${API_URL}/auth/register`, registerData.value);
    if (response.status === 201) {
      // Registro exitoso, puedes redirigir o mostrar mensaje
      isLogin.value = true;
      // Limpiar campos de registro
      registerData.value = {
        nombreUsuario: '', correo: '', contrasena: '', fechaRegistro: new Date().toISOString(), rol: 'CLIENTE', latitud: null, longitud: null
      };
      error.value = '¡Registro exitoso! Ahora inicia sesión.';
    }
  } catch (err) {
    error.value = err.response?.data || 'Error al registrar usuario';
  } finally {
    loading.value = false;
  }
};

// Efectos de interacción (focus/blur)
onMounted(() => {
  const inputs = document.querySelectorAll('.input-group input');
  inputs.forEach(input => {
    input.addEventListener('focus', function() {
      this.parentElement.style.transform = 'scale(1.02)';
    });
    input.addEventListener('blur', function() {
      this.parentElement.style.transform = 'scale(1)';
    });
  });

  // Efecto de movimiento del mouse
  const handleMouseMove = (e) => {
    if (!cardRef.value) return;
    const rect = cardRef.value.getBoundingClientRect();
    const centerX = rect.left + rect.width / 2;
    const centerY = rect.top + rect.height / 2;
    const rotateX = (e.clientY - centerY) / 95;
    const rotateY = (centerX - e.clientX) / 90;
    cardRef.value.style.transform = `perspective(1000px) rotateX(${rotateX}deg) rotateY(${rotateY}deg)`;
  };
  const handleMouseLeave = () => {
    if (!cardRef.value) return;
    cardRef.value.style.transform = 'perspective(1000px) rotateX(0deg) rotateY(0deg)';
  };
  window.addEventListener('mousemove', handleMouseMove);
  window.addEventListener('mouseleave', handleMouseLeave);

  // Limpieza
  onUnmounted(() => {
    window.removeEventListener('mousemove', handleMouseMove);
    window.removeEventListener('mouseleave', handleMouseLeave);
  });
});
</script>

<style scoped>
:root {
  --bg-primary: #0A0E17;
  --bg-secondary: #121A2A;
  --card-bg: #1A2238;
  --border-blue: #2A3A6D;
  --text-primary: #E0E0E0;
  --text-secondary: #A0A0A0;
  --placeholder-color: #6C757D;
  --primary-blue: #3A6BFF;
  --primary-blue-hover: #4A7BFF;
  --blue-gradient-start: #3A6BFF;
  --blue-gradient-end: #5A8BFF;
  --blue-neon: #00D1FF;
  --blue-glow: rgba(58, 107, 255, 0.3);
  --dark-gray: #2C3A58;
}
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}
body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  height: 100vh;
  background: linear-gradient(135deg, var(--bg-primary) 0%, var(--bg-secondary) 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  position: relative;
}
.background-effects {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  overflow: hidden;
}
.floating-particles::before,
.floating-particles::after {
  content: '';
  position: absolute;
  width: 200px;
  height: 200px;
  border-radius: 50%;
  background: radial-gradient(circle, var(--blue-glow) 0%, transparent 70%);
  animation: float 8s ease-in-out infinite;
}
.floating-particles::before {
  top: 20%;
  left: 10%;
  animation-delay: 0s;
}
.floating-particles::after {
  bottom: 20%;
  right: 10%;
  animation-delay: 4s;
}
.wave-animation {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 100px;
  background: linear-gradient(90deg, transparent, var(--blue-glow), transparent);
  opacity: 0.1;
  animation: wave 6s linear infinite;
}
@keyframes float {
  0%, 100% { transform: translateY(0px) scale(1); opacity: 0.5; }
  50% { transform: translateY(-30px) scale(1.1); opacity: 0.8; }
}
@keyframes wave {
  0% { transform: translateX(-100%); }
  100% { transform: translateX(100%); }
}
@keyframes pulse {
  0%, 100% { box-shadow: 0 0 20px var(--blue-glow); }
  50% { box-shadow: 0 0 30px var(--blue-glow), 0 0 40px var(--blue-glow); }
}
.login-container {
  position: relative;
  z-index: 10;
  width: 100%;
  max-width: 400px;
  padding: 20px;
}
.login-card {
  background: var(--card-bg);
  border: 1px solid var(--border-blue);
  border-radius: 20px;
  padding: 40px 30px;
  box-shadow: 
    0 20px 40px rgba(0, 0, 0, 0.4),
    0 0 20px var(--blue-glow),
    inset 0 1px 0 rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
  position: relative;
  overflow: hidden;
  animation: slideUp 0.6s ease-out;
}
.login-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(58, 107, 255, 0.1), transparent);
  animation: shimmer 3s infinite;
}
@keyframes shimmer {
  0% { left: -100%; }
  100% { left: 100%; }
}
.login-card h2 {
  color: var(--text-primary);
  font-size: 2.2rem;
  font-weight: 300;
  text-align: center;
  margin-bottom: 30px;
  text-shadow: 0 0 20px var(--blue-glow);
}
.input-group {
  position: relative;
  margin-bottom: 30px;
}
.input-group input {
  width: 100%;
  padding: 15px 0;
  font-size: 16px;
  color: var(--text-primary);
  background: transparent;
  border: none;
  border-bottom: 2px solid var(--border-blue);
  outline: none;
  transition: all 0.3s ease;
}
.input-group input:focus {
  border-bottom-color: var(--primary-blue);
  box-shadow: 0 2px 10px var(--blue-glow);
}
.input-group input:focus + label,
.input-group input:valid + label {
  top: -20px;
  font-size: 14px;
  color: var(--primary-blue);
  text-shadow: 0 0 10px var(--blue-glow);
}
.input-group label {
  position: absolute;
  top: 15px;
  left: 0;
  font-size: 16px;
  color: var(--placeholder-color);
  pointer-events: none;
  transition: all 0.3s ease;
}
.login-button {
  width: 50%;
  padding: 15px;
  font-size: 16px;
  font-weight: 600;
  color: white;
  background: linear-gradient(135deg, var(--blue-gradient-start) 0%, var(--blue-gradient-end) 100%);
  border: none;
  border-radius: 30px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin: 20px auto;
  display: block;
  box-shadow: 0 4px 15px var(--blue-glow);
  position: relative;
  overflow: hidden;
}
.login-button:hover {
  transform: scale(1.02);
  box-shadow: 0 6px 20px var(--blue-glow);
  animation: pulse 2s infinite;
}
.login-button:active {
  transform: scale(0.98);
}
.login-button.evasive-button {
  cursor: not-allowed;
}
.login-button.evasive-button:hover {
  animation: none;
}
.links {
  display: flex;
  justify-content: space-between;
  margin-top: 25px;
}
.links a {
  color: var(--text-secondary);
  text-decoration: none;
  font-size: 14px;
  transition: all 0.3s ease;
  position: relative;
}
.links a:hover {
  color: var(--primary-blue);
  text-shadow: 0 0 10px var(--blue-glow);
}
.links a::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 0;
  height: 1px;
  background: var(--primary-blue);
  transition: width 0.3s ease;
}
.links a:hover::after {
  width: 100%;
}
@media (max-width: 480px) {
  .login-container {
    padding: 15px;
  }
  .login-card {
    padding: 30px 20px;
  }
  .login-card h2 {
    font-size: 1.8rem;
  }
  .links {
    flex-direction: column;
    gap: 15px;
    text-align: center;
  }
}
@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(50px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
.login-button.loading {
  pointer-events: none;
  opacity: 0.8;
}
.login-button.loading::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 20px;
  height: 20px;
  margin: -10px 0 0 -10px;
  border: 2px solid transparent;
  border-top: 2px solid white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}
@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>