<script setup lang="ts">
import { onMounted, ref } from 'vue'

type PlatformStatistics = {
  numberOfUsers: number
  numberOfChallenges: number
  numberOfSubmittedSolutions: number
  numberOfSolvedChallenges: number
}

const statistics = ref<PlatformStatistics | null>(null)
const isLoading = ref(true)
const loadError = ref('')

async function loadStatistics() {
  try {
    const response = await fetch('http://localhost:8082/api/admin/stats', {
      credentials: 'include',
    })

    if (!response.ok) {
      throw new Error('Could not load platform statistics')
    }

    statistics.value = (await response.json()) as PlatformStatistics
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

    <v-alert v-if="loadError" :text="loadError" type="error" variant="tonal" class="mb-4" />

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
</template>

<style scoped></style>
