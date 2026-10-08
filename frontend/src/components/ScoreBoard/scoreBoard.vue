<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import SortComponentScoreBoard from '@/components/ScoreBoard/SortComponentScoreBoard.vue'
import type { UserStats } from '@/utils/ScoreBoard/types'
import { sortUsers } from '@/utils/ScoreBoard/sorting'
import { loadUserStats } from '@/utils/ScoreBoard/api'

const users = ref<UserStats[]>([])
const sortBy = ref('score')

const sortedUsers = computed(() => sortUsers(users.value, sortBy.value))

onMounted(async () => {
  try {
    users.value = await loadUserStats()
  } catch (error) {
    console.error('Error loading user stats:', error)
  }
})
</script>

<template>
  <v-container>
    <div class="mb-4">
      <SortComponentScoreBoard v-model="sortBy" />
    </div>

    <v-container >
      <v-row  no-gutters>
        <v-col>User name</v-col>
        <v-col>score</v-col>
        <v-col>Number of easy challenges solved</v-col>
        <v-col>Number of medium challenges solved</v-col>
        <v-col>Number of hard challenges solved</v-col>
        <v-col>Total challenges solved</v-col>
      </v-row>
      <v-row v-for="user in sortedUsers" :key="user.userName" no-gutters>
        <v-col>{{ user.userName }}</v-col>
        <v-col>{{ user.score }}</v-col>
        <v-col>{{ user.numberOfEasyChallengesSolved }}</v-col>
        <v-col>{{ user.numberOfMediumChallengesSolved }}</v-col>
        <v-col>{{ user.numberOfHardChallengesSolved }}</v-col>
        <v-col>{{ user.totalChallengesSolved }}</v-col>
      </v-row>
    </v-container>
  </v-container>
</template>

<style scoped>
</style>
