<script setup lang="ts">
import { ref } from "vue";
import { inputsMatch, isblank, isValidPassword } from "@/utils/UserInputCheck";
import ErrorFeedback from "@/components/FeedBack/Error.vue";
import SuccessFeedback from "@/components/FeedBack/Succes.vue";

const emit = defineEmits<{ abort: [] }>();

const currentPassword = ref("");
const newPassword = ref("");
const confirmNewPassword = ref("");
const validationError = ref("");
const saveError = ref("");
const validationSuccess = ref("");
const isSubmitting = ref(false);

async function changePassword() {
  validationError.value = "";
  saveError.value = "";
  validationSuccess.value = "";

  if (isblank(currentPassword.value)) {
    validationError.value = "Current password cannot be blank.";
    return;
  }

  if (isblank(newPassword.value)) {
    validationError.value = "New password cannot be blank.";
    return;
  }

  if (!isValidPassword(newPassword.value)) {
    validationError.value =
      "Password must be at least 8 characters and contain an uppercase letter, lowercase letter, and number.";
    return;
  }

  if (!inputsMatch(newPassword.value, confirmNewPassword.value)) {
    validationError.value = "New password and confirmation do not match.";
    return;
  }

  isSubmitting.value = true;

  try {
    const response = await fetch("http://localhost:8082/api/user/change-password", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify({
        currentPassword: currentPassword.value,
        newPassword: newPassword.value,
      }),
    });

    if (!response.ok) {
      const error = await response.json().catch(() => null);
      saveError.value = error?.detail ?? error?.message ?? "Unable to change password.";
      return;
    }

    validationSuccess.value = "Password changed successfully.";
    currentPassword.value = "";
    newPassword.value = "";
    confirmNewPassword.value = "";
  } catch {
    saveError.value = "Unable to reach the server. Please try again later.";
  } finally {
    isSubmitting.value = false;
  }
}

function abortChange() {
  emit("abort");
}
</script>

<template>
  <ErrorFeedback v-if="validationError" :message="validationError" />
  <ErrorFeedback v-if="saveError" :message="saveError" />
  <SuccessFeedback v-if="validationSuccess" :message="validationSuccess" />

  <v-form @submit.prevent="changePassword">
    <v-text-field
      v-model="currentPassword"
      label="Current password"
      type="password"
      autocomplete="current-password"
    />
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
    <v-card-actions class="justify-center ga-2 px-0">
      <v-btn type="submit" color="primary" :loading="isSubmitting" :disabled="isSubmitting">
        Save
      </v-btn>
      <v-btn type="button" variant="text" :disabled="isSubmitting" @click="abortChange">
        Abort
      </v-btn>
    </v-card-actions>
  </v-form>
</template>
