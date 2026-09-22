<script setup lang="ts">
import { onMounted } from "vue";
import { isLoadingUser, loadCurrentUser } from "./authenticator/auth";

onMounted(() => {
  loadCurrentUser().catch(() => {
  });
});
</script>

<template>
  <v-app>
    <v-main v-if="!isLoadingUser">
      <RouterView />
    </v-main>

    <v-main v-else>
      Loading...
    </v-main>
  </v-app>
</template>
