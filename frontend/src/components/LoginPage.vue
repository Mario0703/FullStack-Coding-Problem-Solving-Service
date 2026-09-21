<script setup lang="ts">


function LogIn() {
  console.log("Login function called");
}

</script>

<template>
  <v-container style="display: flex; align-items: center; justify-content: center;">
    <v-form class="signup-form" @submit.prevent="LogIn">
      <h1>Login to your account</h1>
      <v-text-field label="Username" placeholder="Type your username" ></v-text-field>
      <v-text-field label="Password" placeholder="Type your password" type="password"></v-text-field>
      <v-btn type="submit" color="primary">Login</v-btn>
    </v-form>
  </v-container>

</template>

<style scoped>

</style>
