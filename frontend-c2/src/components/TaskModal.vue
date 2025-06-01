<template>
  <div class="modal-overlay" @click="closeModal">
    <div class="modal-content" @click.stop>
      <div class="modal-header">
        <h2>{{ task ? 'Editar Tarea' : 'Nueva Tarea' }}</h2>
        <button class="close-button" @click="closeModal">&times;</button>
      </div>

      <form @submit.prevent="handleSubmit" class="task-form">
        <div class="form-group">
          <label for="titulo">Título</label>
          <input
            id="titulo"
            v-model="formData.titulo"
            type="text"
            required
            placeholder="Ingrese el título de la tarea"
          >
        </div>

        <div class="form-group">
          <label for="descripcion">Descripción</label>
          <textarea
            id="descripcion"
            v-model="formData.descripcion"
            required
            placeholder="Ingrese la descripción de la tarea"
            rows="4"
          ></textarea>
        </div>

        <div class="form-group">
          <label for="sector">Sector</label>
          <select
            id="sector"
            v-model="formData.sectorId"
            required
          >
            <option value="">Seleccione un sector</option>
            <option v-for="sector in sectors" :key="sector.id" :value="sector.id">
              {{ sector.nombre }}
            </option>
          </select>
        </div>

        <div class="form-group">
          <label for="fechaVencimiento">Fecha de Vencimiento</label>
          <input
            id="fechaVencimiento"
            v-model="formData.fechaVencimiento"
            type="datetime-local"
            required
          >
        </div>

        <div class="form-actions">
          <button type="button" class="btn-secondary" @click="closeModal">
            Cancelar
          </button>
          <button type="submit" class="btn-primary">
            {{ task ? 'Guardar Cambios' : 'Crear Tarea' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

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
  fechaVencimiento: ''
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

onMounted(() => {
  if (props.task) {
    formData.value = {
      titulo: props.task.titulo,
      descripcion: props.task.descripcion,
      sectorId: props.task.sector.id,
      fechaVencimiento: new Date(props.task.fechaVencimiento)
        .toISOString()
        .slice(0, 16) // Formato requerido para datetime-local
    }
  } else {
    // Establecer fecha mínima por defecto (1 hora desde ahora)
    const defaultDate = new Date()
    defaultDate.setHours(defaultDate.getHours() + 1)
    formData.value.fechaVencimiento = defaultDate
      .toISOString()
      .slice(0, 16)
  }
})
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: var(--bg-primary);
  border-radius: 8px;
  padding: 2rem;
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
  position: relative;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.modal-header h2 {
  margin: 0;
  color: var(--text-primary);
}

.close-button {
  background: transparent;
  border: none;
  font-size: 1.5rem;
  color: var(--text-secondary);
  cursor: pointer;
  padding: 0.5rem;
  transition: color 0.3s ease;
}

.close-button:hover {
  color: var(--danger);
}

.task-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group label {
  color: var(--text-secondary);
  font-size: 0.9rem;
}

.form-group input,
.form-group textarea,
.form-group select {
  padding: 0.75rem;
  border: 1px solid var(--border-blue);
  border-radius: 4px;
  background: var(--bg-secondary);
  color: var(--text-primary);
  font-size: 1rem;
  transition: border-color 0.3s ease;
}

.form-group input:focus,
.form-group textarea:focus,
.form-group select:focus {
  outline: none;
  border-color: var(--primary-blue);
  box-shadow: 0 0 0 2px var(--blue-glow);
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1rem;
}

.btn-primary,
.btn-secondary {
  padding: 0.75rem 1.5rem;
  border-radius: 4px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-primary {
  background: var(--primary-blue);
  color: white;
  border: none;
}

.btn-primary:hover {
  background: var(--primary-blue-hover);
  box-shadow: 0 0 12px var(--blue-glow);
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

@media (max-width: 480px) {
  .modal-content {
    padding: 1rem;
  }

  .form-actions {
    flex-direction: column;
  }

  .btn-primary,
  .btn-secondary {
    width: 100%;
  }
}
</style> 