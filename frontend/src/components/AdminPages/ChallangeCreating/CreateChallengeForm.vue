<script setup lang="ts">
import {
  categoryOptions,
  difficultyOptions,
  languageOptions,
} from '@/utils/Challenges/Types/options'
import { languageLabel } from '@/utils/Challenges/API/execution'
import type { CreateChallengeForm } from '@/utils/Challenges/Types/authoringTypes'
import { useChallengeForm } from '@/utils/Challenges/Authoring/useChallengeForm'
import CodeEditor from '@/components/code-execution/CodeEditor.vue'

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
  createNumberOfSamples,
  otherLanguages,
  extraLanguages,
  extraSetups,
  isExtraLanguage,
  toggleExtraLanguage,
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
    <v-label class="mb-2">Reference solution</v-label>
    <CodeEditor
      v-model="solution"
      :language="language ?? ''"
      height="300px"
      class="mb-6"
    />
    <v-label class="mb-2">User starter template</v-label>
    <CodeEditor
      v-model="template"
      :language="language ?? ''"
      height="300px"
      class="mb-3"
    />
    <div class="text-caption mb-3">
      {{
        language
          ? `Template shown to users for ${languageLabel(language)}`
          : 'Select a language to load a starter template'
      }}
    </div>
    <v-textarea
      v-model="templateExplanation"
      label="User template explanation"
      placeholder="Explain what the user should implement and which names or arguments must stay unchanged."
      rows="3"
      auto-grow
      :max-rows="8"
      class="mb-6"
    />
    <v-label class="mb-2">Runner</v-label>
    <CodeEditor
      v-model="runner"
      :language="language ?? ''"
      height="300px"
      class="mb-3"
    />
    <div class="text-caption mb-3">
      The server uses this code to read test input, call the user's function, and print its result.
    </div>
    <v-textarea
      v-model="runnerExplanation"
      label="Runner explanation"
      placeholder="Explain how input is parsed, how the user function is called, and what is printed."
      rows="3"
      auto-grow
      :max-rows="8"
      class="mb-6"
    />

    <v-divider class="my-6" />
    <v-label class="mb-2">Additional languages (optional)</v-label>
    <div class="text-caption mb-3">
      Enable any language solvers may also use. Each language needs its own starter template
      and runner, since the runner wraps the challenge's function for that language.
    </div>
    <div class="d-flex flex-wrap ga-2 mb-4">
      <v-chip
        v-for="option in otherLanguages"
        :key="option"
        :color="isExtraLanguage(option) ? 'primary' : undefined"
        :variant="isExtraLanguage(option) ? 'flat' : 'outlined'"
        @click="toggleExtraLanguage(option)"
      >
        {{ languageLabel(option) }}
      </v-chip>
    </div>
    <v-card
      v-for="option in extraLanguages"
      :key="option"
      variant="tonal"
      class="mb-4 pa-4"
    >
      <v-card-title class="px-0 pt-0 text-subtitle-1">{{ languageLabel(option) }} setup</v-card-title>
      <v-label class="mb-2">Starter template</v-label>
      <CodeEditor
        v-model="extraSetups[option].template"
        :language="option"
        height="220px"
        class="mb-3"
      />
      <v-textarea
        v-model="extraSetups[option].templateExplanation"
        label="Template explanation"
        rows="2"
        auto-grow
        :max-rows="6"
        class="mb-3"
      />
      <v-label class="mb-2">Runner</v-label>
      <CodeEditor
        v-model="extraSetups[option].runner"
        :language="option"
        height="220px"
        class="mb-3"
      />
      <v-textarea
        v-model="extraSetups[option].runnerExplanation"
        label="Runner explanation"
        rows="2"
        auto-grow
        :max-rows="6"
      />
    </v-card>

    <v-number-input
      v-model="numberOfTestCases"
      label="Number of test cases"
      :min="1"
      :max="50"
      @update:model-value="createNumberOfSamples(testCases, $event, { input: '', expectedOutput: '' })"
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
      @update:model-value="createNumberOfSamples(examples, $event, { inputExample: '', outputExample: '' })"
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
