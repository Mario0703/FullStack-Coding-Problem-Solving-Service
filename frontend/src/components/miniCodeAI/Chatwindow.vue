<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { greetings, responseFormatter } from './AIClient'
import ErrorFeedback from '@/components/FeedBack/Error.vue'
import ThoughtLine from '@/components/ui/ThoughtLine.vue'

const props = defineProps<{
  challengeId?: string
  code?: string
  language?: string
}>()

defineOptions({ name: 'MiniHelperChat' })

const userInput = ref<string>('')
const isAsking = ref(false)
const hasError = ref('')

// ThoughtLine trace for the in-flight AI request; appended as the agent works.
const showThoughtLine = ref(false)
const thinkingSteps = ref<string[]>([])

const conversationId = crypto.randomUUID()

const conversation = reactive(new Map<string, string[]>())
conversation.set('user', [])
conversation.set('ai', [])

const messages = computed<{ role: string, text: string }[]>(() => {
  const userMessages = conversation.get('user') || []
  const aiMessages = conversation.get('ai') || []
  const combinedMessages: { role: string; text: string }[] = []

  const length = Math.max(userMessages.length, aiMessages.length)
  for (let index = 0; index < length; index += 1) {
    const userMessage = userMessages[index]
    const aiMessage = aiMessages[index]
    if (userMessage !== undefined) {
      combinedMessages.push({ role: 'user', text: userMessage })
    }
    if (aiMessage !== undefined) {
      combinedMessages.push({ role: 'ai', text: aiMessage })
    }
  }
  return combinedMessages
})

async function askAI() {
  if (userInput.value.trim() === '') {
    hasError.value = 'Please enter a question for the AI.'
    return
  }

  if (!props.challengeId) {
    hasError.value = 'Open a challenge before asking the AI for help.'
    return
  }

  hasError.value = ''
  isAsking.value = true
  showThoughtLine.value = true
  thinkingSteps.value = ['Reading your question']

  const stepTimer = window.setTimeout(() => {
    thinkingSteps.value = [...thinkingSteps.value, 'Studying the challenge and your code']
  }, 1200)

  const question = userInput.value
  conversation.get('user')?.push(question)
  userInput.value = ''

  try {
    const answer = await responseFormatter(question, {
      challengeId: props.challengeId,
      currentCode: props.code,
      currentLanguage: props.language,
      conversationId,
    })
    conversation.get('ai')?.push(answer)
  } catch (error) {
    showThoughtLine.value = false
    hasError.value =
      error instanceof Error ? error.message : 'An unknown error occurred'
  } finally {
    clearTimeout(stepTimer)
    isAsking.value = false
  }
}
</script>

<template>
  <v-container>
    <v-card color="grey-lighten-2" variant="tonal">
      <v-card-title>MiniCode AI helper</v-card-title>
      <v-card-subtitle>Ask this AI for hints for the given challenge</v-card-subtitle>
      <v-card-text>
        <p class="text-body-2 mb-4">{{ greetings[0] }}</p>

        <ErrorFeedback v-if="hasError" :message="hasError" />

        <div v-if="messages.length > 0" class="d-flex flex-column ga-3 mb-4">
          <v-card
            v-for="(message, index) in messages"
            :key="index"
            :color="message.role === 'ai' ? 'green' : 'light-blue'"
            variant="tonal"
          >
            <v-card-text class="text-body-2" style="white-space: pre-wrap">{{ message.text }}</v-card-text>
          </v-card>
        </div>
        <div style="margin: 16px 0;">
          <ThoughtLine
            v-if="showThoughtLine"
            class="mt-3"
            :working="isAsking"
            :steps="thinkingSteps"
          />
        </div>
        <v-textarea
          v-model="userInput"
          label="Ask the AI"
          placeholder="Type your question here..."
          rows="4"
          variant="outlined"
          hide-details="auto"
        ></v-textarea>
        <v-btn color="grey-darken-4" variant="elevated" class="mt-2" :loading="isAsking" @click="askAI"> Submit </v-btn>

      </v-card-text>
    </v-card>
  </v-container>
</template>

<style scoped></style>
