<script setup lang="ts">
const easyQuestionsSolved = 10
const mediumQuestionsSolved = 5
const hardQuestionsSolved = 2
</script>

<template>
  <v-container class="py-8">
    <v-row align="stretch">
      <v-col cols="12" sm="4">
        <v-card class="border-lg h-100" border="info" color="green-lighten-1" variant="outlined">
          <v-card-title class="text-headline-large font-weight-bold text-center">
            Easy questions solved
          </v-card-title>
          <v-card-text class="text-display-medium font-weight-bold text-center">
            {{ easyQuestionsSolved }}
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" sm="4">
        <v-card class="border-lg h-100" border="info" color="yellow-lighten-1" variant="outlined">
          <v-card-title class="text-headline-large font-weight-bold text-center">
            Medium questions solved
          </v-card-title>
          <v-card-text class="text-display-medium font-weight-bold text-center">
            {{ mediumQuestionsSolved }}
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" sm="4">
        <v-card class="border-lg h-100" border="info" color="red-darken-4" variant="outlined">
          <v-card-title class="text-headline-large font-weight-bold text-center">
            Hard questions solved
          </v-card-title>
          <v-card-text class="text-display-medium font-weight-bold text-center">
            {{ hardQuestionsSolved }}
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>
