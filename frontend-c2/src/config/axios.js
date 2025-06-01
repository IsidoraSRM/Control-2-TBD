import axios from 'axios';

// Configuración por defecto
axios.defaults.baseURL = 'http://localhost:8080';
axios.defaults.headers.common['Accept'] = 'application/json';
axios.defaults.headers.post['Content-Type'] = 'application/json';

// Interceptor de solicitudes
axios.interceptors.request.use(
    (config) => {
        const user = JSON.parse(localStorage.getItem('user') || '{}');
        if (user.token) {
            config.headers.Authorization = `Bearer ${user.token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Interceptor de respuestas
axios.interceptors.response.use(
    (response) => response,
    async (error) => {
        if (error.response) {
            // Manejar errores específicos
            switch (error.response.status) {
                case 401: // No autorizado
                    localStorage.removeItem('user');
                    window.location.href = '/login';
                    break;
                case 403: // Prohibido
                    console.error('No tienes permisos para realizar esta acción');
                    break;
                case 404: // No encontrado
                    console.error('Recurso no encontrado');
                    break;
                case 500: // Error del servidor
                    console.error('Error en el servidor');
                    break;
                default:
                    console.error('Error en la solicitud:', error.response.data);
            }
        }
        return Promise.reject(error);
    }
); 