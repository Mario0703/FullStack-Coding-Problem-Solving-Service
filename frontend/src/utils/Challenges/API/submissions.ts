import { baseUrl } from '@/utils/api'
import { readErrorMessage } from '@/utils/apiError'
import type { SubmissionStatus } from './execution'

export type UserSubmission = {
  id: string
  challengeId: string
  challengeName: string
  difficulty: 'EASY' | 'MEDIUM' | 'HARD'
  code: string
  language: string
  exitCode: number
  status: SubmissionStatus
  createdAt: string,
  testCasesPassed: number
  totalTestCases: number
  testCasesFailed: number
}

type testCase = 'passed' | 'failed' | 'total'


export type SolvedChallenge = {
  challengeId: string
  challengeName: string
  difficulty: 'EASY' | 'MEDIUM' | 'HARD'
  solvedAt: string
  languages: string[]
}

const SUBMISSIONS_URL = `${baseUrl}/api/submission`

async function loadTestCaseValues(path: 'passed' | 'failed' | 'total'): Promise<number[]> {
  const response = await fetch(`${SUBMISSIONS_URL}/${path}`, {
    credentials: 'include',
  })

  if (!response.ok) {
    throw new Error(await readErrorMessage(response, `Failed to fetch ${path} test cases`))
  }

  const responseData = await response.json()
  const isResponseDataArray = responseData instanceof Array

  if (!isResponseDataArray) {
    throw new Error(await readErrorMessage(response, `Expected array for ${path} test cases`))
  }

  return responseData
}

export function loadSubmissionTestCaseValues(): Promise<[number[], number[], number[]]> {
  return Promise.all([
    loadTestCaseValues('passed'),
    loadTestCaseValues('failed'),
    loadTestCaseValues('total'),
  ])
}

export async function loadUserSubmissions(): Promise<UserSubmission[]> {
  const response = await fetch(SUBMISSIONS_URL, {
    credentials: 'include',
  })

  if (!response.ok) {
    throw new Error(await readErrorMessage(response, 'Failed to fetch submissions'))
  }

  return (await response.json()) as UserSubmission[]
}

export async function loadSolvedChallenges(): Promise<SolvedChallenge[]> {
  const response = await fetch(`${SUBMISSIONS_URL}/solved`, {
    credentials: 'include',
  })

  if (!response.ok) {
    throw new Error(await readErrorMessage(response, 'Failed to fetch solved challenges'))
  }

  return (await response.json()) as SolvedChallenge[]
}


export async function loadSolvedChallengeIds(): Promise<Set<string>> {
  const response = await fetch(`${baseUrl}/api/code/playground/SolvedChallenges`, {
    credentials: 'include',
  })

  if (!response.ok) {
    throw new Error(await readErrorMessage(response, 'Failed to fetch solved challenges'))
  }

  const challengeIds = (await response.json()) as string[]
  return new Set(challengeIds)
}

export async function loadAttemptedChallengeIds(): Promise<Set<string>> {

  const [submissions, solvedIds] = await Promise.all([
    loadUserSubmissions(),
    loadSolvedChallengeIds(),
  ])

  return new Set([
    ...solvedIds,
    ...submissions.map((submission) => submission.challengeId),
  ])
}
