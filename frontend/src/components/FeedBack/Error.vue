<script setup>
defineProps({
  title: {
    type: String,
    default: 'Error',
  },
  message: {
    type: String,
    required: true,
  },
})
</script>

<template>
  <v-alert
    icon="$error"
    color="error"
    :title="title"
    :text="message"
  />
</template>

<style scoped>

</style>
