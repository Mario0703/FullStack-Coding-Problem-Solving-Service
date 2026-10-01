import { baseUrl } from '@/utils/api'

export interface ChallengeTemplateResponse {
  language: string
  template: string
  explanation: string
}

export interface ExecutionResult {
  exitCode: number
  output: string
}

export interface CodeExecutionRequest {
  userId: string
  challengeId: string
  language: string
  code: string
}

export function languageLabel(value: string): string {
  const labels: Record<string, string> = {
    PYTHON: 'Python',
    JAVA: 'Java',
    'C++': 'C++',
    'C#': 'C#',
  }

  return labels[value] ?? value
}

export const validLanguages = ['PYTHON', 'JAVA', 'C++', 'C#']

export async function loadChallengeTemplate(
  challengeId: string,
): Promise<ChallengeTemplateResponse> {
  const response = await fetch(`${baseUrl}/api/challenge/${challengeId}/template`, {
    credentials: 'include',
  })

  if (!response.ok) {
    throw new Error('The challenge template could not be loaded.')
  }

  return (await response.json()) as ChallengeTemplateResponse
}

export async function submitCode(request: CodeExecutionRequest): Promise<ExecutionResult> {
  const response = await fetch(`${baseUrl}/api/code/playground/submit`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    credentials: 'include',
    body: JSON.stringify(request),
  })

  if (!response.ok) {
    throw new Error(`Error: ${await response.text()}`)
  }

  return (await response.json()) as ExecutionResult
}
