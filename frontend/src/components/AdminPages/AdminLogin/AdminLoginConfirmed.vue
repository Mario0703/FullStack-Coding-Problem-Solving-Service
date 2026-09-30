<script setup lang="ts">
import { useRouter } from "vue-router";

const router = useRouter();

function goToAdminDashboard() {
  router.push({ name: "adminDashboard" });
}
</script>

<template>
  <v-container class="d-flex flex-column align-center justify-center h-screen">
    <v-card class="pa-4 text-center">
      <v-alert
        title="Admin login successful"
        text="You are now logged in as an administrator."
        type="success"
      />
    </v-card>
    <v-btn color="primary" class="mt-4" @click="goToAdminDashboard">
      Go to admin dashboard
    </v-btn>
  </v-container>
</template>
