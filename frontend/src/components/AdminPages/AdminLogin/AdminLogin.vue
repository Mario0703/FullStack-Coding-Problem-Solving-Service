<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import type { SubmitEventPromise } from 'vuetify'
import { loadCurrentAdmin } from '@/authenticator/auth'
import LoginPage from '@/components/authentication/LoginPage.vue'
import { loginAdmin, type AdminLoginCredentials } from '@/utils/AdminLoginUtils'

const router = useRouter()
const adminUserName = ref('')
const adminPassword = ref('')
const loginError = ref('')
const isSubmitting = ref(false)

async function adminSignIn(_event: SubmitEventPromise) {
  loginError.value = ''

  const adminLoginRequest: AdminLoginCredentials = {
    adminUserName: adminUserName.value.trim(),
    adminPassword: adminPassword.value,
  }

  const hasAdminFilledOutFields = adminLoginRequest.adminUserName && adminLoginRequest.adminPassword

  if (!hasAdminFilledOutFields) {
    loginError.value = 'Admin username and password are required.'
    return
  }

  isSubmitting.value = true

  try {
    const response = await loginAdmin(adminLoginRequest)
    const isAdminFieldsValid = response.status === 400 || response.status === 401

    if (isAdminFieldsValid) {
      loginError.value = 'Invalid admin username or password.'
      return
    }

    if (!response.ok) {
      loginError.value = 'The server could not log you in. Please try again later.'
      return
    }

    await loadCurrentAdmin()
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
    :width="300"
  />
</template>
