<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { baseUrl } from '@/utils/api'

export type PublicUser = {
  userId: string
  userName: string
  email: string
}

const props = defineProps<{
  userId: string
}>()

const publicUser = ref<PublicUser | null>(null)
const error = ref('')

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
  <div class="public-user-profile">
    <p v-if="error">{{ error }}</p>
    <template v-else-if="publicUser">
      <h2>{{ publicUser.userName }}</h2>
      <p>{{ publicUser.email }}</p>
    </template>
  </div>
</template>
