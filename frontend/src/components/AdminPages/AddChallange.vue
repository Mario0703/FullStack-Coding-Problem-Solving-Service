<script setup lang="ts">
import { ref } from "vue";
import {
  categoryOptions,
  difficultyOptions,
  type Challenge,
  type ChallengeCategory,
  type ChallengeDifficulty,
} from "@/utils/AdminUtils";


const emit = defineEmits<{
  created: [challenge: Challenge];
}>();

const name = ref("");
const description = ref("");
const difficulty = ref<ChallengeDifficulty | null>(null);
const catagory = ref<ChallengeCategory | null>(null);
const formError = ref("");
const isSubmitting = ref(false);

function resetForm() {
  name.value = "";
  description.value = "";
  difficulty.value = null;
  catagory.value = null;
}

async function createChallenge() {
  formError.value = "";

  if (!name.value.trim() || !description.value.trim() || !difficulty.value || !catagory.value) {
    formError.value = "Please complete all fields before creating the challenge.";
    return;
  }

  isSubmitting.value = true;

  try {
    const response = await fetch("http://localhost:8082/api/challenge/create", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      credentials: "include",
      body: JSON.stringify({
        name: name.value.trim(),
        description: description.value.trim(),
        difficulty: difficulty.value,
        catagory: catagory.value,
      }),
    });

    if (!response.ok) {
      formError.value = "The challenge could not be created. Please check the fields and try again.";
      return;
    }

    const createdChallenge = (await response.json()) as Challenge;
    emit("created", createdChallenge);
    resetForm();
  } catch {
    formError.value = "Unable to reach the server. Please try again later.";
  } finally {
    isSubmitting.value = false;
  }
}
</script>

<template>
  <v-card class="mx-auto" max-width="720" variant="outlined">
    <v-card-title class="text-h5 font-weight-bold pa-6">Add challenge</v-card-title>
    <v-card-text>
      <v-alert
        v-if="formError"
        title="Challenge creation failed"
        :text="formError"
        type="error"
        variant="tonal"
        class="mb-4"
      />

      <v-form @submit.prevent="createChallenge">
        <v-text-field
          v-model="name"
          label="Challenge name"
          placeholder="Two Sum"
          :counter="50"
          maxlength="50"
          required
        />
        <v-textarea
          v-model="description"
          label="Description"
          placeholder="Describe what the user needs to solve."
          :counter="500"
          maxlength="500"
          rows="4"
          required
        />
        <v-select v-model="difficulty" label="Difficulty" :items="difficultyOptions" required />
        <v-select v-model="catagory" label="Category" :items="categoryOptions" required />
        <v-btn
          type="submit"
          color="primary"
          :loading="isSubmitting"
          :disabled="isSubmitting"
        >
          Create challenge
        </v-btn>
      </v-form>
    </v-card-text>
  </v-card>
</template>

<style scoped>
</style>
