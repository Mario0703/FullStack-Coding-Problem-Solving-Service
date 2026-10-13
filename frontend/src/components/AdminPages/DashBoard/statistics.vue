<script setup lang="ts">
import { computed } from 'vue'
import type { PlatformStatistics } from '@/utils/Admin/AdminStatisticsUtils.ts'

defineOptions({
  name: 'DashboardStatistics',
})

const props = defineProps<{
  statistics: PlatformStatistics | null
  isLoading: boolean
}>()

interface StatCard {
  title: string
  subtitle: string
  icon: string
  color: string
  value: number
}

// One definition per metric so the template renders a single colored card.
const statCards = computed<StatCard[]>(() => {
  const stats = props.statistics

  return [
    {
      title: 'Users',
      subtitle: 'Registered accounts',
      icon: 'mdi-account-group',
      color: 'blue',
      value: stats?.numberOfUsers ?? 0,
    },
    {
      title: 'Challenges',
      subtitle: 'Coding challenges',
      icon: 'mdi-code-braces',
      color: 'indigo',
      value: stats?.numberOfChallenges ?? 0,
    },
    {
      title: 'Submissions',
      subtitle: 'Submitted solutions',
      icon: 'mdi-send',
      color: 'orange',
      value: stats?.numberOfSubmittedSolutions ?? 0,
    },
    {
      title: 'Solved',
      subtitle: 'Solved challenges',
      icon: 'mdi-check-decagram',
      color: 'green',
      value: stats?.numberOfSolvedChallenges ?? 0,
    },
  ]
})
</script>

<template>
  <v-card-title class="text-h4 font-weight-bold px-0 mb-4 d-flex align-center">
    <v-icon icon="mdi-chart-box-outline" color="indigo" class="mr-2" />
    Platform statistics
  </v-card-title>

  <v-row v-if="isLoading">
    <v-col cols="12" class="d-flex justify-center">
      <v-progress-circular indeterminate color="primary" />
    </v-col>
  </v-row>

  <v-row v-else-if="statistics">
    <v-col v-for="card in statCards" :key="card.title" cols="12" sm="6" md="3">
      <v-card :color="card.color" variant="tonal" class="fill-height">
        <v-card-item>
          <template #prepend>
            <v-avatar :color="card.color" size="44">
              <v-icon :icon="card.icon" size="26" />
            </v-avatar>
          </template>
          <v-card-title>{{ card.title }}</v-card-title>
          <v-card-subtitle>{{ card.subtitle }}</v-card-subtitle>
        </v-card-item>
        <v-card-text class="text-h4 font-weight-bold">
          {{ card.value.toLocaleString() }}
        </v-card-text>
      </v-card>
    </v-col>
  </v-row>
</template>