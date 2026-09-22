<script setup lang="ts">
import { currentUser } from "../authenticator/auth";
import Logout from "./Logout.vue";
</script>

<template>
  <v-container class="d-flex flex-column align-center">
    <div v-if="currentUser">
      <p>Username: {{ currentUser.userName }}</p>
      <p>Email: {{ currentUser.email }}</p>
      <Logout class="mt-4" />
    </div>
    <p v-else>No user data available.</p>
  </v-container>
</template>
