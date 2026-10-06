<script setup lang="ts">
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { formatChallengeValue } from '@/utils/AdminUtils'
import { getChallengeExamples, getChallengeSolution, listChallenges, type ChallengeExample, type ChallengeSummary } from '@/utils/ChallangeUtil'
import { baseUrl } from '@/utils/api'
import ChallangeSolve from '@/components/code-execution/ChallangeSolve.vue'
import CodeEditor from '@/components/code-execution/CodeEditor.vue'
import ErrorFeedback from '@/components/FeedBack/Error.vue'
import { languageLabel, validLanguages } from '@/utils/CodeExecutorUtils'

type Solver = {
  userId: string
  userName: string
  code: string
  language: string
  solvedAt: string
}

function formatSubmittedDate(value: string): string {
  if (!value) {
    return 'Unknown'
  }

  return new Date(value).toLocaleDateString()
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
const challenge = ref<ChallengeSummary | null>(null)
const examples = ref<ChallengeExample[]>([])
const loadError = ref('')
const isLoading = ref(true)
const solvedByPeople = ref<Solver[]>([])
const selectedLanguage = ref<string | null>(null)

const languageItems = validLanguages.map((value) => ({
  title: languageLabel(value),
  value,
}))

const filteredSolvedByPeople = computed(() =>{
  if (!selectedLanguage.value) {
    return solvedByPeople.value
  }
  return solvedByPeople.value.filter((person) => person.language === selectedLanguage.value)
})

const showSolution = ref(false)
const showSolutionsByPeople = ref(false)
const solution = ref('')
const solutionError = ref('')
const isLoadingSolution = ref(false)

watch(showSolution, async (enabled) => {
  solutionError.value = ''

  if (!enabled) {
    solution.value = ''
    return
  }

  const challengeId = challenge.value?.id
  if (!challengeId) {
    return
  }

  isLoadingSolution.value = true
  try {
    solution.value = await getChallengeSolution(challengeId)
  } catch (error) {
    solutionError.value =
      error instanceof Error ? error.message : 'The solution could not be loaded.'
  } finally {
    isLoadingSolution.value = false
  }
})


async function loadChallenge() {
  try {
    const challengeId = String(route.params.id)
    const [challenges, solvedBy, challengeExamples] = await Promise.all([
      listChallenges(),
      loadSolvedByPeople(challengeId),
      // A missing examples endpoint should not take down the whole page.
      getChallengeExamples(challengeId).catch(() => [] as ChallengeExample[]),
    ])
    solvedByPeople.value = solvedBy
    examples.value = challengeExamples
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

    <v-card v-if="examples.length > 0" variant="outlined" class="mt-4">
      <v-card-title class="text-h5">Examples</v-card-title>
      <v-list lines="three">
        <v-list-item v-for="(example, index) in examples" :key="example.id ?? index">
          <v-list-item-title class="font-weight-bold">
            Example {{ index + 1 }}
          </v-list-item-title>
          <template #subtitle>
            <div class="d-flex align-center flex-wrap ga-2 mt-2">
              <v-chip size="small" color="primary" variant="tonal">Input</v-chip>
              <span>{{ example.inputExample }}</span>
            </div>
            <div class="d-flex align-center flex-wrap ga-2 mt-1">
              <v-chip size="small" color="success" variant="tonal">Output</v-chip>
              <span>{{ example.outputExample }}</span>
            </div>
          </template>
        </v-list-item>
      </v-list>
    </v-card>

    <ChallangeSolve v-if="challenge" :challenge-id="challenge.id" />
  </v-container>
  <v-container>
    <v-select
      v-model="selectedLanguage"
      :items="languageItems"
      item-title="title"
      item-value="value"
      label="Filter solutions by languages"
      clearable
      class="mb-4"
    />
    <v-checkbox v-model="showSolution" label="Show instructor solution"></v-checkbox>
    <v-checkbox
      v-model="showSolutionsByPeople"
      label="Show solutions solved by"
    ></v-checkbox>

  </v-container>
  <v-container v-if="challenge && showSolution">
    <h3 class="text-h5 mb-4">Instructor solution</h3>
    <v-alert v-if="solutionError" type="error" variant="tonal">{{ solutionError }}</v-alert>
    <v-container v-else-if="isLoadingSolution" class="d-flex justify-center pa-4">
      <v-progress-circular indeterminate color="primary" />
    </v-container>
    <CodeEditor
      v-else-if="solution"
      :model-value="solution"
      :language="challenge.language"
      readonly
      height="240px"
    />
  </v-container>
  <v-container v-if="showSolutionsByPeople && challenge && filteredSolvedByPeople.length > 0">
    <h3 class="text-h5 mb-4">Solved by</h3>
    <v-row v-for="(person, index) in filteredSolvedByPeople" :key="index">
      <v-col>
        <v-card class="mb-4">
          <v-card-title>{{ person.userName }}</v-card-title>
          <v-card-subtitle>Submitted: {{ formatSubmittedDate(person.solvedAt) }}</v-card-subtitle>
          <v-card-text>
            <CodeEditor
              :model-value="person.code"
              :language="person.language || challenge.language"
              readonly
              height="200px"
            />
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

