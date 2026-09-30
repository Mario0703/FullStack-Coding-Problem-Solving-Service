<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const logoutError = ref('')

async function logout() {
  logoutError.value = ''

  try {
    const response = await fetch('http://localhost:8082/api/admin/logout', {
      method: 'POST',
      credentials: 'include',
    })

    if (!response.ok) {
      throw new Error('Logout failed')
    }

    await router.push({ name: 'admin login' })
  } catch {
    logoutError.value = 'Unable to log out. Please try again.'
  }
}
</script>

<template>
  <v-alert v-if="logoutError" type="error" :text="logoutError" />
  <v-btn color="red" @click="logout">Logout</v-btn>
</template>
