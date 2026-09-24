<script setup lang="ts">

import {ref} from "vue";
import router from "@/router";
import {
  hasValidSignupDetails,
  inputsMatch,
  isValidEmail,
  isValidPassword,
} from '@/utils/UserInputCheck'

const userName = ref("");
const email = ref("");
const confirmEmail = ref("");
const password = ref("");
const confirmPassword = ref("");
const signupError = ref("");

type UserData = {
  userName: string;
  email: string;
  password: string;
};


async function sendUserData() {

  signupError.value = ''

  if (!canSubmit()) return

  const userData: UserData = {
    userName: userName.value,
    email: email.value,
    password: password.value,
  }

  try {
    const response = await fetch('http://localhost:8082/api/user/signup', {

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
  const isValid = hasValidSignupDetails({
    userName: userName.value,
    email: email.value,
    confirmEmail: confirmEmail.value,
    password: password.value,
    confirmPassword: confirmPassword.value,
  })

  if (!isValid) {
    alert('You have invalid fields. Please check your inputs and try again.')
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

      <v-alert
        v-if="signupError"
        title="Signup failed"
        :text="signupError"
        type="error"
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

      <v-alert
      v-if="email && !isValidEmail(email)"
        title="Invalid email"
        text="Enter a valid email address."
        type="error"
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

      <v-alert title="Email mismatch" text="Email addresses must match." type="error" class="mt-2" v-if="confirmEmail && !inputsMatch(email, confirmEmail)">
      </v-alert>


      <v-text-field class="mt-4"
        v-model="password"
        label="Password"
        name="password"
        type="password"
        autocomplete="new-password"
        required
      />

      <v-alert title="Invalid password" text="  Passwords must be at least 8 characters long and contain an uppercase letter,
        lowercase letter, and number." type ="error" v-if="password && !isValidPassword(password)">

      </v-alert>

      <v-alert
        text="Passwords must match."
        title="Passwords don't match" type="error"
        class="mt-2 mb-2"
        v-if="confirmPassword && !inputsMatch(password, confirmPassword)">
      </v-alert>

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
