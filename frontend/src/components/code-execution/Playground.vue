<script setup lang="ts">
import {ref} from "vue";

const code = ref("");
const language = ref("python");
const output = ref("");
const isExecuting = ref(false);
const supportedLanagues = ref(["python", "C++", "Java", "c#"]);

async function executeCode() {
  if (!code.value.trim()) {
    output.value = "Please enter Python code first.";
    return;
  }

  isExecuting.value = true;
  output.value = "";

  try {
    const response = await fetch("http://localhost:8082/api/code/playground/submit", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        language: language.value,
        code: code.value,
      }),
    });

    const result = await response.text();
    output.value = response.ok ? result : `Error: ${result}`;
  } catch (error) {
    output.value = `Could not connect to the backend: ${String(error)}`;
  } finally {
    isExecuting.value = false;
  }
}





</script>

<template>
  <v-container>
    <v-textarea
      v-model="code"
      label="Python code"
      rows="10"
      auto-grow
      lin
    ></v-textarea>
    <v-select
      v-model="language"
      :items="supportedLanagues"
      label="Language"
    ></v-select>
    <v-btn color="primary" :loading="isExecuting" @click="executeCode">
      Execute
    </v-btn>
    <v-card v-if="output" class="mt-4">
      <v-card-title>Output</v-card-title>
      <v-card-text>{{ output }}</v-card-text>
    </v-card>
  </v-container>
</template>
