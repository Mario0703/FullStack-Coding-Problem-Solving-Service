import type { Challenge } from "@/utils/AdminUtils";

const CHALLENGES_URL = "http://localhost:8082/api/challenge/ListChallanges";

export async function listChallenges(): Promise<Challenge[]> {
  const response = await fetch(CHALLENGES_URL, {
    credentials: "include",
  });

  if (!response.ok) {
    throw new Error("Could not load challenges");
  }

  return (await response.json()) as Challenge[];
}
