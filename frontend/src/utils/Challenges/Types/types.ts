export type ChallengeDifficulty = 'EASY' | 'MEDIUM' | 'HARD'

export type ChallengeCategory = 'GENERAL' | 'ARRAYS' | 'STRINGS' | 'TREES'

export type Language = 'python' | 'java' | 'csharp' | 'cplusplus'

export type ChallengeOption<T extends string> = {
  title: string
  value: T
}

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
  difficulty: ChallengeDifficulty
  category: ChallengeCategory
  numberOfTestCases: number
  language: Language
}

/** Full challenge shape used by the admin management screens. */
export type Challenge = {
  id: string
  name: string
  description: string
  difficulty: ChallengeDifficulty
  category: ChallengeCategory
  numberOfTestCases: number
  testCases: ChallengeTestCase[]
  inputOutput: ChallengeExample[]
  language: Language
}
