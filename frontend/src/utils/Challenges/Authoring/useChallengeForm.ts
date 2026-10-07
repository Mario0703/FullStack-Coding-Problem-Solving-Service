import { reactive, watch } from 'vue'
import type { ComputedRef } from 'vue'
import { FormSetup } from '@/utils/Challenges/Authoring/FormSetup.ts'
import { Languages } from './Languages'
import { ChallengeFormBuilder } from './ChallengeFormBuilder'
import type {
  CreateChallengeForm,
  Example,
  LanguageSetupInput,
  TestCase,
} from '../Types/authoringTypes'
import type { ChallengeCategory, ChallengeDifficulty, Language } from '../Types/types'

/** Every value the user edits on the challenge form. */
export interface ChallengeFormFields {
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
  numberOfTestCases: number
  testCases: TestCase[]
  numberOfExamples: number
  examples: Example[]
  extraLanguages: Language[]
  extraSetups: Record<Language, LanguageSetupInput>
}

export interface ChallengeFormActions {
  resetForm(): void
  toCreateChallengeForm(): CreateChallengeForm
  createNumberOfSamples<T>(arr: T[], newSize: number, defaultValue: T): void
  isExtraLanguage(value: Language): boolean
  toggleExtraLanguage(value: Language): void
}

/** The shape returned by {@link useChallengeForm}. */
export interface ChallengeFormController {
  fields: ChallengeFormFields
  actions: ChallengeFormActions
  otherLanguages: ComputedRef<Language[]>
}

export function useChallengeForm(): ChallengeFormController {
  const setup = new FormSetup()
  const languages = new Languages(setup)
  const builder = new ChallengeFormBuilder(setup)

  watch(setup.language, (selectedLanguage) => {
    if (selectedLanguage) {
      languages.applyLanguage(selectedLanguage)
    }
  })

  /**
   * Every value the user edits. `reactive` unwraps the FormSetup refs, so the
   * template binds them directly: `fields.name`, `fields.testCases`, and so on.
   */
  const fields = reactive({
    name: setup.name,
    description: setup.description,
    difficulty: setup.difficulty,
    category: setup.category,
    language: setup.language,
    solution: setup.solution,
    template: setup.template,
    templateExplanation: setup.templateExplanation,
    runner: setup.runner,
    runnerExplanation: setup.runnerExplanation,
    numberOfTestCases: setup.numberOfTestCases,
    testCases: setup.testCases,
    numberOfExamples: setup.numberOfExamples,
    examples: setup.examples,
    extraLanguages: setup.extraLanguages,
    extraSetups: setup.extraSetups,
  })

  /**
   * Everything that acts on the form. `toCreateChallengeForm` is the single
   * place that maps the user's input to the outgoing API payload.
   */
  const actions = {
    resetForm(): void {
      setup.resetForm()
    },
    toCreateChallengeForm(): CreateChallengeForm {
      return builder.toCreateChallengeForm()
    },
    createNumberOfSamples<T>(arr: T[], newSize: number, defaultValue: T): void {
      while (newSize > arr.length) arr.push({ ...defaultValue })
      arr.length = newSize
    },
    isExtraLanguage(value: Language): boolean {
      return languages.isExtraLanguage(value)
    },
    toggleExtraLanguage(value: Language): void {
      languages.toggleExtraLanguage(value)
    },
  }

  return {
    fields,
    actions,
    otherLanguages: languages.otherLanguages,
  }
}
