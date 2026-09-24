<script setup lang="ts">
import { ref } from 'vue'
import { createChallenge as createChallengeRequest } from '@/utils/ChallangeUtil'
import { isChallengeFormValid } from '@/utils/ChallengeValidation'
import { useChallengeForm } from '@/components/AdminPages/useChallengeForm'
import {
  categoryOptions,
  difficultyOptions,
  languageOptions,
  type Challenge,
} from '@/utils/AdminUtils'

const emit = defineEmits<{
  created: [challenge: Challenge]
}>()

const formError = ref('')
const isSubmitting = ref(false)

const {
  name,
  description,
  difficulty,
  category,
  language,
  solution,
  template,
  templateExplanation,
  runner,
  runnerExplanation,
  numberOfTestCases,
  testCases,
  resetForm,
  resizeTestCases,
  toChallengeForm,
} = useChallengeForm()

async function createChallenge() {
  formError.value = ''

  const challengeData = toChallengeForm()

  if (!isChallengeFormValid(challengeData)) {
    formError.value =
      'Please complete the solution, template, explanations, runner, and every test case before creating the challenge.'
    return
  }

  isSubmitting.value = true

  try {
    const createdChallenge = await createChallengeRequest(challengeData)

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
        <v-select v-model="category" label="Category" :items="categoryOptions" required />
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
