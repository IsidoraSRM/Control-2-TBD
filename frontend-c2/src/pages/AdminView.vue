<template>
  <div class="admin-dashboard">
    <!-- Header con información del usuario -->
    <header class="dashboard-header">
      <div class="header-content">
        <h1>Dashboard de Consultas para el Administrador</h1>
        <div class="user-info">
          <span class="user-email">{{ currentUser?.correo }}</span>
        </div>
      </div>
    </header>


    <!-- Panel principal de consultas -->
    <div class="main-content">
      <!-- Panel de Tareas por Usuario y Sector -->
      <div class="query-panel">
        <div class="panel-header">
          <div>
            <h2>Consulta 1: Tareas por Usuario y Sector</h2>
            <p class="query-desc">¿Cuántas tareas ha hecho cada usuario por sector? Ejecuta la consulta para ver los resultados.</p>
          </div>
          <button @click="ejecutarConsultaTareasPorUsuarioSector" class="btn-query">
            <i class="fas fa-play"></i>
            Ejecutar Consulta
          </button>
        </div>
        <div class="table-container">
          <table v-if="tasksByUserSector.length > 0">
            <thead>
              <tr>
                <th>Usuario</th>
                <th>Sector</th>
                <th>Tareas Completadas</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(task, index) in tasksByUserSector" :key="index">
                <td>{{ task.nombreUsuario }}</td>
                <td>{{ task.nombreSector }}</td>
                <td>{{ task.cantidadTareas }}</td>
              </tr>
            </tbody>
          </table>
          <div v-else-if="consultaEjecutada" class="no-data">
            No hay datos disponibles
          </div>
        </div>
      </div>

      <!-- Panel de Tarea más cercana por usuario -->
      <div class="query-panel">
        <div class="panel-header">
          <div>
            <h2>Consulta 2: Tarea más cercana a un usuario</h2>
            <p class="query-desc">Busca la tarea más cercana a un usuario ingresando su ID.</p>
          </div>
          <div class="input-query-group">
            <input v-model="idUsuarioCercanoConsulta2" type="number" min="1" placeholder="ID de usuario" class="input-id" />
            <button @click="ejecutarConsultaTareaCercana" class="btn-query">
              <i class="fas fa-play"></i>
              Ejecutar Consulta
            </button>
          </div>
        </div>
        <div class="table-container">
          <table v-if="tareaCercanaConsulta2">
            <thead>
              <tr>
                <th>ID Tarea</th>
                <th>Título</th>
                <th>Sector</th>
                <th>Distancia (m)</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{{ tareaCercanaConsulta2.idTarea }}</td>
                <td>{{ tareaCercanaConsulta2.titulo }}</td>
                <td>{{ tareaCercanaConsulta2.nombreSector }}</td>
                <td>{{ tareaCercanaConsulta2.distanciaMetros ? tareaCercanaConsulta2.distanciaMetros.toFixed(2) : '' }}</td>
              </tr>
            </tbody>
          </table>
          <div v-else-if="consultaCercanaEjecutada2" class="no-data">
            No se encontró una tarea cercana para ese usuario.
          </div>
        </div>
      </div>

      <!-- Panel de Sector con más tareas completadas -->
      <div class="query-panel">
        <div class="panel-header">
          <div>
            <h2>Consulta 3: Sector con más tareas completadas </h2>
            <p class="query-desc">Busca el sector con más tareas completadas para un usuario en un radio de 2km.</p>
          </div>
          <div class="input-query-group">
            <input v-model="idUsuarioCercanoConsulta3" type="number" min="1" placeholder="ID de usuario" class="input-id" />
            <button @click="ejecutarConsultaTopSector" class="btn-query">
              <i class="fas fa-play"></i>
              Ejecutar Consulta
            </button>
          </div>
        </div>
        <div class="table-container">
          <table v-if="topSector">
            <thead>
              <tr>
                <th>ID Sector</th>
                <th>Nombre</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{{ topSector.idsector }}</td>
                <td>{{ topSector.nombre }}</td>
              </tr>
            </tbody>
          </table>
          <div v-else-if="consultaTopSectorEjecutada" class="no-data">
            No se encontró un sector con tareas completadas cerca de ese usuario.
          </div>
        </div>
      </div>

      <div class="query-panel">
        <div class="panel-header">
          <div>
            <h2>Consulta 4: Promedio global de distancia de tareas completadas</h2>
            <p class="query-desc">¿Cuál es el promedio de distancia entre las tareas completadas y el punto registrado del usuario?</p>
          </div>
          <button @click="ejecutarConsultaPromedioGlobalDistancia" class="btn-query">
            <i class="fas fa-play"></i>
            Ejecutar Consulta
          </button>
        </div>
        <div class="table-container">
          <table v-if="promedioGlobalDistancia !== null">
            <thead>
              <tr>
                <th>Promedio Global Distancia (m)</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{{ promedioGlobalDistancia.toFixed(2) }}</td>
              </tr>
            </tbody>
          </table>
          <div v-else-if="consultaPromedioGlobalDistanciaEjecutada" class="no-data">
            No hay datos disponibles.
          </div>
        </div>
      </div>

      

      <!-- Panel de Sectores con más tareas pendientes -->
      <div class="query-panel">
        <div class="panel-header">
          <div>
            <h2>Consulta 5: Sectores con más tareas pendientes</h2>
            <p class="query-desc">Muestra los sectores geográficos donde se concentran la mayor cantidad de tareas pendientes.</p>
          </div>
          <button @click="ejecutarConsultaSectoresPendientes" class="btn-query">
            <i class="fas fa-play"></i>
            Ejecutar Consulta
          </button>
        </div>
        <div class="table-container">
          <table v-if="sectoresPendientes.length > 0">
            <thead>
              <tr>
                <th>ID Sector</th>
                <th>Nombre</th>
                <th>Cantidad de Tareas Pendientes</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="sector in sectoresPendientes" :key="sector.idsector">
                <td>{{ sector.idsector }}</td>
                <td>{{ sector.nombre }}</td>
                <td>{{ sector.cantidadTareasPendientes || sector.cantidad_tareas_pendientes || sector.cantidad || 0 }}</td>
              </tr>
            </tbody>
          </table>
          <div v-else-if="consultaSectoresPendientesEjecutada" class="no-data">
            No hay sectores con tareas pendientes.
          </div>
        </div>
      </div>

      <!-- Panel de Tarea pendiente más cercana por usuario -->
      <div class="query-panel">
        <div class="panel-header">
          <div>
            <h2>Consulta 6: Tarea pendiente más cercana a un usuario</h2>
            <p class="query-desc">Busca la tarea pendiente más cercana a un usuario ingresando su ID.</p>
          </div>
          <div class="input-query-group">
            <input v-model="idUsuarioConsulta6" type="number" min="1" placeholder="ID de usuario" class="input-id" />
            <button @click="ejecutarConsultaPendienteCercana" class="btn-query">
              <i class="fas fa-play"></i>
              Ejecutar Consulta
            </button>
          </div>
        </div>
        <div class="table-container">
          <table v-if="tareaPendienteCercana">
            <thead>
              <tr>
                <th>Usuario</th>
                <th>ID Tarea</th>
                <th>Título</th>
                <th>Sector</th>
                <th>Distancia (m)</th>
                <th>Fecha de vencimiento</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{{ tareaPendienteCercana[0] }}</td>
                <td>{{ tareaPendienteCercana[1] }}</td>
                <td>{{ tareaPendienteCercana[2] }}</td>
                <td>{{ tareaPendienteCercana[3] }}</td>
                <td>{{ tareaPendienteCercana[8] ? tareaPendienteCercana[8].toFixed(2) : '' }}</td>
                <td>{{ tareaPendienteCercana[9] }}</td>
              </tr>
            </tbody>
          </table>
          <div v-else-if="consultaPendienteCercanaEjecutada" class="no-data">
            No se encontró una tarea pendiente cercana para ese usuario.
          </div>
        </div>
      </div>

      <div class="query-panel">
        <div class="panel-header">
          <div>
            <h2>Consulta 7: Cantidad de tareas por usuario y sector</h2>
            <p class="query-desc">Muestra cuántas tareas ha realizado cada usuario por sector.</p>
          </div>
          <button @click="ejecutarConsultaCantidadTareasPorUsuarioPorSector" class="btn-query">
            <i class="fas fa-play"></i>
            Ejecutar Consulta
          </button>
        </div>
        <div class="table-container">
          <table v-if="cantidadTareasPorUsuarioPorSector.length > 0">
            <thead>
              <tr>
                <th>Usuario</th>
                <th>Sector</th>
                <th>Cantidad de Tareas</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(fila, idx) in cantidadTareasPorUsuarioPorSector" :key="idx">
                <td>{{ fila[0] }}</td>
                <td>{{ fila[1] }}</td>
                <td>{{ fila[2] }}</td>
              </tr>
            </tbody>
          </table>
          <div v-else-if="consultaCantidadTareasPorUsuarioPorSectorEjecutada" class="no-data">
            No hay datos disponibles.
          </div>
        </div>
      </div>

      <div class="query-panel">
        <div class="panel-header">
          <div>
            <h2>Consulta 8: Sector con más tareas completadas (5km)</h2>
            <p class="query-desc">Busca el sector con más tareas completadas para un usuario en un radio de 5km.</p>
          </div>
          <div class="input-query-group">
            <input v-model="idUsuarioCercanoConsulta8" type="number" min="1" placeholder="ID de usuario" class="input-id" />
            <button @click="ejecutarConsultaTopSector5km" class="btn-query">
              <i class="fas fa-play"></i>
              Ejecutar Consulta
            </button>
          </div>
        </div>
        <div class="table-container">
          <table v-if="topSector5km">
            <thead>
              <tr>
                <th>ID Sector</th>
                <th>Nombre</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{{ topSector5km.idsector }}</td>
                <td>{{ topSector5km.nombre }}</td>
              </tr>
            </tbody>
          </table>
          <div v-else-if="consultaTopSector5kmEjecutada" class="no-data">
            No se encontró un sector con tareas completadas cerca de ese usuario (5km).
          </div>
        </div>
      </div>

      <!-- Panel de Promedio de distancia de tareas completadas -->
      <div class="query-panel">
        <div class="panel-header">
          <div>
            <h2>Consulta 9: Promedio de distancia de tareas completadas</h2>
            <p class="query-desc">Obtener promedio de distancia de tareas completadas para el usuario.</p>
          </div>
          <div class="input-query-group">
            <input v-model="idUsuarioConsulta4" type="number" min="1" placeholder="ID de usuario" class="input-id" />
            <button @click="ejecutarConsultaPromedioDistancia" class="btn-query">
              <i class="fas fa-play"></i>
              Ejecutar Consulta
            </button>
          </div>
        </div>
        <div class="table-container">
          <table v-if="promedioDistancia !== null && Array.isArray(promedioDistancia)">
            <thead>
              <tr>
                <th>Usuario</th>
                <th>Promedio Distancia (m)</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{{ promedioDistancia[1] }}</td>
                <td>{{ promedioDistancia[2] ? promedioDistancia[2].toFixed(2) : '' }}</td>
              </tr>
            </tbody>
          </table>
          <div v-else-if="consultaPromedioDistanciaEjecutada" class="no-data">
            No se encontró promedio de distancia para ese usuario.
          </div>
        </div>
      </div>



    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { LMap, LTileLayer, LPolygon, LPopup } from '@vue-leaflet/vue-leaflet';
import 'leaflet/dist/leaflet.css';
import TaskService from '../services/taskService';

const router = useRouter();
const currentUser = ref(null);
const stats = ref({
  totalUsers: 0,
  totalTasks: 0,
  activeSectors: 0,
  completionRate: 0,
  averageDistance: 0,
  sectorEfficiency: 0,
  averageTime: 0
});
const tasksByUserSector = ref([]);
const sectores = ref([]);
const mapZoom = ref(12);
const mapCenter = ref([-33.45, -70.65]);
const consultaEjecutada = ref(false);
const idUsuarioCercano = ref("");
const tareaCercana = ref(null);
const consultaCercanaEjecutada = ref(false);
const sectoresPendientes = ref([]);
const consultaSectoresPendientesEjecutada = ref(false);

const idUsuarioCercanoConsulta2 = ref("");
const tareaCercanaConsulta2 = ref(null);
const consultaCercanaEjecutada2 = ref(false);

const idUsuarioCercanoConsulta3 = ref("");
const topSector = ref(null);
const consultaTopSectorEjecutada = ref(false);

const idUsuarioConsulta4 = ref("");
const promedioDistancia = ref(null);
const consultaPromedioDistanciaEjecutada = ref(false);

const idUsuarioConsulta6 = ref("");
const tareaPendienteCercana = ref(null);
const consultaPendienteCercanaEjecutada = ref(false);

const cantidadTareasPorUsuarioPorSector = ref([]);
const consultaCantidadTareasPorUsuarioPorSectorEjecutada = ref(false);

const idUsuarioCercanoConsulta8 = ref("");
const topSector5km = ref(null);
const consultaTopSector5kmEjecutada = ref(false);

const promedioGlobalDistancia = ref(null);
const consultaPromedioGlobalDistanciaEjecutada = ref(false);

onMounted(async () => {
  const user = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : null;
  if (!user || user.role !== 'ADMIN') {
    router.push('/login');
    return;
  }
  currentUser.value = user;
});


async function ejecutarConsultaTareasPorUsuarioSector() {
  const service = new TaskService();
  consultaEjecutada.value = false;
  try {
    const response = await service.axios.get('/api/tareas/tareas-por-usuario-sector');
    tasksByUserSector.value = response.data;
    consultaEjecutada.value = true;
  } catch (error) {
    tasksByUserSector.value = [];
    consultaEjecutada.value = true;
    console.error('Error ejecutando consulta:', error);
  }
}

async function ejecutarConsultaTareaCercana() {
  tareaCercanaConsulta2.value = null;
  consultaCercanaEjecutada2.value = false;
  if (!idUsuarioCercanoConsulta2.value) return;
  const service = new TaskService();
  try {
    const response = await service.axios.get(`/api/tareas/cercana/${idUsuarioCercanoConsulta2.value}`);
    tareaCercanaConsulta2.value = response.data;
    consultaCercanaEjecutada2.value = true;
  } catch (error) {
    tareaCercanaConsulta2.value = null;
    consultaCercanaEjecutada2.value = true;
    console.error('Error ejecutando consulta de tarea cercana:', error);
  }
}

async function ejecutarConsultaTopSector() {
  topSector.value = null;
  consultaTopSectorEjecutada.value = false;
  if (!idUsuarioCercanoConsulta3.value) return;
  try {
    // Llamar al nuevo endpoint con el id de usuario
    const service = new TaskService();
    const sectorResponse = await service.axios.get(`/api/sectores/top-sector-2km?userId=${idUsuarioCercanoConsulta3.value}`);
    topSector.value = sectorResponse.data;
    consultaTopSectorEjecutada.value = true;
  } catch (error) {
    topSector.value = null;
    consultaTopSectorEjecutada.value = true;
    console.error('Error ejecutando consulta de sector:', error);
  }
}

function getSectorColor(sectorId) {
  // Lógica para asignar colores basados en la cantidad de tareas
  const stats = getSectorStats(sectorId);
  const ratio = stats.completedTasks / (stats.activeTasks + stats.completedTasks);
  if (ratio > 0.7) return '#4CAF50';
  if (ratio > 0.4) return '#FFC107';
  return '#F44336';
}

function getSectorStats(sectorId) {
  // Mock de estadísticas por sector - Reemplazar con datos reales
  return {
    activeTasks: Math.floor(Math.random() * 10),
    completedTasks: Math.floor(Math.random() * 20)
  };
}

async function ejecutarConsultaSectoresPendientes() {
  sectoresPendientes.value = [];
  consultaSectoresPendientesEjecutada.value = false;
  const service = new TaskService();
  try {
    const response = await service.getSectoresConMasTareasPendientes();
    sectoresPendientes.value = response.data;
    consultaSectoresPendientesEjecutada.value = true;
  } catch (error) {
    sectoresPendientes.value = [];
    consultaSectoresPendientesEjecutada.value = true;
  }
}

async function ejecutarConsultaPromedioDistancia() {
  promedioDistancia.value = null;
  consultaPromedioDistanciaEjecutada.value = false;
  if (!idUsuarioConsulta4.value) return;
  const service = new TaskService();
  try {
    const response = await service.axios.get(`/api/tareas/promedio-distancia/${idUsuarioConsulta4.value}`);
    promedioDistancia.value = response.data;
    consultaPromedioDistanciaEjecutada.value = true;
  } catch (error) {
    promedioDistancia.value = null;
    consultaPromedioDistanciaEjecutada.value = true;
    console.error('Error ejecutando consulta de promedio de distancia:', error);
  }
}

async function ejecutarConsultaPendienteCercana() {
  tareaPendienteCercana.value = null;
  consultaPendienteCercanaEjecutada.value = false;
  if (!idUsuarioConsulta6.value) return;
  const service = new TaskService();
  try {
    const response = await service.axios.get(`/api/tareas/pendiente-cercana/${idUsuarioConsulta6.value}`);
    tareaPendienteCercana.value = response.data;
    consultaPendienteCercanaEjecutada.value = true;
  } catch (error) {
    tareaPendienteCercana.value = null;
    consultaPendienteCercanaEjecutada.value = true;
    console.error('Error ejecutando consulta de tarea pendiente cercana:', error);
  }
}

async function ejecutarConsultaCantidadTareasPorUsuarioPorSector() {
  cantidadTareasPorUsuarioPorSector.value = [];
  consultaCantidadTareasPorUsuarioPorSectorEjecutada.value = false;
  const service = new TaskService();
  try {
    const response = await service.axios.get('/api/tareas/cantidad-tareas-por-usuario-por-sector');
    cantidadTareasPorUsuarioPorSector.value = response.data;
    consultaCantidadTareasPorUsuarioPorSectorEjecutada.value = true;
  } catch (error) {
    cantidadTareasPorUsuarioPorSector.value = [];
    consultaCantidadTareasPorUsuarioPorSectorEjecutada.value = true;
    console.error('Error ejecutando consulta de cantidad de tareas por usuario por sector:', error);
  }
}

async function ejecutarConsultaTopSector5km() {
  topSector5km.value = null;
  consultaTopSector5kmEjecutada.value = false;
  if (!idUsuarioCercanoConsulta8.value) return;
  try {
    const service = new TaskService();
    const sectorResponse = await service.axios.get(`/api/sectores/top-sector-5km?userId=${idUsuarioCercanoConsulta8.value}`);
    topSector5km.value = sectorResponse.data;
    consultaTopSector5kmEjecutada.value = true;
  } catch (error) {
    topSector5km.value = null;
    consultaTopSector5kmEjecutada.value = true;
    console.error('Error ejecutando consulta de sector 5km:', error);
  }
}

async function ejecutarConsultaPromedioGlobalDistancia() {
  promedioGlobalDistancia.value = null;
  consultaPromedioGlobalDistanciaEjecutada.value = false;
  const service = new TaskService();
  try {
    const response = await service.axios.get('/api/tareas/promedio-global-distancia-tareas-completadas');
    promedioGlobalDistancia.value = response.data;
    consultaPromedioGlobalDistanciaEjecutada.value = true;
  } catch (error) {
    promedioGlobalDistancia.value = null;
    consultaPromedioGlobalDistanciaEjecutada.value = true;
    console.error('Error ejecutando consulta de promedio global de distancia:', error);
  }
}
</script>

<style scoped>
.admin-dashboard {
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

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
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
  display: grid;
  gap: 2rem;
}

.query-panel, .map-panel {
  background: var(--bg-secondary);
  border-radius: 12px;
  padding: 1.5rem;
  border: 1px solid var(--border-blue);
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.panel-header h2 {
  font-size: 1.4rem;
  margin: 0;
  color: var(--text-primary);
}

.query-desc {
  color: var(--text-secondary);
  font-size: 0.95rem;
  margin-bottom: 0.5rem;
}

.btn-query {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1.2rem;
  border: 1px solid var(--primary-blue);
  border-radius: 6px;
  background: var(--primary-blue);
  color: #fff;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s, box-shadow 0.2s;
}

.btn-query:hover {
  background: #2a4bff;
  box-shadow: 0 0 8px var(--blue-glow);
}

.table-container {
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}

th, td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid var(--border-blue);
}

th {
  background: var(--dark-gray);
  color: var(--text-primary);
}

tr:hover {
  background: rgba(58, 107, 255, 0.1);
}

.metrics-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-top: 2rem;
}

.metric-card {
  background: var(--card-bg);
  border-radius: 12px;
  padding: 1.5rem;
  text-align: center;
  border: 1px solid var(--border-blue);
}

.metric-card h3 {
  font-size: 1.1rem;
  color: var(--text-primary);
  margin-bottom: 1rem;
}

.metric-value {
  font-size: 2rem;
  font-weight: 600;
  color: var(--primary-blue);
  margin-bottom: 0.5rem;
}

.metric-card p {
  color: var(--text-secondary);
  font-size: 0.9rem;
}

.map-container {
  height: 400px;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid var(--border-blue);
}

.no-data {
  text-align: center;
  padding: 2rem;
  color: var(--text-secondary);
}

.input-query-group {
  display: flex;
  align-items: center;
  gap: 0.7rem;
}

.input-id {
  padding: 0.5rem 1rem;
  border: 1px solid var(--border-blue);
  border-radius: 6px;
  font-size: 1rem;
  width: 120px;
}

@media (max-width: 768px) {
  .admin-dashboard {
    padding: 1rem;
  }
  
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .metrics-grid {
    grid-template-columns: 1fr;
  }
}
</style>
