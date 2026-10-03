<script setup lang="ts">
defineProps({
  title: {
    type: String,
    default: 'Warning',
  },
  message: {
    type: String,
    required: true,
  },
})
</script>

<template>
  <v-alert
    icon="$warning"
    color="warning"
    :title="title"
    :text="message"
  />
</template>
