<script setup lang="ts">
import { onMounted, ref, watch } from "vue";
import { formatChallengeValue, type Challenge } from "@/utils/AdminUtils";
import { listAdminChallenges } from "@/utils/ChallangeUtil";

const props = defineProps<{
  newChallenge?: Challenge | null;
}>();

const challenges = ref<Challenge[]>([]);
const loadError = ref("");
const isLoading = ref(true);
import { useRouter } from "vue-router";

const router = useRouter();

function viewChallangeDetails(challengeId: string) {
  router.push(`/admin/challenge/${challengeId}`);
}

async function loadChallenges() {
  loadError.value = "";
  isLoading.value = true;

  try {
    const loadedChallenges = await listAdminChallenges();
    challenges.value = props.newChallenge
      ? [
          props.newChallenge,
          ...loadedChallenges.filter((challenge) => challenge.id !== props.newChallenge?.id),
        ]
      : loadedChallenges;
  } catch {
    loadError.value = "Unable to load challenges. Please try again later.";
  } finally {
    isLoading.value = false;
  }
}


watch(
  () => props.newChallenge,
  (challenge) => {
    if (challenge && !challenges.value.some(({ id }) => id === challenge.id)) {
      challenges.value = [challenge, ...challenges.value];
    }
  },
);


onMounted(loadChallenges);

async function deleteChallenge(challengeId: string) {
  try {
    const response = await fetch(
      `http://localhost:8082/api/challenge/delete?challengeId=${encodeURIComponent(challengeId)}`,
      {
        method: 'DELETE',
        credentials: 'include',
      },
    );

    if (!response.ok) {
      throw new Error('Failed to delete challenge');
    }

    challenges.value = challenges.value.filter(({ id }) => id !== challengeId);
  } catch {
    loadError.value = 'Unable to delete challenge. Please try again.';
  }
}


</script>

<template>
  <v-container>
    <v-card class="d-flex align-center justify-space-between mb-4" variant="text">
      <v-card-title class="text-h5 font-weight-bold px-0">Challenges</v-card-title>
      <v-card-actions class="pa-0">
        <v-btn variant="text" :loading="isLoading" @click="loadChallenges">Refresh</v-btn>
      </v-card-actions>
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
      text="No challenges have been created yet."
      type="info"
      variant="tonal"
    />

    <v-row v-else>
      <v-col v-for="challenge in challenges" :key="challenge.id" cols="12" md="6">
        <v-card class="h-100" variant="outlined">
          <v-card-title class="d-flex align-center justify-space-between">
            {{ challenge.name }}
            <v-chip size="small" color="primary">
              {{ formatChallengeValue(challenge.difficulty) }}
            </v-chip>
          </v-card-title>
          <v-card-subtitle>{{ formatChallengeValue(challenge.category) }}</v-card-subtitle>
          <v-card-text>{{ challenge.description }}</v-card-text>
          <v-container>
            <v-list class="d-flex">
              <v-list-item class="flex-none">
                <v-btn @click="viewChallangeDetails(challenge.id)" color="primary">View Details</v-btn>
              </v-list-item>
              <v-list-item class="flex-none">
                <v-btn @click="deleteChallenge(challenge.id)" color="red">Delete Challenge</v-btn>
              </v-list-item>
            </v-list>
          </v-container>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped></style>
