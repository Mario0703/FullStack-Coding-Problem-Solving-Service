import { baseUrl } from '@/utils/api'
import { readErrorMessage } from '@/utils/apiError'
import type {
  Challenge,
  ChallengeExample,
  ChallengeSummary,
  ChallengeTestCase,
} from '../Types/types'
import type { CreateChallengeForm } from '../Types/authoringTypes'

const CHALLENGES_URL = `${baseUrl}/api/challenge/list`
const CREATE_CHALLENGE_URL = `${baseUrl}/api/challenge/create`
const CHALLENGE_URL = `${baseUrl}/api/challenge`

export async function listChallenges(): Promise<ChallengeSummary[]> {
  const response = await fetch(CHALLENGES_URL, {
    credentials: 'include',
  })

  if (!response.ok) {
    throw new Error(await readErrorMessage(response, 'Could not load challenges'))
  }
  const responseData: ChallengeSummary[] = await response.json()
  return responseData
}

export async function getChallengeExamples(challengeId: string): Promise<ChallengeExample[]> {
  const response = await fetch(`${CHALLENGE_URL}/${encodeURIComponent(challengeId)}/examples`, {
    credentials: 'include',
  })

  if (!response.ok) {
    throw new Error(await readErrorMessage(response, 'Could not load challenge examples'))
  }

  const responseData: ChallengeExample[] = await response.json()
  return responseData
}

export async function getChallengeTestCases(challengeId: string): Promise<ChallengeTestCase[]> {
  const response = await fetch(`${CHALLENGE_URL}/${encodeURIComponent(challengeId)}/test-cases`, {
    credentials: 'include',
  })

  if (!response.ok) {
    throw new Error(await readErrorMessage(response, 'Could not load challenge test cases'))
  }

  const responseData: ChallengeTestCase[] = await response.json()
  return responseData
}

export async function getChallengeSolution(challengeId: string): Promise<string> {
  const response = await fetch(`${CHALLENGE_URL}/Solution/${encodeURIComponent(challengeId)}`, {})

  if (!response.ok) {
    throw new Error(await readErrorMessage(response, 'Could not load the challenge solution'))
  }

  const responseData: string = await response.text()
  return responseData
}

export async function createChallenge(challengeData: CreateChallengeForm): Promise<Challenge> {
  const response = await fetch(CREATE_CHALLENGE_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify(challengeData),
  })

  if (!response.ok) {
    throw new Error(await readErrorMessage(response, 'The challenge could not be created.'))
  }
  const responseData: unknown = await response.json()
  if (!responseData || typeof responseData !== 'object') {
    throw new Error(await readErrorMessage(response, 'Invalid response from the server.'))
  }

  return responseData as Challenge
}
