import { describe, expect, it } from 'vitest'
import { validateResetPassword } from '@/utils/Users/validation'

describe('reset password validation', () => {
  it('accepts a strong password with a matching confirmation', () => {
    expect(validateResetPassword('NewPassword123', 'NewPassword123').isValid).toBe(true)
    expect(validateResetPassword('NewPassword123', 'NewPassword123').error).toBe('')
  })

  it('rejects a blank new password', () => {
    const result = validateResetPassword('', '')
    expect(result.isValid).toBe(false)
    expect(result.error).toBe('New password cannot be blank.')
  })

  it('rejects a weak new password', () => {
    const result = validateResetPassword('password123', 'password123')
    expect(result.isValid).toBe(false)
    expect(result.error).toBe(
      'Password must be at least 8 characters and contain an uppercase letter, lowercase letter, and number.',
    )
  })

  it('rejects a confirmation that does not match the new password', () => {
    const result = validateResetPassword('NewPassword123', 'OtherPassword123')
    expect(result.isValid).toBe(false)
    expect(result.error).toBe('Password and confirmation do not match.')
  })
})