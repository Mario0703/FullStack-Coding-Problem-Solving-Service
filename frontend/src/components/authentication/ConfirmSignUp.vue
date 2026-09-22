<script setup lang="ts">
import { useRouter } from 'vue-router'
const router = useRouter()

function SendUserToLogin() {
  router.push({ name: 'login' })
}

</script>

<template>
  <v-container class="d-flex flex-column align-center justify-center h-screen">
    <v-card class="pa-4 text-center">
      <v-alert
        title="You can now go on to solve challenges"
        text="You have successfully made an account and are now signed in."
        type="success"
      />
    </v-card>

    <v-btn
      color="primary"
      class="mt-4"
      @click="SendUserToLogin"
    >
      Click me to login and start solving challenges
    </v-btn>
  </v-container>
</template>
