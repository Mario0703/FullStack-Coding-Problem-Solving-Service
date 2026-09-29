<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { formatChallengeValue, type Challenge } from '@/utils/AdminUtils'
import {
  getChallengeExamples,
  getChallengeTestCases,
  listAdminChallenges,
  type ChallengeExample,
  type ChallengeTestCase,
} from '@/utils/ChallangeUtil'

type ChallengeDetails = Challenge & {
  numberOfTestCases: number
  testCases: ChallengeTestCase[]
  inputOutput: ChallengeExample[]
}

const route = useRoute()
const challenge = ref<ChallengeDetails | null>(null)
const isLoading = ref(true)
const loadError = ref('')

async function loadChallenge() {
  try {
    const challengeId = String(route.params.id)
    const [challenges, examples, testCases] = await Promise.all([
      listAdminChallenges(),
      getChallengeExamples(challengeId),
      getChallengeTestCases(challengeId),
    ])
    const challengeSummary = challenges.find((item) => item.id === challengeId)

    challenge.value = challengeSummary
      ? { ...challengeSummary, inputOutput: examples, testCases, numberOfTestCases: testCases.length }
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

onMounted(loadChallenge)

</script>

<template>
  <v-container class="py-8">
    <v-container v-if="isLoading" class="d-flex justify-center pa-8">
      <v-progress-circular indeterminate color="primary" />
    </v-container>

    <v-alert
      v-else-if="loadError"
      title="Challenge unavailable"
      :text="loadError"
      type="error"
      variant="tonal"
    />

    <template v-else-if="challenge">
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

      <v-btn to="/admin/dashboard" variant="text" class="mt-4">Back to dashboard</v-btn>
    </template>
  </v-container>

</template>

<style scoped>

</style>
