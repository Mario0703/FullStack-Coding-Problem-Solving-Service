<script setup lang="ts">
import {computed, onMounted, ref, watch} from "vue";
import type {Challenge, ChallengeDifficulty} from "@/utils/AdminUtils";
import { formatChallengeValue } from "@/utils/AdminUtils";
import { listChallenges } from "@/utils/ChallangeUtil";

const challenges = ref<Challenge[]>([]);
const loadError = ref("");
const isLoading = ref(true);
const selectedDifficulty = ref<ChallengeDifficulty | null>(null);

async function loadChallengeList() {
  loadError.value = "";
  isLoading.value = true;

  try {
    challenges.value = await listChallenges();
  } catch {
    loadError.value = "Unable to load challenges. Please try again later.";
  } finally {
    isLoading.value = false;
  }
}

onMounted(loadChallengeList);

function filterByDifficulty(
  difficulty: ChallengeDifficulty | null,
  challenges: Challenge[]
): Challenge[] {
  if (!difficulty) {
    return challenges;
  }

  return challenges.filter(
    challenge => challenge.difficulty === difficulty
  );
}

const filteredChallenges = computed(() =>
  filterByDifficulty(
    selectedDifficulty.value,
    challenges.value
  )
)



</script>

<template>
  <v-container class="py-8">
    <v-select
      v-model="selectedDifficulty"
      label="Filter by difficulty"
      :items="['EASY', 'MEDIUM', 'HARD']"
      clearable
    />
    <v-btn v-model="selectedDifficulty" @click="selectedDifficulty = null">remove filter</v-btn>
    <v-card class="mb-8" variant="text">
      <v-card-title class="text-h4 font-weight-bold px-0">Coding challenges</v-card-title>
      <v-card-subtitle class="text-body-1 mt-2 px-0">
        Choose a challenge and start practicing.
      </v-card-subtitle>
    </v-card>

    <v-alert
      v-if="loadError"
      title="Could not load challenges"
      :text="loadError"
      type="error"
      variant="tonal"
      class="mb-4"
    />

    <v-container v-if="isLoading" class="d-flex justify-center pa-8">
      <v-progress-circular indeterminate color="primary" />
    </v-container>

    <v-alert
      v-else-if="challenges.length === 0"
      text="No challenges are available yet."
      type="info"
      variant="tonal"
    />

    <v-row v-else align="stretch">
      <v-col
        v-for="challenge in filteredChallenges"
        :key="challenge.id"
        cols="12"
        sm="6"
        lg="4"
      >
        <v-card
          class="h-100"
          variant="outlined"
          link
          :to="{ name: 'solveChallenge', params: { id: challenge.id } }"
        >
          <v-card-title class="d-flex align-center justify-space-between">
            {{ challenge.name }}
            <v-chip size="small" color="primary">
              {{ formatChallengeValue(challenge.difficulty) }}
            </v-chip>
          </v-card-title>
          <v-card-subtitle>
            {{ formatChallengeValue(challenge.category) }}
          </v-card-subtitle>
          <v-card-text>{{ challenge.description }}</v-card-text>
          <v-card-actions>
            <v-btn color="primary">Solve challenge</v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>
