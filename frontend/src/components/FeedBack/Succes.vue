<script setup lang="ts">
defineProps({
  title: {
    type: String,
    default: 'Success',
  },
  message: {
    type: String,
    required: true,
  },
})
</script>

<template>
  <v-alert
    icon="$success"
    color="success"
    :title="title"
    :text="message"
  />
</template>
