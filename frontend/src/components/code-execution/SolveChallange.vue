<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import type { Challenge } from '@/utils/AdminUtils'
import { formatChallengeValue } from '@/utils/AdminUtils'
import { listChallenges } from '@/utils/ChallangeUtil'
import ChallangeSolve from '@/components/code-execution/ChallangeSolve.vue'
import ErrorFeedback from '@/components/FeedBack/Error.vue'


async function loadSolvedByPeople(challengeId: string) {
  try {
    const response = await fetch(`/api/challenges/${challengeId}/solved-by`)
    if (!response.ok) {
      throw new Error('Failed to fetch solved by people')
    }
    const data = await response.json()
    console.log('Solved by people:', data)
  } catch (error) {
    console.error('Error loading solved by people:', error)
  }
}

const route = useRoute()
const challenge = ref<Challenge | null>(null)
const loadError = ref('')
const isLoading = ref(true)
let solvedBypeople = ref<string[]>([])

async function loadChallenge() {
  try {
    const challengeId = String(route.params.id)
    const challenges = await listChallenges()
    solvedBypeople = await loadSolvedByPeople(challengeId)
    challenge.value = challenges.find((item) => item.id === challengeId) ?? null

    if (!challenge.value) {
      loadError.value = 'The requested challenge could not be found.'
    }
  } catch {
    loadError.value = 'Unable to load the challenge. Please try again later.'
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

    <ErrorFeedback
      v-else-if="loadError"
      title="Challenge unavailable"
      :message="loadError"
      variant="tonal"
    />

    <v-card v-else-if="challenge" variant="outlined">
      <v-card-title class="text-h4">{{ challenge.name }}</v-card-title>
      <v-card-subtitle>
        {{ formatChallengeValue(challenge.difficulty) }} ·
        {{ formatChallengeValue(challenge.category) }}
      </v-card-subtitle>
      <v-card-text class="text-body-1">{{ challenge.description }}</v-card-text>
      <v-card-actions>
        <v-btn to="/challenges" variant="text">Back to challenges</v-btn>
      </v-card-actions>
    </v-card>
    <ChallangeSolve v-if="challenge" :challenge-id="challenge.id" />
  </v-container>
  <v-container>
    <v-row v-for="person in solvedBypeople" :key="person">
      <v-col>
        <v-card title="Solved by" class="mb-4">
          <v-card-text>{{ person }}</v-card-text>
          <v-container>
            <v-btn class="ma-4" color="primary">View profile</v-btn>
            <v-btn class="ma-4" color="primary">View solutions</v-btn>
          </v-container>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped></style>
