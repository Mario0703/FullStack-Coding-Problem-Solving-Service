<script setup lang="ts">

import {ref} from "vue";

const name = ref("");
const email = ref("");
const confirmEmail = ref("");
const password = ref("");
const confirmPassword = ref("");

type UserData = {
  userName: string;
  email: string;
  password: string;
};


async function sendUserData() {
  const userData: UserData = {
    userName: name.value,
    email: email.value,
    password: password.value,
  }

  const response = await fetch('http://localhost:8082/api/user/signup', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(userData),
  })

  if (!response.ok) {
    throw new Error('The API failed to send the data')
  }

  const savedUser = await response.json()
  console.log(savedUser)
}

function confirm_user_input(typed_input: string, confirmed_typed_input: string): boolean {
  const is_typed_input_equal = typed_input === confirmed_typed_input;
  const is_typed_input_not_empty = typed_input.length > 0 && confirmed_typed_input.length > 0;

  if (is_typed_input_equal && is_typed_input_not_empty) {
    return true;
  } else {
    return false;
  }
}




</script>

<template>
  <v-container style="display: flex; align-items: center; justify-content: center;">
    <v-form class="signup-form" @submit.prevent="sendUserData">
      <h1>Create your account</h1>
      <p>Sign up to start solving coding challenges.</p>

      <v-text-field
        v-model="name"
        label="Name"
        name="name"
        autocomplete="name"
        required
      />

      <v-text-field
        v-model="email"
        label="Email"
        name="email"
        type="email"
        autocomplete="email"
        required
      />

      <v-text-field
        v-model="confirmEmail"
        label="Confirm email"
        name="confirm-email"
        type="email"
        autocomplete="email"
        required
      />

      <p v-if="confirm_user_input(email,confirmEmail)"> Emails must be equal to eachother.</p>

      <v-text-field
        v-model="password"
        label="Password"
        name="password"
        type="password"
        autocomplete="new-password"
        required
      />

      <p>Passwords must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one number.</p>
      <p v-if="confirm_user_input(password,confirmPassword)">Passwords must be equal to eachother.</p>

      <v-text-field
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
<style scoped>

</style>
