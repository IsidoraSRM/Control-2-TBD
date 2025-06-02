<template>
  <div class="client-dashboard">
    <!-- Header con información del usuario -->
    <header class="dashboard-header">
      <div class="header-content">
        <h1>Panel de Control</h1>
        <div class="user-info">
          <span class="user-email">{{ currentUser?.correo }}</span>
        </div>
      </div>
    </header>


    <!-- Panel de control principal -->
    <div class="main-content">
      <div class="content-header">
        <div class="actions-panel">
          <button @click="showTaskModal = true" class="btn-create">
            <i class="fas fa-plus"></i>
            Nueva Tarea
          </button>
          <button @click="fetchTasks" class="btn-refresh">
            <i class="fas fa-sync-alt"></i>
            Actualizar
          </button>
        </div>
        <div class="filters-panel">
          <div class="search-box">
            <i class="fas fa-search"></i>
            <input 
              v-model="filters.search" 
              placeholder="Buscar por título o descripción..."
              class="search-input"
            />
          </div>
          <select v-model="filters.estado" class="filter-select">
            <option value="">Todos los estados</option>
            <option value="PENDIENTE">Pendiente</option>
            <option value="COMPLETADA">Completada</option>
          </select>
          <select v-model="filters.sector" class="filter-select">
            <option value="">Todos los sectores</option>
            <option v-for="sector in sectores" :key="sector.idsector" :value="sector.idsector">
              {{ sector.nombre }}
            </option>
          </select>
        </div>
      </div>

      <div class="dashboard-grid">
        <!-- Panel de tareas -->
        <div class="tasks-panel">
          <div class="panel-header">
            <h2>Lista de Tareas</h2>
            <div class="task-stats">
              <span>Total: {{ tareas.length }}</span>
              <span>Sectores: {{ sectores.length }}</span>
            </div>
          </div>
          <div class="tasks-table-container">
            <table class="tasks-table">
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
                  <td class="title-cell">{{ t.titulo }}</td>
                  <td class="description-cell">{{ t.descripcion }}</td>
                  <td class="date-cell">{{ t.fechavencimiento }}</td>
                  <td class="status-cell">
                    <span :class="['status-badge', t.estado.toLowerCase()]">
                      {{ t.estado }}
                    </span>
                  </td>
                  <td class="sector-cell">{{ sectorNombre(t.idsector) }}</td>
                  <td class="actions-cell">
                    <button 
                      @click="completarTarea(t.idtarea)" 
                      v-if="t.estado !== 'COMPLETADA'" 
                      class="btn-complete"
                    >
                      <i class="fas fa-check"></i>
                    </button>
                    <button @click="eliminarTarea(t.idtarea)" class="btn-delete">
                      <i class="fas fa-trash-alt"></i>
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <!-- Panel del mapa -->
        <div class="map-panel">
          <div class="panel-header">
            <h2>Visualización Geográfica</h2>
          </div>
          <div class="map-container">
            <l-map :zoom="mapZoom" :center="mapCenter" style="height: 400px; width: 100%;">
              <l-tile-layer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
              <l-polygon
                v-for="sector in sectores"
                :key="sector.idsector"
                :lat-lngs="sector.vertices.map(v => [v.lat, v.lng])"
                :color="'blue'"
                :fill-opacity="0.1"
              />
              <l-marker
                v-for="t in tareas"
                :key="t.idtarea"
                :lat-lng="[t.lat, t.lng]"
                :icon="getMarkerIcon(t.estado === 'COMPLETADA' ? 'completed' : 'pending')"
              >
                <l-popup>
                  <b>{{ t.titulo }}</b><br />
                  {{ t.descripcion }}<br />
                  <span>Sector: {{ sectorNombre(t.idsector) }}</span>
                </l-popup>
              </l-marker>
            </l-map>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal para crear tarea con mapa Vue Leaflet -->
    <TaskModal
      v-if="showTaskModal"
      :sectors="sectores"
      @close="showTaskModal = false"
      @save="handleTaskSave"
    />
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import TaskService from '../services/taskService'
import { LMap, LTileLayer, LMarker, LPopup, LPolygon } from '@vue-leaflet/vue-leaflet'
import 'leaflet/dist/leaflet.css'
import { getMarkerIcon } from '../utils/mapIcons'
import TaskModal from '../components/TaskModal.vue'

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
const mapZoom = ref(12)
const mapCenter = ref([-33.45, -70.65])
const tareaMarker = ref(null)
const tareaSector = ref(null)

onMounted(async () => {
  const user = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : null
  if (!user) return router.push('/login')
  currentUser.value = user
  await fetchSectores()
  await fetchTasks()
  await fetchStats()
})

const fetchTasks = async () => {
  const service = new TaskService()
  const res = await service.getUserTasks(currentUser.value.userId)
  tareas.value = res.data
}
const fetchSectores = async () => {
  const service = new TaskService()
  const res = await service.getSectors()
  sectores.value = res.data
}
const fetchStats = async () => {
  const service = new TaskService()
  const userId = currentUser.value?.idusuario || currentUser.value?.userId
  if (userId) {
    stats.value = await service.getStats(userId)
  }
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

// --- Modal de tarea con mapa Vue Leaflet ---
watch(showTaskModal, (nuevo) => {
  if (nuevo) {
    // Centra el mapa en Santiago y limpia selección
    nuevaTarea.value.lat = ''
    nuevaTarea.value.lng = ''
    nuevaTarea.value.idsector = ''
    tareaMarker.value = null
    tareaSector.value = null
  }
})

function onMapClick(e) {
  const { lat, lng } = e.latlng || e
  nuevaTarea.value.lat = lat
  nuevaTarea.value.lng = lng
  // Detectar sector
  let foundSector = null
  for (const sector of sectores.value) {
    if (sector.vertices && sector.vertices.length > 2) {
      if (isPointInPolygon([lat, lng], sector.vertices)) {
        foundSector = sector.idsector
        break
      }
    }
  }
  nuevaTarea.value.idsector = foundSector
  tareaMarker.value = [lat, lng]
  tareaSector.value = foundSector
}

function isPointInPolygon(point, polygon) {
  const x = point[1], y = point[0]
  let inside = false
  for (let i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
    const xi = polygon[i].lng, yi = polygon[i].lat
    const xj = polygon[j].lng, yj = polygon[j].lat
    const intersect = ((yi > y) !== (yj > y)) &&
      (x < (xj - xi) * (y - yi) / ((yj - yi) || 1e-10) + xi)
    if (intersect) inside = !inside
  }
  return inside
}

function handleTaskSave(taskData) {
  crearTareaDesdeModal(taskData)
}

async function crearTareaDesdeModal(taskData) {
  const service = new TaskService()
  await service.createTask({
    ...taskData,
    fechavencimiento: taskData.fechaVencimiento,
    estado: 'PENDIENTE',
    idusuario: currentUser.value.userId,
    idsector: taskData.sectorId
  })
  showTaskModal.value = false
  await fetchTasks()
}
</script>

<style scoped>
.client-dashboard {
  min-height: 100vh;
  background: var(--bg-primary);
  color: var(--text-primary);
  display: flex;
  flex-direction: column;
  gap: 2rem;
  padding: 2rem;
}

.dashboard-header {
  background: var(--bg-secondary);
  padding: 1.5rem 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border: 1px solid var(--border-blue);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-content h1 {
  font-size: 1.8rem;
  font-weight: 600;
  background: linear-gradient(to right, var(--blue-gradient-start), var(--blue-gradient-end));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin: 0;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.user-email {
  color: var(--text-secondary);
  font-size: 0.9rem;
}

.btn-logout {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  border: 1px solid var(--border-blue);
  border-radius: 6px;
  background: transparent;
  color: var(--text-primary);
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-logout:hover {
  background: var(--primary-blue-hover);
  border-color: var(--primary-blue);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.stat-card {
  background: var(--card-bg);
  border-radius: 12px;
  padding: 1.5rem;
  display: flex;
  align-items: center;
  gap: 1.5rem;
  border: 1px solid var(--border-blue);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  background: var(--dark-gray);
}

.stat-info {
  flex: 1;
}

.stat-info h3 {
  margin: 0;
  font-size: 0.9rem;
  color: var(--text-secondary);
  margin-bottom: 0.5rem;
}

.stat-value {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--text-primary);
}

.main-content {
  background: var(--bg-secondary);
  border-radius: 12px;
  padding: 2rem;
  border: 1px solid var(--border-blue);
}

.content-header {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 2rem;
}

.actions-panel {
  display: flex;
  gap: 1rem;
}

.btn-create, .btn-refresh {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  border-radius: 6px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-create {
  background: var(--primary-blue);
  color: white;
  border: none;
}

.btn-create:hover {
  background: var(--primary-blue-hover);
  box-shadow: 0 0 12px var(--blue-glow);
}

.btn-refresh {
  background: transparent;
  color: var(--text-primary);
  border: 1px solid var(--border-blue);
}

.btn-refresh:hover {
  border-color: var(--primary-blue);
  box-shadow: 0 0 8px var(--blue-glow);
}

.filters-panel {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.search-box {
  flex: 1;
  min-width: 300px;
  position: relative;
}

.search-box i {
  position: absolute;
  left: 1rem;
  top: 50%;
  transform: translateY(-50%);
  color: var(--placeholder-color);
}

.search-input {
  width: 100%;
  padding: 0.75rem 1rem 0.75rem 2.5rem;
  border: 1px solid var(--border-blue);
  border-radius: 6px;
  background: var(--bg-primary);
  color: var(--text-primary);
  font-size: 0.9rem;
}

.filter-select {
  padding: 0.75rem 1rem;
  border: 1px solid var(--border-blue);
  border-radius: 6px;
  background: var(--bg-primary);
  color: var(--text-primary);
  min-width: 200px;
  font-size: 0.9rem;
}

.dashboard-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
  margin-top: 2rem;
}

.tasks-panel, .map-panel {
  background: var(--card-bg);
  border-radius: 12px;
  border: 1px solid var(--border-blue);
  overflow: hidden;
}

.panel-header {
  padding: 1.5rem;
  border-bottom: 1px solid var(--border-blue);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.panel-header h2 {
  margin: 0;
  font-size: 1.2rem;
  color: var(--text-primary);
}

.task-stats {
  display: flex;
  gap: 1rem;
  color: var(--text-secondary);
  font-size: 0.9rem;
}

.tasks-table-container {
  overflow-x: auto;
  padding: 1rem;
}

.tasks-table {
  width: 100%;
  border-collapse: collapse;
}

.tasks-table th {
  text-align: left;
  padding: 1rem;
  color: var(--text-secondary);
  font-weight: 500;
  border-bottom: 1px solid var(--border-blue);
}

.tasks-table td {
  padding: 1rem;
  border-bottom: 1px solid var(--border-blue);
}

.title-cell {
  font-weight: 500;
  color: var(--primary-blue);
}

.description-cell {
  color: var(--text-secondary);
  max-width: 300px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.status-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 999px;
  font-size: 0.8rem;
  font-weight: 500;
}

.status-badge.pendiente {
  background: rgba(234, 179, 8, 0.2);
  color: #fbbf24;
}

.status-badge.completada {
  background: rgba(34, 197, 94, 0.2);
  color: #4ade80;
}

.actions-cell {
  display: flex;
  gap: 0.5rem;
}

.btn-complete, .btn-delete {
  width: 32px;
  height: 32px;
  border-radius: 6px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-complete {
  background: rgba(34, 197, 94, 0.2);
  color: #4ade80;
  border: 1px solid #4ade80;
}

.btn-delete {
  background: rgba(239, 68, 68, 0.2);
  color: #f87171;
  border: 1px solid #f87171;
}

.map-container {
  width: 100%;
  height: 300px;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid var(--border-blue);
  margin-bottom: 1rem;
  display: block;
}

@media (max-width: 1200px) {
  .dashboard-grid {
    grid-template-columns: 1fr;
  }
  
  .client-dashboard {
    padding: 1rem;
  }
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .filters-panel {
    flex-direction: column;
  }
  
  .search-box {
    min-width: 100%;
  }
  
  .filter-select {
    width: 100%;
  }
}
</style>
