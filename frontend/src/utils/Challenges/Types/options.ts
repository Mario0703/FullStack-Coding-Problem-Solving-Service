import type {
  ChallengeCategory,
  ChallengeDifficulty,
  ChallengeOption,
  Language,
} from './types'

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
