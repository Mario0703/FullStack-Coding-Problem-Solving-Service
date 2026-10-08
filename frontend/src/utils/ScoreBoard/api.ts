import { baseUrl } from '@/utils/api'
import type { UserStats } from './types'

export async function loadUserStats(): Promise<UserStats[]> {
  const response = await fetch(`${baseUrl}/api/scoreboard/user-stats`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: 'include',
  })

  if (!response.ok) {
    throw new Error(`HTTP error! status: ${response.status}`)
  }

  return response.json()
}
