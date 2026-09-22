import { describe, expect, it } from 'vitest'
import {
  getLoginValidationErrors,
  hasValidSignupDetails,
  inputsMatch,
  isValidEmail,
  isValidPassword,
} from '@/utils/UserInputCheck'

describe('signup validation', () => {
  it('accepts matching non-empty confirmation values', () => {
    expect(inputsMatch('user@example.com', 'user@example.com')).toBe(true)
  })

  it('rejects empty or different confirmation values', () => {
    expect(inputsMatch('', '')).toBe(false)
    expect(inputsMatch('user@example.com', 'other@example.com')).toBe(false)
  })

  it('accepts a password with eight characters, uppercase, lowercase, and a number', () => {
    expect(isValidPassword('Password1')).toBe(true)
  })

  it('rejects a password that misses a required character type or length', () => {
    expect(isValidPassword('password1')).toBe(false)
    expect(isValidPassword('PASSWORD1')).toBe(false)
    expect(isValidPassword('Password')).toBe(false)
    expect(isValidPassword('Pass1')).toBe(false)
  })

  it('accepts complete, matching signup details', () => {
    expect(
      hasValidSignupDetails({
        email: 'user@example.com',
        confirmEmail: 'user@example.com',
        password: 'Password1',
        confirmPassword: 'Password1',
      }),
    ).toBe(true)
  })

  it('rejects signup details with mismatched confirmation fields', () => {
    expect(
      hasValidSignupDetails({
        email: 'user@example.com',
        confirmEmail: 'other@example.com',
        password: 'Password1',
        confirmPassword: 'Password2',
      }),
    ).toBe(false)
  })

  it('rejects signup details with an invalid email format', () => {
    expect(
      hasValidSignupDetails({
        email: 'not-an-email',
        confirmEmail: 'not-an-email',
        password: 'Password1',
        confirmPassword: 'Password1',
      }),
    ).toBe(false)
  })

  it('accepts an email with a valid format', () => {
    expect(isValidEmail('root2112@gmail.com')).toBe(true)
  })

  it('rejects an email without a valid format', () => {
    expect(isValidEmail('not-an-email')).toBe(false)
  })

  it('returns shared login validation errors', () => {
    expect(getLoginValidationErrors({
      identifier: '',
      password: '',
      identifierType: 'email',
    })).toEqual(['Email is required', 'Password is required'])

    expect(getLoginValidationErrors({
      identifier: 'not-an-email',
      password: 'Password1',
      identifierType: 'email',
    })).toEqual(['Email is invalid'])
  })
})
