<script setup lang="ts">
import { currentUser } from "../authenticator/auth";
import Logout from "./Logout.vue";
import UserDashBoard from "@/components/UserDashBoard.vue";




</script>

<template>
  <v-container class="d-flex flex-column align-center py-8 px-4">
    <div v-if="currentUser" class="w-100">
      <v-card class="w-100 mx-auto bg-grey-lighten-4" border="primary" max-width="720" variant="outlined">
        <v-card-title class="text-h5 font-weight-bold text-center pa-6 pb-2">
          User Information
        </v-card-title>
        <v-card-text class="pa-6 pt-2">
          <v-row align="stretch" class="ma-n2">
            <v-col cols="12" sm="6" class="d-flex pa-2">
              <v-card class="w-100 h-100 d-flex flex-column bg-white" border="primary" variant="outlined">
                <v-card-title class="text-subtitle-1 font-weight-bold text-center">
                  User name
                </v-card-title>
                <v-card-text class="text-body-1 text-center flex-grow-1">
                  {{ currentUser.userName }}
                </v-card-text>
                <v-card-actions class="justify-center pb-4">
                  <v-btn color="primary">Change username</v-btn>
                </v-card-actions>
              </v-card>
            </v-col>

            <v-col cols="12" sm="6" class="d-flex pa-2">
              <v-card class="w-100 h-100 d-flex flex-column bg-white" border="primary" variant="outlined">
                <v-card-title class="text-subtitle-1 font-weight-bold text-center">
                  Email
                </v-card-title>
                <v-card-text class="text-body-1 text-center flex-grow-1 text-break">
                  {{ currentUser.email }}
                </v-card-text>
                <v-card-actions class="justify-center pb-4">
                  <v-btn color="primary">Change email</v-btn>
                </v-card-actions>
              </v-card>
            </v-col>
          </v-row>
        </v-card-text>
      </v-card>

      <div class="d-flex justify-center mt-6">
        <Logout />
      </div>
      <UserDashBoard />
    </div>
    <p v-else class="text-body-1">No user data available.</p>
  </v-container>
</template>
