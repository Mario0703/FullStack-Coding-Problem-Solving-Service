<script setup lang="ts">
import type { ChallengeDifficulty } from '@/utils/AdminUtils'

defineProps<{
  modelValue: ChallengeDifficulty | null
}>()

const emit = defineEmits<{
  'update:modelValue': [value: ChallengeDifficulty | null]
}>()
</script>

<template>
  <div class="d-flex align-center ga-2">
    <v-select
      class="flex-grow-1"
      :model-value="modelValue"
      label="Filter by difficulty"
      :items="['EASY', 'MEDIUM', 'HARD']"
      clearable
      hide-details
      @update:model-value="emit('update:modelValue', $event)"
    >
      <template #prepend-inner>
        <v-icon icon="mdi-filter-variant" />
      </template>
    </v-select>
    <v-btn variant="outlined" @click="emit('update:modelValue', null)"> Remove filter </v-btn>
  </div>
</template>
