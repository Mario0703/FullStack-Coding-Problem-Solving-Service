const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d).{8,}$/

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
    inputsMatch(details.email, details.confirmEmail) &&
    inputsMatch(details.password, details.confirmPassword) &&
    isValidPassword(details.password)
  )
}
