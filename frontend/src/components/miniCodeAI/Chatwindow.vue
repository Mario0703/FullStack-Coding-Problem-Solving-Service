<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { greetings, responseFormatter } from './AIClient'
import ErrorFeedback from '@/components/FeedBack/Error.vue'

const props = defineProps<{
  /** The challenge being solved, so the AI tools know what to look up. */
  challengeId?: string
  /** The code currently in the editor, passed to the AI as context. */
  code?: string
  /** The language currently selected in the editor. */
  language?: string
}>()

const userInput = ref<string>('')
const isAsking = ref(false)
const hasError = ref('')



const conversation = reactive(new Map<string, string[]>())
conversation.set('user', [])
conversation.set('ai', [])





const messages = computed<{ role: string, text: string }[]>(() => {
  const userMessages = conversation.get('user') || []
  const aiMessages = conversation.get('ai') || []
  const combinedMessages: { role: string; text: string }[] = []
  return combinedMessages.concat(
    userMessages.map((text) => ({ role: 'user', text })),
    aiMessages.map((text) => ({ role: 'ai', text }))
  )
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
  const question = userInput.value
  conversation.get('user')?.push(question)
  userInput.value = ''

  try {
    const answer = await responseFormatter(question, {
      challengeId: props.challengeId,
      currentCode: props.code,
      currentLanguage: props.language,
    })
    conversation.get('ai')?.push(answer)
  } catch (error) {
    hasError.value =
      error instanceof Error ? error.message : 'An unknown error occurred'
  } finally {
    isAsking.value = false
  }
}
</script>

<template>
  <v-container>
    <v-card>
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
            variant="flat"
          >
            <v-card-text class="text-body-2" style="white-space: pre-wrap">{{ message.text }}</v-card-text>
          </v-card>
        </div>

        <v-textarea
          v-model="userInput"
          label="Ask the AI"
          placeholder="Type your question here..."
          rows="4"
          variant="outlined"
          hide-details="auto"
        ></v-textarea>
        <v-btn color="primary" class="mt-2" :loading="isAsking" @click="askAI"> Submit </v-btn>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<style scoped></style>
