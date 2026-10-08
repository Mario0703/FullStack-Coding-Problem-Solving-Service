import { ref } from 'vue'
import type { UserData } from './types'
import { fetchCurrentUser, fetchCurrentAdmin } from './api'

export const currentUser = ref<UserData | null>(null)
export const currentRole = ref<string | null>(null)
export const isLoadingUser = ref(true)
export const hasCheckedUser = ref(false)
export const authError = ref('')

export async function loadCurrentUser(): Promise<UserData | null> {
  isLoadingUser.value = true
  authError.value = ''

  try {
    const user = await fetchCurrentUser()
    currentUser.value = user
    currentRole.value = user?.role ?? null
    return user
  } catch (error) {
    currentUser.value = null
    authError.value = 'Unable to check your login status. Please try again later.'
    throw error
  } finally {
    hasCheckedUser.value = true
    isLoadingUser.value = false
  }
}

export async function loadCurrentAdmin(): Promise<string> {
  const role = await fetchCurrentAdmin()
  currentRole.value = role
  return role
}

export function setCurrentUser(user: UserData | null): void {
  currentUser.value = user
  currentRole.value = user?.role ?? null
}

export function clearCurrentUser(): void {
  currentUser.value = null
  currentRole.value = null
  hasCheckedUser.value = true
}

export function setCurrentRole(role: string | null): void {
  currentRole.value = role
}

export function resetAuthState(): void {
  currentUser.value = null
  currentRole.value = null
  isLoadingUser.value = true
  hasCheckedUser.value = false
  authError.value = ''
}
