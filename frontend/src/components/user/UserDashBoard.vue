<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import type { Challenge, ChallengeDifficulty } from '@/utils/AdminUtils'
import { listChallenges } from '@/utils/ChallangeUtil'
import { loadSolvedChallengeIds } from '@/utils/SolvedChallengeUtil'

const challenges = ref<Challenge[]>([])
const solvedChallengeIds = ref<Set<string>>(new Set())

onMounted(async () => {
  try {
    const [loadedChallenges, solvedIds] = await Promise.all([
      listChallenges(),
      loadSolvedChallengeIds(),
    ])
    challenges.value = loadedChallenges
    solvedChallengeIds.value = solvedIds
  } catch {
    challenges.value = []
    solvedChallengeIds.value = new Set()
  }
})

function solvedCount(difficulty: ChallengeDifficulty): number {
  return challenges.value.filter(
    (challenge) =>
      challenge.difficulty === difficulty && solvedChallengeIds.value.has(challenge.id),
  ).length
}

const easyQuestionsSolved = computed(() => solvedCount('EASY'))
const mediumQuestionsSolved = computed(() => solvedCount('MEDIUM'))
const hardQuestionsSolved = computed(() => solvedCount('HARD'))
const totalQuestionsSolved = computed(
  () => easyQuestionsSolved.value + mediumQuestionsSolved.value + hardQuestionsSolved.value,
)
const totalPoints = computed(
  () =>
    easyQuestionsSolved.value +
    mediumQuestionsSolved.value * 2 +
    hardQuestionsSolved.value * 3,
)



const dummyQuestions = [
  { id: 1, title: "Two Sum", difficulty: "Easy" },
  { id: 2, title: "Add Two Numbers", difficulty: "Medium" },
  { id: 3, title: "Longest Substring Without Repeating Characters", difficulty: "Hard" },
  { id: 4, title: "Median of Two Sorted Arrays", difficulty: "Hard" },
  { id: 5, title: "Longest Palindromic Substring", difficulty: "Medium" },
]


</script>

<template>
  <v-container>
    <v-card class="border-lg mb-8 bg-surface" border="primary" variant="outlined">
      <v-card-title class="text-h4 font-weight-bold text-center pa-6">
        Stats
      </v-card-title>
      <v-card-text class="pa-6 pt-0">
        <v-row align="stretch">
          <v-col cols="12" sm="4">
            <v-card class="border-lg h-100 bg-green-lighten-4" border="primary" variant="outlined">
              <v-card-title class="text-headline-medium font-weight-bold text-center">
                Easy questions solved
              </v-card-title>
              <v-card-text class="text-display-medium font-weight-bold text-center">
                {{ easyQuestionsSolved }}
              </v-card-text>
            </v-card>
          </v-col>

          <v-col cols="12" sm="4">
            <v-card class="border-lg h-100 bg-yellow-lighten-4" border="primary" variant="outlined">
              <v-card-title class="text-headline-medium font-weight-bold text-center">
                Medium questions solved
              </v-card-title>
              <v-card-text class="text-display-medium font-weight-bold text-center">
                {{ mediumQuestionsSolved }}
              </v-card-text>
            </v-card>
          </v-col>

          <v-col cols="12" sm="4">
            <v-card class="border-lg h-100 bg-red-lighten-4" border="primary" variant="outlined">
              <v-card-title class="text-headline-medium font-weight-bold text-center">
                Hard questions solved
              </v-card-title>
              <v-card-text class="text-display-medium font-weight-bold text-center">
                {{ hardQuestionsSolved }}
              </v-card-text>
            </v-card>
          </v-col>

          <v-col cols="12" sm="6">
            <v-card class="border-lg h-100 bg-blue-lighten-4" border="primary" variant="outlined">
              <v-card-title class="text-headline-medium font-weight-bold text-center">
                Total questions solved
              </v-card-title>
              <v-card-text class="text-display-medium font-weight-bold text-center">
                {{ totalQuestionsSolved }}
              </v-card-text>
            </v-card>
          </v-col>

          <v-col cols="12" sm="6">
            <v-card class="border-lg h-100 bg-purple-lighten-4" border="primary" variant="outlined">
              <v-card-title class="text-headline-medium font-weight-bold text-center">
                Total points earned
              </v-card-title>
              <v-card-text class="text-display-medium font-weight-bold text-center">
                {{ totalPoints }}
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </v-container>
  <v-container class="py-8">
    <v-card class="border-lg bg-grey-lighten-4" border="primary" variant="outlined">
      <v-card-title class="text-h4 font-weight-bold text-center pa-6">
        Questions
      </v-card-title>
      <v-card-text class="pa-6 pt-0">
        <v-row align="stretch">
          <v-col
            v-for="question in dummyQuestions"
            :key="question.id"
            cols="12"
            sm="6"
            md="4"
          >
            <v-card class="border-lg h-100 bg-white" border="primary" variant="outlined">
              <v-card-title class="text-h6 font-weight-bold">
                {{ question.title }}
              </v-card-title>
              <v-card-text class="text-body-1">
                Difficulty: {{ question.difficulty }}
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </v-container>
</template>
