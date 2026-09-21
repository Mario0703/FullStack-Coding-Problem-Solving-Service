<script setup lang="ts">
import { computed, ref } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const userName = ref("");
const password = ref("");
const loginError = ref("");

const passwordIsNotEmpty = computed(() => password.value.length > 0);
const userNameIsNotEmpty = computed(() => userName.value.length > 0);

type UserData = {
  userName: string;
  password: string;
};

type SessionUser = {
  userId: string;
  userName: string;
  email: string;
};

async function LogIn() {
  loginError.value = "";

  const userData: UserData = {
    userName: userName.value,
    password: password.value,
  };

  if (!userNameIsNotEmpty.value) {
    loginError.value = "Username is required";
  }

  if (!passwordIsNotEmpty.value) {
    loginError.value = "Password is required";
  }

  if (!userNameIsNotEmpty.value || !passwordIsNotEmpty.value) {
    return;
  }

  const response = await fetch("http://localhost:8082/api/user/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(userData),
  });

  if (!response.ok) {
    loginError.value = "Invalid username or password";
    return;
  }

  const loggedInUser = (await response.json()) as SessionUser;
  sessionStorage.setItem("loggedInUser", JSON.stringify(loggedInUser));
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
      <v-text-field v-model="userName" label="Username" placeholder="Type your username" />
      <v-text-field
        v-model="password"
        label="Password"
        placeholder="Type your password"
        type="password"
      />
      <v-btn type="submit" color="primary">Login</v-btn>
    </v-form>
  </v-container>
</template>

<style scoped>
</style>
