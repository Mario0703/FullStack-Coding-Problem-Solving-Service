import type { CreateChallengeForm } from '@/utils/LanguageType'
import type { Challenge, Language } from '@/utils/AdminUtils'
import { baseUrl } from '@/utils/api'
import { readErrorMessage } from '@/utils/apiError'

const CHALLENGES_URL = `${baseUrl}/api/challenge/list`;
const ADMIN_CHALLENGES_URL = `${baseUrl}/api/challenge/ListChallanges`;
const CREATE_CHALLENGE_URL = `${baseUrl}/api/challenge/create`
const CHALLENGE_URL = `${baseUrl}/api/challenge`

export type ChallengeExample = {
  id: string
  inputExample: string
  outputExample: string
}

export type ChallengeTestCase = {
  id: string
  testInput: string
  testOutput: string
}

/**
 * Public, non-sensitive view returned by the challenge list endpoints. The
 * backend never sends hidden test cases, the runner, the instructor solution
 * or submission history here.
 */
export type ChallengeSummary = {
  id: string
  name: string
  description: string
  difficulty: 'EASY' | 'MEDIUM' | 'HARD'
  category: 'GENERAL' | 'ARRAYS' | 'STRINGS' | 'TREES'
  numberOfTestCases: number
  language: Language
}

export async function listChallenges(): Promise<ChallengeSummary[]> {
  const response = await fetch(CHALLENGES_URL, {
    credentials: "include",
  });

  if (!response.ok) {
    throw new Error(await readErrorMessage(response, "Could not load challenges"));
  }

  return (await response.json()) as ChallengeSummary[];
}

export async function listAdminChallenges(): Promise<ChallengeSummary[]> {
  const response = await fetch(ADMIN_CHALLENGES_URL, {
    credentials: "include",
  });

  if (!response.ok) {
    throw new Error(await readErrorMessage(response, "Could not load admin challenges"));
  }

  return (await response.json()) as ChallengeSummary[];
}

export async function getChallengeExamples(challengeId: string): Promise<ChallengeExample[]> {
  const response = await fetch(`${CHALLENGE_URL}/${encodeURIComponent(challengeId)}/examples`, {
    credentials: 'include',
  })

  if (!response.ok) {
    throw new Error(await readErrorMessage(response, 'Could not load challenge examples'))
  }

  return (await response.json()) as ChallengeExample[]
}

export async function getChallengeTestCases(challengeId: string): Promise<ChallengeTestCase[]> {
  const response = await fetch(`${CHALLENGE_URL}/${encodeURIComponent(challengeId)}/test-cases`, {
    credentials: 'include',
  })

  if (!response.ok) {
    throw new Error(await readErrorMessage(response, 'Could not load challenge test cases'))
  }

  return (await response.json()) as ChallengeTestCase[]
}

export async function getChallengeSolution(challengeId: string): Promise<string> {
  const response = await fetch(`${CHALLENGE_URL}/Solution/${encodeURIComponent(challengeId)}`, {
  })

  if (!response.ok) {
    throw new Error(await readErrorMessage(response, 'Could not load the challenge solution'))
  }

  return await response.text()
}

export type ChallengeAiExample = {
  input: string
  output: string
}

export type ChallengeAiTestCase = {
  input: string
  expectedOutput: string
}

/** Aggregated data the MiniCode AI helper is allowed to read for a challenge. */
export type ChallengeAiContext = {
  id: string
  name: string
  description: string
  difficulty: string
  category: string
  language: string
  examples: ChallengeAiExample[]
  testCases: ChallengeAiTestCase[]
  hints: string[]
  solution: string | null
  userCode: string | null
  userLanguage: string | null
  solved: boolean
}

export async function getChallengeAiContext(challengeId: string): Promise<ChallengeAiContext> {
  const response = await fetch(`${CHALLENGE_URL}/${encodeURIComponent(challengeId)}/ai-context`, {
    credentials: 'include',
  })

  if (!response.ok) {
    throw new Error(await readErrorMessage(response, 'Could not load the AI context'))
  }

  return (await response.json()) as ChallengeAiContext
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

  return (await response.json()) as Challenge
}
