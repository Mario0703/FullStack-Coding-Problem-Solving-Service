<script setup lang="ts">
import { computed, reactive, ref } from 'vue'
import { greetings, responseFormatter } from './AIClient'
import ErrorFeedback from '@/components/FeedBack/Error.vue'

type ChatMessage = {
  role: 'user' | 'ai'
  text: string
}

const userInput = ref<string>('')
const isAsking = ref(false)
const hasError = ref('')



const converSations = reactive(new Map<string, string[]>())
converSations.set('user', [])
converSations.set('ai', [])


const messages = computed<ChatMessage[]>(() => {
  const questions = converSations.get('user') ?? []
  const answers = converSations.get('ai') ?? []
  const transcript: ChatMessage[] = []

  questions.forEach((question, index) => {
    transcript.push({ role: 'user', text: question })
    const answer = answers[index]
    if (answer !== undefined) {
      transcript.push({ role: 'ai', text: answer })
    }
  })

  return transcript
})

async function askAI() {
  if (userInput.value.trim() === '') {
    hasError.value = 'Please enter a question for the AI.'
    return
  }

  hasError.value = ''
  isAsking.value = true
  const question = userInput.value

  try {
    const answer = await responseFormatter(question)
    converSations.get('user')?.push(question)
    converSations.get('ai')?.push(answer)
    userInput.value = ''
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
