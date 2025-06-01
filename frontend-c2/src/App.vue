<script setup>
import AppHeader from './components/layout/AppHeader.vue'
import AppFooter from './components/layout/AppFooter.vue'
import './assets/main.css'
</script>

<template>
  <div class="app">
    <AppHeader />
    <main class="page-container">
      <router-view />
    </main>
    <AppFooter />
  </div>
</template>

<style>
.app {
  width: 100%;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--bg-primary);
  overflow-x: hidden;
}

.page-container {
  flex: 1;
  width: 100%;
  display: flex;
  padding-top: 60px; /* Altura del header */
}
</style>
