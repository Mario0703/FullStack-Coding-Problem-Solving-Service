import type { ChallengeDifficulty } from '@/utils/Challenges/Types/types'
import type { SolvedChallenge, UserSubmission } from '@/utils/Challenges/API/submissions'
import type { DashboardCard } from './types'

export function createSolvedCards(solvedChallenges: SolvedChallenge[]): DashboardCard[] {
  return solvedChallenges.map((solved) => ({
    key: `solved-${solved.challengeId}`,
    challengeId: solved.challengeId,
    challengeName: solved.challengeName,
    difficulty: solved.difficulty,
    status: 'ACCEPTED',
    isSolved: true,
    languages: solved.languages,
    solvedAt: solved.solvedAt,
    testCasesPassed: 0,
    testCasesFailed: 0,
    totalTestCases: 0,
  }))
}

export function createReceiptCards(submissions: UserSubmission[]): DashboardCard[] {
  return submissions.map((submission) => ({
    key: submission.id,
    challengeId: submission.challengeId,
    challengeName: submission.challengeName,
    difficulty: submission.difficulty,
    status: submission.status,
    isSolved: false,
    languages: [],
    solvedAt: '',
    testCasesPassed: submission.testCasesPassed,
    testCasesFailed: submission.testCasesFailed,
    totalTestCases: submission.totalTestCases,
    submissionId: submission.id,
  }))
}

export function filterCards(
  cards: DashboardCard[],
  options: {
    search: string
    selectedDifficulty: ChallengeDifficulty | null
    selectedChallengeId: string | null
  },
): DashboardCard[] {
  const { search, selectedDifficulty, selectedChallengeId } = options

  return cards.filter((card) => {
    const matchesDifficulty = !selectedDifficulty || card.difficulty === selectedDifficulty
    const matchesChallenge = !selectedChallengeId || card.challengeId === selectedChallengeId
    const matchesSearch = card.challengeName
      .toLowerCase()
      .includes(search.trim().toLowerCase())

    return matchesDifficulty && matchesChallenge && matchesSearch
  })
}

export function getChallengeFilterItems(cards: DashboardCard[]): Array<{ value: string; title: string }> {
  return [...new Map(cards.map((card) => [card.challengeId, card.challengeName]))]
    .map(([value, title]) => ({ value, title }))
    .sort((a, b) => a.title.localeCompare(b.title))
}

export function countSolvedByDifficulty(
  solvedChallenges: SolvedChallenge[],
  difficulty: ChallengeDifficulty,
): number {
  return solvedChallenges.filter((solved) => solved.difficulty === difficulty).length
}

export function calculateTotalPoints(
  easy: number,
  medium: number,
  hard: number,
): number {
  return easy + medium * 2 + hard * 3
}

export function mergeSubmissionsWithTestCases(
  submissions: UserSubmission[],
  passed: number[],
  failed: number[],
  total: number[],
): UserSubmission[] {
  return submissions.map((submission, index) => ({
    ...submission,
    testCasesPassed: passed[index] ?? 0,
    testCasesFailed: failed[index] ?? 0,
    totalTestCases: total[index] ?? 0,
  }))
}

export function formatSolvedDate(value: string): string {
  return new Date(value).toLocaleDateString()
}

export function filterUsersBySearch<T extends { name: string }>(
  users: T[],
  searchQuery: string,
): T[] {
  const query = searchQuery.trim().toLowerCase()

  if (!query) {
    return users
  }

  return users.filter((user) => user.name.toLowerCase().includes(query))
}
