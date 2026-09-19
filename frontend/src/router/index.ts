import { createRouter, createWebHistory } from 'vue-router'
import UserSignup from '../components/UserSignup.vue'
import LandingPage from "@/components/landingPage.vue";
import ChallangePage from "@/components/ChallangePage.vue";
import ConfirmSignUp from "@/components/confirmSignUp.vue";

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
    }
  ],
})

export default router
