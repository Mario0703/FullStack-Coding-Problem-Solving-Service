<script setup lang="ts">
import { computed } from "vue";
import { useRouter } from "vue-router";
import { currentUser } from "@/utils/Users/state";
import SuccessFeedback from "@/components/FeedBack/Succes.vue";

const router = useRouter();
const userName = computed(() => currentUser.value?.userName);

function SendUserToUser() {
  router.push({ name: "user" });
}
</script>

<template>
  <v-container class="d-flex flex-column align-center justify-center h-screen">
    <v-card color="grey-lighten-2" class="pa-4 text-center" variant="tonal">
      <SuccessFeedback
        title="Login successful"
        :message="`Welcome, ${userName}! You are now logged in and can proceed to your user page.`"
      />
    </v-card>
    <v-btn color="grey-darken-4" variant="elevated" class="mt-4" @click="SendUserToUser">
      Go to my user page
    </v-btn>
  </v-container>
</template>
