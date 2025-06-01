<script setup>
// No se necesitan imports adicionales por ahora
</script>

<template>
  <div class="how-work">
    <section class="hero-section">
      <div class="content-wrapper">
        <h1>¿Cómo Funciona?</h1>
        <p class="subtitle">
          Descubre cómo nuestro sistema de gestión de tareas georeferenciadas optimiza tu trabajo
        </p>
      </div>
    </section>

    <section class="features-section">
      <div class="content-wrapper">
        <div class="feature-grid">
          <!-- Registro de Usuarios -->
          <div class="feature-card">
            <div class="icon-container">
              <i class="fas fa-user-plus"></i>
            </div>
            <h3>Registro de Usuarios</h3>
            <p>
              Regístrate con tu ubicación geográfica para comenzar. Utilizamos PostGIS para almacenar y
              procesar datos espaciales precisos de tu localización.
            </p>
          </div>

          <!-- Gestión de Tareas -->
          <div id="gestion-tareas" class="feature-card">
            <div class="icon-container">
              <i class="fas fa-tasks"></i>
            </div>
            <h3>Gestión de Tareas</h3>
            <p>
              Crea, edita y administra tareas con ubicaciones específicas. Cada tarea se asocia a un sector
              geográfico para mejor organización.
            </p>
          </div>

          <!-- Geolocalización -->
          <div id="geolocalizacion" class="feature-card">
            <div class="icon-container">
              <i class="fas fa-map-marker-alt"></i>
            </div>
            <h3>Geolocalización Inteligente</h3>
            <p>
              PostGIS permite calcular distancias, radios de acción y agrupaciones espaciales para optimizar
              la asignación de tareas.
            </p>
          </div>

          <!-- Arquitectura Backend -->
          <div id="arquitectura-backend" class="feature-card">
            <div class="icon-container">
              <i class="fas fa-server"></i>
            </div>
            <h3>Arquitectura Backend</h3>
            <p>
              Sistema monolítico Spring Boot con PostgreSQL + PostGIS. Implementa JPA/Hibernate con soporte
              espacial y autenticación JWT para seguridad.
            </p>
          </div>

          <!-- Gestión Espacial -->
          <div class="feature-card">
            <div class="icon-container">
              <i class="fas fa-globe"></i>
            </div>
            <h3>Gestión Espacial</h3>
            <p>
              Utiliza PostGIS para almacenar puntos geométricos (SRID:4326) y realizar cálculos espaciales
              como distancias, radios y agrupaciones.
            </p>
          </div>

          <!-- API REST -->
          <div class="feature-card">
            <div class="icon-container">
              <i class="fas fa-code"></i>
            </div>
            <h3>API REST</h3>
            <p>
              Endpoints RESTful para gestión de usuarios, tareas y sectores. Incluye operaciones CRUD y
              consultas espaciales complejas.
            </p>
          </div>
        </div>
      </div>
    </section>

    <section class="analytics-section">
      <div class="content-wrapper">
        <h2>Análisis Espacial con PostGIS</h2>
        <div class="analytics-grid">
          <div class="analytics-card">
            <i class="fas fa-chart-bar"></i>
            <h4>Tareas por Sector</h4>
            <p>
              Cálculo de densidad de tareas por área geográfica usando funciones espaciales de PostGIS.
            </p>
          </div>
          <div class="analytics-card">
            <i class="fas fa-route"></i>
            <h4>Distancias y Proximidad</h4>
            <p>
              Cálculo de distancias entre usuarios y tareas usando ST_Distance y ST_DWithin.
            </p>
          </div>
          <div class="analytics-card">
            <i class="fas fa-layer-group"></i>
            <h4>Agrupación Espacial</h4>
            <p>
              Análisis de clusters de tareas usando ST_ClusterWithin y otras funciones de agrupación.
            </p>
          </div>
          <div class="analytics-card">
            <i class="fas fa-calculator"></i>
            <h4>Métricas Avanzadas</h4>
            <p>
              Cálculo de promedios, radios y concentraciones de tareas usando funciones geométricas.
            </p>
          </div>
        </div>
      </div>
    </section>

    <section class="technical-section">
      <div class="content-wrapper">
        <h2>Detalles Técnicos</h2>
        <div class="tech-grid">
          <!-- Modelo de Datos -->
          <div class="tech-card">
            <h4>Modelo de Datos</h4>
            <ul>
              <li><strong>Usuarios:</strong> {idUsuario, nombreUsuario, contraseña(hash), correo, localizacion(Point), rol}</li>
              <li><strong>Tareas:</strong> {idTarea, titulo, descripcion, fechaVencimiento, estado, localizacion(Point), idUsuario, idSector}</li>
              <li><strong>Sectores:</strong> {idSector, nombre, descripcion, localizacion(Point)}</li>
            </ul>
          </div>

          <!-- Consultas Espaciales -->
          <div class="tech-card">
            <h4>Consultas Espaciales Principales</h4>
            <ul>
              <li>ST_Distance: Cálculo de distancias entre puntos</li>
              <li>ST_DWithin: Búsqueda de tareas en radio específico</li>
              <li>ST_ClusterWithin: Agrupación de tareas por proximidad</li>
              <li>Conversión coordenadas: {lat, lng} ↔ Point(SRID:4326)</li>
            </ul>
          </div>

          <!-- Seguridad -->
          <div class="tech-card">
            <h4>Seguridad Implementada</h4>
            <ul>
              <li>Autenticación JWT con roles (ADMIN, TRABAJADOR, CLIENTE)</li>
              <li>Contraseñas hasheadas con BCrypt</li>
              <li>Validación de datos espaciales</li>
              <li>Control de acceso por roles en endpoints</li>
            </ul>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.how-work {
  width: 100%;
  background: var(--bg-primary);
  color: var(--text-primary);
}

.content-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
}

.hero-section {
  padding: 6rem 0 4rem;
  text-align: center;
  border-bottom: 1px solid var(--border-blue);
}

.hero-section h1 {
  font-size: 3.5rem;
  margin-bottom: 1.5rem;
  background: linear-gradient(120deg, var(--primary-blue) 0%, var(--blue-neon) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.subtitle {
  font-size: 1.3rem;
  color: var(--text-secondary);
  max-width: 800px;
  margin: 0 auto;
  line-height: 1.6;
  padding: 0 1rem;
}

.features-section {
  padding: 4rem 0;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
  margin-top: 2rem;
}

.feature-card {
  background: var(--bg-secondary);
  border: 1px solid var(--border-blue);
  border-radius: 12px;
  padding: 2rem;
  text-align: center;
  transition: all 0.3s ease;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 20px var(--blue-glow);
  border-color: var(--blue-neon);
}

.icon-container {
  width: 60px;
  height: 60px;
  background: var(--dark-gray);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 1.5rem;
}

.icon-container i {
  font-size: 1.5rem;
  color: var(--blue-neon);
}

.feature-card h3 {
  font-size: 1.5rem;
  margin-bottom: 1rem;
  color: var(--text-primary);
}

.feature-card p {
  color: var(--text-secondary);
  line-height: 1.6;
}

.process-section {
  padding: 4rem 0;
  background: var(--bg-secondary);
}

.process-section h2 {
  text-align: center;
  font-size: 2.5rem;
  margin-bottom: 3rem;
  color: var(--text-primary);
}

.process-steps {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 2rem;
}

.step {
  text-align: center;
  position: relative;
}

.step-number {
  width: 40px;
  height: 40px;
  background: var(--primary-blue);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 1rem;
  font-size: 1.2rem;
  font-weight: bold;
  color: var(--text-primary);
}

.step h4 {
  font-size: 1.2rem;
  margin-bottom: 0.5rem;
  color: var(--text-primary);
}

.step p {
  color: var(--text-secondary);
  font-size: 0.9rem;
  line-height: 1.6;
}

.analytics-section {
  padding: 4rem 0;
}

.analytics-section h2 {
  text-align: center;
  font-size: 2.5rem;
  margin-bottom: 3rem;
}

.analytics-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 2rem;
}

.analytics-card {
  background: var(--bg-secondary);
  border: 1px solid var(--border-blue);
  border-radius: 12px;
  padding: 1.5rem;
  text-align: center;
  transition: all 0.3s ease;
}

.analytics-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 15px var(--blue-glow);
}

.analytics-card i {
  font-size: 2rem;
  color: var(--primary-blue);
  margin-bottom: 1rem;
}

.analytics-card h4 {
  font-size: 1.1rem;
  margin-bottom: 0.5rem;
  color: var(--text-primary);
}

.analytics-card p {
  color: var(--text-secondary);
  font-size: 0.9rem;
  line-height: 1.6;
}

.metrics-section {
  padding: 4rem 0;
  background: var(--bg-secondary);
}

.metrics-section h2 {
  text-align: center;
  font-size: 2.5rem;
  margin-bottom: 3rem;
}

.metrics-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 2rem;
  max-width: 800px;
  margin: 0 auto;
}

.metric-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  background: var(--bg-primary);
  padding: 1.5rem;
  border-radius: 12px;
  border: 1px solid var(--border-blue);
}

.metric-item i {
  font-size: 1.5rem;
  color: var(--primary-blue);
}

.metric-item p {
  color: var(--text-secondary);
  font-size: 1rem;
  line-height: 1.4;
}

.technical-section {
  padding: 4rem 0;
  background: var(--bg-secondary);
}

.tech-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
}

.tech-card {
  background: var(--bg-primary);
  border: 1px solid var(--border-blue);
  border-radius: 12px;
  padding: 1.5rem;
}

.tech-card h4 {
  color: var(--primary-blue);
  margin-bottom: 1rem;
  font-size: 1.2rem;
}

.tech-card ul {
  list-style: none;
  padding: 0;
}

.tech-card li {
  color: var(--text-secondary);
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
  line-height: 1.4;
}

.queries-section {
  padding: 4rem 0;
}

.queries-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 2rem;
}

.query-card {
  background: var(--bg-secondary);
  border: 1px solid var(--border-blue);
  border-radius: 12px;
  padding: 1.5rem;
}

.query-card h4 {
  color: var(--primary-blue);
  margin-bottom: 1rem;
  font-size: 1.2rem;
}

.query-card pre {
  background: var(--bg-primary);
  padding: 1rem;
  border-radius: 8px;
  overflow-x: auto;
}

.query-card code {
  color: var(--text-secondary);
  font-size: 0.85rem;
  line-height: 1.4;
}

@media (max-width: 1024px) {
  .feature-grid,
  .process-steps,
  .analytics-grid,
  .tech-grid,
  .queries-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .feature-grid,
  .process-steps,
  .analytics-grid,
  .metrics-list,
  .tech-grid,
  .queries-grid {
    grid-template-columns: 1fr;
  }

  .hero-section h1 {
    font-size: 2.5rem;
  }
}
</style>
