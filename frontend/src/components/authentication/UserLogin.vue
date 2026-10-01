<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import type { SubmitEventPromise } from 'vuetify'
import { currentUser, type UserData } from '@/authenticator/auth'
import { getLoginValidationErrors } from '@/utils/UserInputCheck'
import LoginPage from '@/components/authentication/LoginPage.vue'
import { baseUrl } from '@/utils/api'

const router = useRouter()
const name = ref('')
const password = ref('')
const loginWithEmail = ref(false)
const loginError = ref('')
const isSubmitting = ref(false)

async function login(_event: SubmitEventPromise) {
  loginError.value = ''

  const validationErrors = getLoginValidationErrors({
    identifier: name.value,
    password: password.value,
    identifierType: loginWithEmail.value ? 'email' : 'username',
  })

  if (validationErrors.length > 0) {
    loginError.value = validationErrors.join(' ')
    return
  }

  const loginData = {
    password: password.value,
    ...(loginWithEmail.value
      ? { email: name.value.trim() }
      : { userName: name.value.trim() }),
  }

  isSubmitting.value = true

  try {
    const response = await fetch(`${baseUrl}/api/user/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify(loginData),
    })

    if (response.status === 400 || response.status === 401) {
      loginError.value = 'Invalid email/username or password.'
      return
    }

    if (!response.ok) {
      loginError.value = 'The server could not log you in. Please try again later.'
      return
    }

    currentUser.value = (await response.json()) as UserData
    await router.push({ name: 'loginConfirmed' })
  } catch {
    loginError.value = 'Unable to reach the server. Please try again later.'
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <LoginPage
    login-title="Login to your account"
    :login-error="loginError"
    :login="login"
    :is-submitting="isSubmitting"
    v-model:login-with-email="loginWithEmail"
    v-model:name="name"
    v-model:password="password"
  />
</template>
