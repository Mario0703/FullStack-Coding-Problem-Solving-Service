<script setup lang="ts">
import MenuBar from './MenuBar.vue'
import PlayGround from "@/components/PlayGround.vue";
import Footer from "@/components/Footer.vue";
</script>

<template>
  <v-container>
    <MenuBar />
    <h1>MiniLeetCode</h1>
  <span>
    MiniLeetCode is designed for anyone wanting to learn programming fundamentals through bite-sized problems.
    Build skills and practice solving common algorithmic tasks efficiently
  </span>
    <div style="margin-top: 20px;">
      <RouterLink to="/Signup">
        <v-btn color="Secondary">Create an account</v-btn>
      </RouterLink>
    </div>
  </v-container>
  <v-container>
    <h2>What does this service offer</h2>
    <span>
      This service offers a variety of coding challenges you can solve.<br />
      You choose your favorite programming language and start solving the challenges immediately.<br />
      You can select curated sets of diverse challenges suited perfectly for your skill level and begin practicing right away.
    </span>
    <div style="margin-top: 20px;">
      <RouterLink to="/challenges">
        <v-btn color="primary">Start by viewing available challenges</v-btn>
      </RouterLink>
    </div>
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
    <PlayGround></PlayGround>
  </v-container>
<Footer></Footer>
</template>

<style scoped>

</style>
