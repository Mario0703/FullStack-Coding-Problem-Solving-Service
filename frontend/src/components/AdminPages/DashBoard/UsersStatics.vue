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
  <v-card color="teal" variant="tonal">
    <v-card-title class="text-h4 font-weight-bold px-4 mb-4 d-flex align-center">
      <v-icon icon="mdi-account-multiple" class="mr-2" />
      Users
    </v-card-title>
    <v-card-text>
      <v-text-field v-model="searchQuery" label="Search users by username" variant="outlined" clearable hide-details>
        <template #prepend-inner><v-icon icon="mdi-magnify" /></template>
      </v-text-field>
    </v-card-text>

    <v-row class="px-4 font-weight-bold">
      <v-col cols="12" sm="6" md="3">
        <v-icon icon="mdi-account-outline" size="small" class="mr-1" /> User Name
      </v-col>
      <v-col cols="12" sm="6" md="3">
        <v-icon icon="mdi-email-outline" size="small" class="mr-1" /> User Email
      </v-col>
      <v-col cols="12" sm="6" md="3">
        <v-icon icon="mdi-send-outline" size="small" class="mr-1" /> Submissions
      </v-col>
      <v-col cols="12" sm="6" md="3">
        <v-icon icon="mdi-check-circle-outline" size="small" class="mr-1" /> Solved
      </v-col>
    </v-row>
    <v-row v-if="isLoading">
      <v-col cols="12" class="d-flex justify-center"><v-progress-circular indeterminate color="primary" /></v-col>
    </v-row>
    <v-row v-for="user in filteredUsers" v-else :key="user.id" class="px-4 align-center">
      <v-col cols="12" sm="6" md="3">
        <div class="d-flex align-center ga-2">
          <v-avatar color="teal" size="32">
            <span class="font-weight-bold">{{ user.name.charAt(0).toUpperCase() }}</span>
          </v-avatar>
          <!-- The name links to the user's public profile page. -->
          <v-btn
            variant="text"
            color="teal"
            class="px-1 text-body-1"
            :to="{ name: 'publicProfile', params: { userId: user.id } }"
            prepend-icon="mdi-open-in-new"
          >
            {{ user.name }}
          </v-btn>
        </div>
      </v-col>
      <v-col cols="12" sm="6" md="3">{{ user.email }}</v-col>
      <v-col cols="12" sm="6" md="3">
        <v-chip color="orange" variant="tonal" size="small">{{ user.numberOfSubmissions }}</v-chip>
      </v-col>
      <v-col cols="12" sm="6" md="3">
        <v-chip color="green" variant="tonal" size="small">{{ user.numberOfSolvedChallenges }}</v-chip>
      </v-col>
    </v-row>
    <v-row v-if="!isLoading && filteredUsers.length === 0">
      <v-col cols="12" class="px-4 text-medium-emphasis">No users match your search.</v-col>
    </v-row>
  </v-card>
</template>