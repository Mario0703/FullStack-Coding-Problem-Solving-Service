<script setup lang="ts">
import { ref } from 'vue'
import AddChallange from '@/components/AdminPages/AddChallange.vue'
import ManageChallenges from '@/components/AdminPages/ManageChallenges.vue'
import type { Challenge } from '@/utils/AdminUtils'
import { useRouter } from 'vue-router'

const newChallenge = ref<Challenge | null>(null)
const successMessage = ref('')
const route = useRouter()

function addChallenge(challenge: Challenge) {
  newChallenge.value = challenge
  successMessage.value = 'Challenge created successfully.'
}

function goToStats() {
  route.push({ name: 'adminDashboardStats' })
}
</script>

<template>
  <v-container class="py-8">
    <v-container class="mb-8">
      <v-card variant="text">
        <v-card-title class="text-h4 font-weight-bold px-0"> Admin dashboard </v-card-title>
        <v-btn @click="goToStats">CLick to view stats</v-btn>

        <v-card-subtitle class="text-body-1 mt-2 px-0">
          Create and review coding challenges.
        </v-card-subtitle>
      </v-card>
    </v-container>

    <AddChallange @created="addChallenge" />

    <v-alert
      v-if="successMessage"
      :text="successMessage"
      type="success"
      variant="tonal"
      class="mt-4"
    />

    <v-divider class="my-8" />

    <ManageChallenges :new-challenge="newChallenge" />
  </v-container>
</template>

<style scoped></style>
