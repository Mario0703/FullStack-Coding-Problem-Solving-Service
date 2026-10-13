<script setup lang="ts">
import { ref } from 'vue'
import AddChallange from '@/components/AdminPages/AddChallange.vue'
import AddStaff from '@/components/AdminPages/Staff/AddStaff.vue'
import ManageChallenges from '@/components/AdminPages/ManageChallenges.vue'
import Logout from '@/components/authentication/Logout.vue'
import type { Challenge } from '@/utils/Challenges/Types/types'
import type { UserData } from '@/utils/Users/types'
import { useRouter } from 'vue-router'
import SuccessFeedback from '@/components/FeedBack/Succes.vue'

const newChallenge = ref<Challenge | null>(null)
const successMessage = ref('')
const route = useRouter()

function addChallenge(challenge: Challenge) {
  newChallenge.value = challenge
  successMessage.value = 'Challenge created successfully.'
}

function staffCreated(staff: UserData) {
  successMessage.value = `${staff.userName} was added to the MiniLeetCode staff.`
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
        <div class="d-flex flex-wrap ga-2 mt-2">
          <v-btn color="grey-darken-4" variant="elevated" @click="goToStats">Click to view stats</v-btn>
          <AddStaff @created="staffCreated" />
        </div>

        <v-card-subtitle class="text-body-1 mt-2 px-0">
          Create and review coding challenges.
        </v-card-subtitle>
        <Logout class="mt-4" />
      </v-card>
    </v-container>

    <AddChallange @created="addChallenge" />

    <SuccessFeedback
      v-if="successMessage"
      :message="successMessage"
      variant="tonal"
      class="mt-4"
    />

    <v-divider class="my-8" />

    <ManageChallenges :new-challenge="newChallenge" />
  </v-container>
</template>

<style scoped></style>
