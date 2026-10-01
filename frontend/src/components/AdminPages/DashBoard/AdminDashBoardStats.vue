<script setup lang="ts">
import { onMounted, ref } from 'vue'
import ErrorFeedback from '@/components/FeedBack/Error.vue'
import Statistics from '@/components/AdminPages/DashBoard/statistics.vue'
import UsersStatics from '@/components/AdminPages/DashBoard/UsersStatics.vue'
import type {
  PlatformStatistics as PlatformStatisticsData,
  UserStatistics as UserStatisticsData,
} from '@/utils/AdminStatisticsUtils'
import { baseUrl } from '@/utils/api'

const statistics = ref<PlatformStatisticsData | null>(null)
const users = ref<UserStatisticsData[]>([])
const isLoading = ref(true)
const loadError = ref('')

async function loadStatistics() {
  try {
    const permissionResponse = await fetch(`${baseUrl}/api/admin/stats/permission`, {
      credentials: 'include',
    })

    if (permissionResponse.status === 403) {
      loadError.value = 'You do not have permission to view platform statistics.'
      return
    }

    if (!permissionResponse.ok) {
      throw new Error('Could not check statistics permission')
    }

    const [statisticsResponse, usersResponse] = await Promise.all([
      fetch(`${baseUrl}/api/admin/stats`, { credentials: 'include' }),
      fetch(`${baseUrl}/api/admin/stats/users/list`, { credentials: 'include' }),
    ])

    if (!statisticsResponse.ok || !usersResponse.ok) {
      throw new Error('Could not load platform statistics')
    }

    statistics.value = (await statisticsResponse.json()) as PlatformStatisticsData
    users.value = (await usersResponse.json()) as UserStatisticsData[]
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
    <ErrorFeedback v-if="loadError" :message="loadError" variant="tonal" class="mb-4" />
    <Statistics :statistics="statistics" :is-loading="isLoading" />
    <UsersStatics :users="users" :is-loading="isLoading" class="mt-8" />
  </v-container>
</template>
