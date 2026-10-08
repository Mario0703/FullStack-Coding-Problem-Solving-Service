export type UserStats = {
  userName: string
  score: number
  numberOfEasyChallengesSolved: number
  numberOfMediumChallengesSolved: number
  numberOfHardChallengesSolved: number
  totalChallengesSolved: number
}

export type SortField = keyof Omit<UserStats, 'userName'>
