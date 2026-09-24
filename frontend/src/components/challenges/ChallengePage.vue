<script setup lang="ts">
import { onMounted, ref } from "vue";
import type { Challenge } from "@/utils/AdminUtils";
import { formatChallengeValue } from "@/utils/AdminUtils";
import { listChallenges } from "@/utils/ChallangeUtil";

const challenges = ref<Challenge[]>([]);
const loadError = ref("");
const isLoading = ref(true);

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

</script>

<template>
  <v-container class="py-8">
    <div class="mb-8">
      <h1 class="text-h4 font-weight-bold">Coding challenges</h1>
      <p class="text-body-1 mt-2">Choose a challenge and start practicing.</p>
    </div>

    <v-alert
      v-if="loadError"
      title="Could not load challenges"
      :text="loadError"
      type="error"
      variant="tonal"
      class="mb-4"
    />

    <div v-if="isLoading" class="d-flex justify-center pa-8">
      <v-progress-circular indeterminate color="primary" />
    </div>

    <v-alert
      v-else-if="challenges.length === 0"
      text="No challenges are available yet."
      type="info"
      variant="tonal"
    />

    <v-row v-else align="stretch">
      <v-col
        v-for="challenge in challenges"
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
            <span>{{ challenge.name }}</span>
            <v-chip size="small" color="primary">
              {{ formatChallengeValue(challenge.difficulty) }}
            </v-chip>
          </v-card-title>
          <v-card-subtitle>
            {{ formatChallengeValue(challenge.catagory) }}
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
