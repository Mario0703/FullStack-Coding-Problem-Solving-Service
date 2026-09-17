<script setup lang="ts">

import {ref} from "vue";

const name = ref("");
const email = ref("");
const password = ref("");

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

</script>

<template>
    <form @submit.prevent="sendUserData">
      <h1>Create your account</h1>
      <p>Sign up to start solving coding challenges.</p>

      <label for="name">Name</label>
      <input v-model="name" id="name" name="name" type="text" autocomplete="name" required />

      <label for="email">Email</label>
      <input v-model="email" id="email" name="email" type="email" autocomplete="email" required/>

      <label for="password">Password</label>
      <input
        id="password"
        name="password"
        type="password"
        autocomplete="new-password"
        v-model="password"
        required
      />

      <button type="submit">Sign up</button>
      </form>
</template>
