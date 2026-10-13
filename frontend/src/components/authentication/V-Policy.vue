<script setup lang="ts">
// Security & privacy notice shown to account holders.
//
// This is intentionally plain text so it can be embedded in a dialog or a
// dedicated policy page. Keep it factual: the app is a CV project, data is
// stored in a database, and users should not submit sensitive information.
const privacyText =
  'MiniLeetCode is a personal CV / portfolio project and is not intended for production use. ' +
  'The account details you provide (username, email and password) are stored securely in a ' +
  'database. Because this project exists to demonstrate software engineering skills rather ' +
  'than to run a real service, please do not enter sensitive or real personal information, ' +
  'and avoid reusing a password from another site.'

const props = defineProps<{
  /** Whether the user has accepted the privacy policy. */
  hasAgreed: boolean
}>()

const emit = defineEmits<{
  'update:hasAgreed': [value: boolean]
  accept: []
  decline: []
}>()

function accept() {
  emit('update:hasAgreed', true)
  emit('accept')
}

function decline() {
  emit('update:hasAgreed', false)
  emit('decline')
}
</script>

<template>
  <v-sheet class="policy-sheet">
    <v-card-title class="px-0 font-weight-bold">Security &amp; privacy notice</v-card-title>
    <v-card-text class="px-0 text-body-medium">{{ privacyText }}</v-card-text>

    <v-alert
      v-if="props.hasAgreed"
      type="success"
      variant="tonal"
      density="compact"
      class="mb-2"
    >
      You have accepted the privacy policy.
    </v-alert>

    <v-card-actions class="px-0">
      <v-btn color="grey-darken-4" variant="elevated" @click="accept">Accept</v-btn>
      <v-btn variant="text" color="grey-darken-4" @click="decline">Decline</v-btn>
    </v-card-actions>
  </v-sheet>
</template>
