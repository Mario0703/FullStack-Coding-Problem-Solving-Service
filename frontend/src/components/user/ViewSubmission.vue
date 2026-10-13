<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import CodeEditor from '@/components/code-execution/CodeEditor.vue'
import ErrorFeedback from '@/components/FeedBack/Error.vue'
import { getSubmissionDetails } from '@/utils/Users/api'
import type { SubmissionDetails } from '@/utils/Users/types'
import {
  languageLabel,
  submissionStatusColor,
  submissionStatusLabel,
} from '@/utils/Challenges/API/execution'

const route = useRoute()
const submission = ref<SubmissionDetails | null>(null)
const errorMessage = ref('')

onMounted(async () => {
  const submissionId = route.params.submissionId

  if (!submissionId || Array.isArray(submissionId)) {
    errorMessage.value = 'Invalid submission ID'
    return
  }

  try {
    submission.value = await getSubmissionDetails(submissionId)
  } catch {
    errorMessage.value = 'Failed to load submission'
  }
})
</script>

<template>
  <v-container>
    <ErrorFeedback v-if="errorMessage" :message="errorMessage" />

    <v-card v-else-if="submission" color="grey-lighten-2" variant="tonal">
      <v-card-title class="text-h5 font-weight-bold pa-6">
        {{ submission.challengeName }}
      </v-card-title>
      <v-card-text class="pa-6 pt-0">
        <v-row dense>
          <v-col cols="12" sm="6">
            <v-card class="h-100" color="blue-lighten-5" variant="tonal">
              <v-card-text>
                <div class="text-caption text-medium-emphasis">Language</div>
                <div class="text-body-1 font-weight-bold">{{ languageLabel(submission.language) }}</div>
              </v-card-text>
            </v-card>
          </v-col>
          <v-col cols="12" sm="6">
            <v-card class="h-100" color="purple-lighten-5" variant="tonal">
              <v-card-text>
                <div class="text-caption text-medium-emphasis">Created at</div>
                <div class="text-body-1 font-weight-bold">{{ submission.createdAt }}</div>
              </v-card-text>
            </v-card>
          </v-col>
          <v-col cols="12" sm="6">
            <v-card class="h-100" color="orange-lighten-5" variant="tonal">
              <v-card-text>
                <div class="text-caption text-medium-emphasis">Exit code</div>
                <div class="text-body-1 font-weight-bold">{{ submission.exitCode }}</div>
              </v-card-text>
            </v-card>
          </v-col>
          <v-col cols="12" sm="6">
            <v-card class="h-100" color="grey-lighten-4" variant="tonal">
              <v-card-text>
                <div class="text-caption text-medium-emphasis">Status</div>
                <v-chip
                  :color="submissionStatusColor(submission.status)"
                  size="small"
                  variant="tonal"
                >
                  {{ submissionStatusLabel(submission.status) }}
                </v-chip>
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>

        <v-card class="mt-6" color="grey-lighten-4" variant="tonal">
          <v-card-title class="text-subtitle-1">Test cases</v-card-title>
          <v-card-text>
            <v-row class="text-center" dense>
              <v-col cols="4">
                <div class="text-h6 font-weight-bold text-success">
                  {{ submission.testCasesPassed }}
                </div>
                <div class="text-caption">Passed</div>
              </v-col>
              <v-col cols="4">
                <div class="text-h6 font-weight-bold text-error">
                  {{ submission.testCasesFailed }}
                </div>
                <div class="text-caption">Failed</div>
              </v-col>
              <v-col cols="4">
                <div class="text-h6 font-weight-bold">{{ submission.totalTestCases }}</div>
                <div class="text-caption">Total</div>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>

        <div class="text-subtitle-1 font-weight-bold mt-6 mb-2">Submitted code</div>
        <v-card color="grey-lighten-4" variant="outlined">
          <CodeEditor
            :model-value="submission.code"
            :language="submission.language"
            readonly
            height="320px"
          />
        </v-card>
      </v-card-text>
    </v-card>
  </v-container>
</template>
