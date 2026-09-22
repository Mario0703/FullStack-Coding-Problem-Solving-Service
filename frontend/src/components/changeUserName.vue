<script setup lang="ts">
import { ref } from "vue";
import { currentUser } from "@/authenticator/auth";
import {inputsMatch, isblank} from "@/utils/UserInputCheck";

const newUserName = ref("");
const confirmNewUserName = ref("");
const validationError = ref("");

function changeUserName() {
  validationError.value = "";

  if (!isblank(newUserName.value)) {
    validationError.value = "New user name cannot be blank.";
  }

  if (!inputsMatch(newUserName.value, confirmNewUserName.value)) {
    validationError.value = "New user name and confirmation do not match.";
  }
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
    <v-btn type="submit" color="primary">Save</v-btn>
  </v-form>
</template>
