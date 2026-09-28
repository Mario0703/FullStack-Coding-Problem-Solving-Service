import type { challangeForm } from '@/components/AdminPages/LanguageType'
import type { Challenge } from '@/utils/AdminUtils'

const CHALLENGES_URL = "http://localhost:8082/api/challenge/list";
const ADMIN_CHALLENGES_URL = "http://localhost:8082/api/challenge/ListChallanges";
const CREATE_CHALLENGE_URL = 'http://localhost:8082/api/challenge/create'

export async function listChallenges(): Promise<Challenge[]> {
  const response = await fetch(CHALLENGES_URL, {
    credentials: "include",
  });

  if (!response.ok) {
    throw new Error("Could not load challenges");
  }

  return (await response.json()) as Challenge[];
}

export async function listAdminChallenges(): Promise<Challenge[]> {
  const response = await fetch(ADMIN_CHALLENGES_URL, {
    credentials: "include",
  });

  if (!response.ok) {
    throw new Error("Could not load admin challenges");
  }

  return (await response.json()) as Challenge[];
}

export async function createChallenge(challengeData: challangeForm): Promise<Challenge | null> {
  const response = await fetch(CREATE_CHALLENGE_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify(challengeData),
  })

  if (!response.ok) {
    return null
  }

  return (await response.json()) as Challenge
}
