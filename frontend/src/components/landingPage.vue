<script setup lang="ts">


</script>

<template>
  <v-container>
  <v-app-bar color="primary">
    <v-app-bar-title>MiniLeetCode</v-app-bar-title>
    <v-btn color="Primary">Sign in</v-btn>
    <v-btn color="Primary">Explore coding challenges</v-btn>
    <v-btn color="Primary">Get started</v-btn>
  </v-app-bar>
    <h1>MiniLeetCode</h1>
  <span>
    MiniLeetCode is designed for anyone wanting to learn programming fundamentals through bite-sized problems.
    Build skills and practice solving common algorithmic tasks efficiently
  </span>
      <v-btn color="Secondary">Create an account</v-btn>
  </v-container>
  <v-container>
    <h2>What does this service offer</h2>
    <span>
      This service offers a variety of coding challenges you can solve.<br />
      You choose your favorite programming language and start solving the challenges immediately.<br />
      You can select curated sets of diverse challenges suited perfectly for your skill level and begin practicing right away.
    </span>
    <v-bt color="primary">Start by viewing available challenges</v-bt>
  </v-container>
  <v-container>
    <h2>What this service supports</h2>
    <span style="font-size: 18px; line-height: 1.6; margin-bottom: 30px;">
      This platform provides a wide selection of coding challenges to enhance your programming skills.<br />
      You can choose from supported languages (Python, C#, Java, and C++)
      or use our playground to practice without an account.
      Our powerful development tools such as Playground help you test,
      debug and even write your own projects online
    </span>
    <div>
      <v-list
        bg-color="transparent"
        rounded
        lines="two"
      >
        <v-list-item title="Array" />
        <v-divider />
        <v-list-item title="Linked List" />
        <v-divider />
        <v-list-item title="Binary Tree" />
      </v-list>
    </div>
  </v-container>

</template>

<style scoped>

</style>
