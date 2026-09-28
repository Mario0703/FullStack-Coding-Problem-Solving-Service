<script setup lang="ts">
function AdminSignIn(){

}

</script>

<template>
  <v-container class="d-flex align-center justify-center">
    <v-form @submit.prevent="AdminSignIn" class="elevation-12 pa-6" style="max-width: 400px; width: 100%;">
      <v-text-field
        placeholder="AdminUserName"
        outlined
        dense
        class="mb-4"
      ></v-text-field>
      <v-text-field
        placeholder="AdminPassword"
        outlined
        dense
        type="password"
        class="mb-4"
      ></v-text-field>
      <v-btn type="submit" color="primary" block>Login</v-btn>
    </v-form>
  </v-container>
</template>

<style scoped>

</style>
