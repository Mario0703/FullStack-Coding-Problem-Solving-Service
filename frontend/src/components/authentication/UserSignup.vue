<script setup lang="ts">

import {ref} from "vue";
import ErrorFeedback from '@/components/FeedBack/Error.vue'
import router from "@/router";
import {
  getSignupValidationError,
  inputsMatch,
  isValidEmail,
  isValidPassword,
} from '@/utils/signupValidation'
import { baseUrl } from '@/utils/api'
import type { SignupData } from '@/utils/signupValidation'

const userName = ref("");
const email = ref("");
const confirmEmail = ref("");
const password = ref("");
const confirmPassword = ref("");
const signupError = ref("");


async function sendUserData() {

  signupError.value = ''

  if (!canSubmit()) return

  const userData: SignupData = {
    userName: userName.value,
    email: email.value,
    password: password.value,
  }

  try {
    const response = await fetch(`${baseUrl}/api/user/signup`, {

      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(userData),
    })

    if (!response.ok) {
      signupError.value = 'We could not create your account. Please try again.'
      return
    }

    await response.json()
    router.push({ name: 'confirmSignup' })
  } catch {
    signupError.value = 'Unable to reach the server. Please try again later.'
  }
}

function canSubmit(): boolean {
  const validationError = getSignupValidationError({
    userName: userName.value,
    email: email.value,
    confirmEmail: confirmEmail.value,
    password: password.value,
    confirmPassword: confirmPassword.value,
  })

  if (validationError) {
    signupError.value = validationError
    return false
  }

  return true
}


</script>

<template>
  <v-container class="d-flex align-center justify-center">
    <v-form class="signup-form" @submit.prevent="sendUserData">
      <v-card-title class="text-h5 font-weight-bold px-0">Create your account</v-card-title>
      <v-card-subtitle class="px-0">Sign up to start solving coding challenges.</v-card-subtitle>

      <ErrorFeedback
        v-if="signupError"
        title="Signup failed"
        :message="signupError"
        class="mb-4"
      />

      <v-text-field class="mt-4"
        v-model="userName"
        label="username"
        name="name"
        autocomplete="name"
        required
      />

      <v-text-field class="mt-4"
        v-model="email"
        label="Email"
        name="email"
        type="email"
        autocomplete="email"
        required
      />

      <ErrorFeedback
      v-if="email && !isValidEmail(email)"
        title="Invalid email"
        message="Enter a valid email address."
        class="mt-2"
      />

      <v-text-field class="mt-4"
        v-model="confirmEmail"
        label="Confirm email"
        name="confirm-email"
        type="email"
        autocomplete="email"
        required
      />

      <ErrorFeedback title="Email mismatch" message="Email addresses must match." class="mt-2" v-if="confirmEmail && !inputsMatch(email, confirmEmail)" />


      <v-text-field class="mt-4"
        v-model="password"
        label="Password"
        name="password"
        type="password"
        autocomplete="new-password"
        required
      />

      <ErrorFeedback title="Invalid password" message="Passwords must be at least 8 characters long and contain an uppercase letter,
        lowercase letter, and number." v-if="password && !isValidPassword(password)">
      </ErrorFeedback>

      <ErrorFeedback
        message="Passwords must match."
        title="Passwords don't match"
        class="mt-2 mb-2"
        v-if="confirmPassword && !inputsMatch(password, confirmPassword)">
      </ErrorFeedback>

      <v-text-field class="mt-4"
        v-model="confirmPassword"
        label="Confirm password"
        name="confirm-password"
        type="password"
        autocomplete="new-password"
        required
      />

      <v-btn type="submit" color="primary">Sign up</v-btn>
    </v-form>
  </v-container>
</template>
