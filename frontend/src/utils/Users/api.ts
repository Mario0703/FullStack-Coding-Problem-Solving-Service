import { baseUrl } from '@/utils/api'
import type {
  ChangeEmailRequest,
  ChangePasswordRequest,
  ChangeUserNameRequest,
  ForgotPasswordRequest,
  PublicUser,
  ResetPasswordRequest,
  SignupData,
  SubmissionDetails,
  UserData,
} from './types'

export async function fetchCurrentUser(): Promise<UserData | null> {
  const response = await fetch(`${baseUrl}/api/auth/me`, {
    method: 'GET',
    credentials: 'include',
  })

  if (response.status === 401) {
    return null
  }

  if (!response.ok) {
    throw new Error('Unable to check authentication')
  }

  return (await response.json()) as UserData
}

export async function fetchCurrentAdmin(): Promise<string> {
  const response = await fetch(`${baseUrl}/api/auth/admin`, {
    credentials: 'include',
  })

  if (!response.ok) {
    throw new Error('Admin is not authenticated')
  }

  return (await response.text()).trim()
}

export async function signupUser(userData: SignupData): Promise<Response> {
  const response = await fetch(`${baseUrl}/api/user/signup`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(userData),
  })

  return response
}

export async function changeEmail(newEmail: string): Promise<Response> {
  const body: ChangeEmailRequest = { newEmail: newEmail.trim() }

  return fetch(`${baseUrl}/api/user/change-email`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify(body),
  })
}

export async function changePassword(
  currentPassword: string,
  newPassword: string,
): Promise<Response> {
  const body: ChangePasswordRequest = { currentPassword, newPassword }

  const response = await fetch(`${baseUrl}/api/user/change-password`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify(body),
  })

  return response
}

export async function changeUserName(newUserName: string): Promise<Response> {
  const body: ChangeUserNameRequest = { newUserName: newUserName.trim() }

  return fetch(`${baseUrl}/api/user/change-username`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify(body),
  })
}

export async function requestPasswordReset(email: string): Promise<Response> {
  const body: ForgotPasswordRequest = { email: email.trim() }

  return fetch(`${baseUrl}/api/user/forgot-password`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  })
}

export async function resetPassword(token: string, newPassword: string): Promise<Response> {
  const body: ResetPasswordRequest = { token, newPassword }

  return fetch(`${baseUrl}/api/user/reset-password`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(body),
  })
}

export async function getPublicUserProfile(userId: string): Promise<PublicUser> {
  if (!userId) {
    throw new Error('UserId is required to fetch public user profile data.')
  }

  const response = await fetch(`${baseUrl}/api/public-profile/${userId}`, {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
    },
  })

  if (!response.ok) {
    throw new Error(`Failed to fetch public user profile data: ${response.statusText}`)
  }

  const data = await response.json()

  if (data === null) {
    throw new Error('Public user profile data is null.')
  }

  return data as PublicUser
}

export async function getSubmissionDetails(submissionId: string): Promise<SubmissionDetails> {
  if (!submissionId) {
    throw new Error('Invalid submission ID')
  }

  const response = await fetch(`${baseUrl}/api/submission/details?submissionId=${submissionId}`, {
    credentials: 'include',
  })

  if (!response.ok) {
    throw new Error('Failed to load submission')
  }

  return (await response.json()) as SubmissionDetails
}
