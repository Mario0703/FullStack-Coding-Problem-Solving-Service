<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getPublicUserProfile } from '@/utils/Users/api'
import ErrorFeedback from '@/components/FeedBack/Error.vue'
import type { PublicUser } from '@/utils/Users/types'

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
  <v-container class="d-flex align-center justify-center py-8">
    <ErrorFeedback v-if="loadError" :message="loadError" class="w-100 mb-4" style="max-width: 720px" />

    <v-container v-else-if="isLoading" class="d-flex justify-center pa-4">
      <v-progress-circular indeterminate color="primary" />
    </v-container>

    <v-card v-else-if="publicUser" class="w-200" max-width="720" border="primary" variant="outlined">
      <v-card-title class="text-h5 font-weight-bold text-center pa-6 pb-2">
        <v-icon icon="mdi-account-circle" class="mr-2" />
        User Profile
      </v-card-title>

      <v-card-text class="pa-6 pt-2">
        <div class="d-flex flex-column align-center ga-2 mb-6">
          <v-avatar size="96" color="primary" variant="tonal">
            <v-icon icon="mdi-account-circle" size="64" />
          </v-avatar>
          <div class="text-h5 font-weight-bold">{{ publicUser.userName }}</div>
        </div>

        <v-row align="stretch" class="ma-n2">
          <v-col cols="12" sm="6" class="d-flex pa-2">
            <v-card class="w-100 h-100 d-flex flex-column" variant="outlined" border="primary">
              <v-card-title class="text-subtitle-1 font-weight-bold text-center">
                <v-icon icon="mdi-trophy" class="mr-2 text-amber-darken-2" />
                Score
              </v-card-title>
              <v-card-text class="text-h4 text-center flex-grow-1">
                {{ publicUser.score }}
              </v-card-text>
            </v-card>
          </v-col>

          <v-col cols="12" sm="6" class="d-flex pa-2">
            <v-card class="w-100 h-100 d-flex flex-column" variant="outlined" border="primary">
              <v-card-title class="text-subtitle-1 font-weight-bold text-center">
                <v-icon icon="mdi-format-list-checks" class="mr-2" />
                Total challenges solved
              </v-card-title>
              <v-card-text class="text-h4 text-center flex-grow-1">
                {{ publicUser.totalChallengesSolved }}
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>

        <v-row align="stretch" class="ma-n2">
          <v-col cols="12" sm="4" class="d-flex pa-2">
            <v-card class="w-100 h-100 d-flex flex-column" variant="outlined" border="success">
              <v-card-title class="text-subtitle-1 font-weight-bold text-center">
                <v-icon icon="mdi-leaf" class="mr-2 text-success" />
                Easy challenges solved
              </v-card-title>
              <v-card-text class="text-h4 text-center flex-grow-1 text-success">
                {{ publicUser.numberOfEasyChallengesSolved }}
              </v-card-text>
            </v-card>
          </v-col>

          <v-col cols="12" sm="4" class="d-flex pa-2">
            <v-card class="w-100 h-100 d-flex flex-column" variant="outlined" border="warning">
              <v-card-title class="text-subtitle-1 font-weight-bold text-center">
                <v-icon icon="mdi-fire" class="mr-2 text-warning" />
                Medium challenges solved
              </v-card-title>
              <v-card-text class="text-h4 text-center flex-grow-1 text-warning">
                {{ publicUser.numberOfMediumChallengesSolved }}
              </v-card-text>
            </v-card>
          </v-col>

          <v-col cols="12" sm="4" class="d-flex pa-2">
            <v-card class="w-100 h-100 d-flex flex-column" variant="outlined" border="error">
              <v-card-title class="text-subtitle-1 font-weight-bold text-center">
                <v-icon icon="mdi-skull" class="mr-2 text-error" />
                Hard challenges solved
              </v-card-title>
              <v-card-text class="text-h4 text-center flex-grow-1 text-error">
                {{ publicUser.numberOfHardChallengesSolved }}
              </v-card-text>
            </v-card>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </v-container>
</template>
