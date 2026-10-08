import { createRouter, createWebHistory } from 'vue-router'
import { currentUser, hasCheckedUser, loadCurrentAdmin, loadCurrentUser } from '@/utils/Users/state'
import {routes} from '@/router/routes.ts'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: routes,
})

router.beforeEach(async (to) => {
  if (to.meta.requiresAdmin) {
    try {
      await loadCurrentAdmin()
      return true
    } catch {
      return { name: 'admin login' }
    }
  }

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
