<script setup lang="ts">
import Footer from '@/components/navigation/Footer.vue'
</script>

<template>
  <v-container>
    <v-card variant="text">
      <v-card-title class="px-0">MiniLeetCode</v-card-title>
      <v-card-text class="px-0">
        MiniLeetCode is designed for anyone wanting to learn programming fundamentals through
        bite-sized problems. Build skills and practice solving common algorithmic tasks efficiently
      </v-card-text>
      <v-card-actions class="mt-5 px-0">
        <RouterLink to="/Signup">
          <v-btn color="Secondary">Create an account</v-btn>
        </RouterLink>
      </v-card-actions>
    </v-card>
  </v-container>
  <v-container>
    <v-card variant="text">
      <v-card-title class="px-0">What does this service offer</v-card-title>
      <v-card-text class="px-0">
        This service offers a variety of coding challenges you can solve.<br />
        You choose your favorite programming language and start solving the challenges immediately.<br />
        You can select curated sets of diverse challenges suited perfectly for your skill level and
        begin practicing right away.
      </v-card-text>
      <v-card-actions class="mt-5 px-0">
        <RouterLink to="/challenges">
          <v-btn color="primary">Start by viewing available challenges</v-btn>
        </RouterLink>
      </v-card-actions>
    </v-card>
  </v-container>
  <v-container>
    <v-card variant="text">
      <v-card-title class="px-0">What this service supports</v-card-title>
      <v-card-text class="text-body-1 px-0 mb-7">
        This platform provides a wide selection of coding challenges to enhance your programming
        skills.<br />
        You can choose from supported languages (Python, C#, Java, and C++) and use the challenge
        editor to write and test your solution.
      </v-card-text>
      <v-list bg-color="transparent" rounded lines="two">
        <v-list-item title="Array" />
        <v-divider />
        <v-list-item title="Linked List" />
        <v-divider />
        <v-list-item title="Binary Tree" />
      </v-list>
    </v-card>
  </v-container>
  <Footer></Footer>
</template>
