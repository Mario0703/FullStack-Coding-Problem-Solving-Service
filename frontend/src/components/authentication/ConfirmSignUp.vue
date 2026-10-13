<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

function goToLogin() {
  router.push({ name: 'login' })
}
</script>

<template>
  <v-container class="d-flex align-center justify-center h-screen">
    <v-sheet
      class="pa-4 text-center mx-auto"
      border
      color="surface"
      elevation="4"
      max-width="600"
      rounded="lg"
      width="100%"
    >
      <v-icon
        class="mb-5"
        color="success"
        icon="mdi-check-circle"
        size="112"
      />

      <h2 class="text-headline-small mt-0 mb-6">Your account has been created</h2>

      <p class="mb-4 text-medium-emphasis text-body-medium">
        You have successfully made an account and are now signed in.
        <br />
        Click below to log in and start solving challenges.
      </p>

      <v-divider class="mb-4" />

      <div class="text-end">
        <v-btn
          class="text-none"
          color="grey-darken-4"
          variant="elevated"
          rounded
          @click="goToLogin"
        >
          Continue to login
        </v-btn>
      </div>
    </v-sheet>
  </v-container>
</template>
