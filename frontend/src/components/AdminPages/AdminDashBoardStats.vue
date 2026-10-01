<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import ErrorFeedback from '@/components/FeedBack/Error.vue'

type PlatformStatistics = {
  numberOfUsers: number
  numberOfChallenges: number
  numberOfSubmittedSolutions: number
  numberOfSolvedChallenges: number
}

type UserStatistics = {
  id: string
  name: string
  email: string
  numberOfSubmissions: number
  numberOfSolvedChallenges: number
}

const statistics = ref<PlatformStatistics | null>(null)
const users = ref<UserStatistics[]>([])
const searchQuery = ref('')
const isLoading = ref(true)
const loadError = ref('')

const filteredUsers = computed(() => {
  const query = searchQuery.value.trim().toLowerCase()

  if (!query) {
    return users.value
  }

  return users.value.filter((user) => user.name.toLowerCase().includes(query))
})

async function loadStatistics() {
  try {
    const [statisticsResponse, usersResponse] = await Promise.all([
      fetch('http://localhost:8082/api/admin/stats', { credentials: 'include' }),
      fetch('http://localhost:8082/api/admin/stats/users/list', { credentials: 'include' }),
    ])

    if (!statisticsResponse.ok || !usersResponse.ok) {
      throw new Error('Could not load platform statistics')
    }

    statistics.value = (await statisticsResponse.json()) as PlatformStatistics
    users.value = (await usersResponse.json()) as UserStatistics[]
  } catch {
    loadError.value = 'Unable to load platform statistics. Please try again later.'
  } finally {
    isLoading.value = false
  }
}

onMounted(loadStatistics)
</script>

<template>
  <v-container class="py-8">
    <v-card-title class="text-h4 font-weight-bold px-0 mb-4">Platform statistics</v-card-title>

    <ErrorFeedback v-if="loadError" :message="loadError" variant="tonal" class="mb-4" />

    <v-row v-if="isLoading">
      <v-col cols="12" class="d-flex justify-center">
        <v-progress-circular indeterminate color="primary" />
      </v-col>
    </v-row>

    <v-row v-else-if="statistics">
      <v-col cols="12" sm="6" md="3">
        <v-card variant="outlined">
          <v-card-title>Users</v-card-title>
          <v-card-text class="text-h4">{{ statistics.numberOfUsers }}</v-card-text>
        </v-card>
      </v-col>
      <v-col cols="12" sm="6" md="3">
        <v-card variant="outlined">
          <v-card-title>Challenges</v-card-title>
          <v-card-text class="text-h4">{{ statistics.numberOfChallenges }}</v-card-text>
        </v-card>
      </v-col>
      <v-col cols="12" sm="6" md="3">
        <v-card variant="outlined">
          <v-card-title>Submissions</v-card-title>
          <v-card-text class="text-h4">{{ statistics.numberOfSubmittedSolutions }}</v-card-text>
        </v-card>
      </v-col>
      <v-col cols="12" sm="6" md="3">
        <v-card variant="outlined">
          <v-card-title>Solved challenges</v-card-title>
          <v-card-text class="text-h4">{{ statistics.numberOfSolvedChallenges }}</v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
  <v-container>
    <v-card>
      <v-card-title class="text-h4 font-weight-bold px-4 mb-4"> Users </v-card-title>

      <v-card-text>
        <v-text-field
          v-model="searchQuery"
          label="Search users by username"
          variant="outlined"
          clearable
          hide-details
        >
          <template #prepend-inner>
            <v-icon icon="mdi-magnify" />
          </template>
        </v-text-field>
      </v-card-text>

      <v-row class="px-4 font-weight-bold">
        <v-col cols="12" sm="6" md="3">User Name</v-col>
        <v-col cols="12" sm="6" md="3">User Email</v-col>
        <v-col cols="12" sm="6" md="3">Number of Submissions</v-col>
        <v-col cols="12" sm="6" md="3">Number of Solved Challenges</v-col>
      </v-row>

      <v-row v-if="isLoading">
        <v-col cols="12" class="d-flex justify-center">
          <v-progress-circular indeterminate color="primary" />
        </v-col>
      </v-row>

      <v-row v-for="user in filteredUsers" v-else :key="user.id" class="px-4 align-center">
        <v-col cols="12" sm="6" md="3">{{ user.name }}</v-col>
        <v-col cols="12" sm="6" md="3">{{ user.email }}</v-col>
        <v-col cols="12" sm="6" md="3">{{ user.numberOfSubmissions }}</v-col>
        <v-col cols="12" sm="6" md="3">
          {{ user.numberOfSolvedChallenges }}
        </v-col>
      </v-row>
    </v-card>
  </v-container>
</template>

<style scoped></style>
