import { baseUrl } from '@/utils/api'

export type UserSubmission = {
  id: string
  challengeId: string
  challengeName: string
  difficulty: 'EASY' | 'MEDIUM' | 'HARD'
  code: string
  language: string
  exitCode: number
  status: 'ACCEPTED' | 'WRONG_ANSWER'
  createdAt: string,
  testCasesPassed: number
  totalTestCases: number
  testCasesFailed: number
}

const SUBMISSIONS_URL = `${baseUrl}/api/submission`

async function loadTestCaseValues(path: 'passed' | 'failed' | 'total'): Promise<number[]> {
  const response = await fetch(`${SUBMISSIONS_URL}/${path}`, {
    credentials: 'include',
  })

  if (response.status === 401) {
    return []
  }

  if (!response.ok) {
    throw new Error(`Failed to fetch ${path} test cases`)
  }

  return (await response.json()) as number[]
}

export function loadSubmissionTestCaseValues(): Promise<[
  number[],
  number[],
  number[],
]> {
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

  if (response.status === 401) {
    return []
  }

  if (!response.ok) {
    throw new Error('Failed to fetch submissions')
  }

  return (await response.json()) as UserSubmission[]
}

export async function loadPublicSubmissions(userId: string): Promise<UserSubmission[]> {
  const response = await fetch(`${baseUrl}/api/challenge/users/${userId}/submissions`, {
    credentials: 'include',
  })

  if (response.status === 401) {
    return []
  }

  if (!response.ok) {
    throw new Error('Failed to fetch public submissions')
  }

  return (await response.json()) as UserSubmission[]
}

export async function loadPublicSubmissionTestCaseValues(userId: string): Promise<[
  number[],
  number[],
  number[],
]> {
  const response = await fetch(`${baseUrl}/api/challenge/users/${userId}/submission-stats`, {
    credentials: 'include',
  })

  if (response.status === 401) {
    return [[], [], []]
  }

  if (!response.ok) {
    throw new Error('Failed to fetch public submission stats')
  }

  return (await response.json()) as [number[], number[], number[]]
}

export async function loadSolvedChallengeIds(): Promise<Set<string>> {
  const submissions = await loadUserSubmissions()
  return new Set(
    submissions
      .filter((submission) => submission.status === 'ACCEPTED')
      .map((submission) => submission.challengeId),
  )
}
