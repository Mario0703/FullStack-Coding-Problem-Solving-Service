import { createRouter, createWebHistory } from 'vue-router'
import UserSignup from '@/components/authentication/UserSignup.vue'
import LandingPage from "@/components/home/LandingPage.vue";
import ChallengePage from "@/components/challenges/ChallengePage.vue";
import ConfirmSignUp from "@/components/authentication/ConfirmSignUp.vue";
import LoginPage from "@/components/authentication/LoginPage.vue";
import UserLoginConfirmed from "@/components/authentication/UserLoginConfirmed.vue";
import UserPage from "@/components/user/UserPage.vue";
import {
  currentUser,
  hasCheckedUser,
  loadCurrentUser,
} from "@/authenticator/auth";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/Signup',
      name: 'signup',
      component: UserSignup,
    },{
       path: '/',
       name: 'home',
       component: LandingPage,
    }, {
      path: '/challenges',
      name: 'challenges',
      component: ChallengePage,
    }, {
      path: '/confirm-signup',
      name: 'confirmSignup',
      component: ConfirmSignUp,
    },{
      path: '/Login',
      name: 'login',
      component: LoginPage,
    }, {
      path: '/login-confirmed',
      name: 'loginConfirmed',
      component: UserLoginConfirmed,
      meta: { requiresAuth: true },
    }, {
      path: '/User',
      name: 'user',
      component: UserPage,
      meta: { requiresAuth: true },
    }
  ],
})

router.beforeEach(async (to) => {
  if (!to.meta.requiresAuth) {
    return true
  }

  if (!hasCheckedUser.value) {
    try {
      await loadCurrentUser()
    } catch {
      return { name: 'login' }
    }
  }

  if (!currentUser.value) {
    return {
      name: 'login',
      query: { redirect: to.fullPath },
    }
  }

  return true
})

export default router
