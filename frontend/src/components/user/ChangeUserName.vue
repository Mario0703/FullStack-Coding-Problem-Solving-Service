<script setup lang="ts">
import { ref } from "vue";
import { currentUser } from "@/authenticator/auth";
import {inputsMatch, isblank} from "@/utils/Login/UserInputCheck.ts";
import ErrorFeedback from "@/components/FeedBack/Error.vue";
import { baseUrl } from '@/utils/api';
import { readErrorMessage } from '@/utils/apiError';

const emit = defineEmits<{ abort: [] }>();
const newUserName = ref("");
const confirmNewUserName = ref("");
const validationError = ref("");
const saveError = ref("");
const isSubmitting = ref(false);

async function changeUserName() {
  validationError.value = "";
  saveError.value = "";

  if (isblank(newUserName.value)) {
    validationError.value = "New user name cannot be blank.";
    return;
  }

  if (!inputsMatch(newUserName.value, confirmNewUserName.value)) {
    validationError.value = "New user name and confirmation do not match.";
    return;
  }

  isSubmitting.value = true;

  try {
    const response = await fetch(`${baseUrl}/api/user/change-username`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify({ newUserName: newUserName.value.trim() }),
    });

    if (!response.ok) {
      saveError.value = await readErrorMessage(response, "Unable to change user name.");
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

  <v-form @submit.prevent="changeUserName">
    <v-text-field
      :model-value="currentUser?.userName ?? ''"
      label="Current user name"
      disabled
    />
    <v-text-field
      v-model="newUserName"
      placeholder="Type new user name"
      label="New user name"
    />
    <v-text-field
      v-model="confirmNewUserName"
      placeholder="Confirm new user name"
      label="Confirm new user name"
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
