import type { ChallengeExample, ChallengeTestCase } from '@/utils/ChallangeUtil.ts'

export type ChallengeDifficulty = 'EASY' | 'MEDIUM' | 'HARD'

export type ChallengeCategory = 'GENERAL' | 'ARRAYS' | 'STRINGS' | 'TREES'

export type Language = 'python' | 'java' | 'csharp' | 'cplusplus'

export type Challenge = {
  id: string
  name: string
  description: string
  difficulty: ChallengeDifficulty
  category: ChallengeCategory
  numberOfTestCases: number
  testCases: ChallengeTestCase[]
  inputOutput: ChallengeExample[]
}

export type ChallengeOption<T extends string> = {
  title: string
  value: T
}

export const languageOptions: ChallengeOption<Language>[] = [
  { title: 'Python', value: 'python' },
  { title: 'Java', value: 'java' },
  { title: 'C++', value: 'cplusplus' },
  { title: 'C#', value: 'csharp' },
]

export const difficultyOptions: ChallengeOption<ChallengeDifficulty>[] = [
  { title: 'Easy', value: 'EASY' },
  { title: 'Medium', value: 'MEDIUM' },
  { title: 'Hard', value: 'HARD' },
]

export const categoryOptions: ChallengeOption<ChallengeCategory>[] = [
  { title: 'General', value: 'GENERAL' },
  { title: 'Arrays', value: 'ARRAYS' },
  { title: 'Strings', value: 'STRINGS' },
  { title: 'Trees', value: 'TREES' },
]

export function formatChallengeValue(value: string): string {
  return value.charAt(0) + value.slice(1).toLowerCase()
}
