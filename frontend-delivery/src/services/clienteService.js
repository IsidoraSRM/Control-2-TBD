import axios from 'axios';

const API_URL = 'http://localhost:8080/customers';

export default {
  getClienteQueMasHaGastado() {
    return axios.get(`${API_URL}/moreSpent`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('authToken')}` // Requiere autenticación ADMIN
      }
    });
  },
  getAllSummaries() {
    return axios.get(`${API_URL}/order-summaries`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('authToken')}` // Requiere autenticación ADMIN
      }
    });
  },
  getclienteByUserId(userId) {
    return axios.get(`${API_URL}/${userId}`, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('authToken')}` 
      }
    });
  }     
};