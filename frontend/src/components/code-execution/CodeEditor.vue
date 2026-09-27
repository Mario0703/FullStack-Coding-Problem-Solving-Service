<script setup lang="ts">
import { computed } from 'vue'
import { Codemirror } from 'vue-codemirror'
import { cpp } from '@codemirror/lang-cpp'
import { java } from '@codemirror/lang-java'
import { python } from '@codemirror/lang-python'
import { oneDark } from '@codemirror/theme-one-dark'
import { csharp } from '@replit/codemirror-lang-csharp'

const props = withDefaults(
  defineProps<{
    modelValue: string
    language: string
    readonly?: boolean
    height?: string
  }>(),
  { readonly: false, height: '400px' },
)

const emit = defineEmits<{
  'update:modelValue': [value: string]
}>()

const extensions = computed(() => {
  const languageExtension = {
    PYTHON: python(),
    JAVA: java(),
    'C++': cpp(),
    'C#': csharp(),
  }[props.language.toUpperCase()]

  return languageExtension ? [languageExtension, oneDark] : [oneDark]
})
</script>

<template>
  <Codemirror
    :model-value="modelValue"
    :extensions="extensions"
    :editable="!readonly"
    :style="{ height, fontSize: '14px' }"

    placeholder="Write your code here..."
    @update:model-value="emit('update:modelValue', $event)"
  />
</template>
