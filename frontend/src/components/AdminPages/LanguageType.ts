import type {
  ChallengeCategory,
  ChallengeDifficulty,
  Language,
} from '@/utils/AdminUtils'

export type TestCase = {
  input: string
  output: string
}

export type Example = {
  input: string
  output: string
}

export type LanguageSetup = {
  userTemplate: string
  userTemplateExplanation: string
  runner: string
  runnerExplanation: string
}

export type challangeForm = {
  name: string
  description: string
  difficulty: ChallengeDifficulty | null
  category: ChallengeCategory | null
  language: Language | null
  solution: string
  template: string
  templateExplanation: string
  runner: string
  runnerExplanation: string
  testCases: TestCase[]
  numberOfTestCases: number
  examples: Example[]
  numberOfExamples: number
}
