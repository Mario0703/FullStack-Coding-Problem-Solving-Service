<script setup lang="ts">
import { onMounted, ref } from "vue";
import AddChallange from "@/components/AdminPages/AddChallange.vue";
import { formatChallengeValue, type Challenge } from "@/utils/AdminUtils";
import { listChallenges } from "@/utils/ChallangeUtil";


const challenges = ref<Challenge[]>([]);
const loadError = ref("");
const successMessage = ref("");
const isLoading = ref(true);

async function loadChallenges() {
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

function addChallenge(challenge: Challenge) {
  challenges.value = [challenge, ...challenges.value];
  successMessage.value = "Challenge created successfully.";
}

onMounted(loadChallenges);
</script>

<template>
  <v-container class="py-8">
    <div class="mb-8">
      <h1 class="text-h4 font-weight-bold">Admin dashboard</h1>
      <p class="text-body-1 mt-2">Create and review coding challenges.</p>
    </div>

    <AddChallange @created="addChallenge" />

    <v-alert
      v-if="successMessage"
      :text="successMessage"
      type="success"
      variant="tonal"
      class="mt-4"
    />

    <v-divider class="my-8" />

    <div class="d-flex align-center justify-space-between mb-4">
      <h2 class="text-h5 font-weight-bold">Challenges</h2>
      <v-btn variant="text" @click="loadChallenges">Refresh</v-btn>
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
      text="No challenges have been created yet."
      type="info"
      variant="tonal"
    />

    <v-row v-else>
      <v-col v-for="challenge in challenges" :key="challenge.id" cols="12" md="6">
        <v-card class="h-100" variant="outlined">
          <v-card-title class="d-flex align-center justify-space-between">
            <span>{{ challenge.name }}</span>
            <v-chip size="small" color="primary">
              {{ formatChallengeValue(challenge.difficulty) }}
            </v-chip>
          </v-card-title>
          <v-card-subtitle>{{ formatChallengeValue(challenge.category) }}</v-card-subtitle>
          <v-card-text>{{ challenge.description }}</v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
</style>
