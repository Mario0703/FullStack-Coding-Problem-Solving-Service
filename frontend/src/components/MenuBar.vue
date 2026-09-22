<script setup lang="ts">
</script>
<template>
  <v-app-bar color="primary">
    <v-app-bar-title>MiniLeetCode</v-app-bar-title>
    <v-spacer />
    <v-btn color="white" variant="text" :to="{ name: 'signup' }">Sign up</v-btn>
    <v-btn color="white" variant="text" to="/challenges">Explore coding challenges</v-btn>
    <v-btn color="white" variant="text" to="/about">About the project</v-btn>
    <v-btn color="white" variant="text" to="/contact">Contact</v-btn>
    <v-btn color="white" variant="text" to="/">Home</v-btn>
    <v-btn color="white" variant="text" to="/login">Login</v-btn>
  </v-app-bar>
</template>
