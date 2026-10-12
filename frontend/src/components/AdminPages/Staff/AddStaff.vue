<script setup lang="ts">
import { ref } from 'vue'
import StaffAccountForm from '@/components/AdminPages/Staff/StaffAccountForm.vue'
import type { UserData } from '@/utils/Users/types'

const emit = defineEmits<{
  created: [staff: UserData]
}>()

const isDialogOpen = ref(false)

function staffCreated(staff: UserData) {
  emit('created', staff)
}
</script>

<template>
  <v-dialog v-model="isDialogOpen" max-width="1000">
    <template #activator="{ props: activatorProps }">
      <v-btn v-bind="activatorProps" color="primary">
        Add staff to MiniLeetCode
      </v-btn>
    </template>

    <v-card>
      <v-card-title class="text-h5 font-weight-bold pa-6">
        Add staff to MiniLeetCode
      </v-card-title>

      <v-card-subtitle class="text-body-1 px-6 pb-0">
        Create an admin or a moderator. They sign in with the user name and password set here.
      </v-card-subtitle>

      <v-card-text class="pa-6">
        <v-row>
          <v-col cols="12" md="6">
            <StaffAccountForm title="Admin account" staff-role="ADMIN" @created="staffCreated" />
          </v-col>
          <v-col cols="12" md="6">
            <StaffAccountForm title="Moderator account" staff-role="MODERATOR" @created="staffCreated" />
          </v-col>
        </v-row>
      </v-card-text>

      <v-card-actions class="justify-end pa-6">
        <v-btn variant="text" @click="isDialogOpen = false">Close</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>