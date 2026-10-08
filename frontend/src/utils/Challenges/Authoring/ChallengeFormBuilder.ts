import type { CreateChallengeForm, LanguageSetupInput } from '../Types/authoringTypes'
import type { Language } from '../Types/types'
import type { FormSetup } from './FormSetup'


export class ChallengeFormBuilder {
  public constructor(private readonly form: FormSetup) {}

  public toCreateChallengeForm(): CreateChallengeForm {
    const primary: LanguageSetupInput = {
      language: this.form.language.value as Language,
      template: this.form.template.value.trim(),
      templateExplanation: this.form.templateExplanation.value.trim(),
      runner: this.form.runner.value.trim(),
      runnerExplanation: this.form.runnerExplanation.value.trim(),
    }

    const extraSetupsPayload: LanguageSetupInput[] = this.form.extraLanguages.value
      .map((value) => this.form.extraSetups.value[value])
      .filter((setup): setup is LanguageSetupInput => Boolean(setup))
      .map((setup) => ({
        language: setup.language,
        template: setup.template.trim(),
        templateExplanation: setup.templateExplanation.trim(),
        runner: setup.runner.trim(),
        runnerExplanation: setup.runnerExplanation.trim(),
      }))

    return {
      name: this.form.name.value.trim(),
      description: this.form.description.value.trim(),
      difficulty: this.form.difficulty.value,
      category: this.form.category.value,
      solution: this.form.solution.value.trim(),
      template: this.form.template.value.trim(),
      templateExplanation: this.form.templateExplanation.value.trim(),
      runner: this.form.runner.value.trim(),
      runnerExplanation: this.form.runnerExplanation.value.trim(),
      language: this.form.language.value,
      testCases: this.form.testCases.value.map((testCase) => ({
        input: testCase.input.trim(),
        expectedOutput: testCase.expectedOutput.trim(),
      })),
      numberOfTestCases: this.form.testCases.value.length,
      examples: this.form.examples.value.map((example) => ({
        inputExample: example.inputExample.trim(),
        outputExample: example.outputExample.trim(),
      })),
      numberOfExamples: this.form.numberOfExamples.value,
      languageSetups: [primary, ...extraSetupsPayload],
    }
  }
}
