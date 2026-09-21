<script setup lang="ts">
type UserData = {
  email?: string;
  userName?: string;
  password?: string;
};

const storedUser = sessionStorage.getItem("loggedInUser");
const user: UserData = storedUser ? JSON.parse(storedUser) : {};
</script>

<template>
  <v-container class="d-flex flex-column align-center">
    <h1>User page</h1>
    <span>Username: {{ user.userName || "Not available" }}</span>
  </v-container>
</template>
