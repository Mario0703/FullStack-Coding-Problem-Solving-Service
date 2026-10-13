<script setup lang="ts">
import { ref } from 'vue'
import {
  createStaffMember,
  type StaffAccountData,
  type StaffRole,
} from '@/utils/Admin/staffApi'
import { validateSignup } from '@/utils/Users/validation'
import ErrorFeedback from '@/components/FeedBack/Error.vue'
import SuccessFeedback from '@/components/FeedBack/Succes.vue'
import type { UserData } from '@/utils/Users/types'

const props = defineProps<{
  title: string
  staffRole: StaffRole
}>()

const emit = defineEmits<{
  created: [staff: UserData]
}>()

const userName = ref('')
const email = ref('')
const confirmEmail = ref('')
const password = ref('')
const confirmPassword = ref('')
const validationError = ref('')
const createError = ref('')
const createSuccess = ref('')
const isSubmitting = ref(false)

async function submitStaffAccount() {
  validationError.value = ''
  createError.value = ''
  createSuccess.value = ''

  const validation = validateSignup({
    userName: userName.value,
    email: email.value,
    confirmEmail: confirmEmail.value,
    password: password.value,
    confirmPassword: confirmPassword.value,
  })
  if (!validation.isValid) {
    validationError.value = validation.error
    return
  }

  isSubmitting.value = true

  try {
    const staff: StaffAccountData = {
      userName: userName.value.trim(),
      email: email.value.trim(),
      password: password.value,
      staffRole: props.staffRole,
    }

    const createdStaff = await createStaffMember(staff)
    createSuccess.value = `${createdStaff.userName} was created successfully.`
    clearForm()
    emit('created', createdStaff)
  } catch (error) {
    createError.value =
      error instanceof Error
        ? error.message
        : 'Unable to reach the server. Please try again later.'
  } finally {
    isSubmitting.value = false
  }
}

function clearForm() {
  userName.value = ''
  email.value = ''
  confirmEmail.value = ''
  password.value = ''
  confirmPassword.value = ''
}
</script>

<template>
  <v-card>
    <v-card-title class="text-h6 font-weight-bold pa-4">{{ title }}</v-card-title>

    <v-card-text class="pa-4 pt-0">
      <ErrorFeedback v-if="validationError" :message="validationError" class="mb-4" />
      <ErrorFeedback v-if="createError" :message="createError" class="mb-4" />
      <SuccessFeedback v-if="createSuccess" :message="createSuccess" class="mb-4" />

      <v-form @submit.prevent="submitStaffAccount">
        <v-text-field v-model="userName" label="User name" autocomplete="off" />
        <v-text-field v-model="email" label="Email" type="email" autocomplete="off" />
        <v-text-field v-model="confirmEmail" label="Confirm email" type="email" autocomplete="off" />
        <v-text-field v-model="password" label="Password" type="password" autocomplete="new-password" />
        <v-text-field
          v-model="confirmPassword"
          label="Confirm password"
          type="password"
          autocomplete="new-password"
        />

        <v-btn
          type="submit"
          color="grey-darken-4"
          variant="elevated"
          :loading="isSubmitting"
          :disabled="isSubmitting"
        >
          Create account
        </v-btn>
      </v-form>
    </v-card-text>
  </v-card>
</template>