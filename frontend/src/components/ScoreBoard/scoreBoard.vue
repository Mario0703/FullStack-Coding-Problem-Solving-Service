<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import { baseUrl } from '@/utils/api'
import SortComponentScoreBoard from '@/components/ScoreBoard/SortComponentScoreBoard.vue'

type UserStats = {
  userName: string
  score: number
  numberOfEasyChallengesSolved: number
  numberOfMediumChallengesSolved: number
  numberOfHardChallengesSolved: number
  totalChallengesSolved: number
}

const users = ref<UserStats[]>([])
const sortBy = ref('score')

const sortGetters: Record<string, (user: UserStats) => number> = {
  score: (user) => user.score,
  totalChallengesSolved: (user) => user.totalChallengesSolved,
  numberOfEasyChallengesSolved: (user) => user.numberOfEasyChallengesSolved,
  numberOfMediumChallengesSolved: (user) => user.numberOfMediumChallengesSolved,
  numberOfHardChallengesSolved: (user) => user.numberOfHardChallengesSolved,
}

const sortedUsers = computed(() => {
  const getValue = sortGetters[sortBy.value] ?? ((user: UserStats) => user.score)

  return [...users.value].sort((a, b) => getValue(b) - getValue(a))
})

async function loadUserStats(): Promise<UserStats[]> {
  const response = await fetch(`${baseUrl}/api/scoreboard/user-stats`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: 'include',
  })

  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`)
  }

  return response.json()
}

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
