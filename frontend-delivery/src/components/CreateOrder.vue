<template>
  <div class="create-container">

    <div v-if="loading" class="loading-spinner">
      <div class="spinner"></div>
      <p>Procesando su solicitud...</p>
    </div>
    <div v-else-if="error" class="error-message">
      <div class="message-content">
        <div class="message-icon error-icon">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="12" r="10"></circle>
            <line x1="12" y1="8" x2="12" y2="12"></line>
            <line x1="12" y1="16" x2="12.01" y2="16"></line>
          </svg>
        </div>
        <h3>Error</h3>
        <p>{{ error }}</p>
        <button class="create-button" @click="resetForm">Intentar nuevamente</button>
      </div>
    </div>
    <div v-else-if="success" class="success-message">
      <div class="message-content">
        <div class="message-icon success-icon">
          <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
            <polyline points="22 4 12 14.01 9 11.01"></polyline>
          </svg>
        </div>
        <h3>¡Pedido creado correctamente!</h3>
        <p>Su pedido ha sido registrado con éxito.</p>
        <router-link to="/homeClient" class="create-button">Volver al inicio</router-link>
      </div>
    </div>
    <div v-else class="create-card">
      <div class="card-header">
        <h2>Registrar Pedido</h2>
        <p class="subtitle">Complete el formulario para solicitar su servicio</p>
      </div>
      
      <form @submit.prevent="createOrder">homeClient
        <!-- Progreso -->
        <div class="progress-bar">
          <div class="progress-step active">
            <div class="step-number">1</div>
            <span>Servicio</span>
          </div>
          <div class="progress-step">
            <div class="step-number">2</div>
            <span>Ubicación</span>
          </div>
          <div class="progress-step">
            <div class="step-number">3</div>
            <span>Pago</span>
          </div>
        </div>
        
        <!-- Sección 1: Información del Servicio -->
        <div class="form-section">
          <h3 class="section-title">
            <span class="section-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"></path>
                <polyline points="3.27 6.96 12 12.01 20.73 6.96"></polyline>
                <line x1="12" y1="22.08" x2="12" y2="12"></line>
              </svg>
            </span>
            Información del Servicio
          </h3>
          
          <div class="form-group">
            <label for="service-name">Nombre del Servicio</label>
            <div class="input-container">
              <input 
                type="text" 
                id="service-name" 
                v-model="orderData.nombre_servicio" 
                placeholder="Ej. Trámite notarial" 
                required
              />
            </div>
          </div>
          
          <div class="form-group">
            <label for="service-description">Descripción</label>
            <div class="input-container">
              <textarea 
                id="service-description" 
                v-model="orderData.descripcion" 
                placeholder="Describa detalladamente el servicio requerido"
                rows="3" 
                required
              ></textarea>
            </div>
          </div>
          
          <div class="form-group">
            <label for="service-category">Tipo de Servicio</label>
            <div class="select-container">
              <select 
                id="service-category" 
                v-model="orderData.categoria"
                required
              >
                <option value="" disabled selected>Seleccione una categoría</option>
                <option value="Legal">Legal</option>
                <option value="Financiero">Financiero</option>
                <option value="Médico">Médico</option>
                <option value="Educativo">Educativo</option>
                <option value="Otro">Otro</option>
              </select>
              <div class="select-arrow"></div>
            </div>
          </div>
        </div>
        
        <!-- Sección 2: Dirección -->
        <div class="form-section">
          <h3 class="section-title">
            <span class="section-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M21 10c0 7-9 13-9 13s-9-6-9-13a9 9 0 0 1 18 0z"></path>
                <circle cx="12" cy="10" r="3"></circle>
              </svg>
            </span>
            Direcciones
          </h3>
          
          <div class="form-group">
            <label for="address-origin">Dirección de Origen</label>
            <div class="input-container">
              <input 
                type="text" 
                id="address-origin" 
                v-model="orderData.direccionInicio" 
                placeholder="Dirección donde se recogerá el documento" 
                required
              />
            </div>
          </div>
          
          <div class="form-group">
            <label for="address-destination">Dirección de Destino</label>
            <div class="input-container">
              <input 
                type="text" 
                id="address-destination" 
                v-model="orderData.direccionDestino" 
                placeholder="Dirección donde se entregará el documento" 
                required
              />
            </div>
          </div>
        </div>
        
        <!-- Sección 3: Información de Pago -->
        <div class="form-section">
          <h3 class="section-title">
            <span class="section-icon">
              <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <rect x="1" y="4" width="22" height="16" rx="2" ry="2"></rect>
                <line x1="1" y1="10" x2="23" y2="10"></line>
              </svg>
            </span>
            Información de Pago
          </h3>
          
          <div class="form-group">
            <label for="payment-method">Método de Pago</label>
            <div class="select-container">
              <select 
                id="payment-method" 
                v-model="orderData.nombreMetodo"
                required
              >
                <option value="" disabled selected>Seleccione método de pago</option>
                <option value="Tarjeta de Crédito">Tarjeta de Crédito</option>
                <option value="Transferencia Bancaria">Transferencia Bancaria</option>
                <option value="Efectivo">Efectivo</option>
              </select>
              <div class="select-arrow"></div>
            </div>
          </div>
          
          <div class="form-group">
            <label for="amount">Monto a pagar</label>
            <div class="input-container">
              <span class="currency-symbol">$</span>
              <input 
                type="number" 
                id="amount" 
                v-model="orderData.monto" 
                placeholder="Ingrese el monto a pagar" 
                min="1000"
                required
              />
            </div>
          </div>
          
          <div class="form-group">
            <label>Prioridad</label>
            <div class="priority-selector">
              <div class="priority-option">
                <input type="radio" id="priority-high" name="priority" value="ALTA" v-model="orderData.prioridad" required>
                <label for="priority-high" class="priority-label high">
                  <span class="priority-dot"></span>
                  Alta
                </label>
              </div>
              <div class="priority-option">
                <input type="radio" id="priority-medium" name="priority" value="MEDIA" v-model="orderData.prioridad">
                <label for="priority-medium" class="priority-label medium">
                  <span class="priority-dot"></span>
                  Media
                </label>
              </div>
              <div class="priority-option">
                <input type="radio" id="priority-low" name="priority" value="BAJA" v-model="orderData.prioridad">
                <label for="priority-low" class="priority-label low">
                  <span class="priority-dot"></span>
                  Baja
                </label>
              </div>
            </div>
          </div>
        </div>
        
        <div class="form-actions">
          <button type="submit" class="create-button" :disabled="isSubmitting">
            <span>{{ isSubmitting ? 'Procesando...' : 'Hacer Pedido' }}</span>
            <svg v-if="!isSubmitting" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
              <line x1="5" y1="12" x2="19" y2="12"></line>
              <polyline points="12 5 19 12 12 19"></polyline>
            </svg>
            <div v-else class="button-spinner"></div>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import clienteService from '@/services/clienteService';
import orderService from '@/services/orderService';

export default {
    name: 'CreateOrder',
    data() {
        return {
            orderData: {
                clienteId: null,
                nombre_servicio: '',
                descripcion: '',
                categoria: '',
                direccionInicio: '',
                direccionDestino: '',
                nombreMetodo: '',
                monto: '',
                prioridad: ''
            },
            loading: false,
            isSubmitting: false,
            success: false,
            error: null
        }
    },
    created() {
        this.getClientId();
    },
    methods: {
        async getClientId() {
            this.loading = true;
            try {
                const userId = localStorage.getItem('userId');
                
                if (!userId) {
                    this.error = "No se pudo identificar al usuario. Por favor, inicie sesión nuevamente.";
                    return;
                }
                
                const response = await clienteService.getclienteByUserId(userId);
                this.orderData.clienteId = response.data;
                
                if (!this.orderData.clienteId) {
                    this.error = "No se encontró información de cliente para este usuario.";
                    return;
                }
                
                console.log("Cliente ID obtenido:", this.orderData.clienteId);
            } catch (error) {
                console.error("Error al obtener ID de cliente:", error);
                this.error = "Error al cargar información del cliente: " + (error.response?.data || error.message);
            } finally {
                this.loading = false;
            }
        },
        
        async createOrder() {
            if (!this.orderData.clienteId) {
                this.error = "No se ha podido obtener su ID de cliente. Por favor, inténtelo más tarde.";
                return;
            }
            
            this.isSubmitting = true;
            
            try {
                console.log("Enviando datos del pedido:", this.orderData);
                const response = await orderService.registerOrder(this.orderData);
                console.log("Pedido creado:", response.data);
                this.success = true;
            } catch (error) {
                console.error("Error al crear pedido:", error);
                this.error = "Error al crear el pedido: " + (error.response?.data || error.message);
            } finally {
                this.isSubmitting = false;
            }
        },
        
        resetForm() {
            this.error = null;
            this.success = false;
            this.orderData = {
                clienteId: this.orderData.clienteId, // Mantenemos el ID del cliente
                nombre_servicio: '',
                descripcion: '',
                categoria: '',
                direccionInicio: '',
                direccionDestino: '',
                nombreMetodo: '',
                monto: '',
                prioridad: ''
            };
        }
    }
}
</script>

<style scoped>
/* Variables de color */
:root {
  --primary-color: #125A6C;
  --primary-light: #1b7d95;
  --accent-color: #B55529;
  --accent-hover: #D17600;
  --success-color: #2e7d32;
  --error-color: #c62828;
  --bg-color: #f8f9fa;
  --card-shadow: 0 8px 24px rgba(18, 90, 108, 0.12);
  --text-color: #333;
  --text-light: #666;
  --border-color: #e0e0e0;
}

/* Estilos generales */
.create-container {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 30px 20px;
  background-color: var(--bg-color);
  min-height: 100vh;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.create-card {
  background: #ffffff;
  padding: 0;
  border-radius: 12px;
  box-shadow: var(--card-shadow);
  width: 100%;
  max-width: 600px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.create-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 12px 28px rgba(18, 90, 108, 0.18);
}

.card-header {
  background: linear-gradient(135deg, var(--primary-color), var(--primary-light));
  padding: 25px 30px;
  text-align: center;
  color: rgb(0, 0, 0);
}

h2 {
  font-weight: 600;
  font-size: 28px;
  margin: 0;
  letter-spacing: 0.5px;
}

.subtitle {
  font-size: 16px;
  font-weight: normal;
  margin: 8px 0 0;
  opacity: 0.9;
}

form {
  padding: 30px;
}

/* Barra de progreso */
.progress-bar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 25px;
  position: relative;
}

.progress-bar::before {
  content: '';
  position: absolute;
  top: 17px;
  left: 10%;
  right: 10%;
  height: 2px;
  background-color: #e0e0e0;
  z-index: 1;
}

.progress-step {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  z-index: 2;
  color: #999;
  flex: 1;
}

.step-number {
  width: 35px;
  height: 35px;
  border-radius: 50%;
  background-color: #e0e0e0;
  color: #666;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 8px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.progress-step span {
  font-size: 14px;
  font-weight: 500;
}

.progress-step.active .step-number {
  background-color: var(--accent-color);
  color: white;
}

.progress-step.active {
  color: var(--accent-color);
}

/* Secciones de formulario */
.form-section {
  background-color: #fff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}

.section-title {
  display: flex;
  align-items: center;
  font-size: 18px;
  font-weight: 600;
  color: var(--primary-color);
  margin-bottom: 20px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}

.section-icon {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 32px;
  height: 32px;
  background-color: rgba(18, 90, 108, 0.1);
  border-radius: 50%;
  margin-right: 12px;
}

.section-icon svg {
  color: var(--primary-color);
}

/* Campos de formulario */
.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: var(--text-color);
  font-size: 15px;
}

.input-container {
  position: relative;
  display: flex;
  align-items: center;
}

input, select, textarea {
  width: 100%;
  padding: 12px 15px;
  border: 1px solid var(--border-color);
  border-radius: 6px;
  font-size: 15px;
  transition: all 0.25s ease;
  background-color: white;
  color: var(--text-color);
}

input:focus, select:focus, textarea:focus {
  border-color: var(--accent-color);
  box-shadow: 0 0 0 3px rgba(209, 118, 0, 0.15);
  outline: none;
}

input::placeholder, textarea::placeholder {
  color: #aaa;
}

.select-container {
  position: relative;
}

select {
  appearance: none;
  padding-right: 35px;
  cursor: pointer;
}

.select-arrow {
  position: absolute;
  right: 15px;
  top: 50%;
  transform: translateY(-50%);
  pointer-events: none;
  width: 10px;
  height: 10px;
  border-right: 2px solid #999;
  border-bottom: 2px solid #999;
  transform: translateY(-70%) rotate(45deg);
}

textarea {
  min-height: 100px;
  resize: vertical;
}

/* Selector de prioridad */
.priority-selector {
  display: flex;
  justify-content: space-between;
  margin-top: 5px;
}

.priority-option {
  flex: 1;
  margin: 0 5px;
}

.priority-option:first-child {
  margin-left: 0;
}

.priority-option:last-child {
  margin-right: 0;
}

.priority-option input[type="radio"] {
  display: none;
}

.priority-label {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 12px;
  border: 1px solid var(--border-color);
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-weight: 500;
}

.priority-dot {
  width: 18px;
  height: 18px;
  border-radius: 50%;
  margin-bottom: 8px;
}

.priority-label.high .priority-dot {
  background-color: #ffcdd2;
}

.priority-label.medium .priority-dot {
  background-color: #fff9c4;
}

.priority-label.low .priority-dot {
  background-color: #c8e6c9;
}

.priority-option input[type="radio"]:checked + .priority-label {
  border-color: var(--accent-color);
  background-color: rgba(209, 118, 0, 0.05);
}

.priority-option input[type="radio"]:checked + .priority-label.high .priority-dot {
  background-color: #f44336;
}

.priority-option input[type="radio"]:checked + .priority-label.medium .priority-dot {
  background-color: #ffc107;
}

.priority-option input[type="radio"]:checked + .priority-label.low .priority-dot {
  background-color: #4caf50;
}

/* Símbolo de moneda */
.currency-symbol {
  position: absolute;
  left: 15px;
  top: 50%;
  transform: translateY(-50%);
  color: #666;
  font-weight: 500;
  font-size: 16px;
}

input[type="number"] {
  padding-left: 25px;
}

/* Botón de acción */
.form-actions {
  margin-top: 30px;
  display: flex;
  justify-content: center;
}

.create-button {
  width: 100%;
  padding: 14px 20px;
  background: #B55529;
  color: rgb(0, 0, 0);
  border: none;
  border-radius: 6px;
  font-size: 16px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  box-shadow: 0 4px 10px rgba(181, 85, 41, 0.3);
}

.create-button:hover {
  background: var(--accent-hover);
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(181, 85, 41, 0.4);
}

.create-button:active {
  transform: translateY(0);
}

.create-button:disabled {
  background: #ccc;
  cursor: not-allowed;
  box-shadow: none;
}

.create-button svg {
  transition: transform 0.2s ease;
}

.create-button:hover svg {
  transform: translateX(3px);
}

/* Spinner botón */
.button-spinner {
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255,255,255,0.3);
  border-radius: 50%;
  border-top-color: #fff;
  animation: spin 0.8s linear infinite;
}

/* Mensajes de éxito y error */
.error-message, .success-message {
  background-color: #fff;
  padding: 0;
  border-radius: 12px;
  box-shadow: var(--card-shadow);
  width: 100%;
  max-width: 500px;
  overflow: hidden;
}

.message-content {
  padding: 30px;
  text-align: center;
}

.error-message .message-content {
  border-top: 6px solid var(--error-color);
}

.success-message .message-content {
  border-top: 6px solid var(--success-color);
}

.message-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  margin: 0 auto 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.error-icon {
  background-color: rgba(198, 40, 40, 0.1);
  color: var(--error-color);
}

.success-icon {
  background-color: rgba(46, 125, 50, 0.1);
  color: var(--success-color);
}

.error-message h3, .success-message h3 {
  margin-top: 0;
  font-size: 22px;
  color: var(--text-color);
  margin-bottom: 10px;
}

.error-message p, .success-message p {
  color: var(--text-light);
  margin-bottom: 25px;
  font-size: 16px;
}

/* Spinner de carga */
.loading-spinner {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 50px;
  background-color: white;
  border-radius: 12px;
  box-shadow: var(--card-shadow);
  width: 100%;
  max-width: 400px;
}

.spinner {
  width: 60px;
  height: 60px;
  border: 4px solid rgba(18, 90, 108, 0.1);
  border-radius: 50%;
  border-top-color: var(--primary-color);
  animation: spin 1s linear infinite;
  margin-bottom: 20px;
}

.loading-spinner p {
  color: var(--text-light);
  font-size: 16px;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Responsividad */
@media (max-width: 768px) {
  .create-card, .error-message, .success-message, .loading-spinner {
    max-width: 100%;
  }
  
  .priority-selector {
    flex-direction: column;
    gap: 10px;
  }
  
  .priority-option {
    margin: 0;
  }
  
  .section-title {
    font-size: 16px;
  }
  
  .card-header {
    padding: 20px;
  }
  
  form {
    padding: 20px;
  }
  
  .form-section {
    padding: 15px;
  }
}
</style>