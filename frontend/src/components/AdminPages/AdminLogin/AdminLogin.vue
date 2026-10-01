<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import type { SubmitEventPromise } from 'vuetify'
import { isAdmin } from '@/authenticator/auth'
import LoginPage from '@/components/authentication/LoginPage.vue'

const router = useRouter()
const adminUserName = ref('')
const adminPassword = ref('')
const loginError = ref('')
const isSubmitting = ref(false)

async function adminSignIn(_event: SubmitEventPromise) {
  loginError.value = ''
  isSubmitting.value = true

  try {
    const response = await fetch('http://localhost:8082/api/admin/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify({
        adminUserName: adminUserName.value.trim(),
        adminPassword: adminPassword.value,
      }),
    })

    if (!response.ok) {
      loginError.value = 'Invalid admin username or password.'
      return
    }

    isAdmin.value = true
    await router.push({ name: 'adminLoginConfirmed' })
  } catch {
    loginError.value = 'Unable to reach the server. Please try again later.'
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <LoginPage
    login-title="Admin login"
    :login-error="loginError"
    :is-submitting="isSubmitting"
    :login="adminSignIn"
    :allow-email-login="false"
    v-model:name="adminUserName"
    v-model:password="adminPassword"
  />
</template>
