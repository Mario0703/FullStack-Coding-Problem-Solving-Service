<script setup lang="ts">

import {ref} from "vue";

const easyQuestionsSolved = ref(10);
const mediumQuestionsSolved = ref(5);
const hardQuestionsSolved = ref(2);

</script>

<template>
  <v-container>
    <v-col>
      <bold> Number of easy questions solved</bold>
      <v-row>
        {{easyQuestionsSolved}}
      </v-row>
    </v-col>
    <v-col>
      <bold> Number of medium questions solved</bold>
      <v-row>
        {{mediumQuestionsSolved}}
      </v-row>
    </v-col>
    <v-col>
      <bold>Number of hard questions solved</bold>
      <v-row>
        {{hardQuestionsSolved}}
      </v-row>
    </v-col>
  </v-container>

</template>

<style scoped>

</style>
