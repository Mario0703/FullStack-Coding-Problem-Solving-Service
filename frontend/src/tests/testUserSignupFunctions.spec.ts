import { describe, expect, it } from 'vitest'
import {
  emailIsValid,
  hasValidSignupDetails,
  inputsMatch,
  isValidPassword,
} from '@/utils/signupValidation'

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
    expect(emailIsValid('root2112@gmail.com')).toBe(true)
  })

  it('rejects an email without a valid format', () => {
    expect(emailIsValid('not-an-email')).toBe(false)
  })
})
