<template>
  <div class="modal-overlay" @click="closeModal">
    <div class="modal-content" @click.stop>
      <div class="modal-header">
        <h2>{{ task ? 'Editar Tarea' : 'Nueva Tarea' }}</h2>
        <button class="close-button" @click="closeModal">
          <i class="fas fa-times"></i>
        </button>
      </div>

      <form @submit.prevent="handleSubmit" class="task-form">
        <div class="form-grid">
          <div class="form-group">
            <label for="titulo">Título de la Tarea</label>
            <div class="input-wrapper">
              <i class="fas fa-tasks"></i>
              <input
                id="titulo"
                v-model="formData.titulo"
                type="text"
                required
                placeholder="Ej: Inspección de Sector Norte"
              >
            </div>
          </div>

          <div class="form-group">
            <label for="descripcion">Descripción Detallada</label>
            <div class="input-wrapper">
              <i class="fas fa-align-left"></i>
              <textarea
                id="descripcion"
                v-model="formData.descripcion"
                required
                placeholder="Describe los detalles de la tarea..."
                rows="4"
              ></textarea>
            </div>
          </div>


          <div class="form-group">
            <label for="fechaVencimiento">Fecha de Vencimiento</label>
            <div class="input-wrapper">
              <i class="fas fa-calendar-alt"></i>
              <input
                id="fechaVencimiento"
                v-model="formData.fechaVencimiento"
                type="datetime-local"
                required
              >
            </div>
          </div>
        </div>

        <div class="map-section">
          <label>Ubicación de la Tarea</label>
          <div class="map-container">
            <l-map
              :zoom="mapZoom"
              :center="marker || mapCenter"
              style="height: 300px; width: 100%;"
              @click="onMapClick"
            >
              <l-tile-layer url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png" />
              <l-polygon
                v-for="sector in sectors"
                :key="sector.idsector"
                :lat-lngs="sector.vertices.map(v => [v.lat, v.lng])"
                :color="'blue'"
                :fill-opacity="0.1"
              />
              <l-marker
                v-if="marker"
                :lat-lng="marker"
                :icon="getMarkerIcon('pending')"
              />
            </l-map>
          </div>
          <div v-if="formData.lat && formData.lng" class="coordinates-info">
            <div class="coordinate">
              <i class="fas fa-map-pin"></i>
              <span>Lat: {{ formData.lat.toFixed(6) }}</span>
            </div>
            <div class="coordinate">
              <i class="fas fa-map-pin"></i>
              <span>Lng: {{ formData.lng.toFixed(6) }}</span>
            </div>
            <div v-if="formData.sectorId">
              <small>Sector detectado: {{ sectors.find(s => s.idsector === formData.sectorId)?.nombre }}</small>
            </div>
            <div v-else>
              <small style="color: red;">No hay sector para este punto</small>
            </div>
          </div>
        </div>

        <div class="form-actions">
          <button type="button" class="btn-secondary" @click="closeModal">
            <i class="fas fa-times"></i>
            Cancelar
          </button>
          <button 
            type="submit" 
            class="btn-primary"
            :disabled="!isFormValid"
          >
            <i class="fas fa-save"></i>
            {{ task ? 'Guardar Cambios' : 'Crear Tarea' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { LMap, LTileLayer, LMarker, LPolygon } from '@vue-leaflet/vue-leaflet'
import 'leaflet/dist/leaflet.css'
import { getMarkerIcon } from '../utils/mapIcons'

const props = defineProps({
  task: {
    type: Object,
    default: null
  },
  sectors: {
    type: Array,
    required: true
  }
})

const emit = defineEmits(['close', 'save'])
const formData = ref({
  titulo: '',
  descripcion: '',
  sectorId: '',
  fechaVencimiento: '',
  lat: null,
  lng: null
})
const marker = ref(null)
const mapCenter = ref([-33.45, -70.65])
const mapZoom = ref(13)

const isFormValid = computed(() => {
  return formData.value.titulo &&
         formData.value.descripcion &&
         formData.value.sectorId &&
         formData.value.fechaVencimiento &&
         formData.value.lat &&
         formData.value.lng
})

const closeModal = () => {
  emit('close')
}

const handleSubmit = () => {
  emit('save', {
    ...formData.value,
    fechaVencimiento: new Date(formData.value.fechaVencimiento).toISOString()
  })
}

function onMapClick(e) {
  const { lat, lng } = e.latlng || e
  formData.value.lat = lat
  formData.value.lng = lng
  marker.value = [lat, lng]
  // Detectar sector
  let foundSector = null
  for (const sector of props.sectors) {
    if (sector.vertices && sector.vertices.length > 2) {
      if (isPointInPolygon([lat, lng], sector.vertices)) {
        foundSector = sector.idsector
        break
      }
    }
  }
  formData.value.sectorId = foundSector
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

watch(() => props.task, (task) => {
  if (task) {
    formData.value = {
      titulo: task.titulo,
      descripcion: task.descripcion,
      sectorId: task.sectorId,
      fechaVencimiento: new Date(task.fechaVencimiento).toISOString().slice(0, 16),
      lat: task.lat,
      lng: task.lng
    }
    marker.value = [task.lat, task.lng]
    mapCenter.value = [task.lat, task.lng]
  } else {
    formData.value = {
      titulo: '', descripcion: '', sectorId: '', fechaVencimiento: '', lat: null, lng: null
    }
    marker.value = null
    mapCenter.value = [-33.45, -70.65]
  }
})

onMounted(async () => {
  // Inicializar mapa después de que el DOM esté listo
  setTimeout(() => {
    // Inicializar datos si estamos editando
    if (props.task) {
      formData.value = {
        titulo: props.task.titulo,
        descripcion: props.task.descripcion,
        sectorId: props.task.sector.id,
        fechaVencimiento: new Date(props.task.fechaVencimiento)
          .toISOString()
          .slice(0, 16),
        lat: props.task.lat,
        lng: props.task.lng
      }
      marker.value = [props.task.lat, props.task.lng]
      mapCenter.value = [props.task.lat, props.task.lng]
    } else {
      // Fecha por defecto (1 hora desde ahora)
      const defaultDate = new Date()
      defaultDate.setHours(defaultDate.getHours() + 1)
      formData.value.fechaVencimiento = defaultDate
        .toISOString()
        .slice(0, 16)
    }
  }, 100)
})
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.75);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  backdrop-filter: blur(4px);
}

.modal-content {
  background: var(--bg-secondary);
  border-radius: 16px;
  width: 90%;
  max-width: 800px;
  max-height: 90vh;
  overflow-y: auto;
  position: relative;
  border: 1px solid var(--border-blue);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.3);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem 2rem;
  border-bottom: 1px solid var(--border-blue);
}

.modal-header h2 {
  margin: 0;
  font-size: 1.5rem;
  color: var(--text-primary);
  background: linear-gradient(to right, var(--blue-gradient-start), var(--blue-gradient-end));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.close-button {
  background: transparent;
  border: none;
  color: var(--text-secondary);
  font-size: 1.25rem;
  cursor: pointer;
  padding: 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
  border-radius: 8px;
}

.close-button:hover {
  color: var(--text-primary);
  background: var(--dark-gray);
}

.task-form {
  padding: 2rem;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group:nth-child(2) {
  grid-column: span 2;
}

.form-group label {
  color: var(--text-secondary);
  font-size: 0.9rem;
  font-weight: 500;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.input-wrapper i {
  position: absolute;
  left: 1rem;
  color: var(--placeholder-color);
  font-size: 1rem;
}

.input-wrapper input,
.input-wrapper textarea,
.input-wrapper select {
  width: 100%;
  padding: 0.75rem 1rem 0.75rem 2.5rem;
  border: 1px solid var(--border-blue);
  border-radius: 8px;
  background: var(--bg-primary);
  color: var(--text-primary);
  font-size: 0.9rem;
  transition: all 0.3s ease;
}

.input-wrapper input:focus,
.input-wrapper textarea:focus,
.input-wrapper select:focus {
  outline: none;
  border-color: var(--primary-blue);
  box-shadow: 0 0 0 2px var(--blue-glow);
}

.input-wrapper input::placeholder,
.input-wrapper textarea::placeholder {
  color: var(--placeholder-color);
}

.map-section {
  margin-bottom: 2rem;
}

.map-section label {
  display: block;
  color: var(--text-secondary);
  font-size: 0.9rem;
  font-weight: 500;
  margin-bottom: 0.5rem;
}

.map-container {
  width: 100%;
  height: 300px;
  border-radius: 8px;
  overflow: hidden;
  border: 1px solid var(--border-blue);
  margin-bottom: 1rem;
}

.coordinates-info {
  display: flex;
  gap: 1.5rem;
  padding: 1rem;
  background: var(--bg-primary);
  border-radius: 8px;
  border: 1px solid var(--border-blue);
}

.coordinate {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: var(--text-secondary);
  font-size: 0.9rem;
}

.coordinate i {
  color: var(--primary-blue);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  padding-top: 1rem;
  border-top: 1px solid var(--border-blue);
}

.btn-primary,
.btn-secondary {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-primary {
  background: var(--primary-blue);
  color: white;
  border: none;
}

.btn-primary:hover:not(:disabled) {
  background: var(--primary-blue-hover);
  box-shadow: 0 0 12px var(--blue-glow);
}

.btn-primary:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-secondary {
  background: transparent;
  color: var(--text-primary);
  border: 1px solid var(--border-blue);
}

.btn-secondary:hover {
  border-color: var(--primary-blue);
  box-shadow: 0 0 8px var(--blue-glow);
}

@media (max-width: 768px) {
  .form-grid {
    grid-template-columns: 1fr;
  }

  .form-group:nth-child(2) {
    grid-column: span 1;
  }

  .modal-content {
    width: 95%;
    margin: 1rem;
  }

  .form-actions {
    flex-direction: column-reverse;
  }

  .btn-primary,
  .btn-secondary {
    width: 100%;
    justify-content: center;
  }
}
</style> 