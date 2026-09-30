<script setup lang="ts">
import { ref } from 'vue'
import {
  categoryOptions,
  difficultyOptions,
  languageOptions,
  type Challenge,
} from '@/utils/AdminUtils'

const props = defineProps<{
  current: Challenge
}>()

const emit = defineEmits<{
  update: [challenge: Challenge]
}>()

const updatedChallenge = ref<Challenge>({
  ...props.current,
})
</script>

<template>
  <v-form @submit.prevent="emit('update', updatedChallenge)">
    <v-text-field disabled :value="props.current.name" />
    <v-text-field v-model="updatedChallenge.name" placeholder="Type an updated challenge name" />
    <v-text-field disabled :value="props.current.description" />
    <v-text-field
      v-model="updatedChallenge.description"
      placeholder="Type an updated challenge description"
    />
    <v-text-field disabled :value="props.current.difficulty" />
    <v-select v-model="updatedChallenge.difficulty" :items="difficultyOptions" required />
    <v-text-field disabled :value="props.current.category" />
    <v-select v-model="updatedChallenge.category" :items="categoryOptions" required />
    <v-btn type="submit">Update Challenge</v-btn>
  </v-form>
</template>

<style scoped></style>
