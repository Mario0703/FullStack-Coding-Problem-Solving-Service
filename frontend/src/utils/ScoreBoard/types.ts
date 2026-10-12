export type UserStats = {
  userId: string
  userName: string
  score: number
  numberOfEasyChallengesSolved: number
  numberOfMediumChallengesSolved: number
  numberOfHardChallengesSolved: number
  totalChallengesSolved: number
}

export type SortField = keyof Omit<UserStats, 'userName' | 'userId'>