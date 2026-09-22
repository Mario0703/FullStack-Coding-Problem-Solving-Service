<script setup lang="ts">
const easyQuestionsSolved = 10
const mediumQuestionsSolved = 5
const hardQuestionsSolved = 2
</script>

<template>
  <v-container class="py-8">
    <v-row align="stretch">
      <v-col cols="12" sm="4">
        <v-card class="border-lg h-100" border="primary" color="green-lighten-4" variant="outlined">
          <v-card-title class="text-headline-large font-weight-bold text-center">
            Easy questions solved
          </v-card-title>
          <v-card-text class="text-display-medium font-weight-bold text-center">
            {{ easyQuestionsSolved }}
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" sm="4">
        <v-card class="border-lg h-100" border="primary" color="yellow-lighten-4" variant="outlined">
          <v-card-title class="text-headline-large font-weight-bold text-center">
            Medium questions solved
          </v-card-title>
          <v-card-text class="text-display-medium font-weight-bold text-center">
            {{ mediumQuestionsSolved }}
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" sm="4">
        <v-card class="border-lg h-100" border="primary" color="red-lighten-4" variant="outlined">
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
  <v-container>
    <v-row>
      <v-col cols="12" sm="6">
        <v-card class="border-lg h-100" border="primary" color="blue-lighten-4" variant="outlined">
          <v-card-title class="text-headline-large font-weight-bold text-center">
            Total questions solved
          </v-card-title>
          <v-card-text class="text-display-medium font-weight-bold text-center">
            {{ easyQuestionsSolved + mediumQuestionsSolved + hardQuestionsSolved }}
          </v-card-text>
        </v-card>
      </v-col>

      <v-col cols="12" sm="6">
        <v-card class="border-lg h-100" border="primary" color="purple-lighten-4" variant="outlined">
          <v-card-title class="text-headline-large font-weight-bold text-center">
            Total points earned
          </v-card-title>
          <v-card-text class="text-display-medium font-weight-bold text-center">
            {{ easyQuestionsSolved * 1 + mediumQuestionsSolved * 2 + hardQuestionsSolved * 3 }}
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>
