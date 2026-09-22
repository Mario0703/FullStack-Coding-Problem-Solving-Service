<script setup lang="ts">
import { ref } from "vue";
import { currentUser } from "@/authenticator/auth";
import {inputsMatch, isblank} from "@/utils/UserInputCheck";

const emit = defineEmits<{ abort: [] }>();
const newUserName = ref("");
const confirmNewUserName = ref("");
const validationError = ref("");

function changeUserName() {
  validationError.value = "";

  if (isblank(newUserName.value)) {
    validationError.value = "New user name cannot be blank.";
    return;
  }

  if (!inputsMatch(newUserName.value, confirmNewUserName.value)) {
    validationError.value = "New user name and confirmation do not match.";
  }
}

function abortChange() {
  emit("abort");
}
</script>

<template>
  <v-alert v-if="validationError" type="error" :text="validationError" />

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
      <v-btn type="submit" color="primary">Save</v-btn>
      <v-btn type="button" variant="text" @click="abortChange">Abort</v-btn>
    </v-card-actions>
  </v-form>
</template>
