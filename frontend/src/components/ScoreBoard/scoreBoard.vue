<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'
import SortComponentScoreBoard from '@/components/ScoreBoard/SortComponentScoreBoard.vue'
import type { UserStats } from '@/utils/ScoreBoard/types'
import { sortUsers } from '@/utils/ScoreBoard/sorting'
import { filterUsersByUserName } from '@/utils/ScoreBoard/filtering'
import { loadUserStats } from '@/utils/ScoreBoard/api'
import ErrorFeedback from '@/components/FeedBack/Error.vue'
import FadeContent from '@/components/ui/FadeContent.vue'

const users = ref<UserStats[]>([])
const sortBy = ref('score')
const searchQuery = ref('')
const loadError = ref('')

const visibleUsers = computed(() =>
  sortUsers(filterUsersByUserName(users.value, searchQuery.value), sortBy.value),
)

onMounted(async () => {
  loadError.value = ''
  try {
    users.value = await loadUserStats()
  } catch (error) {
    loadError.value = 'Unable to load the scoreboard. Please try again later.'
    console.error('Error loading user stats:', error)
  }
})
</script>

<template>
  <FadeContent
    :blur="true"
    :duration="1000"
    :initial-opacity="0"
    ease="power2.out"
  >
    <v-container class="py-8">
      <v-row class="mb-4" dense>
        <v-col cols="12" md="8">
          <v-text-field
            v-model="searchQuery"
            label="Search by user name"
            variant="outlined"
            clearable
            hide-details
          >
            <template #prepend-inner>
              <v-icon icon="mdi-magnify" />
            </template>
          </v-text-field>
        </v-col>
        <v-col cols="12" md="4">
          <SortComponentScoreBoard v-model="sortBy" />
        </v-col>
      </v-row>

      <ErrorFeedback v-if="loadError" :message="loadError" class="mb-4" />

      <v-table class="scoreboard-grid">
        <thead>
          <tr>
            <th>
              <v-icon icon="mdi-account" class="mr-2" />
              User name
            </th>
            <th>
              <v-icon icon="mdi-trophy" class="mr-2" />
              Score
            </th>
            <th>
              <v-icon icon="mdi-leaf" class="mr-2 text-success" />
              Number of easy challenges solved
            </th>
            <th>
              <v-icon icon="mdi-fire" class="mr-2 text-warning" />
              Number of medium challenges solved
            </th>
            <th>
              <v-icon icon="mdi-skull" class="mr-2 text-error" />
              Number of hard challenges solved
            </th>
            <th>
              <v-icon icon="mdi-format-list-checks" class="mr-2" />
              Total challenges solved
            </th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="visibleUsers.length === 0">
            <td colspan="6" class="text-center text-medium-emphasis">
              No users match your search.
            </td>
          </tr>
          <tr v-for="user in visibleUsers" :key="user.userId">
            <td>
              <RouterLink
                class="text-primary text-decoration-none font-weight-medium"
                :to="{ name: 'publicProfile', params: { userId: user.userId } }"
              >
                {{ user.userName }}
              </RouterLink>
            </td>
            <td>{{ user.score }}</td>
            <td>{{ user.numberOfEasyChallengesSolved }}</td>
            <td>{{ user.numberOfMediumChallengesSolved }}</td>
            <td>{{ user.numberOfHardChallengesSolved }}</td>
            <td>{{ user.totalChallengesSolved }}</td>
          </tr>
        </tbody>
      </v-table>
    </v-container>
  </FadeContent>
</template>

<style scoped>
.scoreboard-grid :deep(th),
.scoreboard-grid :deep(td) {
  border: 1px solid rgba(var(--v-border-color), var(--v-border-opacity));
}
</style>