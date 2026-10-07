import { watch } from 'vue'
import { FormSetup } from '@/utils/Challenges/Authoring/FormSetup.ts'
import { Languages } from './Languages'
import { ChallengeFormBuilder } from './ChallengeFormBuilder'
import type { CreateChallengeForm } from '../Types/authoringTypes'
import type { Language } from '../Types/types'

export function useChallengeForm() {
  const setup = new FormSetup()
  const languages = new Languages(setup)
  const builder = new ChallengeFormBuilder(setup)

  watch(setup.language, (selectedLanguage) => {
    if (selectedLanguage) {
      languages.applyLanguage(selectedLanguage)
    }
  })

  function createNumberOfSamples<T>(arr: T[], newSize: number, defaultValue: T) {
    while (newSize > arr.length) arr.push({ ...defaultValue })
    arr.length = newSize
  }

  function resetForm() {
    setup.resetForm()
  }

  function toCreateChallengeForm(): CreateChallengeForm {
    return builder.toCreateChallengeForm()
  }

  return {
    ...setup,
    resetForm,
    toCreateChallengeForm,
    createNumberOfSamples,
    otherLanguages: languages.otherLanguages,
    isExtraLanguage: (value: Language) => languages.isExtraLanguage(value),
    toggleExtraLanguage: (value: Language) => languages.toggleExtraLanguage(value),
  }
}
