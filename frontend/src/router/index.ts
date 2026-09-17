import { createRouter, createWebHistory } from 'vue-router'
import UserSignup from '../components/UserSignup.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/Signup',
      name: 'home',
      component: UserSignup,
    },
  ],
})

export default router
