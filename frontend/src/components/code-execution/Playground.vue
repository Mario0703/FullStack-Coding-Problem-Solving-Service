<script setup lang="ts">
import { ref, watch } from 'vue'
import { currentUser } from '@/authenticator/auth'

type ChallengeTemplateResponse = {
  language: string
  template: string
  explanation: string
}

type ExecutionResult = {
  exitCode: number
  output: string
}

const props = defineProps<{
  challengeId?: string
}>()

defineOptions({ name: 'ChallengePlayground' })

const language = ref('')
const code = ref('')
const explanation = ref('')
const output = ref('')
const loadError = ref('')
const isLoading = ref(false)
const isExecuting = ref(false)

function languageLabel(value: string) {
  const labels: Record<string, string> = {
    PYTHON: 'Python',
    JAVA: 'Java',
    'C++': 'C++',
    'C#': 'C#',
  }

  return labels[value] ?? value
}

async function loadTemplate(challengeId: string | undefined) {
  language.value = ''
  code.value = ''
  explanation.value = ''
  output.value = ''
  loadError.value = ''

  if (!challengeId) {
    loadError.value = 'Open a challenge to load its coding template.'
    return
  }

  isLoading.value = true

  try {
    const response = await fetch(`http://localhost:8082/api/challenge/${challengeId}/template`, {
      credentials: 'include',
    })

    if (!response.ok) {
      throw new Error('The challenge template could not be loaded.')
    }

    const result = (await response.json()) as ChallengeTemplateResponse
    language.value = result.language
    code.value = result.template
    explanation.value = result.explanation
  } catch (error) {
    loadError.value =
      error instanceof Error ? error.message : 'The challenge template could not be loaded.'
  } finally {
    isLoading.value = false
  }
}

watch(
  () => props.challengeId,
  (challengeId) => loadTemplate(challengeId),
  { immediate: true },
)

async function executeCode() {
  if (!code.value.trim() || !language.value || !props.challengeId) {
    output.value = 'Please load a challenge and enter code first.'
    return
  }

  if (!currentUser.value?.userId) {
    output.value = 'You must be signed in before submitting code.'
    return
  }

  isExecuting.value = true
  output.value = ''

  try {
    const response = await fetch('http://localhost:8082/api/code/playground/submit', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify({
        userId: currentUser.value.userId,
        challengeId: props.challengeId,
        language: language.value.toLowerCase(),
        code: code.value,
      }),
    })
    console.log(code.value)

    if (!response.ok) {
      output.value = `Error: ${await response.text()}`
      return
    }

    const result = (await response.json()) as ExecutionResult
    output.value = `Exit code: ${result.exitCode}\n${result.output}`
  } catch (error) {
    output.value = `Could not connect to the backend: ${String(error)}`
  } finally {
    isExecuting.value = false
  }
}
</script>

<template>
  <v-container class="py-8">
    <v-container v-if="isLoading" class="d-flex justify-center pa-8">
      <v-progress-circular indeterminate color="primary" />
    </v-container>

    <v-alert v-else-if="loadError" :text="loadError" type="error" variant="tonal" />

    <template v-else>
      <v-alert v-if="explanation" :text="explanation" type="info" variant="tonal" class="mb-4" />

      <v-textarea @input="console.log('Code changed:', $event.target.value)" v-model="code" :label="`${languageLabel(language)} code`" rows="16" auto-grow />

      <v-btn color="primary" :loading="isExecuting" @click="executeCode"> Submit </v-btn>

      <v-card v-if="output" class="mt-4">
        <v-card-title>Output</v-card-title>
        <v-card-text>{{ output }}</v-card-text>
      </v-card>
    </template>
  </v-container>
</template>

<style scoped></style>
