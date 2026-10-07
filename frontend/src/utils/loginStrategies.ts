import { baseUrl } from '@/utils/api'
import { readErrorMessage } from '@/utils/apiError'
import { loginAdmin } from '@/utils/AdminLoginUtils'

export type LoginCredentials = {
  identifier: string
  password: string
}


export type LoginAction = (credentials: LoginCredentials) => Promise<void>

async function postLogin(url: string, body: unknown, fallback: string): Promise<void> {
  let response: Response
  try {
    response = await fetch(url, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify(body),
    })
  } catch {
    throw new Error('Unable to reach the server. Please try again later.')
  }

  if (!response.ok) {
    throw new Error(await readErrorMessage(response, fallback))
  }
}

export const loginWithUserName: LoginAction = ({ identifier, password }) =>
  postLogin(
    `${baseUrl}/api/user/login`,
    { userName: identifier, password },
    'Invalid username/email or password.',
  )

export const loginWithEmail: LoginAction = ({ identifier, password }) =>
  postLogin(
    `${baseUrl}/api/user/login`,
    { email: identifier, password },
    'Invalid username/email or password.',
  )

/** Sign in as an admin. */
export const loginAsAdmin: LoginAction = async ({ identifier, password }) => {
  let response: Response
  try {
    response = await loginAdmin({ adminUserName: identifier, adminPassword: password })
  } catch {
    throw new Error('Unable to reach the server. Please try again later.')
  }

  if (!response.ok) {
    throw new Error(await readErrorMessage(response, 'Invalid admin username or password.'))
  }
}

/** User login strategies, keyed by the identifier type. */
export const userLoginStrategies = {
  username: loginWithUserName,
  email: loginWithEmail,
} satisfies Record<'username' | 'email', LoginAction>



