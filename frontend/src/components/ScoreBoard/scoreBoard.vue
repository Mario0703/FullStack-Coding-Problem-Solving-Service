<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { baseUrl } from '@/utils/api'

type UserStats = {
  userName: string
  score: number
  numberOfEasyChallengesSolved: number
  numberOfMediumChallengesSolved: number
  numberOfHardChallengesSolved: number
  totalChallengesSolved: number
}

const users = ref<UserStats[]>([])

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
    <v-row>
      <v-col>User name</v-col>
      <v-col>score</v-col>
      <v-col>Number of easy challenges solved</v-col>
      <v-col>Number of medium challenges solved</v-col>
      <v-col>Number of hard challenges solved</v-col>
      <v-col>Total challenges solved</v-col>
    </v-row>
    <v-row v-for="user in users" :key="user.userName">
      <v-col>{{ user.userName }}</v-col>
      <v-col>{{ user.score }}</v-col>
      <v-col>{{ user.numberOfEasyChallengesSolved }}</v-col>
      <v-col>{{ user.numberOfMediumChallengesSolved }}</v-col>
      <v-col>{{ user.numberOfHardChallengesSolved }}</v-col>
      <v-col>{{ user.totalChallengesSolved }}</v-col>
    </v-row>
  </v-container>
</template>

<style scoped></style>
