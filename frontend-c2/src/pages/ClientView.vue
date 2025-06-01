<template>
  <div class="client-dashboard">
    <!-- Header con información del usuario -->
    <header class="dashboard-header">
      <h1>Dashboard de Cliente</h1>
      <div class="user-info">
        <span class="welcome-text">Bienvenido, {{ currentUser?.correo }}</span>
        <button @click="logout" class="btn-logout">Cerrar Sesión</button>
      </div>
    </header>

    <!-- Panel principal con estadísticas -->
    <div class="stats-panel">
      <div class="stat-card">
        <h3>Tareas Pendientes</h3>
        <div class="stat-value">{{ stats.pendingTasks }}</div>
      </div>
      <div class="stat-card">
        <h3>Tareas Completadas</h3>
        <div class="stat-value">{{ stats.completedTasks }}</div>
      </div>
      <div class="stat-card">
        <h3>Sectores Activos</h3>
        <div class="stat-value">{{ stats.activeSectors }}</div>
      </div>
      <div class="stat-card">
        <h3>Distancia Promedio</h3>
        <div class="stat-value">{{ stats.averageDistance.toFixed(2) }} km</div>
      </div>
    </div>

    <!-- Panel de control principal -->
    <div class="main-panel">
      <div class="actions-panel">
        <button @click="showTaskModal = true" class="btn-primary">Nueva Tarea</button>
        <button @click="fetchTasks" class="btn-secondary">Refrescar</button>
      </div>
      <div class="filters-panel">
        <input v-model="filters.search" placeholder="Buscar por título o descripción..." />
        <select v-model="filters.estado">
          <option value="">Todos los estados</option>
          <option value="PENDIENTE">Pendiente</option>
          <option value="COMPLETADA">Completada</option>
        </select>
        <select v-model="filters.sector">
          <option value="">Todos los sectores</option>
          <option v-for="sector in sectores" :key="sector.idsector" :value="sector.idsector">{{ sector.nombre }}</option>
        </select>
      </div>
      <div class="stats-panel">
        <div><b>Tareas:</b> {{ tareas.length }}</div>
        <div><b>Sectores:</b> {{ sectores.length }}</div>
        <div><b>Promedio distancia:</b> {{ stats.promedio?.[1] ? (stats.promedio[1].toFixed(2) + ' m') : '-' }}</div>
      </div>
      <div class="tasks-list">
        <table>
          <thead>
            <tr>
              <th>Título</th>
              <th>Descripción</th>
              <th>Fecha Vencimiento</th>
              <th>Estado</th>
              <th>Sector</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="t in filteredTareas" :key="t.idtarea">
              <td>{{ t.titulo }}</td>
              <td>{{ t.descripcion }}</td>
              <td>{{ t.fechavencimiento }}</td>
              <td>{{ t.estado }}</td>
              <td>{{ sectorNombre(t.idsector) }}</td>
              <td>
                <button @click="completarTarea(t.idtarea)" v-if="t.estado !== 'COMPLETADA'" class="btn-success">Completar</button>
                <button @click="eliminarTarea(t.idtarea)" class="btn-danger">Eliminar</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="map-panel">
        <div id="map" ref="mapContainer" style="height:300px;"></div>
      </div>
    </div>

    <!-- Modal para crear tarea -->
    <div v-if="showTaskModal" class="modal-overlay">
      <div class="modal-content">
        <h2>Nueva Tarea</h2>
        <form @submit.prevent="crearTarea">
          <input v-model="nuevaTarea.titulo" placeholder="Título" required />
          <input v-model="nuevaTarea.descripcion" placeholder="Descripción" required />
          <input v-model="nuevaTarea.fechavencimiento" type="date" required />
          <select v-model="nuevaTarea.estado" required>
            <option value="PENDIENTE">Pendiente</option>
            <option value="COMPLETADA">Completada</option>
          </select>
          <input v-model.number="nuevaTarea.lat" placeholder="Latitud" required />
          <input v-model.number="nuevaTarea.lng" placeholder="Longitud" required />
          <select v-model.number="nuevaTarea.idsector" required>
            <option v-for="sector in sectores" :key="sector.idsector" :value="sector.idsector">{{ sector.nombre }}</option>
          </select>
          <button type="submit" class="btn-primary">Crear</button>
          <button type="button" @click="showTaskModal = false" class="btn-secondary">Cancelar</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import TaskService from '../services/taskService'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'

const router = useRouter()
const currentUser = ref(null)
const tareas = ref([])
const sectores = ref([])
const stats = ref({ pendingTasks: 0, completedTasks: 0, activeSectors: 0, averageDistance: 0 })
const showTaskModal = ref(false)
const nuevaTarea = ref({
  titulo: '', descripcion: '', fechavencimiento: '', estado: 'PENDIENTE', lat: '', lng: '', idsector: ''
})
const filters = ref({ search: '', estado: '', sector: '' })
const map = ref(null)
const mapContainer = ref(null)

onMounted(async () => {
  const user = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : null
  if (!user) return router.push('/login')
  currentUser.value = user
  await fetchSectores()
  await fetchTasks()
  await fetchStats()
  setTimeout(() => initMap(), 500)
})

const fetchTasks = async () => {
  const service = new TaskService()
  const res = await service.getUserTasks(currentUser.value.userId)
  tareas.value = res.data
  updateMapMarkers()
}
const fetchSectores = async () => {
  const service = new TaskService()
  const res = await service.getSectors()
  sectores.value = res.data
}
const fetchStats = async () => {
  const service = new TaskService()
  stats.value = await service.getStats(currentUser.value.userId)
}
const crearTarea = async () => {
  const service = new TaskService()
  await service.createTask({
    ...nuevaTarea.value,
    idusuario: currentUser.value.userId
  })
  showTaskModal.value = false
  await fetchTasks()
}
const eliminarTarea = async (id) => {
  const service = new TaskService()
  await service.deleteTask(id)
  await fetchTasks()
}
const completarTarea = async (id) => {
  const service = new TaskService()
  await service.completeTask(id)
  await fetchTasks()
}
const sectorNombre = (idsector) => {
  const s = sectores.value.find(s => s.idsector === idsector)
  return s ? s.nombre : '-'
}
const filteredTareas = computed(() => {
  return tareas.value.filter(t => {
    const matchSearch = t.titulo.toLowerCase().includes(filters.value.search.toLowerCase()) || t.descripcion.toLowerCase().includes(filters.value.search.toLowerCase())
    const matchEstado = !filters.value.estado || t.estado === filters.value.estado
    const matchSector = !filters.value.sector || t.idsector == filters.value.sector
    return matchSearch && matchEstado && matchSector
  })
})
const logout = () => {
  localStorage.removeItem('user')
  router.push('/login')
}
const initMap = () => {
  if (!mapContainer.value) return
  if (map.value) { map.value.remove() }
  map.value = L.map(mapContainer.value).setView([currentUser.value.lat, currentUser.value.lng], 13)
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
  }).addTo(map.value)
  // Marcador usuario
  L.marker([currentUser.value.lat, currentUser.value.lng]).bindPopup('Tu ubicación').addTo(map.value)
  updateMapMarkers()
}
const updateMapMarkers = () => {
  if (!map.value) return
  tareas.value.forEach(t => {
    L.marker([t.lat, t.lng]).bindPopup(t.titulo).addTo(map.value)
  })
}
</script>

<style scoped>
.client-dashboard {
  padding: 2rem;
  max-width: 1400px;
  margin: 0 auto;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.welcome-text {
  font-size: 1.1rem;
  color: var(--text-secondary);
}

.stats-panel {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.stat-card {
  background: var(--bg-secondary);
  border: 1px solid var(--border-blue);
  border-radius: 8px;
  padding: 1.5rem;
  text-align: center;
}

.stat-card h3 {
  color: var(--text-secondary);
  font-size: 1rem;
  margin-bottom: 0.5rem;
}

.stat-value {
  font-size: 2rem;
  font-weight: 600;
  color: var(--primary-blue);
}

.main-panel {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 2rem;
}

.actions-panel {
  grid-column: 1 / -1;
  display: flex;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.filters-panel {
  grid-column: 1 / -1;
  display: flex;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.tasks-list {
  background: var(--bg-secondary);
  border: 1px solid var(--border-blue);
  border-radius: 8px;
  padding: 1.5rem;
  max-height: 600px;
  overflow-y: auto;
}

.task-card {
  background: var(--bg-primary);
  border: 1px solid var(--border-blue);
  border-radius: 6px;
  padding: 1rem;
  margin-bottom: 1rem;
}

.task-card.task-completed {
  opacity: 0.7;
  border-color: var(--success);
}

.task-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.task-status {
  padding: 0.25rem 0.5rem;
  border-radius: 4px;
  font-size: 0.875rem;
}

.task-status.pending {
  background: var(--warning-bg);
  color: var(--warning);
}

.task-status.completed {
  background: var(--success-bg);
  color: var(--success);
}

.task-description {
  color: var(--text-secondary);
  margin-bottom: 1rem;
}

.task-details {
  display: flex;
  justify-content: space-between;
  font-size: 0.875rem;
  color: var(--text-secondary);
  margin-bottom: 1rem;
}

.task-actions {
  display: flex;
  gap: 0.5rem;
}

.map-panel {
  height: 300px;
  background: var(--bg-secondary);
  border: 1px solid var(--border-blue);
  border-radius: 8px;
  overflow: hidden;
}

#map {
  height: 100%;
  width: 100%;
}

.btn-primary,
.btn-secondary,
.btn-success,
.btn-edit,
.btn-danger,
.btn-logout {
  padding: 0.5rem 1rem;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
}

.btn-primary {
  background: var(--primary-blue);
  color: white;
}

.btn-secondary {
  background: var(--secondary);
  color: white;
}

.btn-success {
  background: var(--success);
  color: white;
}

.btn-edit {
  background: var(--warning);
  color: white;
}

.btn-danger {
  background: var(--danger);
  color: white;
}

.btn-logout {
  background: var(--danger);
  color: white;
}

.task-marker {
  width: 30px !important;
  height: 30px !important;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
}

.task-marker.pending {
  background-color: var(--warning);
}

.task-marker.completed {
  background-color: var(--success);
}

.marker-content {
  font-size: 0.75rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 100px;
}

@media (max-width: 1024px) {
  .main-panel {
    grid-template-columns: 1fr;
  }
  
  .map-panel {
    height: 400px;
  }
}

@media (max-width: 768px) {
  .client-dashboard {
    padding: 1rem;
  }
  
  .filters-panel {
    flex-direction: column;
  }
  
  .stats-panel {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 480px) {
  .stats-panel {
    grid-template-columns: 1fr;
  }
  
  .task-actions {
    flex-direction: column;
  }
}
</style>
