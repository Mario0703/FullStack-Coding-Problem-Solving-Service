import { isblank, isValidEmail } from '../signupValidation.ts'

export {
  hasValidSignupDetails,
  isblank,
  isValidEmail,
  inputsMatch,
  isValidPassword,
} from '../signupValidation.ts'

export type { SignupDetails } from '../signupValidation.ts'

export type LoginDetails = {
  identifier: string
  password: string
  identifierType: 'email' | 'username'
}

export function getLoginValidationErrors(details: LoginDetails): string[] {
  const errors: string[] = []

  if (isblank(details.identifier)) {
    errors.push(details.identifierType === 'email' ? 'Email is required' : 'Username is required')
  } else if (details.identifierType === 'email' && !isValidEmail(details.identifier)) {
    errors.push('Email is invalid')
  }

  if (isblank(details.password)) {
    errors.push('Password is required')
  }

  return errors
}
