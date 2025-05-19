<script>
import NavBar from './components/AppNavBar.vue'
import AppFooter from './components/AppFooter.vue'

export default {
  name: 'App',
  components: {
    NavBar,
    AppFooter
  }
}
</script>

<template>
  <v-app>
    <div id="app">
      <NavBar />
      <v-main>
        <router-view />
      </v-main>
      <AppFooter />
    </div>
  </v-app>
</template>


<style>

/* Para agregar en App.vue o en un archivo CSS global */
html, body {
  background-color: #E2DCD2; /* Gris cálido */
  margin: 0;
  padding: 0;
  height: 100%;
}

/* Opcional: para personalizar la barra de desplazamiento */
::-webkit-scrollbar {
  width: 10px;
  height: 100px;
  
}

::-webkit-scrollbar-track {
  background: rgba(18, 90, 108, 0.596);
  
}

::-webkit-scrollbar-thumb {
  background: rgb(18, 90, 108); /* Azul petróleo */
  border-radius: 5px;
}

::-webkit-scrollbar-thumb:hover {
  background: #B55529 ; /* Naranjo quemado */
}

#app {
  background-color: #E2DCD2;
  height: 100%;
  width: 100%;
  font-family: Arial, sans-serif;
  display: flex;
  flex-direction: column;
  padding-top: 20px;
}


.main-content {
  flex: 1; /* Hace que este contenedor ocupe todo el espacio disponible */
  overflow: auto;
  padding-top: 20px; /* Espacio para el navbar fijo */
  padding-bottom: 110px; /* Espacio para el footer fijo */
  height: 100%;
}

footer {
  position: fixed;
  bottom: 0;
  width: 100%;
  height: 20px; /* ajusta según la altura real de tu footer */
  background-color: #125A6C;
  color: white;
  z-index: 100;
}

/*
colores:
- Naranjo quemado (rgb(209, 118, 0))  #D17600 → Botones y CTAs
- Mostaza (rgb(245, 196, 72))   #F5C448 → Secciones destacadas
- Terracota (rgb(181, 85, 41)) #B55529  → Contrastes elegantes
- Gris cálido (rgb(226, 220, 210)) #E2DCD2  → Fondos
- Azul petróleo (rgb(18, 90, 108)) 	#125A6C  → Detalles y profundidad
*/
</style>
