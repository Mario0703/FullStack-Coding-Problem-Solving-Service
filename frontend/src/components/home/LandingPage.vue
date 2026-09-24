<script setup lang="ts">
import Footer from '@/components/navigation/Footer.vue'
</script>

<template>
  <v-container>
    <h1>MiniLeetCode</h1>
    <span>
      MiniLeetCode is designed for anyone wanting to learn programming fundamentals through
      bite-sized problems. Build skills and practice solving common algorithmic tasks efficiently
    </span>
    <div class="mt-5">
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
      You can select curated sets of diverse challenges suited perfectly for your skill level and
      begin practicing right away.
    </span>
    <div class="mt-5">
      <RouterLink to="/challenges">
        <v-btn color="primary">Start by viewing available challenges</v-btn>
      </RouterLink>
    </div>
  </v-container>
  <v-container>
    <h2>What this service supports</h2>
    <span class="text-body-1 d-block mb-7">
      This platform provides a wide selection of coding challenges to enhance your programming
      skills.<br />
      You can choose from supported languages (Python, C#, Java, and C++) and use the challenge
      editor to write and test your solution.
    </span>
    <div>
      <v-list bg-color="transparent" rounded lines="two">
        <v-list-item title="Array" />
        <v-divider />
        <v-list-item title="Linked List" />
        <v-divider />
        <v-list-item title="Binary Tree" />
      </v-list>
    </div>
  </v-container>
  <Footer></Footer>
</template>
