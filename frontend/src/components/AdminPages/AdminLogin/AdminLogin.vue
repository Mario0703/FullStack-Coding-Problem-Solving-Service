<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { loadCurrentAdmin } from '@/authenticator/auth'
import LoginPage from '@/components/authentication/LoginPage.vue'
import { loginAsAdmin } from '@/utils/loginStrategies'

const router = useRouter()
const adminUserName = ref('')
const adminPassword = ref('')
const loginError = ref('')
const isSubmitting = ref(false)

async function adminSignIn() {
  loginError.value = ''

  const hasAdminFilledOutFields = adminUserName.value.trim() && adminPassword.value

  if (!hasAdminFilledOutFields) {
    loginError.value = 'Admin username and password are required.'
    return
  }

  isSubmitting.value = true

  try {
    await loginAsAdmin({ identifier: adminUserName.value.trim(), password: adminPassword.value })
    await loadCurrentAdmin()
    await router.push({ name: 'adminLoginConfirmed' })
  } catch (error) {
    loginError.value = error instanceof Error
      ? error.message
      : 'Unable to reach the server. Please try again later.'
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
    :width="300"
  />
</template>
