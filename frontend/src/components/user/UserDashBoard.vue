<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import type { ChallengeDifficulty } from '@/utils/AdminUtils'
import {
  loadSubmissionTestCaseValues,
  loadUserSubmissions,
  type UserSubmission,
} from '@/utils/SolvedChallengeUtil'
import SearchComponentChallange from '@/components/challenges/SearchComponentChallange.vue'
import FilterComponentChallange from '@/components/challenges/FilterComponentChallange.vue'
import router from '@/router'

const submissions = ref<UserSubmission[]>([])
const search = ref('')
const selectedDifficulty = ref<ChallengeDifficulty | null>(null)

const filteredSubmissions = computed(() =>
  submissions.value.filter((submission) => {
    const matchesDifficulty =
      !selectedDifficulty.value || submission.difficulty === selectedDifficulty.value
    const matchesSearch = submission.challengeName
      .toLowerCase()
      .includes(search.value.trim().toLowerCase())

    return matchesDifficulty && matchesSearch
  }),
)

function viewSubmissionDetails(submissionId: string) {
  router.push(`/submissions/${submissionId}`)
}

onMounted(async () => {
  try {
    const [loadedSubmissions, [passed, failed, total]] = await Promise.all([
      loadUserSubmissions(),
      loadSubmissionTestCaseValues(),
    ])

    submissions.value = loadedSubmissions.map((submission, index) => ({
      ...submission,
      testCasesPassed: passed[index] ?? 0,
      testCasesFailed: failed[index] ?? 0,
      totalTestCases: total[index] ?? 0,
    }))
  } catch {
    submissions.value = []
  }
})

function solvedCount(difficulty: ChallengeDifficulty): number {
  return new Set(
    submissions.value
      .filter((submission) => submission.status === 'ACCEPTED')
      .filter((submission) => submission.difficulty === difficulty)
      .map((submission) => submission.challengeId),
  ).size
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
        <v-row align="stretch">
          <v-col
            v-for="submission in filteredSubmissions"
            :key="submission.id"
            cols="12"
            sm="6"
            md="4"
          >
            <v-card
              class="border-lg h-100 d-flex flex-column bg-black"
              border="primary"
              variant="outlined"
            >
              <v-card-item>
                <v-card-title class="text-h6 font-weight-bold px-0">
                  {{ submission.challengeName }}
                </v-card-title>
                <v-card-subtitle class="px-0 pt-2">
                  <v-chip
                    size="small"
                    variant="tonal"
                    :color="
                      submission.difficulty === 'EASY'
                        ? 'success'
                        : submission.difficulty === 'MEDIUM'
                          ? 'warning'
                          : 'error'
                    "
                  >
                    {{ submission.difficulty }}
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
                        :color="submission.status === 'ACCEPTED' ? 'success' : 'error'"
                        variant="tonal"
                      >
                        <v-icon start>
                          {{
                            submission.status === 'ACCEPTED'
                              ? 'mdi-check-circle'
                              : 'mdi-close-circle'
                          }}
                        </v-icon>
                        {{ submission.status === 'ACCEPTED' ? 'Solved' : 'Not solved' }}
                      </v-chip>
                    </template>
                  </v-list-item>
                  <v-list-item class="px-0" title="TestCases passed">
                    <template #append>
                      <span class="font-weight-bold text-success">{{
                        submission.testCasesPassed
                      }}</span>
                    </template>
                  </v-list-item>
                  <v-list-item class="px-0" title="TestCases failed">
                    <template #append>
                      <span class="font-weight-bold text-error">{{
                        submission.testCasesFailed
                      }}</span>
                    </template>
                  </v-list-item>
                  <v-list-item class="px-0" title="Total test cases">
                    <template #append>
                      <span class="font-weight-bold">{{ submission.totalTestCases }}</span>
                    </template>
                  </v-list-item>
                  <v-list-item>
                    <v-btn @click="viewSubmissionDetails(submission.id)">View Details</v-btn>
                  </v-list-item>
                </v-list>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </v-container>
</template>
