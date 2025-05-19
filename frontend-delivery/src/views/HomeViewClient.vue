<script>
import orderService from '@/services/orderService';

export default {
    name: 'HomeView',
    data() {
        return {
            userName: 'Cargando...',
            activeOrders: 0,
            recentOrders: [],
            filterStatus: 'all',
            searchQuery: '',
            loading: false,
            error: null
        }
    },
    computed: {
        filteredOrders() {
            let orders = this.recentOrders;
            
            if (this.filterStatus !== 'all') {
                orders = orders.filter(order => {
                    if (this.filterStatus === 'active') {
                        return order.estadoPedido !== 'ENTREGADO' && 
                               order.estadoPedido !== 'FALLIDO' && 
                               order.estadoPedido !== 'CANCELADO';
                    } else {
                        const statusMap = {
                            'ENTREGADO': 'Entregado',
                            'EN_CAMINO': 'En camino',
                            'PROCESANDO': 'Procesando',
                            'FALLIDO': 'Fallido',
                            'CANCELADO': 'Cancelado'
                        };
                        return statusMap[order.estadoPedido] === this.filterStatus;
                    }
                });
            }
            
            if (this.searchQuery) {
                const query = this.searchQuery.toLowerCase();
                orders = orders.filter(order => 
                    order.idPedido.toString().toLowerCase().includes(query) || 
                    order.prioridadPedido.toLowerCase().includes(query)
                );
            }
            
            return orders;
        }
    },
    methods: {
        getStatusClass(status) {
            const statusMap = {
                'ENTREGADO': 'success',
                'EN_CAMINO': 'warning',
                'PENDIENTE_CON': 'info',
                'PENDIENTE': 'info',
                'CONFIRMADO': 'info',
                'PROCESANDO': 'info',
                'FALLIDO': 'danger',
                'CANCELADO': 'danger',
                // Agrega cualquier otro estado que pueda aparecer
            };
            return { [statusMap[status]]: true };
        },
        
        formatStatus(status) {
            const statusTranslations = {
                'ENTREGADO': 'Entregado',
                'EN_CAMINO': 'En camino',
                'PENDIENTE_CON': 'Pendiente de confirmación',
                'PENDIENTE': 'Pendiente',
                'CONFIRMADO': 'Confirmado',
                'PROCESANDO': 'Procesando',
                'FALLIDO': 'Fallido',
                'CANCELADO': 'Cancelado'
            };
            return statusTranslations[status] || status;
        },
        
        
        formatDate(dateString) {
            if (!dateString) return '--';
            const date = new Date(dateString);
            return date.toLocaleDateString('es-CL');
        },
        
        startAutoRefresh() {
            this.refreshInterval = setInterval(() => {
                if (!this.loading) {
                    this.loadUserData();
                }
            }, 30000); // Actualiza cada 30 segundos
        },

        // Método para extraer nombre amigable del email
        extractNameFromEmail(email) {
            if (!email) return 'Usuario';
            const namePart = email.split('@')[0];
            // Capitalizar primera letra
            return namePart.charAt(0).toUpperCase() + namePart.slice(1);
        },
                
        async loadUserData() {
            this.loading = true;
            this.error = null;
            
            try {
                // 1. Verificación de autenticación básica
                const authToken = localStorage.getItem('authToken');
                if (!authToken) {
                    throw new Error('No hay sesión activa');
                }

                // 2. Obtener información del usuario
                const userEmail = localStorage.getItem('userEmail') || '';
                this.userName = this.extractNameFromEmail(userEmail);
                
                // 3. Obtener ID del cliente con verificación
                const clienteId = localStorage.getItem('userId');
                console.log('ID de cliente obtenido:', clienteId);
                
                
                if (!clienteId) {
                    throw new Error('No se pudo identificar al cliente');
                }

                // 4. Obtener pedidos con prevención de caché
                const timestamp = new Date().getTime();
                const response = await orderService.getPedidosByClienteId(clienteId, timestamp);
                
                console.log('Datos de pedidos recibidos:', response.data);
                
                // 5. Procesar pedidos
                this.recentOrders = response.data || [];
                
                // 6. Calcular pedidos activos (considerando todos los estados no finalizados)
                this.activeOrders = this.recentOrders.filter(order => {
                    const estado = order.estadoPedido;
                    return !['ENTREGADO', 'FALLIDO', 'CANCELADO'].includes(estado);
                }).length;
                
                // 7. Verificación de datos
                if (this.recentOrders.length === 0) {
                    console.warn('Se recibió una lista vacía de pedidos');
                }
                
            } catch (error) {
                console.error('Error completo:', error);
                console.error('Error response:', error.response);
                
                this.error = error.response?.data?.message || 
                            error.message || 
                            'Error al cargar los pedidos. Intente nuevamente.';
                this.userName = 'Cliente';
                
                // Opcional: Recargar después de un error
                setTimeout(() => this.loadUserData(), 5000);
            } finally {
                this.loading = false;
            }
        }
        


    },
    created() {
        this.loadUserData();
        this.startAutoRefresh();
        console.log("Estados encontrados en los pedidos:", 
            this.recentOrders.map(order => order.estadoPedido));
    },

    activated() {
        this.loadUserData();
    }
}
</script>
<template>
    <div class="home-view">
        <!-- Mensaje de carga/error -->
        <div v-if="loading" class="loading-message">Cargando pedidos...</div>
        <div v-if="error" class="error-message">{{ error }}</div>

        <!-- Hero Section -->
        <section class="hero-section">
            <div class="hero-card card">
                <h1>DocDelivery</h1>
                <h2>Sistema de Gestión de Pedidos - Delivery de documentos</h2>
                <p>Gestiona y realiza seguimiento a todos tus envíos de documentos de manera eficiente y segura.</p>
            </div>
        </section>

        <!-- Sección de Bienvenida -->
        <section class="welcome-section">
            <div class="welcome-card card">
                <h2>Bienvenido, {{ userName }}</h2>
                <p>Tienes {{ activeOrders }} pedidos en curso</p>
                
                <div class="quick-actions">
                    <router-link to="/client" class="btn btn-primary">Nuevo Pedido</router-link>
                </div>
            </div>
        </section>

        <!-- Sección de Pedidos Recientes -->
        <section class="recent-orders">
            <div class="card">
                <div class="section-header">
                    <h3>Pedidos Recientes</h3>
                    
                    <div class="filters">
                        <div class="search-box">
                            <input 
                                type="text" 
                                v-model="searchQuery" 
                                placeholder="Buscar por número o prioridad..."
                            >
                            <i class="fas fa-search"></i>
                        </div>
                        
                        <select v-model="filterStatus" class="status-filter">
                            <option value="all">Todos</option>
                            <option value="active">En curso</option>
                            <option value="Entregado">Entregados</option>
                            <option value="En camino">En camino</option>
                            <option value="Procesando">Procesando</option>
                            <option value="Fallido">Fallidos</option>
                        </select>
                    </div>
                </div>
                
                <div class="table-responsive">
                    <table class="orders-table">
                        <thead>
                            <tr>
                                <th># Pedido</th>
                                <th>Fecha Pedido</th>
                                <th>Fecha Entrega</th>
                                <th>Prioridad</th>
                                <th>Estado</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="order in filteredOrders" :key="order.idPedido">
                                <td>{{ order.idPedido }}</td>
                                <td>{{ formatDate(order.fechaPedido) }}</td>
                                <td>{{ formatDate(order.fechaEntrega) }}</td>
                                <td>{{ order.prioridadPedido }}</td>
                                <td>
                                    <span class="status-badge" :class="getStatusClass(order.estadoPedido)">
                                        {{ formatStatus(order.estadoPedido) }}
                                    </span>
                                </td>   
                            </tr>
                            <tr v-if="filteredOrders.length === 0 && !loading">
                                <td colspan="6" class="no-orders">No se encontraron pedidos</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    </div>
</template>

<style scoped>
.home-view {
    background-color: #F5F5F5;
    min-height: calc(100vh - 120px); /* Ajustar según tu navbar y footer */

}

/* Estilos para las tarjetas */
.card {
    background: white;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
    padding: 20px;
    margin-bottom: 20px;
}

/* Hero Section */
.hero-section {
    margin-bottom: 30px;
}

.hero-card {
    text-align: center;
    background-color: #125A6C;
    color: white;
    width: 100%;
}

.hero-card h1 {
    font-size: 2.5rem;
    margin-bottom: 10px;
}

.hero-card h2 {
    font-size: 1.5rem;
    margin-bottom: 15px;
    font-weight: 400;
}

.hero-card p {
    font-size: 1.1rem;
    max-width: 700px;
    margin: 0 auto;
}

/* Sección de Bienvenida */
.welcome-card {
    text-align: center;
    padding: 30px 20px;
}

.welcome-card h2 {
    color: #125A6C;
    margin-bottom: 10px;
}

.welcome-card p {
    color: #666;
    margin-bottom: 25px;
    font-size: 18px;
}

.quick-actions {
    display: flex;
    justify-content: center;
    gap: 15px;
    flex-wrap: wrap;
}

/* Botones */
.btn {
    padding: 10px 20px;
    border-radius: 5px;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.3s ease;
    border: none;
    display: inline-flex;
    align-items: center;
    gap: 8px;
}

.btn-primary {
    background-color: #20D547;
    color: white;
}

.btn-primary:hover {
    background-color: #1AB53D;
    transform: translateY(-2px);
}

.btn-secondary {
    background-color: #E2DCD2;
    color: #125A6C;
}

.btn-secondary:hover {
    background-color: #D5CEC3;
}

.btn-outline {
    background: transparent;
    border: 1px solid #125A6C;
    color: #125A6C;
}

.btn-outline:hover {
    background-color: rgba(18, 90, 108, 0.1);
}

.btn-action {
    background: none;
    border: none;
    color: #125A6C;
    cursor: pointer;
    padding: 5px 10px;
    margin: 0 5px;
    font-size: 14px;
    transition: color 0.3s;
}

.btn-action:hover {
    color: #D17600;
}

/* Sección de Pedidos Recientes */
.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    flex-wrap: wrap;
    gap: 15px;
}

.filters {
    display: flex;
    gap: 15px;
    align-items: center;
}

.search-box {
    position: relative;
}

.search-box input {
    padding: 8px 15px 8px 35px;
    border: 1px solid #ddd;
    border-radius: 5px;
    width: 200px;
}

.search-box i {
    position: absolute;
    left: 10px;
    top: 50%;
    transform: translateY(-50%);
    color: #777;
}

.status-filter {
    padding: 8px 15px;
    border: 1px solid #ddd;
    border-radius: 5px;
    background: white;
    color: #555;
}

.orders-table {
    width: 100%;
    border-collapse: collapse;
}

.orders-table th {
    text-align: left;
    padding: 12px 15px;
    background-color: #f8f9fa;
    color: #125A6C;
    font-weight: 600;
}

.orders-table td {
    padding: 12px 15px;
    border-bottom: 1px solid #eee;
    vertical-align: middle;
}

.orders-table tr:hover td {
    background-color: #f8f9fa;
}

.status-badge {
    padding: 5px 10px;
    border-radius: 20px;
    font-size: 13px;
    font-weight: 500;
    display: inline-block;
}

.status-badge.success {
    background-color: rgba(32, 213, 71, 0.1);
    color: #20D547;
}

.status-badge.warning {
    background-color: rgba(245, 196, 72, 0.2);
    color: #B55529;
}

.status-badge.info {
    background-color: rgba(18, 90, 108, 0.1);
    color: #125A6C;
}

.status-badge.danger {
    background-color: rgba(181, 85, 41, 0.1);
    color: #B55529;
}

.no-orders {
    text-align: center;
    padding: 30px;
    color: #777;
}

/* Responsive */
@media (max-width: 768px) {
    .section-header {
        flex-direction: column;
        align-items: flex-start;
    }
    
    .filters {
        width: 100%;
        flex-wrap: wrap;
    }
    
    .search-box input {
        width: 100%;
    }
    
    .orders-table {
        display: block;
        overflow-x: auto;
    }
    
    .quick-actions {
        flex-direction: column;
        width: 100%;
    }
    
    .btn {
        width: 100%;
        justify-content: center;
    }
    
    .hero-card h1 {
        font-size: 2rem;
    }
    
    .hero-card h2 {
        font-size: 1.2rem;
    }
}
</style>