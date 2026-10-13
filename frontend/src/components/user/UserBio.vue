<script setup lang="ts">
import { computed, ref } from "vue";
import { changeBio } from "@/utils/Users/api";
import { currentUser } from "@/utils/Users/state";
import type { UserData } from "@/utils/Users/types";
import ErrorFeedback from "@/components/FeedBack/Error.vue";
import { readErrorMessage } from "@/utils/apiError";

// Toggles between a read-only bio and the edit form; saving sends the bio
// to the backend and refreshes the local user state from the response.
const isEditing = ref(false);
const isSaving = ref(false);
const bioText = ref("");
const saveError = ref("");

const hasBio = computed(() => (currentUser.value?.bio ?? "").trim() !== "");

function startEditing() {
  bioText.value = currentUser.value?.bio ?? "";
  saveError.value = "";
  isEditing.value = true;
}

function stopEditing() {
  isEditing.value = false;
  saveError.value = "";
}

async function saveBio() {
  saveError.value = "";
  isSaving.value = true;

  try {
    const response = await changeBio(bioText.value);

    if (!response.ok) {
      saveError.value = await readErrorMessage(response, "Unable to save the bio.");
      return;
    }

    currentUser.value = (await response.json()) as UserData;
    isEditing.value = false;
  } catch {
    saveError.value = "Unable to reach the server. Please try again later.";
  } finally {
    isSaving.value = false;
  }
}
</script>

<template>
  <v-card color="grey-lighten-1" class="w-100 d-flex flex-column" variant="outlined">
    <v-card-title class="text-subtitle-1 font-weight-bold text-center d-flex align-center justify-center">
      <v-icon icon="mdi-text-box-outline" size="small" color="indigo" class="mr-1" />
      <span style="color: white">Bio</span>
    </v-card-title>

    <template v-if="!isEditing">
      <v-card-text class="text-body-1 text-center flex-grow-1">
        <span style="color: white">
          {{ hasBio ? currentUser?.bio : 'No bio yet. Tell others what you practice.' }}
        </span>
      </v-card-text>
      <v-card-actions class="justify-center pb-4">
        <v-btn
          size="large"
          variant="elevated"
          color="grey-darken-4"
          prepend-icon="mdi-pencil-outline"
          @click="startEditing"
        >
          {{ hasBio ? 'Edit bio' : 'Add bio' }}
        </v-btn>
      </v-card-actions>
    </template>

    <v-card-text v-else class="pa-4">
      <ErrorFeedback v-if="saveError" :message="saveError" />

      <v-textarea
        v-model="bioText"
        label="Your bio"
        placeholder="Write a few lines about yourself..."
        rows="3"
        variant="outlined"
        counter
        maxlength="500"
        auto-grow
        hide-details="auto"
      />

      <v-card-actions class="justify-center ga-2 pt-2">
        <v-btn
          color="grey-darken-4"
          variant="elevated"
          prepend-icon="mdi-check"
          :loading="isSaving"
          :disabled="isSaving"
          @click="saveBio"
        >
          Save
        </v-btn>
        <v-btn
          color="grey-darken-4"
          variant="elevated"
          prepend-icon="mdi-close"
          :disabled="isSaving"
          @click="stopEditing"
        >
          Cancel
        </v-btn>
      </v-card-actions>
    </v-card-text>
  </v-card>
</template>