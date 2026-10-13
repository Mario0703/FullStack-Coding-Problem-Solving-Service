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
import {
  createSolvedCards,
  createReceiptCards,
  filterCards,
  getChallengeFilterItems,
  countSolvedByDifficulty,
  calculateTotalPoints,
  mergeSubmissionsWithTestCases,
  formatSolvedDate as formatDate,
} from '@/utils/Users/dashboard'
import type { DashboardCard } from '@/utils/Users/types'
import router from '@/router'

// The dashboard shows one card per solved challenge (from the solved
// challenges table, never duplicated) followed by one card per failed
// attempt from the submission receipt.
const submissions = ref<UserSubmission[]>([])
const solvedChallenges = ref<SolvedChallenge[]>([])
const search = ref('')
const selectedDifficulty = ref<ChallengeDifficulty | null>(null)
const selectedChallengeId = ref<string | null>(null)

const solvedCards = computed<DashboardCard[]>(() => createSolvedCards(solvedChallenges.value))

const receiptCards = computed<DashboardCard[]>(() => createReceiptCards(submissions.value))

const cards = computed(() => [...solvedCards.value, ...receiptCards.value])

const challengeFilterItems = computed(() => getChallengeFilterItems(cards.value))

const filteredCards = computed(() =>
  filterCards(cards.value, {
    search: search.value,
    selectedDifficulty: selectedDifficulty.value,
    selectedChallengeId: selectedChallengeId.value,
  }),
)

function viewSubmissionDetails(submissionId: string) {
  router.push(`/submissions/${submissionId}`)
}

function formatSolvedDate(value: string): string {
  return formatDate(value)
}

onMounted(async () => {
  try {
    const [loadedSubmissions, [passed, failed, total], solved] = await Promise.all([
      loadUserSubmissions(),
      loadSubmissionTestCaseValues(),
      loadSolvedChallenges(),
    ])

    submissions.value = mergeSubmissionsWithTestCases(loadedSubmissions, passed, failed, total)
    solvedChallenges.value = solved
  } catch {
    submissions.value = []
    solvedChallenges.value = []
  }
})

function solvedCount(difficulty: ChallengeDifficulty): number {
  return countSolvedByDifficulty(solvedChallenges.value, difficulty)
}

const easyQuestionsSolved = computed(() => solvedCount('EASY'))
const mediumQuestionsSolved = computed(() => solvedCount('MEDIUM'))
const hardQuestionsSolved = computed(() => solvedCount('HARD'))
const totalQuestionsSolved = computed(
  () => easyQuestionsSolved.value + mediumQuestionsSolved.value + hardQuestionsSolved.value,
)
const totalPoints = computed(() =>
  calculateTotalPoints(
    easyQuestionsSolved.value,
    mediumQuestionsSolved.value,
    hardQuestionsSolved.value,
  ),
)
</script>

<template>
  <v-container>
    <v-card color="grey-lighten-2" class="mb-8" variant="tonal">
      <v-card-title class="text-h4 font-weight-bold text-center pa-6"> Stats </v-card-title>
      <v-card-text class="pa-6 pt-0">
        <v-row align="stretch">
          <v-col cols="12" sm="4">
            <v-card color="green-lighten-4" class="h-100" variant="tonal">
              <v-card-title class="text-headline-medium font-weight-bold text-center">
                Easy questions solved
              </v-card-title>
              <v-card-text class="text-display-medium font-weight-bold text-center">
                {{ easyQuestionsSolved }}
              </v-card-text>
            </v-card>
          </v-col>

          <v-col cols="12" sm="4">
            <v-card color="yellow-lighten-4" class="h-100" variant="tonal">
              <v-card-title class="text-headline-medium font-weight-bold text-center">
                Medium questions solved
              </v-card-title>
              <v-card-text class="text-display-medium font-weight-bold text-center">
                {{ mediumQuestionsSolved }}
              </v-card-text>
            </v-card>
          </v-col>

          <v-col cols="12" sm="4">
            <v-card color="red-lighten-4" class="h-100" variant="tonal">
              <v-card-title class="text-headline-medium font-weight-bold text-center">
                Hard questions solved
              </v-card-title>
              <v-card-text class="text-display-medium font-weight-bold text-center">
                {{ hardQuestionsSolved }}
              </v-card-text>
            </v-card>
          </v-col>

          <v-col cols="12" sm="6">
            <v-card color="blue-lighten-4" class="h-100" variant="tonal">
              <v-card-title class="text-headline-medium font-weight-bold text-center">
                Total questions solved
              </v-card-title>
              <v-card-text class="text-display-medium font-weight-bold text-center">
                {{ totalQuestionsSolved }}
              </v-card-text>
            </v-card>
          </v-col>

          <v-col cols="12" sm="6">
            <v-card color="purple-lighten-4" class="h-100" variant="tonal">
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
    <v-card color="grey-lighten-2" variant="tonal">
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
        />
        <v-row align="stretch">
          <v-col v-for="card in filteredCards" :key="card.key" cols="12" sm="6" md="4">
            <v-card
              color="grey-lighten-1"
              class="h-100 d-flex flex-column"
              variant="tonal"
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
                <v-list class="pa-0" bg-color="transparent" lines="one">
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
                      <v-btn
                        color="grey-darken-4"
                        variant="elevated"
                        @click="viewSubmissionDetails(card.submissionId ?? '')"
                      >
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
