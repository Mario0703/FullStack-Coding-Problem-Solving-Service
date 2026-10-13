<script setup lang="ts">
import { ref } from "vue";
import { currentUser } from "@/utils/Users/state";
import { changeUserName } from "@/utils/Users/api";
import { validateUserNameChange } from "@/utils/Users/validation";
import ErrorFeedback from "@/components/FeedBack/Error.vue";
import { readErrorMessage } from '@/utils/apiError';

const emit = defineEmits<{ abort: [] }>();
const newUserName = ref("");
const confirmNewUserName = ref("");
const validationError = ref("");
const saveError = ref("");
const isSubmitting = ref(false);

async function handleChangeUserName() {
  validationError.value = "";
  saveError.value = "";

  const validation = validateUserNameChange(newUserName.value, confirmNewUserName.value);
  if (!validation.isValid) {
    validationError.value = validation.error;
    return;
  }

  isSubmitting.value = true;

  try {
    const response = await changeUserName(newUserName.value);

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

  <v-form @submit.prevent="handleChangeUserName">
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
      <v-btn type="submit" color="grey-darken-4" variant="elevated" :loading="isSubmitting" :disabled="isSubmitting">
        Save
      </v-btn>
      <v-btn type="button" variant="text" color="grey-darken-4" :disabled="isSubmitting" @click="abortChange">
        Abort
      </v-btn>
    </v-card-actions>
  </v-form>
</template>
