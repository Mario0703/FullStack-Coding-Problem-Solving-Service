<script setup lang="ts">
import { onMounted } from "vue";
import {
  authError,
  hasCheckedUser,
  isLoadingUser,
  loadCurrentUser,
} from "./authenticator/auth";
import MenuBar from "./components/navigation/MenuBar.vue";
import WarningFeedback from "./components/FeedBack/Warning.vue";

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
      <WarningFeedback
        v-if="authError"
        variant="tonal"
        class="ma-4"
        :message="authError"
      />
      <RouterView />
    </v-main>
  </v-app>
</template>
