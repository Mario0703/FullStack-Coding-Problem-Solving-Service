<script setup lang="ts">
import { ref } from "vue";
import { currentUser } from "@/utils/Users/state";
import { changeEmail } from "@/utils/Users/api";
import { validateEmailChange } from "@/utils/Users/validation";
import ErrorFeedback from "@/components/FeedBack/Error.vue";
import { readErrorMessage } from '@/utils/apiError';

const emit = defineEmits<{ abort: [] }>();
const newEmail = ref("");
const confirmNewEmail = ref("");
const validationError = ref("");
const saveError = ref("");
const isSubmitting = ref(false);

async function handleChangeEmail() {
  validationError.value = "";
  saveError.value = "";

  const validation = validateEmailChange(newEmail.value, confirmNewEmail.value);
  if (!validation.isValid) {
    validationError.value = validation.error;
    return;
  }

  isSubmitting.value = true;

  try {
    const response = await changeEmail(newEmail.value);

    if (!response.ok) {
      saveError.value = await readErrorMessage(response, "Unable to change email.");
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
  <ErrorFeedback v-if="validationError" :message="validationError" />
  <ErrorFeedback v-if="saveError" :message="saveError" />

  <v-form @submit.prevent="handleChangeEmail">
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
