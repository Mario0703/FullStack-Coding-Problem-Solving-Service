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
