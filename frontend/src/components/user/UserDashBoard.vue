<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import type { ChallengeDifficulty } from '@/utils/Challenges/Types/types'
import {
  loadSolvedChallenges,
  loadSubmissionTestCaseValues,
  loadUserSubmissions,
  type SolvedChallenge,
  type UserSubmission,
} from '@/utils/Challenges/API/submissions'
import SearchComponentChallange from '@/components/challenges/SearchComponentChallange.vue'
import FilterComponentChallange from '@/components/challenges/FilterComponentChallange.vue'
import {
  languageLabel,
  submissionStatusColor,
  submissionStatusLabel,
} from '@/utils/Challenges/API/execution'
import router from '@/router'

// The dashboard shows one card per solved challenge (from the solved
// challenges table, never duplicated) followed by one card per failed
// attempt from the submission receipt.
type DashboardCard = {
  key: string
  challengeId: string
  challengeName: string
  difficulty: ChallengeDifficulty
  status: string
  isSolved: boolean
  languages: string[]
  solvedAt: string
  testCasesPassed: number
  testCasesFailed: number
  totalTestCases: number
  submissionId?: string
}

const submissions = ref<UserSubmission[]>([])
const solvedChallenges = ref<SolvedChallenge[]>([])
const search = ref('')
const selectedDifficulty = ref<ChallengeDifficulty | null>(null)
const selectedChallengeId = ref<string | null>(null)

const solvedCards = computed<DashboardCard[]>(() =>
  solvedChallenges.value.map((solved) => ({
    key: `solved-${solved.challengeId}`,
    challengeId: solved.challengeId,
    challengeName: solved.challengeName,
    difficulty: solved.difficulty,
    status: 'ACCEPTED',
    isSolved: true,
    languages: solved.languages,
    solvedAt: solved.solvedAt,
    testCasesPassed: 0,
    testCasesFailed: 0,
    totalTestCases: 0,
  })),
)

const receiptCards = computed<DashboardCard[]>(() =>
  submissions.value.map((submission) => ({
    key: submission.id,
    challengeId: submission.challengeId,
    challengeName: submission.challengeName,
    difficulty: submission.difficulty,
    status: submission.status,
    isSolved: false,
    languages: [],
    solvedAt: '',
    testCasesPassed: submission.testCasesPassed,
    testCasesFailed: submission.testCasesFailed,
    totalTestCases: submission.totalTestCases,
    submissionId: submission.id,
  })),
)

const cards = computed(() => [...solvedCards.value, ...receiptCards.value])

const challengeFilterItems = computed(() =>
  [
    ...new Map(cards.value.map((card) => [card.challengeId, card.challengeName])),
  ]
    .map(([value, title]) => ({ value, title }))
    .sort((a, b) => a.title.localeCompare(b.title)),
)

const filteredCards = computed(() =>
  cards.value.filter((card) => {
    const matchesDifficulty =
      !selectedDifficulty.value || card.difficulty === selectedDifficulty.value
    const matchesChallenge =
      !selectedChallengeId.value || card.challengeId === selectedChallengeId.value
    const matchesSearch = card.challengeName
      .toLowerCase()
      .includes(search.value.trim().toLowerCase())

    return matchesDifficulty && matchesChallenge && matchesSearch
  }),
)

function viewSubmissionDetails(submissionId: string) {
  router.push(`/submissions/${submissionId}`)
}

function formatSolvedDate(value: string): string {
  return new Date(value).toLocaleDateString()
}

onMounted(async () => {
  try {
    const [loadedSubmissions, [passed, failed, total], solved] = await Promise.all([
      loadUserSubmissions(),
      loadSubmissionTestCaseValues(),
      loadSolvedChallenges(),
    ])

    submissions.value = loadedSubmissions.map((submission, index) => ({
      ...submission,
      testCasesPassed: passed[index] ?? 0,
      testCasesFailed: failed[index] ?? 0,
      totalTestCases: total[index] ?? 0,
    }))
    solvedChallenges.value = solved
  } catch {
    submissions.value = []
    solvedChallenges.value = []
  }
})

function solvedCount(difficulty: ChallengeDifficulty): number {
  return solvedChallenges.value.filter((solved) => solved.difficulty === difficulty).length
}

const easyQuestionsSolved = computed(() => solvedCount('EASY'))
const mediumQuestionsSolved = computed(() => solvedCount('MEDIUM'))
const hardQuestionsSolved = computed(() => solvedCount('HARD'))
const totalQuestionsSolved = computed(
  () => easyQuestionsSolved.value + mediumQuestionsSolved.value + hardQuestionsSolved.value,
)
const totalPoints = computed(
  () => easyQuestionsSolved.value + mediumQuestionsSolved.value * 2 + hardQuestionsSolved.value * 3,
)
</script>

<template>
  <v-container>
    <v-card class="border-lg mb-8 bg-surface" border="primary" variant="outlined">
      <v-card-title class="text-h4 font-weight-bold text-center pa-6"> Stats </v-card-title>
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
    <v-card class="border-lg bg-surface" border="primary" variant="outlined">
      <v-card-title class="text-h4 font-weight-bold text-center pa-6"> Questions </v-card-title>
      <v-card-text class="pa-6 pt-0">
        <SearchComponentChallange v-model="search" class="mb-4" />
        <FilterComponentChallange v-model="selectedDifficulty" class="mb-6" />
        <v-select
          v-model="selectedChallengeId"
          class="mb-6"
          label="Filter by challenge"
          :items="challengeFilterItems"
          item-title="title"
          item-value="value"
          clearable
          hide-details
          variant="outlined"
        />
        <v-row align="stretch">
          <v-col v-for="card in filteredCards" :key="card.key" cols="12" sm="6" md="4">
            <v-card
              class="border-lg h-100 d-flex flex-column bg-black"
              border="primary"
              variant="outlined"
            >
              <v-card-item>
                <v-card-title class="text-h6 font-weight-bold px-0">
                  {{ card.challengeName }}
                </v-card-title>
                <v-card-subtitle class="px-0 pt-2">
                  <v-chip
                    size="small"
                    variant="tonal"
                    :color="
                      card.difficulty === 'EASY'
                        ? 'success'
                        : card.difficulty === 'MEDIUM'
                          ? 'warning'
                          : 'error'
                    "
                  >
                    {{ card.difficulty }}
                  </v-chip>
                </v-card-subtitle>
              </v-card-item>

              <v-divider />

              <v-card-text class="flex-grow-1">
                <v-list class="pa-0 bg-black" lines="one">
                  <v-list-item class="px-0" title="Status">
                    <template #append>
                      <v-chip
                        size="small"
                        :color="submissionStatusColor(card.status)"
                        variant="tonal"
                      >
                        <v-icon start>
                          {{ card.isSolved ? 'mdi-check-circle' : 'mdi-close-circle' }}
                        </v-icon>
                        {{ submissionStatusLabel(card.status) }}
                      </v-chip>
                    </template>
                  </v-list-item>
                  <template v-if="card.isSolved">
                    <v-list-item class="px-0" title="Languages">
                      <template #append>
                        <span class="font-weight-bold">
                          {{ card.languages.map(languageLabel).join(', ') }}
                        </span>
                      </template>
                    </v-list-item>
                    <v-list-item class="px-0" title="Solved at">
                      <template #append>
                        <span class="font-weight-bold">{{ formatSolvedDate(card.solvedAt) }}</span>
                      </template>
                    </v-list-item>
                  </template>
                  <template v-else>
                    <v-list-item class="px-0" title="TestCases passed">
                      <template #append>
                        <span class="font-weight-bold text-success">{{
                          card.testCasesPassed
                        }}</span>
                      </template>
                    </v-list-item>
                    <v-list-item class="px-0" title="TestCases failed">
                      <template #append>
                        <span class="font-weight-bold text-error">{{
                          card.testCasesFailed
                        }}</span>
                      </template>
                    </v-list-item>
                    <v-list-item class="px-0" title="Total test cases">
                      <template #append>
                        <span class="font-weight-bold">{{ card.totalTestCases }}</span>
                      </template>
                    </v-list-item>
                    <v-list-item>
                      <v-btn @click="viewSubmissionDetails(card.submissionId ?? '')">
                        View Details
                      </v-btn>
                    </v-list-item>
                  </template>
                </v-list>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </v-container>
</template>
