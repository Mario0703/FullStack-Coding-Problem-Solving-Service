<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import type { Challenge } from '@/utils/AdminUtils'
import { formatChallengeValue } from '@/utils/AdminUtils'
import { listChallenges } from '@/utils/ChallangeUtil'
import { baseUrl } from '@/utils/api'
import ChallangeSolve from '@/components/code-execution/ChallangeSolve.vue'
import ErrorFeedback from '@/components/FeedBack/Error.vue'

type Solver = {
  userId: string
  userName: string
  code: string
}

async function loadSolvedByPeople(challengeId: string): Promise<Solver[]> {
  const response = await fetch(`${baseUrl}/api/challenge/${challengeId}/solved-by`, {
    credentials: 'include',
  })
  if (!response.ok) {
    console.error('Solved-by request failed:', response.status, response.statusText)
    return []
  }
  const data = await response.json()
  console.log(data)
  return data as Solver[]
}

const route = useRoute()
const challenge = ref<Challenge | null>(null)
const loadError = ref('')
const isLoading = ref(true)
const solvedByPeople = ref<Solver[]>([])

async function loadChallenge() {
  try {
    const challengeId = String(route.params.id)
    const challenges = await listChallenges()
    solvedByPeople.value = await loadSolvedByPeople(challengeId)
    challenge.value = challenges.find((item) => item.id === challengeId) ?? null

    if (!challenge.value) {
      loadError.value = 'The requested challenge could not be found.'
    }
  } catch (error) {
    console.error('loadChallenge error:', error)
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
  <v-container v-if="solvedByPeople.length > 0">
    <h3 class="text-h5 mb-4">Solved by</h3>
    <v-row v-for="(person, index) in solvedByPeople" :key="index">
      <v-col>
        <v-card class="mb-4">
          <v-card-title>{{ person.userName }}</v-card-title>
          <v-card-text>
            <pre class="code-block">{{ person.code }}</pre>
          </v-card-text>
          <v-card-actions>
            <v-btn
              color="primary"
              :to="{ name: 'publicProfile', params: { userId: person.userId } }"
            >
              View profile
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.code-block {
  background: #1e1e1e;
  color: #d4d4d4;
  padding: 16px;
  border-radius: 8px;
  overflow-x: auto;
  white-space: pre;
}
</style>
