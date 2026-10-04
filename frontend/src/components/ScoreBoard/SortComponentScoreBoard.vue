<script setup lang="ts">
defineProps<{
  modelValue: string
}>()

const emit = defineEmits<{
  'update:modelValue': [value: string]
}>()

const sortOptions = [
  { title: 'Highest score', value: 'score' },
  { title: 'Most total questions solved', value: 'totalChallengesSolved' },
  { title: 'Most Easy questions solved', value: 'numberOfEasyChallengesSolved' },
  { title: 'Most Medium questions solved', value: 'numberOfMediumChallengesSolved' },
  { title: 'Most Hard questions solved', value: 'numberOfHardChallengesSolved' },
]
</script>

<template>
  <v-select
    :model-value="modelValue"
    :items="sortOptions"
    item-title="title"
    item-value="value"
    label="Sort by"
    variant="outlined"
    hide-details
    @update:model-value="emit('update:modelValue', $event)"
  >
    <template #prepend-inner>
      <v-icon icon="mdi-sort-variant" />
    </template>
  </v-select>
</template>
