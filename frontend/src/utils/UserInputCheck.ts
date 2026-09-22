const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d).{8,}$/
const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/

export type SignupDetails = {
  userName?: string
  email: string
  confirmEmail: string
  password: string
  confirmPassword: string
}

export type LoginDetails = {
  identifier: string
  password: string
  identifierType: 'email' | 'username'
}

export function isblank(value: string): boolean {
  return value.trim().length === 0
}

export function inputsMatch(input: string, confirmation: string): boolean {
  return !isblank(input) && input === confirmation
}

export function isValidEmail(value: string): boolean {
  return emailRegex.test(value.trim())
}

export function isValidPassword(value: string): boolean {
  return passwordRegex.test(value)
}

export function hasValidSignupDetails(details: SignupDetails): boolean {
  return (
    (!details.userName || !isblank(details.userName)) &&
    isValidEmail(details.email) &&
    inputsMatch(details.email, details.confirmEmail) &&
    inputsMatch(details.password, details.confirmPassword) &&
    isValidPassword(details.password)
  )
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
