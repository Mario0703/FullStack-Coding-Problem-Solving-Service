<script setup lang="ts">
import {
  categoryOptions,
  difficultyOptions,
  languageOptions,
} from '@/utils/AdminUtils'
import type { CreateChallengeForm } from '@/utils/LanguageType'
import { useChallengeForm } from '@/utils/useChallengeForm'

defineProps<{
  isSubmitting: boolean
}>()

const emit = defineEmits<{
  submit: [challenge: CreateChallengeForm]
}>()

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
  toCreateChallengeForm,
  numberOfExamples,
  examples,
  createNumberOfSampels,
} = useChallengeForm()

function submit() {
  emit('submit', toCreateChallengeForm())
}

defineExpose({ resetForm })
</script>

<template>
  <v-form @submit.prevent="submit">
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
      label="Number of test cases"
      :min="1"
      :max="50"
      @update:model-value="createNumberOfSampels(testCases, $event, { input: '', expectedOutput: '' })"
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
      <v-card-title class="px-0 pt-0 text-subtitle-1">Test case {{ index + 1 }}</v-card-title>
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

    <v-number-input
      v-model="numberOfExamples"
      label="Number of examples"
      :min="0"
      :max="15"
      @update:model-value="createNumberOfSampels(examples, $event, { inputExample: '', outputExample: '' })"
      control-variant="default"
      required
    />
    <v-card v-for="(example, index) in examples" :key="index" variant="tonal" class="mb-4 pa-4">
      <v-card-title class="px-0 pt-0 text-subtitle-1">Example {{ index + 1 }}</v-card-title>
      <v-textarea
        v-model="example.inputExample"
        label="Input"
        placeholder="Enter the exact contents passed to the user's program."
        rows="4"
        auto-grow
        :max-rows="10"
        class="mb-3"
      />
      <v-textarea
        v-model="example.outputExample"
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
</template>
