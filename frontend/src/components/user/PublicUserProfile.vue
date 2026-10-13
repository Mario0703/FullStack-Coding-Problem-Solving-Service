<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getPublicUserProfile } from '@/utils/Users/api'
import ErrorFeedback from '@/components/FeedBack/Error.vue'
import type { PublicUser } from '@/utils/Users/types'
import FadeContent from '@/components/ui/FadeContent.vue'

const props = defineProps<{
  userId: string
}>()

const publicUser = ref<PublicUser | null>(null)
const isLoading = ref(true)
const loadError = ref('')

onMounted(async () => {
  loadError.value = ''
  try {
    publicUser.value = await getPublicUserProfile(props.userId)
  } catch (err) {
    loadError.value = err instanceof Error ? err.message : 'Failed to load public user profile.'
  } finally {
    isLoading.value = false
  }
})
</script>

<template>
  <FadeContent
    :blur="true"
    :duration="1000"
    :initial-opacity="0"
    ease="power2.out"
  >
    <v-container class="d-flex align-center justify-center py-8">
      <ErrorFeedback v-if="loadError" :message="loadError" class="w-100 mb-4" style="max-width: 960px" />

      <v-container v-else-if="isLoading" class="d-flex justify-center pa-4">
        <v-progress-circular indeterminate color="primary" />
      </v-container>

      <v-card v-else-if="publicUser" color="grey-lighten-2" class="w-100" max-width="960" variant="tonal">
        <v-card-title class="text-h4 font-weight-bold text-center pa-8 pb-2">
          <v-icon icon="mdi-account-circle" class="mr-2" />
          User Profile
        </v-card-title>

        <v-card-text class="pa-8 pt-2">
          <div class="d-flex flex-column align-center ga-3 mb-8">
            <v-avatar size="120" color="primary" variant="tonal">
              <v-icon icon="mdi-account-circle" size="80" />
            </v-avatar>
            <div class="text-h4 font-weight-bold">{{ publicUser.userName }}</div>
          </div>

          <v-row align="stretch" class="ma-n2">
            <v-col cols="12" sm="6" class="d-flex pa-2">
              <v-card color="grey-lighten-1" class="w-100 h-100 d-flex flex-column" variant="outlined">
                <v-card-title class="text-subtitle-1 font-weight-bold text-center">
                  <v-icon icon="mdi-trophy" class="mr-2 text-amber-darken-2" />
                  Score
                </v-card-title>
                <v-card-text class="text-h3 text-center flex-grow-1 py-6">
                  {{ publicUser.score }}
                </v-card-text>
              </v-card>
            </v-col>

            <v-col cols="12" sm="6" class="d-flex pa-2">
              <v-card color="grey-lighten-1" class="w-100 h-100 d-flex flex-column" variant="outlined">
                <v-card-title class="text-subtitle-1 font-weight-bold text-center">
                  <v-icon icon="mdi-format-list-checks" class="mr-2" />
                  Total challenges solved
                </v-card-title>
                <v-card-text class="text-h3 text-center flex-grow-1 py-6">
                  {{ publicUser.totalChallengesSolved }}
                </v-card-text>
              </v-card>
            </v-col>
          </v-row>

          <v-row align="stretch" class="ma-n2">
            <v-col cols="12" sm="4" class="d-flex pa-2">
              <v-card color="success" class="w-200 h-100 d-flex flex-column" variant="tonal">
                <v-card-title class="text-subtitle-1 font-weight-bold text-center">
                  <v-icon icon="mdi-leaf" class="mr-2 text-success" />
                  Easy challenges solved
                </v-card-title>
                <v-card-text class="text-h3 text-center flex-grow-1 py-6 text-success">
                  {{ publicUser.numberOfEasyChallengesSolved }}
                </v-card-text>
              </v-card>
            </v-col>

            <v-col cols="12" sm="4" class="d-flex pa-2">
              <v-card color="warning" class="w-100 h-100 d-flex flex-column" variant="tonal">
                <v-card-title class="text-subtitle-1 font-weight-bold text-center">
                  <v-icon icon="mdi-fire" class="mr-2 text-warning" />
                  Medium challenges solved
                </v-card-title>
                <v-card-text class="text-h3 text-center flex-grow-1 py-6 text-warning">
                  {{ publicUser.numberOfMediumChallengesSolved }}
                </v-card-text>
              </v-card>
            </v-col>

            <v-col cols="13" sm="4" class="d-flex pa-2">
              <v-card color="error" class="w-100 h-100 d-flex flex-column" variant="tonal">
                <v-card-title class="text-subtitle-1 font-weight-bold text-center">
                  <v-icon icon="mdi-skull" class="mr-2 text-error" />
                  Hard challenges solved
                </v-card-title>
                <v-card-text class="text-h3 text-center flex-grow-1 py-6 text-error">
                  {{ publicUser.numberOfHardChallengesSolved }}
                </v-card-text>
              </v-card>
            </v-col>
          </v-row>
        </v-card-text>
      </v-card>
    </v-container>
  </FadeContent>
</template>
