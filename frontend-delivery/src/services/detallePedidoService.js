import axios from 'axios';

const API_URL = 'http://localhost:8080/detallepedidos';

// Obtén el token de donde lo tengas almacenado (ej. localStorage)
const token = localStorage.getItem('authToken');

export default {
  getMasPedidosPorCategoria() {
    return axios.get(`${API_URL}/masPedidos`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });
  }
};