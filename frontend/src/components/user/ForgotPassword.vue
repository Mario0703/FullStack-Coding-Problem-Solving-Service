<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";
import { requestPasswordReset } from "@/utils/Users/api";
import { validateForgotPassword } from "@/utils/Users/validation";
import ErrorFeedback from "@/components/FeedBack/Error.vue";
import SuccessFeedback from "@/components/FeedBack/Succes.vue";
import { readErrorMessage } from "@/utils/apiError";
import FadeContent from "@/components/ui/FadeContent.vue";

const router = useRouter();
const email = ref("");
const validationError = ref("");
const requestError = ref("");
const successMessage = ref("");
const isSubmitting = ref(false);

async function submitForgotPassword() {
  validationError.value = "";
  requestError.value = "";
  successMessage.value = "";

  const validation = validateForgotPassword(email.value);
  if (!validation.isValid) {
    validationError.value = validation.error;
    return;
  }

  isSubmitting.value = true;

  try {
    const response = await requestPasswordReset(email.value);

    if (!response.ok) {
      requestError.value = await readErrorMessage(response, "Unable to request the password reset.");
      return;
    }

    successMessage.value =
      "If the email is registered, a password reset link has been sent to it. The link expires in 5 minutes.";
  } catch {
    requestError.value = "Unable to reach the server. Please try again later.";
  } finally {
    isSubmitting.value = false;
  }
}

function returnToLogin() {
  router.push({ name: "login" });
}
</script>

<template>
  <FadeContent
    :blur="true"
    :duration="1000"
    :initial-opacity="0"
    ease="power2.out"
  >
    <v-container class="d-flex align-center justify-center py-8">
      <v-card color="grey-lighten-2" class="w-100" max-width="480" variant="tonal">
        <v-card-title class="text-h5 font-weight-bold text-center pa-6">
          Forgot password
        </v-card-title>

        <v-card-subtitle class="text-body-1 px-6 pb-0">
          Enter your email address to request a password reset.
        </v-card-subtitle>

        <v-card-text class="pa-6 pt-0">

          <ErrorFeedback v-if="validationError" :message="validationError" class="mb-4" />
          <ErrorFeedback v-if="requestError" :message="requestError" class="mb-4" />
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
              <v-btn type="submit" color="grey-darken-4" variant="elevated" :loading="isSubmitting" :disabled="isSubmitting">
                Request reset
              </v-btn>
              <v-btn type="button" variant="text" color="grey-darken-4" :disabled="isSubmitting" @click="returnToLogin">
                Back to login
              </v-btn>
            </v-card-actions>
          </v-form>
        </v-card-text>
      </v-card>
    </v-container>
  </FadeContent>
</template>