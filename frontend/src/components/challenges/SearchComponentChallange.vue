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
    label="Search challenges"
    variant="outlined"
    hide-details
    clearable
    @update:model-value="emit('update:modelValue', $event ?? '')"
  >
    <template #prepend-inner>
      <v-icon icon="mdi-magnify" />
    </template>
  </v-text-field>
</template>
