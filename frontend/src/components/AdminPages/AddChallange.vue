<script setup lang="ts">
import { ref } from 'vue'
import { createChallenge as createChallengeRequest } from '@/utils/Challenges/API/api'
import { isChallengeFormValid } from '@/utils/Challenges/Validation/validation'
import type { CreateChallengeForm } from '@/utils/Challenges/Types/authoringTypes'
import type { Challenge } from '@/utils/Challenges/Types/types'
import ErrorFeedback from '@/components/FeedBack/Error.vue'
import CreateChallengeFormComponent from '@/components/AdminPages/ChallangeCreating/CreateChallengeForm.vue'

const emit = defineEmits<{
  created: [challenge: Challenge]
}>()

const formError = ref('')
const isSubmitting = ref(false)
const challengeForm = ref<InstanceType<typeof CreateChallengeFormComponent> | null>(null)

async function createChallenge(challengeData: CreateChallengeForm) {
  formError.value = ''

  if (!isChallengeFormValid(challengeData)) {
    formError.value =
      'Please complete the solution, template, explanations, runner, and every test case before creating the challenge.'
    return
  }

  isSubmitting.value = true

  try {
    const createdChallenge = await createChallengeRequest(challengeData)
    emit('created', createdChallenge)
    challengeForm.value?.resetForm()
  } catch (error) {
    formError.value = error instanceof Error
      ? error.message
      : 'Unable to reach the server. Please try again later.'
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <v-card color="grey-lighten-2" class="mx-auto" max-width="720" variant="tonal">
    <v-card-title class="text-h5 font-weight-bold pa-6">Add challenge</v-card-title>
    <v-card-text>
      <ErrorFeedback
        v-if="formError"
        title="Challenge creation failed"
        :message="formError"
        variant="tonal"
        class="mb-4"
      />

      <CreateChallengeFormComponent
        ref="challengeForm"
        :is-submitting="isSubmitting"
        @submit="createChallenge"
      />
    </v-card-text>
  </v-card>
</template>
