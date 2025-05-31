<template>
  <div class="home">
    <!-- Hero Section -->
    <section class="hero">
      <div class="hero-content">
        <h1>
          Gestión Inteligente de
          <span class="highlight">Tareas Geoespaciales</span>
        </h1>
        <p>Organiza, ubica y prioriza tus actividades en un mapa interactivo</p>
        <button class="cta-button">Comenzar ahora</button>
      </div>
      <div class="hero-image">
        <div class="map-preview">
          <!-- Simulación de mapa con marcadores -->
          <div class="map-container">
            <div class="marker completed" style="top: 30%; left: 20%">
              <span class="tooltip">Tarea completada</span>
            </div>
            <div class="marker pending" style="top: 50%; left: 60%">
              <span class="tooltip">Tarea pendiente</span>
            </div>
            <div class="marker pending" style="top: 70%; left: 40%">
              <span class="tooltip">Tarea pendiente</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Características -->
    <section class="features">
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
    </section>

    <!-- CTA Final -->
    <section class="final-cta">
      <h2>¿Listo para optimizar tu gestión?</h2>
      <button class="cta-button">Registrarse Gratis</button>
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
  max-width: 1920px;
  margin: 0 auto;
  padding: 0 5%;
  display: grid;
  grid-template-columns: 45% 65%;
  align-items: center;
  justify-content: space-between;
}

.hero-content {
  padding-right: 5%;
}

.hero-content h1 {
  font-size: 4rem;
  line-height: 1.2;
  margin-bottom: 1.5rem;
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
  margin-bottom: 2.5rem;
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
}

.cta-button:hover {
  background: var(--primary-blue-hover);
  box-shadow: 0 0 20px var(--blue-glow);
  transform: translateY(-2px);
}

/* Mapa Preview */
.hero-image {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.map-preview {
  width: 100%;
  max-width: 800px;
  background: var(--bg-secondary);
  border-radius: 20px;
  overflow: hidden;
  aspect-ratio: 16/9;
  border: 1px solid var(--border-blue);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
}

.map-container {
  width: 100%;
  height: 100%;
  position: relative;
  background: linear-gradient(135deg, var(--bg-secondary) 0%, var(--bg-primary) 100%);
}

.marker {
  width: 24px;
  height: 24px;
  border-radius: 50%;
  position: absolute;
  cursor: pointer;
  transition: all 0.3s ease;
}

.marker.completed {
  background: var(--primary-blue);
  box-shadow: 0 0 15px var(--blue-glow);
}

.marker.pending {
  background: var(--blue-neon);
  box-shadow: 0 0 15px var(--blue-neon);
}

.marker:hover {
  transform: scale(1.2);
}

.marker .tooltip {
  position: absolute;
  background: var(--bg-secondary);
  color: var(--text-primary);
  padding: 0.5rem 1rem;
  border-radius: 6px;
  font-size: 0.875rem;
  white-space: nowrap;
  top: -40px;
  left: 50%;
  transform: translateX(-50%);
  opacity: 0;
  visibility: hidden;
  transition: all 0.2s ease;
  border: 1px solid var(--border-blue);
}

.marker:hover .tooltip {
  opacity: 1;
  visibility: visible;
  transform: translateX(-50%) translateY(-5px);
}

/* Features Section */
.features {
  width: 100%;
  max-width: 1920px;
  margin: 0 auto;
  padding: 5rem 5%;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2.5rem;
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
  background: var(--bg-secondary);
  padding: 6rem 5%;
  text-align: center;
  width: 100%;
}

.final-cta h2 {
  color: var(--text-primary);
  font-size: 3rem;
  margin-bottom: 2rem;
  max-width: 800px;
  margin-left: auto;
  margin-right: auto;
}
</style>
