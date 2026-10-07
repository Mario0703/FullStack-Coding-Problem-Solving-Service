<script setup lang="ts">
import {computed, ref} from 'vue'
import { greetings, responseFormatter } from './AIClient'
import ErrorFeedback from '@/components/FeedBack/Error.vue'
const userInput = ref<string>('')
const isAsking = ref(false)
const aiResponse = ref<string>('')
const hasError = ref('')

async function askAI(){
  isAsking.value = true
  try{
    aiResponse.value = await responseFormatter(userInput.value)
    isAsking.value = false
    return aiResponse.value
  } catch (error) {
    if(error instanceof Error) {
      hasError.value = error.message
    }
    hasError.value = 'An unknown error occurred'
    isAsking.value = false
    throw error
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

        <ErrorFeedback message="{{ hasError }}" v-if="hasError" />
        <v-card v-if="aiResponse" variant="tonal" class="mt-4">
          <v-card-text class="text-body-2" style="white-space: pre-wrap">{{
            aiResponse
          }}</v-card-text>
        </v-card>
        <v-textarea
          v-model="userInput"1
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
