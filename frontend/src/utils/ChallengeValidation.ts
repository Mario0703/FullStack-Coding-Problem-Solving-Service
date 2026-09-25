import type { challangeForm } from '@/components/AdminPages/LanguageType'

export function isChallengeFormValid(challengeData: challangeForm): boolean {
  const hasRequiredFields = Boolean(
    challengeData.name.trim() &&
      challengeData.description.trim() &&
      challengeData.difficulty &&
      challengeData.category &&
      challengeData.language &&
      challengeData.solution.trim() &&
      challengeData.template.trim() &&
      challengeData.templateExplanation.trim() &&
      challengeData.runner.trim() &&
      challengeData.runnerExplanation.trim(),
  )

  const hasCompleteTestCases =
    challengeData.testCases.length > 0 &&
    challengeData.testCases.every(
      (testCase) =>
        testCase.input.trim() !== '' && testCase.expectedOutput.trim() !== '',
    )

  return hasRequiredFields && hasCompleteTestCases
}
