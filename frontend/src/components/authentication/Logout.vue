<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { clearCurrentUser } from '@/authenticator/auth'
import ErrorFeedback from '@/components/FeedBack/Error.vue'

defineOptions({
  name: 'UserLogout',
})

const router = useRouter()
const logoutError = ref('')

async function logout() {
  logoutError.value = ''

  try {
    const response = await fetch('http://localhost:8082/api/user/logout', {
      method: 'POST',
      credentials: 'include',
    })

    if (!response.ok) {
      throw new Error('Logout failed')
    }

    clearCurrentUser()
    await router.push({ name: 'login' })
  } catch {
    logoutError.value = 'Unable to log out. Please try again.'
  }
}
</script>

<template>
  <ErrorFeedback v-if="logoutError" :message="logoutError" />
  <v-btn color="red" @click="logout">Logout</v-btn>
</template>
