<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";
import { isAdmin } from "@/authenticator/auth";
import ErrorFeedback from "@/components/FeedBack/Error.vue";

const router = useRouter();
const adminUserName = ref("");
const adminPassword = ref("");
const loginError = ref("");


async function AdminSignIn(){
  const response = await fetch("http://localhost:8082/api/admin/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    credentials: "include",
    body: JSON.stringify({ adminUserName: adminUserName.value, adminPassword: adminPassword.value }),
  });

  if (!response.ok) {
    loginError.value = "Invalid admin username or password.";
    return;
  }

  await router.push({ name: "adminLoginConfirmed" });
  isAdmin.value = true;

}


</script>

<template>
  <v-container class="d-flex align-center justify-center">
    <ErrorFeedback v-if="loginError" :message="loginError" class="mb-4" />
    <v-form @submit.prevent="AdminSignIn" class="elevation-12 pa-6" style="max-width: 400px; width: 100%;">
      <v-text-field
        v-model="adminUserName"
        placeholder="AdminUserName"
        outlined
        dense
        class="mb-4"
      ></v-text-field>
      <v-text-field
        v-model="adminPassword"
        placeholder="AdminPassword"
        outlined
        dense
        type="password"
        class="mb-4"
      ></v-text-field>
      <v-btn type="submit" color="primary" block>Login</v-btn>
    </v-form>
  </v-container>
</template>

<style scoped>

</style>
