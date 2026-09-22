<script setup lang="ts">
import { computed, ref } from "vue";
import { useRouter } from "vue-router";
import { currentUser, type UserData } from "../authenticator/auth";

const router = useRouter();
const email = ref("");
const userName = ref("");
const password = ref("");
const loginError = ref("");
const loginWithEmail = ref(false);

const loginWithUsername = computed(() => !loginWithEmail.value);
const emailIsNotEmpty = computed(() => email.value.trim().length > 0);
const userNameIsNotEmpty = computed(() => userName.value.trim().length > 0);
const passwordIsNotEmpty = computed(() => password.value.trim().length > 0);

type LoginData = {
  email?: string;
  userName?: string;
  password: string;
};

async function LogIn() {
  loginError.value = "";

  const identifierIsNotEmpty = loginWithEmail.value
    ? emailIsNotEmpty.value
    : userNameIsNotEmpty.value;

  if (!identifierIsNotEmpty) {
    loginError.value = loginWithEmail.value
      ? "Email is required"
      : "Username is required";
  }

  if (!passwordIsNotEmpty.value) {
    loginError.value = "Password is required";
  }

  if (!identifierIsNotEmpty || !passwordIsNotEmpty.value) {
    return;
  }

  const loginData: LoginData = {
    password: password.value,
    ...(loginWithEmail.value
      ? { email: email.value.trim() }
      : { userName: userName.value.trim() }),
  };

  const response = await fetch("http://localhost:8082/api/user/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    credentials: "include",
    body: JSON.stringify(loginData),
  });

  if (!response.ok) {
    loginError.value = "Invalid email/username or password";
    return;
  }

  const loggedInUser = (await response.json()) as UserData;
  currentUser.value = loggedInUser;
  await router.push({ name: "loginConfirmed" });
}
</script>

<template>
  <v-container style="display: flex; align-items: center; justify-content: center;">
    <v-alert v-if="loginError" color="error">
      {{ loginError }}
    </v-alert>

    <v-form class="signup-form" @submit.prevent="LogIn">
      <h1>Login to your account</h1>

      <v-switch
        v-model="loginWithEmail"
        label="Log in with email"
        color="primary"
      />

      <div v-if="loginWithEmail">
        <v-text-field
          v-model="email"
          label="Email"
          placeholder="Type your email"
          type="email"
          autocomplete="email"
        />
      </div>

      <div v-if="loginWithUsername">
        <v-text-field
          v-model="userName"
          label="Username"
          placeholder="Type your username"
          autocomplete="username"
        />
      </div>

      <v-text-field
        v-model="password"
        label="Password"
        placeholder="Type your password"
        type="password"
        autocomplete="current-password"
      />
      <v-btn type="submit" color="primary">Login</v-btn>
    </v-form>
  </v-container>
</template>

<style scoped>
</style>
