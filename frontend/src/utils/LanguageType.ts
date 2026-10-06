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

/** One language's authored template/runner, as sent when creating a challenge. */
export interface LanguageSetupInput {
  language: Language
  template: string
  templateExplanation: string
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
  /**
   * Per-language templates/runners. The first entry is the primary language;
   * the rest are the optional additional languages the challenge supports.
   */
  languageSetups: LanguageSetupInput[]
}
