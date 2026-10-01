<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { clearCurrentUser, isAdmin } from '@/authenticator/auth'
import ErrorFeedback from '@/components/FeedBack/Error.vue'

const props = withDefaults(
  defineProps<{
    admin?: boolean
  }>(),
  { admin: false },
)

const router = useRouter()
const logoutError = ref('')

async function logout() {
  logoutError.value = ''

  try {
    const response = await fetch('http://localhost:8082/api/logout', {
      method: 'POST',
      credentials: 'include',
    })

    if (!response.ok) {
      throw new Error('Logout failed')
    }

    clearCurrentUser()
    isAdmin.value = false
    await router.push({ name: props.admin ? 'admin login' : 'login' })
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
