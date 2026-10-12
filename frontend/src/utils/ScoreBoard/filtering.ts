import type { UserStats } from './types'

export function filterUsersByUserName(
  users: UserStats[],
  searchQuery: string | null,
): UserStats[] {
  const query = searchQuery?.trim().toLowerCase() ?? ''

  if (!query) {
    return users
  }

  return users.filter((user) => user.userName.toLowerCase().includes(query))
}