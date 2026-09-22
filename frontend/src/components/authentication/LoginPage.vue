<script setup lang="ts">
import { computed, ref } from "vue";
import { useRouter } from "vue-router";
import { currentUser, type UserData } from "@/authenticator/auth";
import { getLoginValidationErrors } from "@/utils/UserInputCheck";

const router = useRouter();
const email = ref("");
const userName = ref("");
const password = ref("");
const loginError = ref("");
const isSubmitting = ref(false);
const loginWithEmail = ref(false);

const loginWithUsername = computed(() => !loginWithEmail.value);

type LoginData = {
  email?: string;
  userName?: string;
  password: string;
};

async function LogIn() {
  loginError.value = "";
  const validationErrors = getLoginValidationErrors({
    identifier: loginWithEmail.value ? email.value : userName.value,
    password: password.value,
    identifierType: loginWithEmail.value ? "email" : "username",
  });

  if (validationErrors.length > 0) {
    loginError.value = validationErrors.join(" ");
    return;
  }

  const loginData: LoginData = {
    password: password.value,
    ...(loginWithEmail.value
      ? { email: email.value.trim() }
      : { userName: userName.value.trim() }),
  };

  isSubmitting.value = true;

  try {
    const response = await fetch("http://localhost:8082/api/user/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify(loginData),
    });

    if (response.status === 400 || response.status === 401) {
      loginError.value = "Invalid email/username or password.";
      return;
    }

    if (!response.ok) {
      loginError.value = "The server could not log you in. Please try again later.";
      return;
    }

    const loggedInUser = (await response.json()) as UserData;
    currentUser.value = loggedInUser;
    await router.push({ name: "loginConfirmed" });
  } catch {
    loginError.value = "Unable to reach the server. Please try again later.";
  } finally {
    isSubmitting.value = false;
  }
}
</script>

<template>
  <v-container class="d-flex align-center justify-center">
    <v-alert
      v-if="loginError"
      title="Login failed"
      :text="loginError"
      type="error"
      variant="tonal"
      class="mb-4"
    />

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
      <RouterLink class="text-primary text-decoration-none" :to="{ name: 'forgotPassword' }">
        Forgot password?
      </RouterLink>
      <v-btn
        type="submit"
        color="primary"
        :loading="isSubmitting"
        :disabled="isSubmitting"
      >
        Login
      </v-btn>
    </v-form>
  </v-container>
</template>
