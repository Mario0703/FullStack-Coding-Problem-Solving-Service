<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import type { Challenge, ChallengeDifficulty } from '@/utils/AdminUtils'
import { formatChallengeValue } from '@/utils/AdminUtils'
import { listChallenges } from '@/utils/ChallangeUtil'
import { loadSolvedChallengeIds } from '@/utils/SolvedChallengeUtil'
import SearchComponentChallange from './SearchComponentChallange.vue'
import FilterComponentChallange from './FilterComponentChallange.vue'

const challenges = ref<Challenge[]>([])
const loadError = ref('')
const isLoading = ref(true)
const selectedDifficulty = ref<ChallengeDifficulty | null>(null)
const solvedChallengeIds = ref<Set<string>>(new Set())

async function loadChallengeList() {
  loadError.value = ''
  isLoading.value = true

  try {
    challenges.value = await listChallenges()
  } catch {
    loadError.value = 'Unable to load challenges. Please try again later.'
  } finally {
    isLoading.value = false
  }
}

onMounted(() => {
  loadChallengeList()
  loadSolvedChallengeIds()
    .then((ids) => {
      solvedChallengeIds.value = ids
    })
    .catch(() => {
      solvedChallengeIds.value = new Set()
    })
})

function isChallengeSolved(challengeId: string): boolean {
  return solvedChallengeIds.value.has(challengeId)
}

const search = ref('')

const filteredChallenges = computed(() =>
  challenges.value.filter((challenge) => {
    const matchesDifficulty =
      !selectedDifficulty.value || challenge.difficulty === selectedDifficulty.value
    const matchesSearch = challenge.name.toLowerCase().includes(search.value.trim().toLowerCase())
    return matchesDifficulty && matchesSearch
  }),
)
</script>

<template>
  <v-container class="py-8">
    <SearchComponentChallange v-model="search" class="mb-4" />
    <FilterComponentChallange v-model="selectedDifficulty" class="mb-4" />
    <v-card class="mb-8" variant="text">
      <v-card-title class="text-h4 font-weight-bold px-0">Coding challenges</v-card-title>
      <v-card-subtitle class="text-body-1 mt-2 px-0">
        Choose a challenge and start practicing.
      </v-card-subtitle>
    </v-card>

    <v-alert
      v-if="loadError"
      title="Could not load challenges"
      :text="loadError"
      type="error"
      variant="tonal"
      class="mb-4"
    />

    <v-container v-if="isLoading" class="d-flex justify-center pa-8">
      <v-progress-circular indeterminate color="primary" />
    </v-container>

    <v-alert
      v-else-if="challenges.length === 0"
      text="No challenges are available yet."
      type="info"
      variant="tonal"
    />

    <v-row v-else align="stretch">
      <v-col v-for="challenge in filteredChallenges" :key="challenge.id" cols="12" sm="6" lg="4">
        <v-card
          class="h-100"
          variant="outlined"
          link
          :to="{ name: 'solveChallenge', params: { id: challenge.id } }"
        >
          <v-card-title class="d-flex align-center justify-space-between">
            {{ challenge.name }}
            <v-chip size="small" color="primary">
              {{ formatChallengeValue(challenge.difficulty) }}
            </v-chip>
          </v-card-title>
          <v-card-subtitle>
            {{ formatChallengeValue(challenge.category) }}
          </v-card-subtitle>
          <v-card-text>{{ challenge.description }}</v-card-text>
          <v-card-actions>
            <v-btn color="primary">Solve challenge</v-btn>
          </v-card-actions>
          <v-card-text>
            <template v-if="isChallengeSolved(challenge.id)">
              <v-icon icon="mdi-check-circle" color="success" />
              <span class="ml-2">Solved</span>
            </template>
            <template v-else>
              <v-icon icon="mdi-close-circle" color="error" />
              <span class="ml-2">Not solved</span>
            </template>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>
