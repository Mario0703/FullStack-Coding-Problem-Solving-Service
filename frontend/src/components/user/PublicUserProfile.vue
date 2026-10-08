<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getPublicUserProfile } from '@/utils/Users/api'
import type { PublicUser } from '@/utils/Users/types'

const props = defineProps<{
  userId: string
}>()

const publicUser = ref<PublicUser | null>(null)
const error = ref('')

onMounted(async () => {
  try {
    publicUser.value = await getPublicUserProfile(props.userId)
  } catch (err) {
    error.value = err instanceof Error ? err.message : 'Failed to load public user profile.'
  }
})
</script>

<template>

    <v-container v-if="error">
      <v-card>
        <v-text-field :model-value="error" readonly hide-details variant="outlined" />

      </v-card>
    </v-container>

    <v-container v-else-if="publicUser">
      <v-card>
        <v-card-title>User Profile</v-card-title>
        <v-card-text>{{ publicUser.userName }}</v-card-text>
      <v-card-text>Score: {{ publicUser.score }}</v-card-text>
      <v-card-text>Challenges Solved: {{ publicUser.totalChallengesSolved }}</v-card-text>

      <v-list>
        <v-list-item>
          <v-list-item-title>
            Easy Challenges Solved: {{ publicUser.numberOfEasyChallengesSolved }}
          </v-list-item-title>
        </v-list-item>
        <v-list-item>
          <v-list-item-title>
            Medium Challenges Solved: {{ publicUser.numberOfMediumChallengesSolved }}
          </v-list-item-title>
        </v-list-item>
        <v-list-item>
          <v-list-item-title>
            Hard Challenges Solved: {{ publicUser.numberOfHardChallengesSolved }}
          </v-list-item-title>
        </v-list-item>
      </v-list>
      </v-card>
    </v-container>
</template>
