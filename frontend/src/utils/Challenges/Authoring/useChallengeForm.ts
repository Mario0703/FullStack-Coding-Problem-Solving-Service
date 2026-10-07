import { computed, ref, watch } from 'vue'
import { createLanguageSetup, getSupportedLanguages } from './languageSetups'
import type { CreateChallengeForm, Example, LanguageSetupInput, TestCase } from '../Types/authoringTypes'
import type { ChallengeCategory, ChallengeDifficulty, Language } from '../Types/types'

export function useChallengeForm() {
  const name = ref('')
  const description = ref('')
  const difficulty = ref<ChallengeDifficulty | null>(null)
  const category = ref<ChallengeCategory | null>(null)
  const language = ref<Language | null>(null)
  const solution = ref('')
  const template = ref('')
  const templateExplanation = ref('')
  const runner = ref('')
  const runnerExplanation = ref('')
  const numberOfTestCases = ref(1)
  const testCases = ref<TestCase[]>([{ input: '', expectedOutput: '' }])
  const examples = ref<Example[]>([{ inputExample: '', outputExample: '' }])
  const numberOfExamples = ref(1)

  // Languages the challenge additionally supports, and their authored data.
  const extraLanguages = ref<Language[]>([])
  const extraSetups = ref<Record<Language, LanguageSetupInput>>(
    {} as Record<Language, LanguageSetupInput>,
  )

  const otherLanguages = computed(() =>
    getSupportedLanguages().filter((value) => value !== language.value),
  )

  function isExtraLanguage(value: Language): boolean {
    return extraLanguages.value.includes(value)
  }

  function toggleExtraLanguage(value: Language) {
    if (isExtraLanguage(value)) {
      extraLanguages.value = extraLanguages.value.filter((item) => item !== value)
      return
    }

    const setup = createLanguageSetup(value)
    extraSetups.value = {
      ...extraSetups.value,
      [value]: {
        language: value,
        template: setup.userTemplate,
        templateExplanation: setup.userTemplateExplanation,
        runner: setup.runner,
        runnerExplanation: setup.runnerExplanation,
      },
    }
    extraLanguages.value = [...extraLanguages.value, value]
  }

  watch(language, (selectedLanguage) => {
    // A language cannot be both the primary and an additional language.
    if (selectedLanguage) {
      extraLanguages.value = extraLanguages.value.filter((item) => item !== selectedLanguage)
      const setup = createLanguageSetup(selectedLanguage)
      template.value = setup.userTemplate
      templateExplanation.value = setup.userTemplateExplanation
      runner.value = setup.runner
      runnerExplanation.value = setup.runnerExplanation
    }
  })

  function resetForm() {
    name.value = ''
    description.value = ''
    difficulty.value = null
    category.value = null
    language.value = null
    solution.value = ''
    template.value = ''
    templateExplanation.value = ''
    runner.value = ''
    runnerExplanation.value = ''
    numberOfTestCases.value = 1
    testCases.value = [{ input: '', expectedOutput: '' }]
    examples.value = [{ inputExample: '', outputExample: '' }]
    numberOfExamples.value = 1
    extraLanguages.value = []
    extraSetups.value = {} as Record<Language, LanguageSetupInput>
  }

  function createNumberOfSampels<T>(arr: T[], newSize: number, defaultValue: T) {
    while (newSize > arr.length) arr.push({ ...defaultValue })
    arr.length = newSize
  }

  function toCreateChallengeForm(): CreateChallengeForm {
    const primary: LanguageSetupInput = {
      language: language.value as Language,
      template: template.value.trim(),
      templateExplanation: templateExplanation.value.trim(),
      runner: runner.value.trim(),
      runnerExplanation: runnerExplanation.value.trim(),
    }

    const extraSetupsPayload: LanguageSetupInput[] = extraLanguages.value
      .map((value) => extraSetups.value[value])
      .filter((setup): setup is LanguageSetupInput => Boolean(setup))
      .map((setup) => ({
        language: setup.language,
        template: setup.template.trim(),
        templateExplanation: setup.templateExplanation.trim(),
        runner: setup.runner.trim(),
        runnerExplanation: setup.runnerExplanation.trim(),
      }))

    return {
      name: name.value.trim(),
      description: description.value.trim(),
      difficulty: difficulty.value,
      category: category.value,
      solution: solution.value.trim(),
      template: template.value.trim(),
      templateExplanation: templateExplanation.value.trim(),
      runner: runner.value.trim(),
      runnerExplanation: runnerExplanation.value.trim(),
      language: language.value,
      testCases: testCases.value.map((testCase) => ({
        input: testCase.input.trim(),
        expectedOutput: testCase.expectedOutput.trim(),
      })),
      numberOfTestCases: testCases.value.length,
      examples: examples.value.map((example) => ({
        inputExample: example.inputExample.trim(),
        outputExample: example.outputExample.trim(),
      })),
      numberOfExamples: numberOfExamples.value,
      languageSetups: [primary, ...extraSetupsPayload],
    }
  }

  return {
    name,
    description,
    difficulty,
    category,
    language,
    solution,
    template,
    templateExplanation,
    runner,
    runnerExplanation,
    numberOfTestCases,
    testCases,
    resetForm,
    toCreateChallengeForm,
    examples,
    numberOfExamples,
    createNumberOfSamples: createNumberOfSampels,
    otherLanguages,
    extraLanguages,
    extraSetups,
    isExtraLanguage,
    toggleExtraLanguage,
  }
}
