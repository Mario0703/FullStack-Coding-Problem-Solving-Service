<script setup lang="ts">
import { onMounted } from "vue";
import {
  authError,
  hasCheckedUser,
  isLoadingUser,
  loadCurrentUser,
} from "./authenticator/auth";
import MenuBar from "./components/navigation/MenuBar.vue";

onMounted(() => {
  if (!hasCheckedUser.value) {
    loadCurrentUser().catch(() => {
      // authError provides feedback while public pages remain available.
    });
  }
});
</script>

<template>
  <v-app>
    <MenuBar v-if="!isLoadingUser" />

    <v-main v-if="isLoadingUser" class="d-flex align-center justify-center">
      <v-progress-circular indeterminate color="primary" />
    </v-main>

    <v-main v-else>
      <v-alert
        v-if="authError"
        type="warning"
        :text="authError"
        variant="tonal"
        class="ma-4"
      />
      <RouterView />
    </v-main>
  </v-app>
</template>
