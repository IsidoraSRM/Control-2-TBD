<script setup>
import AppHeader from './components/layout/AppHeader.vue'
import './assets/main.css'
</script>

<template>
  <div class="app">
    <AppHeader />
    <main class="page-container">
      <router-view />
    </main>
  </div>
</template>

<style>
.app {
  width: 100%;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background: var(--bg-primary);
}

.page-container {
  flex: 1;
  width: 100%;
  display: flex;
  padding-top: 60px; /* Altura del header */
}
</style>
