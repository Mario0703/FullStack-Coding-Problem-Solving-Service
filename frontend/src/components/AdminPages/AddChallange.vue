<script setup lang="ts">
import { ref, watch } from 'vue'
import { languageSetups } from '@/components/AdminPages/LanguageTemplet.ts'
import type { challangeForm, TestCase } from '@/components/AdminPages/LanguageType'
import {
  categoryOptions,
  difficultyOptions,
  languageOptions,
  type Challenge,
  type ChallengeCategory,
  type ChallengeDifficulty,
  type Language,
} from '@/utils/AdminUtils'

const emit = defineEmits<{
  created: [challenge: Challenge]
}>()


const name = ref('')
const description = ref('')
const difficulty = ref<ChallengeDifficulty | null>(null)
const catagory = ref<ChallengeCategory | null>(null)
const language = ref<Language | null>(null)
const formError = ref('')
const solution = ref('')
const template = ref('')
const templateExplanation = ref('')
const runner = ref('')
const runnerExplanation = ref('')
const isSubmitting = ref(false)
const numberOfTestCases = ref(1)
const testCases = ref<TestCase[]>([{ input: '', expectedOutput: '' }])



watch(language, (selectedLanguage) => {
  if (selectedLanguage) {
    const setup = languageSetups[selectedLanguage]
    template.value = setup.userTemplate
    templateExplanation.value = setup.userTemplateExplanation
    runner.value = setup.runner
    runnerExplanation.value = setup.runnerExplanation
  }
})

function resetForm() {
  name.value = ''
  description.value = ''
  difficulty.value = null
  catagory.value = null
  language.value = null
  solution.value = ''
  template.value = ''
  templateExplanation.value = ''
  runner.value = ''
  runnerExplanation.value = ''
  numberOfTestCases.value = 1
  testCases.value = [{ input: '', expectedOutput: '' }]
}

function resizeTestCases(count: number | null) {
  const nextCount = Math.max(0, count ?? 0)
  testCases.value = Array.from(
    { length: nextCount },
    (_, index) => testCases.value[index] ?? { input: '', expectedOutput: '' },
  )
}




function isChallengeFormValid(challengeData: challangeForm): boolean {
  const hasRequiredFields = Boolean(
    challengeData.name.trim() &&
      challengeData.description.trim() &&
      challengeData.difficulty &&
      challengeData.catagory &&
      challengeData.language &&
      challengeData.solution.trim() &&
      challengeData.template.trim() &&
      challengeData.templateExplanation.trim() &&
      challengeData.runner.trim() &&
      challengeData.runnerExplanation.trim(),
  )

  const hasCompleteTestCases =
    challengeData.testCases.length > 0 &&
    challengeData.testCases.every(
      (testCase) =>
        testCase.input.trim() !== '' && testCase.expectedOutput.trim() !== '',
    )

  return hasRequiredFields && hasCompleteTestCases
}

async function sendChallenge(challengeData: challangeForm): Promise<Challenge | null> {
  const response = await fetch('http://localhost:8082/api/challenge/create', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify(challengeData),
  })

  if (!response.ok) {
    return null
  }

  return (await response.json()) as Challenge
}

async function createChallenge() {
  formError.value = ''

  const challengeData: challangeForm = {
    name: name.value.trim(),
    description: description.value.trim(),
    difficulty: difficulty.value,
    catagory: catagory.value,
    solution: solution.value.trim(),
    template: template.value.trim(),
    templateExplanation: templateExplanation.value.trim(),
    runner: runner.value.trim(),
    runnerExplanation: runnerExplanation.value.trim(),
    language: language.value,
    testCases: testCases.value.map((testCase) => ({
      input: testCase.input.trim(),
      expectedOutput: testCase.expectedOutput.trim(),
    })),
  }

  if (!isChallengeFormValid(challengeData)) {
    formError.value =
      'Please complete the solution, template, explanations, runner, and every test case before creating the challenge.'
    return
  }

  isSubmitting.value = true

  try {
    const createdChallenge = await sendChallenge(challengeData)

    if (!createdChallenge) {
      formError.value = 'The challenge could not be created. Please check the fields and try again.'
      return
    }

    emit('created', createdChallenge)
    resetForm()
  } catch {
    formError.value = 'Unable to reach the server. Please try again later.'
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <v-card class="mx-auto" max-width="720" variant="outlined">
    <v-card-title class="text-h5 font-weight-bold pa-6">Add challenge</v-card-title>
    <v-card-text>
      <v-alert
        v-if="formError"
        title="Challenge creation failed"
        :text="formError"
        type="error"
        variant="tonal"
        class="mb-4"
      />

      <v-form @submit.prevent="createChallenge">
        <v-text-field
          v-model="name"
          label="Challenge name"
          placeholder="Two Sum"
          :counter="50"
          maxlength="50"
          required
        />
        <v-textarea
          v-model="description"
          label="Description"
          placeholder="Describe what the user needs to solve."
          :counter="500"
          maxlength="500"
          rows="4"
          required
        />
        <v-select v-model="difficulty" label="Difficulty" :items="difficultyOptions" required />
        <v-select v-model="catagory" label="Category" :items="categoryOptions" required />
        <v-select v-model="language" label="Language" :items="languageOptions" required />
        <v-textarea
          v-model="solution"
          label="Reference solution"
          placeholder="Enter the complete solution used to validate the challenge."
          rows="12"
          cols="80"
          auto-grow
          :max-rows="24"
          class="mb-6"
        />
        <v-textarea
          v-model="template"
          label="User starter template"
          :hint="
            language
              ? `Template shown to users for ${language}`
              : 'Select a language to load a starter template'
          "
          persistent-hint
          rows="12"
          cols="80"
          auto-grow
          :max-rows="24"
          class="mb-3"
        />
        <v-textarea
          v-model="templateExplanation"
          label="User template explanation"
          placeholder="Explain what the user should implement and which names or arguments must stay unchanged."
          rows="3"
          auto-grow
          :max-rows="8"
          class="mb-6"
        />
        <v-textarea
          v-model="runner"
          label="Runner"
          hint="The server uses this code to read test input, call the user's function, and print its result."
          persistent-hint
          rows="12"
          auto-grow
          :max-rows="24"
          class="mb-3"
        />
        <v-textarea
          v-model="runnerExplanation"
          label="Runner explanation"
          placeholder="Explain how input is parsed, how the user function is called, and what is printed."
          rows="3"
          auto-grow
          :max-rows="8"
          class="mb-6"
        />
        <v-number-input
          v-model="numberOfTestCases"
          @update:model-value="resizeTestCases"
          label="Number of test cases"
          :min="1"
          :max="50"
          control-variant="default"
          required
          class="mb-4"
        />

        <v-card
          v-for="(testCase, index) in testCases"
          :key="index"
          variant="tonal"
          class="mb-4 pa-4"
        >
          <v-card-title class="px-0 pt-0 text-subtitle-1"> Test case {{ index + 1 }} </v-card-title>
          <v-textarea
            v-model="testCase.input"
            label="Input"
            placeholder="Enter the exact contents passed to the user's program."
            rows="4"
            auto-grow
            :max-rows="10"
            class="mb-3"
          />
          <v-textarea
            v-model="testCase.expectedOutput"
            label="Expected output"
            placeholder="Enter the exact output the program should produce."
            rows="4"
            auto-grow
            :max-rows="10"
          />
        </v-card>

        <v-btn type="submit" color="primary" :loading="isSubmitting" :disabled="isSubmitting">
          Create challenge
        </v-btn>
      </v-form>
    </v-card-text>
  </v-card>
</template>

<style scoped></style>
