export type UserData = {
  userId: string
  userName: string
  email: string
  role: string
  bio?: string | null
}

export type PublicUser = {
  userId: string
  userName: string
  email: string
  bio?: string | null
  score: number
  numberOfEasyChallengesSolved: number
  numberOfMediumChallengesSolved: number
  numberOfHardChallengesSolved: number
  totalChallengesSolved: number
}

export type SubmissionDetails = {
  challengeName: string
  language: string
  createdAt: string
  exitCode: number
  status: string
  testCasesPassed: number
  testCasesFailed: number
  totalTestCases: number
  code: string
}

import type { ChallengeDifficulty } from '@/utils/Challenges/Types/types'

export type DashboardCard = {
  key: string
  challengeId: string
  challengeName: string
  difficulty: ChallengeDifficulty
  status: string
  isSolved: boolean
  languages: string[]
  solvedAt: string
  testCasesPassed: number
  testCasesFailed: number
  totalTestCases: number
  submissionId?: string
}

export type ActiveForm = 'username' | 'email' | 'password' | null

export type SignupData = {
  userName: string
  email: string
  password: string
}

export type ChangeEmailRequest = {
  newEmail: string
}

export type ChangePasswordRequest = {
  currentPassword: string
  newPassword: string
}

export type ChangeUserNameRequest = {
  newUserName: string
}

export type ChangeBioRequest = {
  newBio: string
}

export type ForgotPasswordRequest = {
  email: string
}

export type ResetPasswordRequest = {
  token: string
  newPassword: string
}
