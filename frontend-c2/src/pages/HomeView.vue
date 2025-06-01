<script setup>
import { ref, onMounted } from 'vue';
import "leaflet/dist/leaflet.css";
import { LMap, LTileLayer, LMarker, LIcon, LPopup } from "@vue-leaflet/vue-leaflet";
import { getMarkerIcon, markerStyles } from '../utils/mapIcons';

const zoom = ref(13);
const center = ref([-33.4569, -70.6483]); // Santiago, Chile como ejemplo
const markers = ref([
  {
    id: 1,
    position: [-33.4569, -70.6483],
    popup: "Tarea pendiente: Revisión de equipos",
    status: "pending"
  },
  {
    id: 2,
    position: [-33.4489, -70.6583],
    popup: "Tarea completada: Mantenimiento",
    status: "completed"
  }
]);

</script>

<template>
  <div class="home">
    <!-- Hero Section -->
    <section class="hero">
      <div class="content-wrapper hero-inner">
        <div class="hero-content">
          <h1>
            Gestión Inteligente de
            <span class="highlight">Tareas georeferenciadas</span>
          </h1>
          <p>Gestiona y monitorea tus tareas según tu ubicación.</p>
          <router-link to="/login" class="cta-button">Comenzar ahora</router-link>
        </div>
        <div class="hero-image">
          <div class="map-preview">
            <l-map
              v-model:zoom="zoom"
              :center="center"
              :use-global-leaflet="false"
              class="map-container"
            >
              <l-tile-layer
                url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                layer-type="base"
                name="OpenStreetMap"
              />
              <l-marker
                v-for="marker in markers"
                :key="marker.id"
                :lat-lng="marker.position"
                :icon="getMarkerIcon(marker.status)"
              >
                <l-popup>{{ marker.popup }}</l-popup>
              </l-marker>
            </l-map>
          </div>
        </div>
      </div>
    </section>

    <!-- Características -->
    <section class="features">
      <div class="content-wrapper">
        <div class="feature-card">
          <i class="fas fa-map-marker-alt feature-icon"></i>
          <h3>Asociación Geográfica</h3>
          <p>Vincula tareas a ubicaciones exactas con tecnología geoespacial</p>
        </div>

        <div class="feature-card">
          <i class="fas fa-filter feature-icon"></i>
          <h3>Filtros Avanzados</h3>
          <p>Encuentra tareas por estado, sector o proximidad</p>
        </div>

        <div class="feature-card">
          <i class="fas fa-bell feature-icon"></i>
          <h3>Alertas Automáticas</h3>
          <p>Notificaciones para vencimientos y tareas cercanas</p>
        </div>
      </div>
    </section>

    <!-- CTA Final -->
    <section class="final-cta">
      <h2>¿Listo para optimizar tu gestión?</h2>
      <p>Comienza a gestionar tus tareas de manera eficiente y segura.</p>
      <router-link to="/login" class="cta-button">Registrarse Gratis</router-link>
    </section>
  </div>
</template>

<style scoped>
.home {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  background: var(--bg-primary);
}

/* Hero Section */
.hero {
  width: 100%;
  padding: 4rem 0;
  display: flex;
  justify-content: center;
}

.hero-inner {
  display: grid;
  grid-template-columns: 40% 55%;
  gap: 5%;
  align-items: center;
}

.hero-content {
  padding-right: 0;
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.hero-content h1 {
  font-size: 3.5rem;
  line-height: 1.3;
  margin-bottom: 0;
  color: var(--text-primary);
}

.hero-content .highlight {
  background: linear-gradient(120deg, var(--primary-blue) 0%, var(--blue-neon) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-content p {
  font-size: 1.5rem;
  color: var(--text-secondary);
  margin-bottom: 1rem;
  line-height: 1.6;
}

.cta-button {
  background: var(--primary-blue);
  color: var(--text-primary);
  border: none;
  padding: 1rem 2.5rem;
  border-radius: 8px;
  font-size: 1.25rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  width: 200px;
  text-align: center;
  display: inline-block;
  text-decoration: none;
}

.cta-button:hover {
  background: var(--primary-blue-hover);
  box-shadow: 0 0 20px var(--blue-glow);
  transform: translateY(-2px);
}

/* Para el botón en el hero section que no necesita estar centrado */
.hero-content .cta-button {
  margin: 0;
  display: inline-block;
  align-self: center;
}

/* Mapa Preview */
.hero-image {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding-left: 2rem;
}

.map-preview {
  width: 100%;
  height: 500px;
  background: var(--bg-secondary);
  border-radius: 20px;
  overflow: hidden;
  border: 1px solid var(--border-blue);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.map-container {
  width: 100%;
  height: 100%;
  z-index: 1;
}

/* Features Section */
.features {
  width: 1200px;
  padding: 5rem 0;
  margin: 0 auto;
}

.features .content-wrapper {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2.5rem;
  padding: 0 2rem;
}

.feature-card {
  background: var(--bg-secondary);
  border: 1px solid var(--border-blue);
  border-radius: 16px;
  padding: 2.5rem;
  text-align: center;
  transition: all 0.3s ease;
  height: 100%;
}

.feature-card:hover {
  border-color: var(--blue-neon);
  box-shadow: 0 4px 20px var(--blue-glow);
  transform: translateY(-5px);
}

.feature-icon {
  font-size: 3rem;
  color: var(--blue-neon);
  margin-bottom: 1.5rem;
}

.feature-card h3 {
  color: var(--text-primary);
  font-size: 1.75rem;
  margin-bottom: 1rem;
}

.feature-card p {
  color: var(--text-secondary);
  line-height: 1.6;
  font-size: 1.1rem;
}

/* Final CTA */
.final-cta {
  width: 1200px;
  margin: 0 auto;
  background: var(--bg-secondary);
  padding: 3rem 2rem;
  text-align: center;
  border-radius: 20px;
  margin-bottom: 2rem;
}

.final-cta h2 {
  font-size: 2.5rem;
  margin-bottom: 1rem;
  color: var(--text-primary);
}

.final-cta p {
  margin-bottom: 1.5rem;
  color: var(--text-primary);
  font-size: 1.1rem;
}

@media (max-width: 1280px) {
  .features,
  .final-cta {
    width: 90%;
  }
}

@media (max-width: 1024px) {
  .features .content-wrapper {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .features .content-wrapper {
    grid-template-columns: 1fr;
  }
}

/* Estilos de los marcadores personalizados */
.custom-marker-container {
  background: transparent;
  border: none;
}

.custom-marker:hover {
  transform: scale(1.2);
}
</style>
