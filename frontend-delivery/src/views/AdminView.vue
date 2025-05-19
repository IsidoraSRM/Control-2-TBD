<template>
  <div class="admin-dashboard">
    <!-- Barra superior -->
    <div class="admin-header">
      <div class="header-left">
        <h1>Panel de Administración</h1>
        <p class="welcome-message">Bienvenido, Administrador</p>
      </div>
      <div class="header-right">
        <button class="refresh-btn" @click="fetchData">
          <i class="fas fa-sync-alt"></i> Actualizar
        </button>
        <span class="last-updated">Última actualización: {{ lastUpdated }}</span>
      </div>
    </div>
    <!-- Sección principal -->
    <div class="main-content">
      <!-- Consultas SQL -->
      <div class="card query-section">
        <div class="card-header">
          <h2>Consultas Analíticas</h2>
          <select v-model="selectedQuery" class="query-select">
            <option value="" disabled>Seleccione una consulta</option>
            <option value="1">Cliente que más ha gastado en pedidos entregados.</option>
            <option value="2">Productos más pedidos por categoría</option>
            <option value="3">Listar las empresas asociadas con más entregas fallidas.</option>
            <option value="4">Calcular el tiempo promedio entre pedido y entrega por repartidor.</option>
            <option value="5">Obtener los 3 repartidores con mejor rendimiento (basado en entregas y puntuación).</option>
            <option value="6">Obtener el medio de pago más usado en pedidos urgentes</option>
          </select>
          <button class="btn-run-query" @click="runQuery" :disabled="!selectedQuery">
            Ejecutar
          </button>
        </div>
        <div class="card-body">
          <div v-if="queryLoading" class="loading-indicator">
            <i class="fas fa-spinner fa-spin"></i> Procesando consulta...
          </div>
          <div v-else-if="queryResults" class="query-results">
            <div class="results-header">
              <h3>{{ queryTitle }}</h3>
            </div>
            <div class="table-responsive">
              <table class="results-table">
                <thead>
                  <tr>
                    <th v-for="(header, index) in queryHeaders" :key="index">{{ header }}</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(row, rowIndex) in queryResults" :key="rowIndex">
                    <td v-for="(cell, cellIndex) in row" :key="cellIndex">{{ cell }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          <div v-else class="empty-state">
            <i class="fas fa-chart-bar"></i>
            <p>Seleccione y ejecute una consulta para ver los resultados</p>
          </div>
        </div>
      </div>

      <!-- Vistas SQL como estadísticas clave -->
      <div class="card stats-section">
    <div class="card-header">
      <h2>Vistas SQL Clave</h2>
      <select v-model="selectedView" class="query-select" @change="loadView">
        <option value="" disabled>Seleccione una vista</option>
        <option value="13">Resumen de pedidos por cliente</option>
        <option value="14">Desempeño por repartidor</option>
        <option value="15">Empresas con mayor volumen</option>
      </select>
    </div>
    <div class="card-body">
      <div v-if="viewLoading" class="loading-indicator">
        <i class="fas fa-spinner fa-spin"></i> Cargando vista...
      </div>
      
      <!-- Caso específico para Desempeño por repartidor -->
      <RepartidorPerformanceTable v-else-if="selectedView === '14'" />
      <ClientSummary v-else-if="selectedView === '13'" />
      <TopCompany v-else-if="selectedView === '15'" />
      <!-- Otras vistas -->
      <div v-else-if="viewResults" class="query-results">
        <div class="results-header">
          <h3>{{ viewTitle }}</h3>
        </div>
        <div class="table-responsive">
          <table class="results-table">
            <thead>
              <tr>
                <th v-for="(header, index) in viewHeaders" :key="index">{{ header }}</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(row, rowIndex) in viewResults" :key="rowIndex">
                <td v-for="(cell, cellIndex) in row" :key="cellIndex">{{ cell }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div v-else class="empty-state">
        <i class="fas fa-table"></i>
        <p>Seleccione una vista SQL para ver los resultados</p>
      </div>
    </div>
  </div>
    </div>
  </div>
</template>

<script>
import pagoService from '@/services/pagoService'; 
import detallePedidoService from '@/services/detallePedidoService'; 
import repartidorService from '@/services/repartidorService';
import clienteService from '@/services/clienteService';
import RepartidorPerformanceTable from '@/components/PerformanceDist.vue';
import ClientSummary from '@/components/ClientSummary.vue';
import TopCompany from '@/components/TopCompany.vue';
import orderService from '@/services/orderService';
import empresaService from '@/services/empresaService';


export default {
  name: 'AdminView',
  components: {
    RepartidorPerformanceTable,
    ClientSummary,
    TopCompany
  },
  data() {
    return {
      lastUpdated: new Date().toLocaleTimeString(),
      stats: {
        ordersToday: 42,
        ordersChange: 5.3,
        newCustomers: 8,
        pendingDeliveries: 15,
        reportedIssues: 3,
        issuesChange: -2.1
      },
      selectedQuery: '',
      queryLoading: false,
      queryResults: null,
      queryTitle: '',
      queryHeaders: [],
      selectedView: '',
      viewLoading: false,
      viewResults: null,
      viewTitle: '',
      viewHeaders: [],
      notifications: [
        {
          id: 1,
          type: 'warning',
          icon: 'fas fa-exclamation-circle',
          text: 'Pedido #1045 retrasado - Problema con el repartidor',
          time: 'Hace 15 min',
          read: false
        },
        {
          id: 2,
          type: 'success',
          icon: 'fas fa-check-circle',
          text: 'Nuevo registro de cliente premium',
          time: 'Hace 1 hora',
          read: true
        },
        {
          id: 3,
          type: 'info',
          icon: 'fas fa-info-circle',
          text: 'Actualización del sistema programada para hoy a las 23:00',
          time: 'Hace 3 horas',
          read: true
        }
      ]
    }
  },
  methods: {
    
    fetchData() {
      this.lastUpdated = new Date().toLocaleTimeString()
    },
    async runQuery() {
      this.queryLoading = true;
      this.queryResults = null;
      this.queryError = null;

      try {
        if (this.selectedQuery === '1') { // Cliente que más ha gastado
          const response = await clienteService.getClienteQueMasHaGastado();
          console.log('Respuesta del backend:', response.data);

          this.queryTitle = 'Cliente que más ha gastado';
          this.queryHeaders = ['Nombre', 'Email', 'Total Gastado'];

          // Maneja el Optional de Java (puede ser null)
          if (response.data) {
            this.queryResults = [[
              response.data.nombre,
              response.data.email,
              `$${response.data.totalGastado.toLocaleString('es-CL')}` // Formato chileno
            ]];
          } else {
            this.queryResults = [['No se encontraron datos', '', '']];
          }
        }

        else if (this.selectedQuery === '2') {
          const response = await detallePedidoService.getMasPedidosPorCategoria();
          console.log('Respuesta del backend:', response.data);

          this.queryTitle = 'Productos más pedidos por categoría';
          this.queryHeaders = ['Servicio', 'Categoría', 'Cantidad de pedidos'];

          this.queryResults = response.data.map(item => [
            item.servicio,
            item.categoria,
            item.cantidadPedidos
          ]);
        }

        else if (this.selectedQuery === '3') { 
          const response = await empresaService.getEmpresaConMasEntregasFallidas();
          console.log('Respuesta del backend:', response.data);

          this.queryTitle = 'Empresas con más entregas fallidas';
          this.queryHeaders = ['Empresa', 'Entregas Fallidas'];

          this.queryResults = response.data.map(item => [
            item.nombreempresa || 'Empresa no identificada',
            item.entregas_fallidas || 0
          ]);
          
          this.queryResults.sort((a, b) => b[1] - a[1]);
        }




        else if (this.selectedQuery === '4') { // Tiempo promedio por repartidor
          const response = await orderService.getTiempoPromedioEntregaPorRepartidor();
          console.log('Respuesta del backend:', response.data);

          this.queryTitle = 'Tiempo promedio de entrega por repartidor';
          this.queryHeaders = ['Repartidor', 'Tiempo Promedio'];

          // Función para formatear el tiempo (maneja valores negativos)
          const formatTime = (minutes) => {
            const absMinutes = Math.abs(minutes);
            const hours = Math.floor(absMinutes / 60);
            const mins = Math.floor(absMinutes % 60);
            return `${hours}h ${mins}m` + (minutes < 0 ? ' (valor negativo)' : '');
          };

          this.queryResults = response.data.map(item => [
            item.nombre_repartidor || 'Nombre no disponible',
            formatTime(item.tiempo_promedio_minutos)
          ]);
        }

        else if (this.selectedQuery === '5') { 
          const response = await repartidorService.getTop3Repartidores();
          console.log('Respuesta del backend:', response.data);

          this.queryTitle = 'Top 3 Repartidores por Rendimiento';
          this.queryHeaders = ['Nombre', 'Entregas Completadas', 'Puntuación Promedio', 'Rendimiento'];

          this.queryResults = response.data.map(item => [
            item.nombre, 
            item.entregasCompletadas,
            item.puntuacionPromedio.toFixed(2), 
            item.rendimiento.toFixed(2) 
          ]);
        }
        else if (this.selectedQuery === '6') {
          const { data } = await pagoService.getMedioPagoMasUsadoUrgentes();
          console.log('Respuesta del backend:', data);

          this.queryTitle = 'Medio de pago más usado en pedidos urgentes';
          this.queryHeaders = ['Medio de pago', 'Cantidad de veces usado'];

          this.queryResults = Array.isArray(data) ? data : [data];
          this.queryResults = this.queryResults.map(item => [
            item.nombremetododepago,
            item.cantidad
          ]);
        }
      } catch (error) {
        console.error('Error al obtener los datos:', error);
        this.queryError = 'Hubo un problema al cargar los datos. Intenta de nuevo más tarde.';
      } finally {
        this.queryLoading = false;
      }
    },
    
    loadView() {
      if (this.selectedView === '14') {
        return;
      }
      if(this.selectedView === '13'){
        return;

      }
      if(this.selectedView === '15'){
        return;

      }
      this.viewLoading = true
      this.viewResults = null
      
      setTimeout(() => {
        switch(this.selectedView) {
          case '13':
            this.viewTitle = 'Resumen de pedidos por cliente'
            this.viewHeaders = ['Cliente', 'Total gastado', 'N° de pedidos', 'Último pedido']
            this.viewResults = [
              ['Ana Vargas', '$1,450,000', 18, '2023-05-15'],
              ['Luis Méndez', '$1,200,000', 15, '2023-05-14'],
              ['Carlota Ruiz', '$980,000', 12, '2023-05-10']
            ];
            break;
          case '14':
            this.viewTitle = 'Desempeño por repartidor'
            this.viewHeaders = ['Repartidor', 'Pedidos entregados', 'Retrasos']
            this.viewResults = [
              ['Juan Pérez', 50, 2],
              ['Carla Torres', 45, 1],
              ['Pedro López', 60, 0]
            ];
            break;
          case '15':
            this.viewTitle = 'Empresas con mayor volumen'
            this.viewHeaders = ['Empresa', 'Volumen total', 'Pedidos procesados']
            this.viewResults = [
              ['Empresa A', '$5,000,000', 100],
              ['Empresa B', '$4,200,000', 95],
              ['Empresa C', '$3,600,000', 90]
            ];
            break;
        }
        this.viewLoading = false
      }, 1000)
    },
    dismissNotification(id) {
      this.notifications = this.notifications.filter(notification => notification.id !== id);
    },  
  }
}
</script>

<style scoped>
.admin-dashboard {
  padding: 20px;
  min-height: 100vh;
  background-color: #f5f7fa;
}

.admin-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
  margin-bottom: 20px;
}

.header-left h1 {
  margin: 0;
  font-size: 24px;
  color: #2c3e50;
}

.welcome-message {
  margin: 5px 0 0;
  color: #7f8c8d;
  font-size: 14px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.refresh-btn {
  padding: 8px 15px;
  background: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  transition: background 0.3s ease;
}

.refresh-btn:hover {
  background: #2980b9;
}

.last-updated {
  font-size: 13px;
  color: #7f8c8d;
}

.main-content {
  display: grid;
  grid-template-columns: 1fr;
  gap: 20px;
}

@media (min-width: 1200px) {
  .main-content {
    grid-template-columns: 1fr 1fr;
  }
}

.card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 15px rgba(0,0,0,0.08);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.card:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 20px rgba(0,0,0,0.1);
}

.card-header {
  padding: 15px 20px;
  border-bottom: 1px solid #eee;
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 10px;
}

.card-header h2 {
  margin: 0;
  font-size: 18px;
  color: #2c3e50;
  flex-grow: 1;
}

.card-body {
  padding: 20px;
}

.query-select {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  background-color: #f8f9fa;
  font-size: 14px;
  min-width: 200px;
}

.btn-run-query {
  padding: 8px 15px;
  background: #2ecc71;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background 0.3s ease;
}

.btn-run-query:hover {
  background: #27ae60;
}

.btn-run-query:disabled {
  background: #bdc3c7;
  cursor: not-allowed;
}

.loading-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  color: #7f8c8d;
  gap: 10px;
}

.empty-state {
  text-align: center;
  padding: 40px;
  color: #bdc3c7;
}

.empty-state i {
  font-size: 40px;
  margin-bottom: 15px;
}

.empty-state p {
  margin: 0;
  font-size: 14px;
}

.results-header {
  margin-bottom: 15px;
}

.results-header h3 {
  margin: 0;
  font-size: 16px;
  color: #2c3e50;
}

.table-responsive {
  overflow-x: auto;
  border-radius: 6px;
  border: 1px solid #eee;
}

.results-table {
  width: 100%;
  border-collapse: collapse;
}

.results-table th, .results-table td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.results-table th {
  background: #f8f9fa;
  font-weight: 600;
  color: #34495e;
}

.results-table tr:hover td {
  background: #f5f7fa;
}

.results-table tr:last-child td {
  border-bottom: none;
}
</style>