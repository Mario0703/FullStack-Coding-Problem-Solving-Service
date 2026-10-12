<script setup lang="ts">
import { onMounted } from 'vue'
import ErrorFeedback from '@/components/FeedBack/Error.vue'
import Statistics from '@/components/AdminPages/DashBoard/statistics.vue'
import UsersStatics from '@/components/AdminPages/DashBoard/UsersStatics.vue'
import { loadStatistics, isLoading, users, statistics, loadError } from '@/utils/Admin/AdminStatisticsUtils.ts'



onMounted(loadStatistics)
</script>

<template>
  <v-container class="py-8">
    <ErrorFeedback v-if="loadError" :message="loadError" variant="tonal" class="mb-4" />
    <Statistics :statistics="statistics" :is-loading="isLoading" />
    <UsersStatics :users="users" :is-loading="isLoading" class="mt-8" />
  </v-container>
</template>
