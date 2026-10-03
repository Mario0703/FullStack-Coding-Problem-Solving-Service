export interface SignupData {
  userName: string
  email: string
  password: string
}

export interface SignupDetails {
  userName?: string
  email: string
  password: string
  confirmEmail: string
  confirmPassword: string
}

const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d).{8,}$/
const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/

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

export function getSignupValidationError(details: SignupDetails): string {
  return hasValidSignupDetails(details)
    ? ''
    : 'You have invalid fields. Please check your inputs and try again.'
}
