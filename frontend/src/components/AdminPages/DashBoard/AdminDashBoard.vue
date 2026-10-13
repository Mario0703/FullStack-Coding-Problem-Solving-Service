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
import FadeContent from '@/components/ui/FadeContent.vue'

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
  <FadeContent
    :blur="true"
    :duration="1000"
    :initial-opacity="0"
    ease="power2.out"
  >
    <v-container class="py-8">
      <v-container class="mb-8">
        <v-card color="indigo" variant="tonal">
          <v-card-item>
            <template #prepend>
              <v-avatar color="indigo" size="48">
                <v-icon icon="mdi-view-dashboard-outline" size="28" />
              </v-avatar>
            </template>
            <v-card-title class="text-h4 font-weight-bold">Admin dashboard</v-card-title>
            <v-card-subtitle class="text-body-1">Create and review coding challenges.</v-card-subtitle>
          </v-card-item>
          <v-card-actions class="px-4 pb-4 flex-wrap ga-2">
            <v-btn color="indigo" variant="elevated" prepend-icon="mdi-chart-areaspline" @click="goToStats">
              View statistics
            </v-btn>
            <AddStaff @created="staffCreated" />
            <v-spacer />
            <Logout />
          </v-card-actions>
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
  </FadeContent>
</template>

<style scoped></style>
