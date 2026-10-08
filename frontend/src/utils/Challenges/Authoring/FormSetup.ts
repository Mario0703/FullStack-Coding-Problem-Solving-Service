import {ref} from "vue";
import type {
  ChallengeCategory,
  ChallengeDifficulty,
  Language
} from "@/utils/Challenges/Types/types.ts";
import type {
  Example,
  LanguageSetupInput,
  TestCase
} from "@/utils/Challenges/Types/authoringTypes.ts";

export interface FormSetupData {
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
  examples: Example[]
  numberOfExamples: number
  extraLanguages: Language[]
  extraSetups: Record<Language, LanguageSetupInput>
}

export const DEFAULT_FORM_VALUES: FormSetupData = {
  name: '',
  description: '',
  difficulty: null,
  category: null,
  language: null,
  solution: '',
  template: '',
  templateExplanation: '',
  runner: '',
  runnerExplanation: '',
  numberOfTestCases: 1,
  testCases: [{ input: '', expectedOutput: '' }],
  examples: [{ inputExample: '', outputExample: '' }],
  numberOfExamples: 1,
  extraLanguages: [],
  extraSetups: {} as Record<Language, LanguageSetupInput>,
}

export class FormSetup {

  public name = ref('')
  public description = ref('')
  public difficulty = ref<ChallengeDifficulty | null>(null)
  public category = ref<ChallengeCategory | null>(null)
  public language = ref<Language | null>(null)
  public solution = ref('')
  public template = ref('')
  public templateExplanation = ref('')
  public runner = ref('')
  public runnerExplanation = ref('')
  public numberOfTestCases = ref(1)
  public testCases = ref<TestCase[]>([])
  public examples = ref<Example[]>([])
  public numberOfExamples = ref(1)

  // Languages the challenge additionally supports, and their authored data.
  public extraLanguages = ref<Language[]>([])
  public extraSetups = ref<Record<Language, LanguageSetupInput>>(
    {} as Record<Language, LanguageSetupInput>,
  )

  public constructor() {
    this.setFormValues(DEFAULT_FORM_VALUES)
  }

  public resetForm(): void {
    this.setFormValues(DEFAULT_FORM_VALUES)
  }

  public setFormValues(data: Partial<FormSetupData>): void {
    if (data.name !== undefined) this.name.value = data.name
    if (data.description !== undefined) this.description.value = data.description
    if (data.difficulty !== undefined) this.difficulty.value = data.difficulty
    if (data.category !== undefined) this.category.value = data.category
    if (data.language !== undefined) this.language.value = data.language
    if (data.solution !== undefined) this.solution.value = data.solution
    if (data.template !== undefined) this.template.value = data.template
    if (data.templateExplanation !== undefined)
      this.templateExplanation.value = data.templateExplanation
    if (data.runner !== undefined) this.runner.value = data.runner
    if (data.runnerExplanation !== undefined) this.runnerExplanation.value = data.runnerExplanation
    if (data.numberOfTestCases !== undefined) this.numberOfTestCases.value = data.numberOfTestCases
    if (data.testCases !== undefined)
      this.testCases.value = data.testCases.map((testCase) => ({ ...testCase }))
    if (data.numberOfExamples !== undefined) this.numberOfExamples.value = data.numberOfExamples
    if (data.examples !== undefined)
      this.examples.value = data.examples.map((example) => ({ ...example }))
    if (data.extraLanguages !== undefined) this.extraLanguages.value = [...data.extraLanguages]
    if (data.extraSetups !== undefined) this.extraSetups.value = { ...data.extraSetups }
  }
}
