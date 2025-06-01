<script setup>
import { RouterLink } from 'vue-router'
</script>

<template>
  <footer class="footer">
    <div class="footer-content">
      <!-- Sección de descripción -->
      <div class="footer-section">
        <h3>Control TBD</h3>
        <p>Sistema de gestión de tareas georeferenciadas para un control eficiente y moderno.</p>
        <p class="github-text">
          <a href="https://github.com/IsidoraSRM/Control-2-TBD.git" target="_blank">
            Ver repositorio en GitHub
          </a>
        </p>
        <div class="social-links">
          <a href="#" class="social-link">
            <i class="fab fa-linkedin"></i>
          </a>
          <a href="https://github.com/IsidoraSRM/Control-2-TBD.git" 
             target="_blank" 
             class="social-link">
            <i class="fab fa-github"></i>
          </a>
          <a href="#" class="social-link">
            <i class="fab fa-twitter"></i>
          </a>
        </div>
      </div>

      <!-- Enlaces rápidos -->
      <div class="footer-section">
        <h4>Enlaces Rápidos</h4>
        <ul>
          <li><RouterLink to="/">Inicio</RouterLink></li>
          <li><RouterLink to="/login">Iniciar Sesión</RouterLink></li>
        </ul>
      </div>

      <!-- Características con anclas a HowWork.vue -->
      <div class="footer-section">
        <h4>Características</h4>
        <ul>
          <li><RouterLink to="/how-works#gestion-tareas">Gestión de Tareas</RouterLink></li>
          <li><RouterLink to="/how-works#geolocalizacion">Geolocalización Inteligente</RouterLink></li>
          <li><RouterLink to="/how-works#arquitectura-backend">Arquitectura Backend</RouterLink></li>
        </ul>
      </div>

      <!-- Información de contacto -->
      <div class="footer-section">
        <h4>Contacto</h4>
        <ul class="contact-info">
          <li>
            <i class="fas fa-envelope"></i>
            <a href="mailto:info@controltbd.com">info@controltbd.com</a>
          </li>
          <li>
            <i class="fas fa-phone"></i>
            <a href="tel:+56912345678">+569 1234 5678</a>
          </li>
          <li>
            <i class="fas fa-map-marker-alt"></i>
            <span>Santiago, Chile</span>
          </li>
        </ul>
      </div>
    </div>

    <div class="footer-bottom">
      <p>&copy; {{ new Date().getFullYear() }} Control TBD. Todos los derechos reservados.</p>
    </div>
  </footer>
</template>


<style scoped>
.footer {
  width: 100%;
  background: var(--bg-secondary);
  color: var(--text-secondary);
  padding: 4rem 0 0;
  margin-top: 4rem;
  border-top: 1px solid var(--border-blue);
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  z-index: 10;
}

.footer-content {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 3rem;
}

.footer-section h3 {
  color: var(--text-primary);
  font-size: 1.5rem;
  margin-bottom: 1rem;
  font-weight: 600;
}

.footer-section h4 {
  color: var(--text-primary);
  font-size: 1.2rem;
  margin-bottom: 1rem;
  font-weight: 500;
}

.footer-section p {
  line-height: 1.6;
  margin-bottom: 1.5rem;
}

.footer-section ul {
  list-style: none;
  padding: 0;
}

.footer-section ul li {
  margin-bottom: 0.75rem;
}

.footer-section ul li a {
  color: var(--text-secondary);
  text-decoration: none;
  transition: color 0.3s ease;
}

.footer-section ul li a:hover {
  color: var(--primary-blue);
  text-decoration: none;
  background: transparent;
}

.social-links {
  display: flex;
  gap: 1rem;
}

.social-link {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: var(--dark-gray);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--text-primary);
  transition: all 0.3s ease;
  font-size: 1.2rem;
}

.social-link:hover {
  background: var(--primary-blue);
  transform: translateY(-3px);
  box-shadow: 0 5px 15px var(--blue-glow);
}

.contact-info li {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.contact-info i {
  color: var(--primary-blue);
  font-size: 1.1rem;
}

.footer-bottom {
  width: 100%;
  margin-top: 4rem;
  padding: 1.5rem;
  text-align: center;
  background: var(--bg-primary);
  font-size: 0.9rem;
}

.github-text {
  margin: 1rem 0;
}

.github-text a {
  color: var(--primary-blue);
  font-weight: 500;
  transition: all 0.3s ease;
}

.github-text a:hover {
  color: var(--blue-neon);
  text-decoration: underline;
}

@media (max-width: 1024px) {
  .footer-content {
    grid-template-columns: repeat(2, 1fr);
    max-width: 900px;
  }
}

@media (max-width: 640px) {
  .footer-content {
    grid-template-columns: 1fr;
  }
  
  .footer-section {
    text-align: center;
  }
  
  .social-links {
    justify-content: center;
  }
  
  .contact-info li {
    justify-content: center;
  }
}
</style>
