<script setup lang="ts">
import { ref } from "vue";
import { changePassword as changePasswordApi } from "@/utils/Users/api";
import { validatePasswordChange } from "@/utils/Users/validation";
import ErrorFeedback from "@/components/FeedBack/Error.vue";
import SuccessFeedback from "@/components/FeedBack/Succes.vue";
import { readErrorMessage } from '@/utils/apiError';

const emit = defineEmits<{ abort: [] }>();

const currentPassword = ref("");
const newPassword = ref("");
const confirmNewPassword = ref("");
const validationError = ref("");
const saveError = ref("");
const validationSuccess = ref("");
const isSubmitting = ref(false);

async function handleChangePassword() {
  validationError.value = "";
  saveError.value = "";
  validationSuccess.value = "";

  const validation = validatePasswordChange(
    currentPassword.value,
    newPassword.value,
    confirmNewPassword.value,
  );
  if (!validation.isValid) {
    validationError.value = validation.error;
    return;
  }

  isSubmitting.value = true;

  try {
    const response = await changePasswordApi(currentPassword.value, newPassword.value);

    if (!response.ok) {
      saveError.value = await readErrorMessage(response, "Unable to change password.");
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

  <v-form @submit.prevent="handleChangePassword">
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
      <v-btn type="submit" color="grey-darken-4" variant="elevated" :loading="isSubmitting" :disabled="isSubmitting">
        Save
      </v-btn>
      <v-btn type="button" variant="elevated" color="grey-darken-4" :disabled="isSubmitting" @click="abortChange">
        Abort
      </v-btn>
    </v-card-actions>
  </v-form>
</template>
