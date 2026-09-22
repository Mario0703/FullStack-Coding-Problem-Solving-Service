<script setup lang="ts">
import { ref } from "vue";
import { currentUser } from "@/authenticator/auth";
import { inputsMatch, isblank, isValidEmail } from "@/utils/UserInputCheck";

const emit = defineEmits<{ abort: [] }>();
const newEmail = ref("");
const confirmNewEmail = ref("");
const validationError = ref("");

function changeEmail() {
  validationError.value = "";

  if (isblank(newEmail.value)) {
    validationError.value = "New email cannot be blank.";
    return;
  }

  if (!isValidEmail(newEmail.value)) {
    validationError.value = "Enter a valid email address.";
    return;
  }

  if (!inputsMatch(newEmail.value, confirmNewEmail.value)) {
    validationError.value = "New email and confirmation do not match.";
  }
}

function abortChange() {
  emit("abort");
}
</script>

<template>
  <v-alert v-if="validationError" type="error" :text="validationError" />

  <v-form @submit.prevent="changeEmail">
    <v-text-field
      :model-value="currentUser?.email ?? ''"
      label="Current email"
      type="email"
      disabled
    />
    <v-text-field
      v-model="newEmail"
      placeholder="Type new email"
      label="New email"
      type="email"
      autocomplete="email"
    />
    <v-text-field
      v-model="confirmNewEmail"
      placeholder="Confirm new email"
      label="Confirm new email"
      type="email"
      autocomplete="email"
    />
    <v-card-actions class="justify-center ga-2 px-0">
      <v-btn type="submit" color="primary">Save</v-btn>
      <v-btn type="button" variant="text" @click="abortChange">Abort</v-btn>
    </v-card-actions>
  </v-form>
</template>
