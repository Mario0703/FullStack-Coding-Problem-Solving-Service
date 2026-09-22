<script setup lang="ts">
import { ref } from "vue";
import { currentUser } from "@/authenticator/auth";
import { inputsMatch, isblank, isValidEmail } from "@/utils/UserInputCheck";

const emit = defineEmits<{ abort: [] }>();
const newEmail = ref("");
const confirmNewEmail = ref("");
const validationError = ref("");
const saveError = ref("");
const isSubmitting = ref(false);

async function changeEmail() {
  validationError.value = "";
  saveError.value = "";

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
    return;
  }

  isSubmitting.value = true;

  try {
    const response = await fetch("http://localhost:8082/api/user/change-email", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify({ newEmail: newEmail.value.trim() }),
    });

    if (!response.ok) {
      const error = await response.json().catch(() => null);
      saveError.value = error?.detail ?? error?.message ?? "Unable to change email.";
      return;
    }

    currentUser.value = await response.json();
    emit("abort");
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
  <v-alert v-if="validationError" type="error" :text="validationError" />
  <v-alert v-if="saveError" type="error" :text="saveError" />

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
      <v-btn type="submit" color="primary" :loading="isSubmitting" :disabled="isSubmitting">
        Save
      </v-btn>
      <v-btn type="button" variant="text" :disabled="isSubmitting" @click="abortChange">
        Abort
      </v-btn>
    </v-card-actions>
  </v-form>
</template>
