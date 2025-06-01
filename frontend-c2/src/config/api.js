export const API_CONFIG = {
    BASE_URL: import.meta.env.VITE_API_URL || 'http://localhost:8080',
    AUTH_ENDPOINT: import.meta.env.VITE_AUTH_ENDPOINT || '/auth',
    TIMEOUT: 5000
};

export const getApiUrl = (endpoint) => `${API_CONFIG.BASE_URL}${endpoint}`; 