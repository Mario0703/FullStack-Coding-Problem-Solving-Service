<script setup lang="ts">
import ErrorFeedback from '@/components/FeedBack/Error.vue'
import InfoFeedback from '@/components/FeedBack/info.vue'
import { computed, onMounted, ref } from 'vue'
import type { ChallengeDifficulty, ChallengeSummary } from '@/utils/Challenges/Types/types'
import { difficultyChip, formatChallengeValue } from '@/utils/Challenges/Types/options'
import { listChallenges } from '@/utils/Challenges/API/api'
import { loadAttemptedChallengeIds, loadSolvedChallengeIds } from '@/utils/Challenges/API/submissions'
import SearchComponentChallange from './SearchComponentChallange.vue'
import FilterComponentChallange from './FilterComponentChallange.vue'
import FadeContent from '@/components/ui/FadeContent.vue'

const challenges = ref<ChallengeSummary[]>([])
const loadError = ref('')
const isLoading = ref(true)
const selectedDifficulty = ref<ChallengeDifficulty | null>(null)
const solvedChallengeIds = ref<Set<string>>(new Set())
const attemptedChallengeIds = ref<Set<string>>(new Set())

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
  loadAttemptedChallengeIds()
    .then((ids) => {
      attemptedChallengeIds.value = ids
    })
    .catch(() => {
      attemptedChallengeIds.value = new Set()
    })
})

function isChallengeSolved(challengeId: string): boolean {
  return solvedChallengeIds.value.has(challengeId)
}

function isChallengeAttempted(challengeId: string): boolean {
  return attemptedChallengeIds.value.has(challengeId)
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
  <FadeContent
    :blur="true"
    :duration="1000"
    :initial-opacity="0"
    ease="power2.out"
  >
    <v-container class="py-8">
      <SearchComponentChallange v-model="search" class="mb-4" />
      <FilterComponentChallange v-model="selectedDifficulty" class="mb-4" />
      <v-card class="mb-8" variant="text">
        <v-card-title class="text-h4 font-weight-bold px-0">Coding challenges</v-card-title>
        <v-card-subtitle class="text-body-1 mt-2 px-0">
          Choose a challenge and start practicing.
        </v-card-subtitle>
      </v-card>

      <ErrorFeedback
        v-if="loadError"
        title="Could not load challenges"
        :message="loadError"
        variant="tonal"
        class="mb-4"
      />

      <v-container v-if="isLoading" class="d-flex justify-center pa-8">
        <v-progress-circular indeterminate color="primary" />
      </v-container>

      <InfoFeedback
        v-else-if="challenges.length === 0"
        message="No challenges are available yet."
        variant="tonal"
      />

      <v-row v-else align="stretch">
        <v-col v-for="challenge in filteredChallenges" :key="challenge.id" cols="12" sm="6" lg="4">
          <v-card
            color="grey-lighten-2"
            class="h-100"
            variant="tonal"
            link
            :to="{ name: 'solveChallenge', params: { id: challenge.id } }"
          >
            <v-card-title class="d-flex align-center justify-space-between">
              {{ challenge.name }}
              <v-chip v-bind="difficultyChip(challenge.difficulty)" size="small">
                {{ formatChallengeValue(challenge.difficulty) }}
              </v-chip>
            </v-card-title>
            <v-card-subtitle>
              {{ formatChallengeValue(challenge.category) }}
            </v-card-subtitle>
            <v-card-text>{{ challenge.description }}</v-card-text>
            <v-card-actions>
              <v-btn variant="elevated" color="grey-darken-4">Solve challenge</v-btn>
            </v-card-actions>
            <v-card-text>
              <v-card-item v-if="isChallengeSolved(challenge.id)">
                <v-icon icon="mdi-check-circle" color="success" />
                <span  class=" ml-2">Solved</span>
              </v-card-item>
              <v-card-item v-else-if="isChallengeAttempted(challenge.id)">
                <v-icon icon="mdi-alert-circle" color="warning" />
                <span class="ml-2">Attempted</span>
              </v-card-item>
              <v-card-item v-else>
                <v-icon icon="mdi-close-circle" color="error" />
                <span class="ml-2">Not solved</span>
              </v-card-item>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </FadeContent>
</template>
