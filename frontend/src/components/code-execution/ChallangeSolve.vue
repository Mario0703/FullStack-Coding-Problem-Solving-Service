<script setup lang="ts">
import { ref, watch } from 'vue'
import { currentUser } from '@/authenticator/auth'
import CodeEditor from '@/components/code-execution/CodeEditor.vue'
import ErrorFeedback from '@/components/FeedBack/Error.vue'
import InfoFeedback from '@/components/FeedBack/info.vue'
import {
  languageLabel,
  loadChallengeTemplate,
  submissionStatusColor,
  submissionStatusLabel,
  submitCode,
} from '@/utils/CodeExecutorUtils'
import { validLanguages } from '@/utils/CodeExecutorUtils'
const props = defineProps<{
  challengeId?: string
}>()

defineOptions({ name: 'ChallengeSolve' })

const language = ref('')
const code = ref('')
const explanation = ref('')
const output = ref('')
const submissionStatus = ref('')
const loadError = ref('')
const isLoading = ref(false)
const isExecuting = ref(false)

async function loadTemplate(challengeId: string | undefined) {
  language.value = ''
  code.value = ''
  explanation.value = ''
  output.value = ''
  submissionStatus.value = ''
  loadError.value = ''

  if (!challengeId) {
    loadError.value = 'Open a challenge to load its coding template.'
    return
  }

  isLoading.value = true

  try {
    const result = await loadChallengeTemplate(challengeId)
    // The backend may still store the language in upper case; normalize so
    // the editor picks the right syntax highlighting.
    language.value = (result.language ?? '').toLowerCase()
    code.value = result.template
    explanation.value = result.explanation
  } catch (error) {
    loadError.value =
      error instanceof Error ? error.message : 'The challenge template could not be loaded.'
  } finally {
    isLoading.value = false
  }
}

async function selectLanguage(option: string) {
  language.value = option

  if (!props.challengeId) {
    return
  }

  try {
    const result = await loadChallengeTemplate(props.challengeId, option)
    language.value = option
    // Only replace the editor content when the challenge actually has a
    // setup for the requested language (the response echoes it back). If it
    // fell back to the default language, keep whatever the user has typed.
    if ((result.language ?? '').toLowerCase() === option.toLowerCase()) {
      code.value = result.template ?? ''
      explanation.value = result.explanation ?? ''
    }
  } catch {
    // Keep the current code if the per-language template cannot be loaded.
  }
}

watch(
  () => props.challengeId,
  (challengeId) => loadTemplate(challengeId),
  { immediate: true },
)

async function executeCode() {
  const isCodeValid = code.value.trim() && language.value && props.challengeId

  if (!isCodeValid) {
    output.value = 'Please load a challenge and enter code first.'
    return
  }

  if (!currentUser.value?.userId) {
    output.value = 'You must be signed in before submitting code.'
    return
  }

  isExecuting.value = true
  output.value = ''
  submissionStatus.value = ''

  try {
    const result = await submitCode({
        userId: currentUser.value.userId,
        challengeId: props.challengeId,
        language: language.value,
        code: code.value,
    })
    submissionStatus.value = result.status
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

    <ErrorFeedback v-else-if="loadError" :message="loadError" variant="tonal" />

    <template v-else>
      <InfoFeedback v-if="explanation" :message="explanation" variant="tonal" class="mb-4" />

      <div class="d-flex flex-wrap align-center ga-2 mb-4">
        <span class="text-subtitle-1 font-weight-medium">Language:</span>
        <v-btn
          v-for="option in validLanguages"
          :key="option"
          :color="language === option ? 'primary' : undefined"
          :variant="language === option ? 'flat' : 'outlined'"
          size="small"
          @click="selectLanguage(option)"
        >
          {{ languageLabel(option) }}
        </v-btn>
      </div>

      <CodeEditor v-model="code" :language="language" />

      <v-btn color="primary" :loading="isExecuting" @click="executeCode"> Submit </v-btn>

      <v-card v-if="output" class="mt-4">
        <v-card-title class="d-flex align-center ga-3">
          Output
          <v-chip
            v-if="submissionStatus"
            :color="submissionStatusColor(submissionStatus)"
            size="small"
            variant="tonal"
          >
            {{ submissionStatusLabel(submissionStatus) }}
          </v-chip>
        </v-card-title>
        <v-card-text>{{ output }}</v-card-text>
      </v-card>
    </template>
  </v-container>
</template>

<style scoped></style>
