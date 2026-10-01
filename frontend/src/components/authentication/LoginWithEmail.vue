<script setup lang="ts">
defineProps<{
  modelValue: string
}>()

const emit = defineEmits<{
  'update:modelValue': [value: string]
}>()
</script>

<template>
  <v-text-field
    :model-value="modelValue"
    label="Email"
    placeholder="Type your email"
    type="email"
    autocomplete="email"
    @update:model-value="emit('update:modelValue', $event)"
  />
</template>
