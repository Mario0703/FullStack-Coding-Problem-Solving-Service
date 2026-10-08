import { computed } from 'vue'
import type { ComputedRef } from 'vue'
import { createLanguageSetup, getSupportedLanguages } from './languageSetups'
import type { FormSetup } from './FormSetup'
import type { Language } from '../Types/types'


export class Languages {

  public readonly otherLanguages: ComputedRef<Language[]>

  public constructor(private readonly form: FormSetup) {
    this.otherLanguages = computed(() =>
      getSupportedLanguages().filter((value) => value !== this.form.language.value),
    )
  }

  public isExtraLanguage(value: Language): boolean {
    return this.form.extraLanguages.value.includes(value)
  }

  public toggleExtraLanguage(value: Language): void {
    if (this.isExtraLanguage(value)) {
      this.form.extraLanguages.value = this.form.extraLanguages.value.filter(
        (item) => item !== value,
      )
      return
    }

    const setup = createLanguageSetup(value)
    this.form.extraSetups.value = {
      ...this.form.extraSetups.value,
      [value]: {
        language: value,
        template: setup.userTemplate,
        templateExplanation: setup.userTemplateExplanation,
        runner: setup.runner,
        runnerExplanation: setup.runnerExplanation,
      },
    }
    this.form.extraLanguages.value = [...this.form.extraLanguages.value, value]
  }

  /**
   * Loads a language's setup into the form. A language cannot be both the
   * primary and an additional language, so it is removed from the extras first.
   */
  public applyLanguage(value: Language): void {
    this.form.extraLanguages.value = this.form.extraLanguages.value.filter(
      (item) => item !== value,
    )

    const setup = createLanguageSetup(value)
    this.form.setFormValues({
      template: setup.userTemplate,
      templateExplanation: setup.userTemplateExplanation,
      runner: setup.runner,
      runnerExplanation: setup.runnerExplanation,
    })
  }
}
