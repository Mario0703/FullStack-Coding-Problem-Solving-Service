<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import type { Challenge } from "@/utils/AdminUtils";
import { formatChallengeValue } from "@/utils/AdminUtils";
import { listChallenges } from "@/utils/ChallangeUtil";
import Playground from "@/components/code-execution/Playground.vue";

const route = useRoute();
const challenge = ref<Challenge | null>(null);
const loadError = ref("");
const isLoading = ref(true);

async function loadChallenge() {
  try {
    const challengeId = String(route.params.id);
    const challenges = await listChallenges();
    challenge.value = challenges.find((item) => item.id === challengeId) ?? null;

    if (!challenge.value) {
      loadError.value = "The requested challenge could not be found.";
    }
  } catch {
    loadError.value = "Unable to load the challenge. Please try again later.";
  } finally {
    isLoading.value = false;
  }
}

onMounted(loadChallenge);
</script>

<template>
  <v-container class="py-8">
    <div v-if="isLoading" class="d-flex justify-center pa-8">
      <v-progress-circular indeterminate color="primary" />
    </div>

    <v-alert
      v-else-if="loadError"
      :text="loadError"
      title="Challenge unavailable"
      type="error"
      variant="tonal"
    />

    <v-card v-else-if="challenge" variant="outlined">
      <v-card-title class="text-h4">{{ challenge.name }}</v-card-title>
      <v-card-subtitle>
        {{ formatChallengeValue(challenge.difficulty) }} ·
        {{ formatChallengeValue(challenge.catagory) }}
      </v-card-subtitle>
      <v-card-text class="text-body-1">{{ challenge.description }}</v-card-text>
      <v-card-actions>
        <v-btn to="/challenges" variant="text">Back to challenges</v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
  <Playground></Playground>
</template>

<style scoped>
</style>
