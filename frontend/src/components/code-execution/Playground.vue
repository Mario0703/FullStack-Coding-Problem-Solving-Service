<script setup lang="ts">
import { ref, watch } from "vue";

type Language = "python" | "java" | "cpp" | "csharp";

const templates: Record<Language, string> = {
  python: `def main():
    # Write your Python code here
    pass


if __name__ == "__main__":
    main()
`,
  java: `public class Main {
    public static void main(String[] args) {
        // Write your Java code here
    }
}
`,
  cpp: `#include <iostream>

int main() {
    // Write your C++ code here
    return 0;
}
`,
  csharp: `using System;

public class Main {
    public static void Main(string[] args) {
        // Write your C# code here
    }
}
`,
};

const languageOptions = [
  { title: "Python", value: "python" },
  { title: "Java", value: "java" },
  { title: "C++", value: "cpp" },
  { title: "C#", value: "csharp" },
];

const language = ref<Language>("python");
const code = ref(templates.python);
const output = ref("");
const isExecuting = ref(false);

watch(language, (selectedLanguage) => {
  code.value = templates[selectedLanguage];
  output.value = "";
});

async function executeCode() {
  if (!code.value.trim()) {
    output.value = "Please enter code first.";
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
      :label="`${language} code`"
      rows="10"
      auto-grow
    />

    <v-select
      v-model="language"
      :items="languageOptions"
      label="Language"
    />

    <v-btn color="primary" :loading="isExecuting" @click="executeCode">
      Execute
    </v-btn>

    <v-card v-if="output" class="mt-4">
      <v-card-title>Output</v-card-title>
      <v-card-text>{{ output }}</v-card-text>
    </v-card>
  </v-container>
</template>
