import { isblank, isValidEmail, isValidPassword, inputsMatch } from '@/utils/Login/UserInputCheck'

export type ValidationResult = {
  isValid: boolean
  error: string
}

export function validateEmailChange(newEmail: string, confirmNewEmail: string): ValidationResult {
  if (isblank(newEmail)) {
    return { isValid: false, error: 'New email cannot be blank.' }
  }

  if (!isValidEmail(newEmail)) {
    return { isValid: false, error: 'Enter a valid email address.' }
  }

  if (!inputsMatch(newEmail, confirmNewEmail)) {
    return { isValid: false, error: 'New email and confirmation do not match.' }
  }

  return { isValid: true, error: '' }
}

export function validatePasswordChange(
  currentPassword: string,
  newPassword: string,
  confirmNewPassword: string,
): ValidationResult {
  if (isblank(currentPassword)) {
    return { isValid: false, error: 'Current password cannot be blank.' }
  }

  if (isblank(newPassword)) {
    return { isValid: false, error: 'New password cannot be blank.' }
  }

  if (!isValidPassword(newPassword)) {
    return {
      isValid: false,
      error:
        'Password must be at least 8 characters and contain an uppercase letter, lowercase letter, and number.',
    }
  }

  if (!inputsMatch(newPassword, confirmNewPassword)) {
    return { isValid: false, error: 'New password and confirmation do not match.' }
  }

  return { isValid: true, error: '' }
}

export function validateUserNameChange(
  newUserName: string,
  confirmNewUserName: string,
): ValidationResult {
  if (isblank(newUserName)) {
    return { isValid: false, error: 'New user name cannot be blank.' }
  }

  if (!inputsMatch(newUserName, confirmNewUserName)) {
    return { isValid: false, error: 'New user name and confirmation do not match.' }
  }

  return { isValid: true, error: '' }
}

export function validateForgotPassword(email: string): ValidationResult {
  if (isblank(email)) {
    return { isValid: false, error: 'Email cannot be blank.' }
  }

  if (!isValidEmail(email)) {
    return { isValid: false, error: 'Enter a valid email address.' }
  }

  return { isValid: true, error: '' }
}

export function validateSignup(data: {
  userName: string
  email: string
  confirmEmail: string
  password: string
  confirmPassword: string
}): ValidationResult {
  if (isblank(data.userName)) {
    return { isValid: false, error: 'User name cannot be blank.' }
  }

  if (isblank(data.email)) {
    return { isValid: false, error: 'Email cannot be blank.' }
  }

  if (!isValidEmail(data.email)) {
    return { isValid: false, error: 'Enter a valid email address.' }
  }

  if (!inputsMatch(data.email, data.confirmEmail)) {
    return { isValid: false, error: 'Email and confirmation do not match.' }
  }

  if (isblank(data.password)) {
    return { isValid: false, error: 'Password cannot be blank.' }
  }

  if (!isValidPassword(data.password)) {
    return {
      isValid: false,
      error:
        'Password must be at least 8 characters and contain an uppercase letter, lowercase letter, and number.',
    }
  }

  if (!inputsMatch(data.password, data.confirmPassword)) {
    return { isValid: false, error: 'Password and confirmation do not match.' }
  }

  return { isValid: true, error: '' }
}
