<script setup lang="ts">
import { computed, ref } from 'vue'
import type { UserStatistics } from '@/utils/Admin/AdminStatisticsUtils.ts'
import { filterUsersBySearch } from '@/utils/Users/dashboard'

const props = defineProps<{
  users: UserStatistics[]
  isLoading: boolean
}>()

const searchQuery = ref('')
const filteredUsers = computed(() => filterUsersBySearch(props.users, searchQuery.value))
</script>

<template>
  <v-card>
    <v-card-title class="text-h4 font-weight-bold px-4 mb-4">Users</v-card-title>
    <v-card-text>
      <v-text-field v-model="searchQuery" label="Search users by username" variant="outlined" clearable hide-details>
        <template #prepend-inner><v-icon icon="mdi-magnify" /></template>
      </v-text-field>
    </v-card-text>

    <v-row class="px-4 font-weight-bold">
      <v-col cols="12" sm="6" md="3">User Name</v-col>
      <v-col cols="12" sm="6" md="3">User Email</v-col>
      <v-col cols="12" sm="6" md="3">Number of Submissions</v-col>
      <v-col cols="12" sm="6" md="3">Number of Solved Challenges</v-col>
    </v-row>
    <v-row v-if="isLoading">
      <v-col cols="12" class="d-flex justify-center"><v-progress-circular indeterminate color="primary" /></v-col>
    </v-row>
    <v-row v-for="user in filteredUsers" v-else :key="user.id" class="px-4 align-center">
      <v-col cols="12" sm="6" md="3">{{ user.name }}</v-col>
      <v-col cols="12" sm="6" md="3">{{ user.email }}</v-col>
      <v-col cols="12" sm="6" md="3">{{ user.numberOfSubmissions }}</v-col>
      <v-col cols="12" sm="6" md="3">{{ user.numberOfSolvedChallenges }}</v-col>
    </v-row>
  </v-card>
</template>
