import { ref, watch } from 'vue'
import { languageSetups } from '@/components/AdminPages/LanguageTemplet'
import type { challangeForm, TestCase } from '@/components/AdminPages/LanguageType'
import type {
  ChallengeCategory,
  ChallengeDifficulty,
  Language,
} from '@/utils/AdminUtils'

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
  const testCases = ref<TestCase[]>([{ input: '', output: '' }])
  const examples = ref<{ input: string; output: string }[]>([{ input: '', output: '' }])
  const numberOfExamples = ref(1)

  watch(language, (selectedLanguage) => {
    if (selectedLanguage) {
      const setup = languageSetups[selectedLanguage]
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
    testCases.value = [{ input: '', output: '' }]
    examples.value = [{ input: '', output: '' }]
    numberOfExamples.value = 1
  }

  function createNumberOfSampels(arr :{ input : string, output: string}[], newSize : number, defaultValue : { input : string, output: string}){
    while(newSize > arr.length)
      arr.push(defaultValue);
    arr.length = newSize;
  }


  function toChallengeForm(): challangeForm {
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
        output: testCase.output.trim(),
      })),
      numberOfTestCases: testCases.value.length,
      examples: examples.value.map((example) => ({
        input: example.input.trim(),
        output: example.output.trim(),
      })),
      numberOfExamples: numberOfExamples.value,
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
    toChallengeForm,
    examples,
    numberOfExamples,
    createNumberOfSampels
  }
}
