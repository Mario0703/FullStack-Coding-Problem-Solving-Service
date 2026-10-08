import {baseUrl} from "@/utils/api.ts";
import {ref} from "vue";

export interface PlatformStatistics {
  numberOfUsers: number
  numberOfChallenges: number
  numberOfSubmittedSolutions: number
  numberOfSolvedChallenges: number
}

export interface UserStatistics {
  id: string
  name: string
  email: string
  numberOfSubmissions: number
  numberOfSolvedChallenges: number
}

export const statistics = ref<PlatformStatistics | null>(null)
export const users = ref<UserStatistics[]>([])
export const isLoading = ref(true)
export const loadError = ref('')



export async function loadStatistics() {
  try {
    const permissionResponse = await fetch(`${baseUrl}/api/admin/stats/permission`, {
      credentials: 'include',
    })
    const loadError = ref('')
    const isLoading = ref(true)

    if (permissionResponse.status === 403) {
      loadError.value = 'You do not have permission to view platform statistics.'
      return
    }

    if (!permissionResponse.ok) {
      throw new Error('Could not check statistics permission')
    }

    const [statisticsResponse, usersResponse] = await Promise.all([
      fetch(`${baseUrl}/api/admin/stats`, { credentials: 'include' }),
      fetch(`${baseUrl}/api/admin/stats/users/list`, { credentials: 'include' }),
    ])

    if (!statisticsResponse.ok || !usersResponse.ok) {
      throw new Error('Could not load platform statistics')
    }

    statistics.value = (await statisticsResponse.json()) as PlatformStatistics
    users.value = (await usersResponse.json()) as UserStatistics[]
  } catch {
    loadError.value = 'Unable to load platform statistics. Please try again later.'
  } finally {
    isLoading.value = false
  }
}
