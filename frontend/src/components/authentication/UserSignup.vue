<script setup lang="ts">
import { ref } from "vue";
import ErrorFeedback from "@/components/FeedBack/Error.vue";
import router from "@/router";
import {
  getSignupValidationError,
  inputsMatch,
  isValidEmail,
  isValidPassword,
} from "@/utils/signupValidation";
import { signupUser } from "@/utils/Users/api";
import { readErrorMessage } from "@/utils/apiError";
import type { SignupData } from "@/utils/Users/types";
import VPolicy from "@/components/authentication/V-Policy.vue";

const userName = ref("");
const email = ref("");
const confirmEmail = ref("");
const password = ref("");
const confirmPassword = ref("");
const signupError = ref("");

// The privacy policy must be accepted before an account can be created.
const agreedToPolicy = ref(false);
const showPolicy = ref(false);

async function sendUserData() {
  signupError.value = "";

  if (!agreedToPolicy.value) {
    signupError.value =
      "You must read and agree to the privacy policy before creating an account.";
    showPolicy.value = true;
    return;
  }

  if (!canSubmit()) return;

  const userData: SignupData = {
    userName: userName.value,
    email: email.value,
    password: password.value,
  };

  try {
    const response = await signupUser(userData);

    if (!response.ok) {
      signupError.value = await readErrorMessage(
        response,
        "We could not create your account. Please try again.",
      );
      return;
    }

    await response.json();
    router.push({ name: "confirmSignup" });
  } catch {
    signupError.value = "Unable to reach the server. Please try again later.";
  }
}

function canSubmit(): boolean {
  const validationError = getSignupValidationError({
    userName: userName.value,
    email: email.value,
    confirmEmail: confirmEmail.value,
    password: password.value,
    confirmPassword: confirmPassword.value,
  });

  if (validationError) {
    signupError.value = validationError;
    return false;
  }

  return true;
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

      <!-- Agreement is required before the account can be created. -->
      <v-checkbox
        v-model="agreedToPolicy"
        color="primary"
        density="comfortable"
        hide-details
        class="mt-4"
        :error="!agreedToPolicy && Boolean(signupError)"
      >
        <template #label>
          I have read and agree to the
          <a
            href="#"
            class="text-decoration-underline"
            @click.prevent.stop="showPolicy = true"
          >Privacy Policy</a>
        </template>
      </v-checkbox>
      <div v-if="!agreedToPolicy" class="text-caption text-medium-emphasis mb-2">
        You must agree to the privacy policy to create an account.
      </div>

      <v-btn type="submit" color="grey-darken-4" variant="elevated" :disabled="!agreedToPolicy">Sign up</v-btn>
    </v-form>
  </v-container>

  <v-dialog v-model="showPolicy" max-width="640" scrollable>
    <v-card color="grey-lighten-2" class="pa-4" variant="tonal">
      <VPolicy
        v-model:has-agreed="agreedToPolicy"
        @accept="showPolicy = false"
        @decline="showPolicy = false"
      />
      <v-card-actions>
        <v-spacer />
        <v-btn variant="text" color="grey-darken-4" @click="showPolicy = false">Close</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
