import { baseUrl } from '@/utils/api.ts'
import { readErrorMessage } from '@/utils/apiError.ts'
import { loginAdmin } from '@/utils/Login/Admin/AdminLoginUtils.ts'

export type LoginCredentials = {
  identifier: string
  password: string
}

export interface LoginStrategy {
  LoginAction: (credentials: LoginCredentials) => Promise<void>
}

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

export class LoginWithUserName implements LoginStrategy {
  public async LoginAction({ identifier, password }: LoginCredentials): Promise<void> {
    const URL = `${baseUrl}/api/user/login`
    const feedback = 'Invalid username/email or password.'
    await postLogin(URL, { userName: identifier, password }, feedback)
  }
}

export class LoginWithEmail implements LoginStrategy {
  public async LoginAction({ identifier, password }: LoginCredentials): Promise<void> {
    const URL = `${baseUrl}/api/user/login`
    const feedback = 'Invalid username/email or password.'
    await postLogin(URL, { email: identifier, password }, feedback)
  }
}

export class LoginAsAdmin implements LoginStrategy {
  public async LoginAction({ identifier, password }: LoginCredentials): Promise<void> {
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
}

