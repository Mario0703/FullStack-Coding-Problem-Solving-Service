<script setup lang="ts">
import { onMounted } from "vue";
import { currentUser, isAdmin, isAdminAuthenticated } from "@/authenticator/auth";


onMounted(() => {
  isAdminAuthenticated();
});
</script>

<template>
  <v-app-bar color="primary">
    <v-app-bar-title>MiniLeetCode</v-app-bar-title>
    <v-spacer />
    <v-btn color="white" variant="text" :to="{ name: 'home' }">Home</v-btn>
    <v-btn color="white" variant="text" :to="{ name: 'challenges' }">
      Explore coding challenges
    </v-btn>
    <v-btn v-if="!currentUser && !isAdmin" color="white" variant="text" :to="{ name: 'signup' }">
      Sign up
    </v-btn>
    <v-btn v-if="!currentUser && !isAdmin" color="white" variant="text" :to="{ name: 'login' }">
      Login
    </v-btn>
    <v-btn v-if="currentUser" color="white" variant="text" :to="{ name: 'user' }">
      View Profile
    </v-btn>
    <v-btn v-if="isAdmin" color="white" variant="text" :to="{ name: 'adminDashboard' }">
      Admin dashboard
    </v-btn>
  </v-app-bar>
</template>
