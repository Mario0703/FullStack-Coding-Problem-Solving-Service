const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d).{8,}$/
const emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/

export type SignupDetails = {
  email: string
  confirmEmail: string
  password: string
  confirmPassword: string
}

export function inputsMatch(input: string, confirmation: string): boolean {
  return input.length > 0 && input === confirmation
}

export function isValidPassword(value: string): boolean {
  return passwordRegex.test(value)
}

export function hasValidSignupDetails(details: SignupDetails): boolean {
  return (
    emailIsValid(details.email) &&
    inputsMatch(details.email, details.confirmEmail) &&
    inputsMatch(details.password, details.confirmPassword) &&
    isValidPassword(details.password)
  )
}

export function emailIsValid(email: string): boolean {
  return emailRegex.test(email)
}
