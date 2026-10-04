<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { baseUrl } from '@/utils/api'

export type PublicUser = {
  userId: string
  userName: string
  email: string
  score: number
  numberOfEasyChallengesSolved: number
  numberOfMediumChallengesSolved: number
  numberOfHardChallengesSolved: number
}

const props = defineProps<{
  userId: string
}>()

const publicUser = ref<PublicUser | null>(null)
const error = ref('')

const numberOfChallengesSolved = computed(() => {
  if (!publicUser.value) {
    return 0
  }

  return (
    publicUser.value.numberOfEasyChallengesSolved +
    publicUser.value.numberOfMediumChallengesSolved +
    publicUser.value.numberOfHardChallengesSolved
  )
})

async function getPublicUserProfileData(userId: string): Promise<PublicUser> {
  if (!userId) {
    throw new Error('UserId is required to fetch public user profile data.')
  }

  const response = await fetch(`${baseUrl}/api/public-profile/${userId}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  })

  if (!response.ok) {
    throw new Error(`Failed to fetch public user profile data: ${response.statusText}`)
  }

  const data = await response.json()

  if (data === null) {
    throw new Error('Public user profile data is null.')
  }

  return data as PublicUser
}

onMounted(async () => {
  try {
    publicUser.value = await getPublicUserProfileData(props.userId)
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
      <v-card-text>Challenges Solved: {{ numberOfChallengesSolved }}</v-card-text>

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
