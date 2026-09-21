import { createRouter, createWebHistory } from 'vue-router'
import UserSignup from '../components/UserSignup.vue'
import LandingPage from "@/components/landingPage.vue";
import ChallangePage from "@/components/ChallangePage.vue";
import ConfirmSignUp from "@/components/confirmSignUp.vue";
import LoginPage from "@/components/LoginPage.vue";
import UserLoginConfirmed from "@/components/UserLoginConfirmed.vue";
import UserPage from "@/components/UserPage.vue";

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
      component: ChallangePage,
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
    }, {
      path: '/User',
      name: 'user',
      component: UserPage,
    }
  ],
})

export default router
