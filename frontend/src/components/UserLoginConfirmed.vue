<script setup lang="ts">
import { computed } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();
const storedUser = sessionStorage.getItem("loggedInUser");
const user = storedUser ? JSON.parse(storedUser) : undefined;
const userName = computed(() => user?.userName || "user");

function SendUserToUser() {
  router.push({ name: "user" });
}
</script>

<template>
  <v-container class="d-flex flex-column align-center justify-center" style="min-height: 100vh;">
    <v-card class="pa-4 text-center">
      <v-alert
        title="Login successful"
        :text="`Welcome, ${userName}! You are now logged in and can proceed to your user page.`"
        type="success"
      />
    </v-card>

    <v-btn color="primary" class="mt-4" @click="SendUserToUser">
      Go to my user page
    </v-btn>
  </v-container>
</template>

<style scoped>
</style>
