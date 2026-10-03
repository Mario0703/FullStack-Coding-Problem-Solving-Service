import type {
  ChallengeCategory,
  ChallengeDifficulty,
  Language,
} from '@/utils/AdminUtils'

export interface TestCase {
  input: string
  expectedOutput: string
}

export interface Example {
  inputExample: string
  outputExample: string
}

export interface LanguageSetup {
  userTemplate: string
  userTemplateExplanation: string
  runner: string
  runnerExplanation: string
}

export interface CreateChallengeForm {
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
