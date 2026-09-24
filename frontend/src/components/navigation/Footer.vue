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
      <v-card-text class="pa-0">This is a mini LeetCode project</v-card-text>
      <v-divider class="my-4" />
      <v-card-text class="pa-0">&copy; {{ currentYear }} MiniLeetCode | All rights reserved</v-card-text>
    </v-sheet>
  </v-footer>
</template>
