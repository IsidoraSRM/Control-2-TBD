import axios from 'axios';

const API_URL = import.meta.env.VITE_API_URL;

class TaskService {
  constructor() {
    this.axios = axios.create({
      baseURL: API_URL,
      headers: {
        'Content-Type': 'application/json'
      }
    });

    // Interceptor para agregar el token de autenticación
    this.axios.interceptors.request.use(
      (config) => {
        const token = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')).token : null;
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => {
        return Promise.reject(error);
      }
    );
  }

  // Obtener tareas del usuario
  async getUserTasks(userId) {
    // Si el endpoint no filtra por usuario, filtra en frontend
    const res = await this.axios.get('/api/tareas');
    return { data: res.data.filter(t => t.idusuario === userId) };
  }

  // Obtener todas las tareas
  async getAllTasks() {
    return this.axios.get('/api/tareas');
  }

  // Crear nueva tarea
  async createTask(data) {
    return this.axios.post('/api/tareas', data);
  }

  // Actualizar tarea existente
  async updateTask(data) {
    return this.axios.put('/api/tareas', data);
  }

  // Eliminar tarea
  async deleteTask(id) {
    return this.axios.delete(`/api/tareas/delete/${id}`);
  }

  // Marcar tarea como completada
  async completeTask(id) {
    return this.axios.put(`/api/tareas/completar/${id}`);
  }

  // Obtener sectores disponibles (sin JWT)
  async getSectors() {
    return axios.get(`${API_URL}/api/sectores`);
  }

  // Obtener estadísticas de tareas por sector
  async getTaskStatsBySector(userId) {
    try {
      return await this.axios.get(`/tareas/estadisticas/sector/${userId}`);
    } catch (error) {
      this.handleError(error);
    }
  }

  // Obtener tareas por vencer
  async getUpcomingTasks(userId) {
    try {
      return await this.axios.get(`/tareas/proximas/${userId}`);
    } catch (error) {
      this.handleError(error);
    }
  }

  // Obtener estadísticas de tareas
  async getStats(userId) {
    // Promedio distancia tareas completadas
    const prom = await this.axios.get(`/tareas/promedio-distancia/${userId}`);
    // Cantidad tareas por usuario por sector
    const cant = await this.axios.get('/tareas/cantidad-tareas-por-usuario-por-sector');
    return { promedio: prom.data, cantidad: cant.data };
  }

  // Manejo de errores
  handleError(error) {
    if (error.response) {
      // El servidor respondió con un código de error
      const message = error.response.data?.message || 'Error en el servidor';
      if (error.response.status === 401) {
        // Token expirado o inválido
        localStorage.removeItem('user');
        window.location.href = '/login';
      }
      throw new Error(message);
    } else if (error.request) {
      // La petición fue hecha pero no se recibió respuesta
      throw new Error('No se pudo conectar con el servidor');
    } else {
      // Error al configurar la petición
      throw new Error('Error al procesar la solicitud');
    }
  }
}

export default TaskService; 