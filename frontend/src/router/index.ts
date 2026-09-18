import { createRouter, createWebHistory } from 'vue-router'
import UserSignup from '../components/UserSignup.vue'
import LandingPage from "@/components/landingPage.vue";

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
    }
  ],
})

export default router
