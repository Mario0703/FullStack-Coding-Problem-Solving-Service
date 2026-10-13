<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { loadCurrentUser } from '@/utils/Users/state'
import { getLoginValidationErrors } from '@/utils/Login/UserInputCheck.ts'
import LoginPage from '@/components/authentication/LoginPage.vue'
import { loginFactory } from '@/utils/Login/LoginFactory.ts'
import FadeContent from '@/components/ui/FadeContent.vue'

const router = useRouter()
const name = ref('')
const password = ref('')
const loginWithEmail = ref(false)
const loginError = ref('')
const isSubmitting = ref(false)

async function login() {
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

  const loginCredentials = { identifier: name.value.trim(), password: password.value }

  isSubmitting.value = true

  try {
    if (loginWithEmail.value) {
      await loginFactory.createUserLoginWithEmailAndPassword(loginCredentials)
    } else {
      await loginFactory.createUserLoginWithUsernameAndPassword(loginCredentials)
    }
    await loadCurrentUser()
    await router.push({ name: 'loginConfirmed' })
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
  <FadeContent
    :blur="true"
    :duration="1000"
    :initial-opacity="0"
    ease="power2.out"
  >
    <LoginPage
      login-title="Login to your account"
      :login-error="loginError"
      :login="login"
      :is-submitting="isSubmitting"
      v-model:login-with-email="loginWithEmail"
      v-model:name="name"
      v-model:password="password"
    />
  </FadeContent>
</template>
