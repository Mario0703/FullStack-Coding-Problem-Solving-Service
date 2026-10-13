<script setup lang="ts">
import { computed, ref } from "vue";
import { useRoute, useRouter } from "vue-router";
import { resetPassword as resetPasswordApi } from "@/utils/Users/api";
import { validateResetPassword } from "@/utils/Users/validation";
import ErrorFeedback from "@/components/FeedBack/Error.vue";
import SuccessFeedback from "@/components/FeedBack/Succes.vue";
import { readErrorMessage } from "@/utils/apiError";

const route = useRoute();
const router = useRouter();

const token = String(route.query.token ?? "");
const isTokenMissing = computed(() => token === "");

const newPassword = ref("");
const confirmNewPassword = ref("");
const validationError = ref("");
const resetError = ref("");
const resetSuccess = ref("");
const isSubmitting = ref(false);

async function submitResetPassword() {
  validationError.value = "";
  resetError.value = "";
  resetSuccess.value = "";

  const validation = validateResetPassword(newPassword.value, confirmNewPassword.value);
  if (!validation.isValid) {
    validationError.value = validation.error;
    return;
  }

  isSubmitting.value = true;

  try {
    const response = await resetPasswordApi(token, newPassword.value);

    if (!response.ok) {
      resetError.value = await readErrorMessage(response, "Unable to reset the password.");
      return;
    }

    resetSuccess.value = "Your password has been reset. You can now log in with your new password.";
  } catch {
    resetError.value = "Unable to reach the server. Please try again later.";
  } finally {
    isSubmitting.value = false;
  }
}

function requestNewLink() {
  router.push({ name: "forgotPassword" });
}

function returnToLogin() {
  router.push({ name: "login" });
}
</script>

<template>
  <v-container class="d-flex align-center justify-center py-8">
    <v-card color="grey-lighten-2" class="w-100" max-width="480" variant="tonal">
      <v-card-title class="text-h5 font-weight-bold text-center pa-6">
        Reset password
      </v-card-title>

      <v-card-subtitle class="text-body-1 px-6 pb-0">
        Choose a new password for your account.
      </v-card-subtitle>

      <v-card-text class="pa-6 pt-0">

        <ErrorFeedback
          v-if="isTokenMissing"
          message="This reset link is incomplete. Please request a new reset link."
          class="mb-4"
        />
        <ErrorFeedback v-if="validationError" :message="validationError" class="mb-4" />
        <ErrorFeedback v-if="resetError" :message="resetError" class="mb-4" />
        <SuccessFeedback v-if="resetSuccess" :message="resetSuccess" class="mb-4" />

        <v-form v-if="!isTokenMissing" @submit.prevent="submitResetPassword">
          <v-text-field
            v-model="newPassword"
            label="New password"
            type="password"
            autocomplete="new-password"
          />
          <v-text-field
            v-model="confirmNewPassword"
            label="Confirm new password"
            type="password"
            autocomplete="new-password"
          />

          <v-card-actions class="justify-center ga-2 px-0 pt-4">
            <v-btn type="submit" color="primary" :loading="isSubmitting" :disabled="isSubmitting">
              Reset password
            </v-btn>
            <v-btn type="button" variant="text" :disabled="isSubmitting" @click="returnToLogin">
              Back to login
            </v-btn>
          </v-card-actions>
        </v-form>

        <v-card-actions v-else class="justify-center ga-2 px-0">
          <v-btn color="primary" @click="requestNewLink">Request a new link</v-btn>
          <v-btn variant="text" @click="returnToLogin">Back to login</v-btn>
        </v-card-actions>
      </v-card-text>
    </v-card>
  </v-container>
</template>