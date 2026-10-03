<script setup lang="ts">
defineProps({
  title: {
    type: String,
    default: 'Information',
  },
  message: {
    type: String,
    required: true,
  },
})
</script>

<template>
  <v-alert
    icon="$info"
    color="info"
    :title="title"
    :text="message"
  />
</template>
