<script setup lang="ts">
import ErrorFeedback from '@/components/FeedBack/Error.vue'
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import ChangeChallange from '@/components/AdminPages/ChangeChallange.vue'
import { formatChallengeValue } from '@/utils/Challenges/Types/options'
import type { Challenge } from '@/utils/Challenges/Types/types'
import {
  getChallengeExamples,
  getChallengeTestCases,
  listChallenges,
} from '@/utils/Challenges/API/api'

const route = useRoute()
const challenge = ref<Challenge | null>(null)
const isLoading = ref(true)
const loadError = ref('')
const editMode = ref(false)

async function loadChallenge() {
  try {
    const challengeId = String(route.params.id)
    const [challenges, examples, testCases] = await Promise.all([
      listChallenges(),
      getChallengeExamples(challengeId),
      getChallengeTestCases(challengeId),
    ])
    const challengeSummary = challenges.find((item) => item.id === challengeId)

    challenge.value = challengeSummary
      ? {
          ...challengeSummary,
          inputOutput: examples,
          testCases,
          numberOfTestCases: testCases.length,
        }
      : null

    if (!challenge.value) {
      loadError.value = 'The requested challenge could not be found.'
    }
  } catch {
    loadError.value = 'Unable to load the challenge details.'
  } finally {
    isLoading.value = false
  }
}

async function alterChallenge(updatedChallenge: Challenge) {
  console.log('Updated challenge:', updatedChallenge)
}

onMounted(loadChallenge)
</script>

<template>
  <v-container class="py-8">
    <v-container v-if="isLoading" class="d-flex justify-center pa-8">
      <v-progress-circular indeterminate color="primary" />
    </v-container>

    <ErrorFeedback
      v-else-if="loadError"
      title="Challenge unavailable"
      :message="loadError"
      variant="tonal"
    />

    <template v-else-if="challenge && !editMode">
      <v-card variant="outlined" class="mb-6">
        <v-card-title class="text-h4">{{ challenge.name }}</v-card-title>
        <v-card-subtitle>
          {{ formatChallengeValue(challenge.difficulty) }} ·
          {{ formatChallengeValue(challenge.category) }}
        </v-card-subtitle>
        <v-card-text>{{ challenge.description }}</v-card-text>
      </v-card>

      <v-card variant="outlined" class="mb-6">
        <v-card-title>Examples</v-card-title>
        <v-card-text v-if="challenge.inputOutput.length === 0">No examples available.</v-card-text>
        <v-list v-else>
          <v-list-item v-for="(example, index) in challenge.inputOutput" :key="index">
            <v-list-item-title>Example {{ index + 1 }}</v-list-item-title>
            <v-list-item-subtitle class="mt-2">
              <strong>Input:</strong> {{ example.inputExample }}
            </v-list-item-subtitle>
            <v-btn></v-btn>
            <v-list-item-subtitle>
              <strong>Output:</strong> {{ example.outputExample }}
            </v-list-item-subtitle>
          </v-list-item>
        </v-list>
      </v-card>

      <v-card variant="outlined">
        <v-card-title>Test cases</v-card-title>
        <v-card-text v-if="challenge.testCases.length === 0">No test cases available.</v-card-text>
        <v-list v-else>
          <v-list-item v-for="(testCase, index) in challenge.testCases" :key="index">
            <v-list-item-title>Test case {{ index + 1 }}</v-list-item-title>
            <v-list-item-subtitle class="mt-2">
              <strong>Input:</strong> {{ testCase.testInput }}
            </v-list-item-subtitle>
            <v-list-item-subtitle>
              <strong>Output:</strong> {{ testCase.testOutput }}
            </v-list-item-subtitle>
          </v-list-item>
        </v-list>
      </v-card>
      <v-btn @click="editMode = true">Edit Challenge</v-btn>
      <v-btn to="/admin/dashboard" variant="text" class="mt-4">Back to dashboard</v-btn>
    </template>
    <v-container v-else-if="editMode && challenge">
      <ChangeChallange :current="challenge" @update="alterChallenge" />
      <v-btn @click="editMode = false">Abort</v-btn>
    </v-container>
  </v-container>
</template>

<style scoped></style>
