<script setup lang="ts">
import { useRouter } from 'vue-router'
const router = useRouter()

function SenderUserToChallenge() {
  router.push({ name: 'challenges' })
}

</script>

<template>
  <v-container class="d-flex flex-column align-center justify-center" style="min-height: 100vh;">
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
      @click="SenderUserToChallenge"
    >
      Click me to go on and solve some challenges
    </v-btn>
  </v-container>
</template>

<style scoped>

</style>
