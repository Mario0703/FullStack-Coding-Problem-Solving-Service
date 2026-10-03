<script setup lang="ts">
import type { PlatformStatistics } from '@/utils/AdminStatisticsUtils'

defineProps<{
  statistics: PlatformStatistics | null
  isLoading: boolean
}>()
</script>

<template>
  <v-card-title class="text-h4 font-weight-bold px-0 mb-4">Platform statistics</v-card-title>

  <v-row v-if="isLoading">
    <v-col cols="12" class="d-flex justify-center">
      <v-progress-circular indeterminate color="primary" />
    </v-col>
  </v-row>

  <v-row v-else-if="statistics">
    <v-col cols="12" sm="6" md="3">
      <v-card variant="outlined"><v-card-title>Users</v-card-title><v-card-text class="text-h4">{{ statistics.numberOfUsers }}</v-card-text></v-card>
    </v-col>
    <v-col cols="12" sm="6" md="3">
      <v-card variant="outlined"><v-card-title>Challenges</v-card-title><v-card-text class="text-h4">{{ statistics.numberOfChallenges }}</v-card-text></v-card>
    </v-col>
    <v-col cols="12" sm="6" md="3">
      <v-card variant="outlined"><v-card-title>Submissions</v-card-title><v-card-text class="text-h4">{{ statistics.numberOfSubmittedSolutions }}</v-card-text></v-card>
    </v-col>
    <v-col cols="12" sm="6" md="3">
      <v-card variant="outlined"><v-card-title>Solved challenges</v-card-title><v-card-text class="text-h4">{{ statistics.numberOfSolvedChallenges }}</v-card-text></v-card>
    </v-col>
  </v-row>
</template>
