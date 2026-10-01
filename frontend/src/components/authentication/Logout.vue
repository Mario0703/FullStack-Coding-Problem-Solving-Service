<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { clearCurrentUser } from '@/authenticator/auth'
import ErrorFeedback from '@/components/FeedBack/Error.vue'
import { baseUrl } from '@/utils/api'


const router = useRouter()
const logoutError = ref('')

async function logout() {
  logoutError.value = ''

  try {
    const response = await fetch(`${baseUrl}/api/logout`, {
      method: 'POST',
      credentials: 'include',
    })

    if (!response.ok) {
      logoutError.value = 'The server could not log you out. Please try again later.'
      return
    }

    clearCurrentUser()
    await router.push({ name: 'home' })
  } catch {
    logoutError.value = 'Unable to log out. Please try again.'
  }
}
</script>

<template>
  <div class="logout-control">
    <ErrorFeedback v-if="logoutError" :message="logoutError" />
    <v-btn color="red" @click="logout">Logout</v-btn>
  </div>
</template>
