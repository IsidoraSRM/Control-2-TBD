import axios from 'axios';
const API_URL = import.meta.env.VITE_API_URL;

class AuthService {
    async login(credentials) {
        try {
            // Coincide con LoginDto del backend
            const loginData = {
                correo: credentials.correo,
                contrasena: credentials.contrasena
            };

            const response = await axios.post(`${API_URL}/auth/login`, loginData);
            
            if (response.data.token) {
                // Guardar todos los datos que devuelve el backend
                const userData = {
                    token: response.data.token,
                    userId: response.data.userId,
                    role: response.data.role,
                    ubicacion: response.data.ubicacion
                };
                localStorage.setItem('user', JSON.stringify(userData));
            }
            return response.data;
        } catch (error) {
            throw this.handleError(error);
        }
    }

    async register(userData) {
        try {
            // Coincide exactamente con RegisterDto del backend
            const registerData = {
                nombreUsuario: userData.nombreusuario, // Notar la diferencia en mayúscula
                correo: userData.correo,
                contrasena: userData.contrasena,
                fechaRegistro: new Date().toISOString(),
                rol: userData.rol || 'CLIENTE',
                latitud: parseFloat(userData.lat),
                longitud: parseFloat(userData.lng)
            };

            const response = await axios.post(`${API_URL}/auth/register`, registerData);
            return response.data;
        } catch (error) {
            throw this.handleError(error);
        }
    }

    logout() {
        localStorage.removeItem('user');
    }

    getCurrentUser() {
        const userStr = localStorage.getItem('user');
        if (!userStr) return null;
        
        const userData = JSON.parse(userStr);
        // Parsear la ubicación del formato WKT que devuelve PostGIS
        // Ejemplo: "POINT (longitude latitude)"
        let lat = null, lng = null;
        if (userData.ubicacion) {
            const match = userData.ubicacion.match(/POINT \(([-\d.]+) ([-\d.]+)\)/);
            if (match) {
                lng = parseFloat(match[1]);
                lat = parseFloat(match[2]);
            }
        }

        return {
            idusuario: userData.userId,
            rol: userData.role,
            correo: userData.correo,
            lat: lat,
            lng: lng
        };
    }

    handleError(error) {
        if (error.response) {
            // El servidor respondió con un código de error
            const message = error.response.data || 'Error en el servidor';
            if (error.response.status === 401) {
                this.logout(); // Limpiar datos si hay error de autenticación
            }
            return new Error(message);
        } else if (error.request) {
            return new Error('No se pudo conectar con el servidor. Verifica tu conexión.');
        } else {
            return new Error('Error al procesar la solicitud');
        }
    }
}

export default AuthService; 