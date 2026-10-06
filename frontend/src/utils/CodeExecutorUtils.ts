import { baseUrl } from '@/utils/api'

export interface ChallengeTemplateResponse {
  language: string
  template: string
  explanation: string
}

export interface ExecutionResult {
  exitCode: number
  output: string
  passingTestCases: number
  failingTestCases: number
  status: SubmissionStatus
}

export type SubmissionStatus =
  | 'ACCEPTED'
  | 'WRONG_ANSWER'
  | 'COMPILE_ERROR'
  | 'RUNTIME_ERROR'
  | 'TIME_LIMIT'

const submissionStatusMeta: Record<SubmissionStatus, { label: string; color: string }> = {
  ACCEPTED: { label: 'Solved', color: 'success' },
  WRONG_ANSWER: { label: 'Wrong Answer', color: 'error' },
  COMPILE_ERROR: { label: 'Compilation Error', color: 'error' },
  RUNTIME_ERROR: { label: 'Runtime Error', color: 'error' },
  TIME_LIMIT: { label: 'Time Limit Exceeded', color: 'warning' },
}

export function submissionStatusLabel(status: string): string {
  return submissionStatusMeta[status as SubmissionStatus]?.label ?? status
}

export function submissionStatusColor(status: string): string {
  return submissionStatusMeta[status as SubmissionStatus]?.color ?? 'primary'
}

export interface CodeExecutionRequest {
  userId: string
  challengeId: string
  language: string
  code: string
}

export function languageLabel(value: string): string {
  const labels: Record<string, string> = {
    python: 'Python',
    java: 'Java',
    cplusplus: 'C++',
    csharp: 'C#',
  }

  return labels[value] ?? value
}

export const validLanguages = ['python', 'java', 'cplusplus', 'csharp']

export async function loadChallengeTemplate(
  challengeId: string,
  language?: string,
): Promise<ChallengeTemplateResponse> {
  const query = language ? `?language=${encodeURIComponent(language)}` : ''
  const response = await fetch(`${baseUrl}/api/challenge/${challengeId}/template${query}`, {
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
