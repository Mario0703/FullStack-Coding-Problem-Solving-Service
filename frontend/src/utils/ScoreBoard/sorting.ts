import type { UserStats } from './types'

export const sortGetters: Record<string, (user: UserStats) => number> = {
  score: (user) => user.score,
  totalChallengesSolved: (user) => user.totalChallengesSolved,
  numberOfEasyChallengesSolved: (user) => user.numberOfEasyChallengesSolved,
  numberOfMediumChallengesSolved: (user) => user.numberOfMediumChallengesSolved,
  numberOfHardChallengesSolved: (user) => user.numberOfHardChallengesSolved,
}

export function sortUsers(users: UserStats[], sortBy: string): UserStats[] {
  const getValue = sortGetters[sortBy] ?? ((user: UserStats) => user.score)

  return [...users].sort((a, b) => getValue(b) - getValue(a))
}
