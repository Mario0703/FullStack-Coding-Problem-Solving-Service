<script setup lang="ts">
import Footer from '@/components/navigation/Footer.vue'
</script>

<template>
  <v-container>
    <v-card variant="text">
      <v-card-title class="px-0 text-display-large">MiniLeetCode</v-card-title>
      <v-card-text class="px-0 text-body-medium">
        MiniLeetCode is designed for anyone wanting to learn programming fundamentals through
        bite-sized problems. Build skills and practice solving common algorithmic tasks efficiently
      </v-card-text>
      <v-card-actions class="mt-5 px-0">
        <RouterLink to="/Signup">
          <v-btn variant="flat" color="white"  size="large" prepend-icon="mdi-account-plus" > Create an account</v-btn>
        </RouterLink>
      </v-card-actions>
    </v-card>
  </v-container>
  <v-container>
    <v-card variant="text">
      <v-card-title class="px-0 font-weight-bold">What does this service offer</v-card-title>
      <v-card-text class="px-0 py-0">
        This service offers a variety of coding challenges you can solve.
      </v-card-text>
      <v-card-text class="px-0 py-0">
        You choose your favorite programming language and start solving the challenges immediately.
      </v-card-text>
      <v-card-text class="px-0 py-0">
        You can select curated sets of diverse challenges suited perfectly for your skill level and
        begin practicing right away.
      </v-card-text>
      <v-card-actions class="mt-5 px-0">
        <RouterLink to="/challenges">
          <v-btn variant="flat" color="white"  size="large" prepend-icon="mdi-eye">Start by viewing available challenges</v-btn>
        </RouterLink>
      </v-card-actions>
    </v-card>
  </v-container>
  <v-container>
    <v-card variant="text">
      <v-card-title class="px-0 font-weight-bold">What this service supports</v-card-title>
      <v-card-text class="text-body-1 px-0 py-0">
        This platform provides a wide selection of coding challenges to enhance your programming
        skills.
      </v-card-text>
      <v-card-text class="text-body-1 px-0 mb-7 py-0">
        You can choose from supported languages (Python, C#, Java, and C++) and use the challenge
        editor to write and test your solution. You can also view the results of your solution and compare it with other users' solutions.
        Solve challanges on the form
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

  <v-container>
    <v-card variant="text">
      <v-card-title class="px-0 font-weight-bold">How MiniLeetCode works</v-card-title>
      <v-card-text class="px-0 py-0">
        Getting started takes less than a minute. Create a free account, pick a challenge,
        and solve it directly in your browser.
      </v-card-text>
      <v-card-text class="px-0 py-0">
        Write your solution in the built-in editor, choose from Python, Java, C#, or C++, and
        submit it to run the test cases instantly. You will see exactly which cases pass and
        which still need work, so you always know what to improve next.
      </v-card-text>
      <v-list bg-color="transparent" rounded lines="two">
        <v-list-item title="1. Create your free account" />
        <v-divider />
        <v-list-item title="2. Choose a challenge by topic or difficulty" />
        <v-divider />
        <v-list-item title="3. Solve it, submit it, and track your progress" />
      </v-list>
      <v-card-actions class="mt-5 px-0">
        <RouterLink to="/Signup">
          <v-btn color="white"  size="large" variant="flat">Get started for free</v-btn>
        </RouterLink>
      </v-card-actions>
    </v-card>
  </v-container>

  <v-container>
    <v-card variant="text">
      <v-card-title class="px-0 font-weight-bold">Why practice with MiniLeetCode</v-card-title>
      <v-card-text class="px-0 py-0">
        Consistent, bite-sized practice is the fastest way to build real problem-solving
        skills. Every challenge is small enough to finish in one sitting, so you can keep a
        steady habit without feeling overwhelmed.
      </v-card-text>
      <v-card-text class="px-0 py-0">
        Work through arrays, linked lists, binary trees and more, then review your submissions
        to spot patterns and reinforce what you have learned. Whether you are preparing for
        interviews or simply sharpening your fundamentals, there is always a next challenge to
        grow with.
      </v-card-text>
      <v-card-actions class="mt-5 px-0">
        <RouterLink to="/challenges">
          <v-btn color="white"  size="large" variant="flat">Browse all challenges</v-btn>
        </RouterLink>
      </v-card-actions>
    </v-card>
  </v-container>

  <Footer />
</template>
