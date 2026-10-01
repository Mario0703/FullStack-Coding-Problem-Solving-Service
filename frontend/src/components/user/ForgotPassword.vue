<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";
import { isblank, isValidEmail } from "@/utils/UserInputCheck";
import ErrorFeedback from "@/components/FeedBack/Error.vue";
import SuccessFeedback from "@/components/FeedBack/Succes.vue";

const router = useRouter();
const email = ref("");
const validationError = ref("");
const successMessage = ref("");

function submitForgotPassword() {
  validationError.value = "";
  successMessage.value = "";

  if (isblank(email.value)) {
    validationError.value = "Email cannot be blank.";
    return;
  }

  if (!isValidEmail(email.value)) {
    validationError.value = "Enter a valid email address.";
    return;
  }

  successMessage.value =
    "Your request is ready. Password reset delivery will be connected to the backend later.";
}

function returnToLogin() {
  router.push({ name: "login" });
}
</script>

<template>
  <v-container class="d-flex align-center justify-center py-8">
    <v-card class="w-100" max-width="480" border="primary" variant="outlined">
      <v-card-title class="text-h5 font-weight-bold text-center pa-6">
        Forgot password
      </v-card-title>

      <v-card-subtitle class="text-body-1 px-6 pb-0">
        Enter your email address to request a password reset.
      </v-card-subtitle>

      <v-card-text class="pa-6 pt-0">

        <ErrorFeedback v-if="validationError" :message="validationError" class="mb-4" />
        <SuccessFeedback v-if="successMessage" :message="successMessage" class="mb-4" />

        <v-form @submit.prevent="submitForgotPassword">
          <v-text-field
            v-model="email"
            label="Email"
            placeholder="Type your email"
            type="email"
            autocomplete="email"
          />

          <v-card-actions class="justify-center ga-2 px-0 pt-4">
            <v-btn type="submit" color="primary">Request reset</v-btn>
            <v-btn type="button" variant="text" @click="returnToLogin">Back to login</v-btn>
          </v-card-actions>
        </v-form>
      </v-card-text>
    </v-card>
  </v-container>
</template>
