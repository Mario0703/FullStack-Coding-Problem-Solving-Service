<script setup lang="ts">
import { useRouter } from "vue-router";
import SuccessFeedback from "@/components/FeedBack/Succes.vue";
import FadeContent from "@/components/ui/FadeContent.vue";

const router = useRouter();

function goToAdminDashboard() {
  router.push({ name: "adminDashboard" });
}
</script>

<template>
  <FadeContent
    :blur="true"
    :duration="1000"
    :initial-opacity="0"
    ease="power2.out"
  >
    <v-container class="d-flex flex-column align-center justify-center h-screen">
      <v-card color="grey-lighten-2" class="pa-4 text-center" variant="tonal">
        <SuccessFeedback
          title="Admin login successful"
          message="You are now logged in as an administrator."
        />
      </v-card>
      <v-btn color="grey-darken-4" variant="elevated" class="mt-4" @click="goToAdminDashboard">
        Go to admin dashboard
      </v-btn>
    </v-container>
  </FadeContent>
</template>
