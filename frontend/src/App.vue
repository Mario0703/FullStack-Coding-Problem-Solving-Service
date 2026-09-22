<script setup lang="ts">
import { loadCurrentUser } from "./authenticator/auth";
loadCurrentUser();
</script>
<template>
  <v-app>
    <v-main>
      <RouterView />
    </v-main>
  </v-app>
</template>
