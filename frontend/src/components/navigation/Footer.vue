<script setup lang="ts">
import {computed } from 'vue'

defineOptions({
  name: 'AppFooter',
})

const currentYear = computed(() => new Date().getFullYear())
</script>

<template>
  <v-footer border class="text-white text-center py-6">
    <v-sheet class="w-100 mx-auto bg-transparent" max-width="800">
      <p>This is a mini LeetCode project</p>
      <v-divider class="my-4" />
      <p>&copy; {{ currentYear }} MiniLeetCode | All rights reserved</p>
    </v-sheet>
  </v-footer>
</template>
