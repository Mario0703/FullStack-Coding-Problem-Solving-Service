<script setup lang="ts">
import { ref } from "vue";
import { inputsMatch, isblank, isValidPassword } from "@/utils/UserInputCheck";

const emit = defineEmits<{ abort: [] }>();

const currentPassword = ref("");
const newPassword = ref("");
const confirmNewPassword = ref("");
const validationError = ref("");
const validationSuccess = ref("");

function changePassword() {
  validationError.value = "";
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

  validationSuccess.value = "Password fields are valid and ready to be saved.";
}

function abortChange() {
  emit("abort");
}
</script>

<template>
  <v-alert v-if="validationError" type="error" :text="validationError" />
  <v-alert v-if="validationSuccess" type="success" :text="validationSuccess" />

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
      <v-btn type="submit" color="primary">Validate password</v-btn>
      <v-btn type="button" variant="text" @click="abortChange">Abort</v-btn>
    </v-card-actions>
  </v-form>
</template>
