import { describe, expect, it } from 'vitest'
import {
  languageLabel,
  submissionStatusColor,
  submissionStatusLabel,
} from '@/utils/Challenges/API/execution'

describe('submission status labels', () => {
  it('labels known submission statuses', () => {
    expect(submissionStatusLabel('ACCEPTED')).toBe('Solved')
    expect(submissionStatusLabel('WRONG_ANSWER')).toBe('Wrong Answer')
    expect(submissionStatusLabel('COMPILE_ERROR')).toBe('Compilation Error')
    expect(submissionStatusLabel('RUNTIME_ERROR')).toBe('Runtime Error')
    expect(submissionStatusLabel('TIME_LIMIT')).toBe('Time Limit Exceeded')
  })

  it('falls back to the raw status for unknown values', () => {
    expect(submissionStatusLabel('SOMETHING_ELSE')).toBe('SOMETHING_ELSE')
  })
})

describe('submission status colors', () => {
  it('colors accepted submissions as success', () => {
    expect(submissionStatusColor('ACCEPTED')).toBe('success')
  })

  it('colors failed submissions as error and timeouts as warning', () => {
    expect(submissionStatusColor('WRONG_ANSWER')).toBe('error')
    expect(submissionStatusColor('COMPILE_ERROR')).toBe('error')
    expect(submissionStatusColor('RUNTIME_ERROR')).toBe('error')
    expect(submissionStatusColor('TIME_LIMIT')).toBe('warning')
  })

  it('falls back to primary for unknown statuses', () => {
    expect(submissionStatusColor('SOMETHING_ELSE')).toBe('primary')
  })
})

describe('language labels', () => {
  it('labels the supported languages', () => {
    expect(languageLabel('python')).toBe('Python')
    expect(languageLabel('java')).toBe('Java')
    expect(languageLabel('cplusplus')).toBe('C++')
    expect(languageLabel('csharp')).toBe('C#')
  })
})
