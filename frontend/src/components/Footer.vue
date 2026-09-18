<script setup lang="ts">
import {computed } from 'vue'

const currentYear = computed(() => new Date().getFullYear())
</script>

<template>
  <v-footer app border class="footer text-center py-6">
    <div class="footer__content">
      <p>This is a mini LeetCode project</p>
      <v-divider class="footer__divider my-4" />
      <p>&copy; {{ currentYear }} MiniLeetCode | All rights reserved</p>
    </div>
  </v-footer>
</template>

<style scoped>
.footer {
  color: white;
}

.footer__content {
  max-width: 800px;
  margin: 0 auto;
}
</style>
