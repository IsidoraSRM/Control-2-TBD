<template>
    <div class="trabajador-container">
      <h1>Panel de Trabajador</h1>
      <p>Bienvenido al panel de trabajador</p>
      <!-- Contenido del panel de trabajador -->
    <OrderList 
      viewMode="repartidor" 
      :repartidorId="currentRepartidorId" 
    />
    </div>
  </template>
  
  <script>
  import OrderList from '../components/OrderList.vue';
  export default {
    name: 'TrabajadorView',
    components: {
      OrderList
    },
    data() {
      return {
        currentRepartidorId: localStorage.getItem('userId') // Este valor debería venir de tu sistema de autenticación
      };
    }
  }
  </script>
  
  <style scoped>

/*
colores:
- Naranjo quemado (rgb(209, 118, 0))  #D17600 → Botones y CTAs
- Mostaza (rgb(245, 196, 72))   #F5C448 → Secciones destacadas
- Terracota (rgb(181, 85, 41)) #B55529  → Contrastes elegantes
- Gris cálido (rgb(226, 220, 210)) #E2DCD2  → Fondos
- Azul petróleo (rgb(18, 90, 108)) 	#125A6C  → Detalles y profundidad
*/


  .trabajador-container {
    padding: 20px;
  }
  </style>