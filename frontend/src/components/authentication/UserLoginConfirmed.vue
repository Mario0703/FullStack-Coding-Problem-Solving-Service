<script setup lang="ts">
import { computed } from "vue";
import { useRouter } from "vue-router";
import { currentUser } from "@/authenticator/auth";

const router = useRouter();
const userName = computed(() => currentUser.value?.userName || "user");

function SendUserToUser() {
  router.push({ name: "user" });
}
</script>

<template>
  <v-container class="d-flex flex-column align-center justify-center h-screen">
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
