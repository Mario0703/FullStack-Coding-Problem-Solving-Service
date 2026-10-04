<script setup lang="ts">
import { computed, onMounted, ref } from 'vue'

export type PublicUser = {
  userId: string
  userName: string
  email: string
}

</script>

<template>
  <div class="public-user-profile">
    <h2>{{ publicUser.userName }}</h2>
    <p>{{ publicUser.email }}</p>
  </div>
</template>
