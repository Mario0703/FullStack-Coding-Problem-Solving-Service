<script setup lang="ts">
import ErrorFeedback from "@/components/FeedBack/Error.vue";
import LoginWithEmail from "@/components/authentication/LoginWithEmail.vue";
import ForgotPasswordLink from "@/components/authentication/ForgotPasswordLink.vue";
import type { PropType } from "vue";
import type { SubmitEventPromise } from "vuetify";


const props = defineProps({
  loginTitle: {
    type: String,
    required : true
  },
  loginError: {
    type: String,
    required : false
  },
  loginWithEmail: {
    type: Boolean,
    default: false,
  },
  allowEmailLogin: {
    type: Boolean,
    default: true,
  },
  name: {
    type: String,
    default: '',
  },
  password: {
    type: String,
    default: '',
  },
  isSubmitting: {
    type: Boolean,
    default: false,
  },
  login: {
    type: Function as PropType<(event: SubmitEventPromise) => unknown>,
    required: true,
  },
  width: {
    type: [String, Number],
    default: 400,
  },
})

const emit = defineEmits<{
  'update:loginWithEmail': [value: boolean]
  'update:name': [value: string]
  'update:password': [value: string]
}>()

</script>

<template>
  <v-container class="d-flex align-center justify-center">
    <ErrorFeedback
      v-if="props.loginError"
      title="Login failed"
      :message="props.loginError"
      variant="tonal"
      class="mb-4"
    />

    <v-form :style="{ width: `${props.width}px`, maxWidth: '100%' }" class="signup-form" @submit.prevent="props.login">
      <v-card-title class="text-h5 font-weight-bold px-0">{{ props.loginTitle }}</v-card-title>

      <v-switch
        v-if="props.allowEmailLogin"
        :model-value="props.loginWithEmail"
        label="Log in with email"
        color="primary"
        @update:model-value="emit('update:loginWithEmail', Boolean($event))"
      />

      <LoginWithEmail
        v-if="props.allowEmailLogin && props.loginWithEmail"
        :model-value="props.name"
        @update:model-value="emit('update:name', $event)"
      />

      <v-text-field
        v-else
        :model-value="props.name"
        label="Username"
        placeholder="Type your username"
        autocomplete="username"
        @update:model-value="emit('update:name', $event)"
      />

      <v-text-field
        :model-value="props.password"
        label="Password"
        placeholder="Type your password"
        type="password"
        autocomplete="current-password"
        @update:model-value="emit('update:password', $event)"
      />
      <ForgotPasswordLink v-if="props.allowEmailLogin && props.loginWithEmail" />
      <v-btn
        type="submit"
        color="primary"
        :loading="props.isSubmitting"
        :disabled="props.isSubmitting"
      >
        Login
      </v-btn>
    </v-form>
  </v-container>
</template>
