import {
  LoginAsAdmin,
  LoginWithEmail,
  LoginWithUserName,
  type LoginCredentials,
} from '@/utils/Login/loginStrategies.ts'

interface LoginFactory {
  createLoginAdminWithUserNameAndPassword(credentials: LoginCredentials): Promise<void>
  createUserLoginWithEmailAndPassword(credentials: LoginCredentials): Promise<void>
  createUserLoginWithUsernameAndPassword(credentials: LoginCredentials): Promise<void>
}

export class LoginFactoryImpl implements LoginFactory {
  public async createLoginAdminWithUserNameAndPassword(
    credentials: LoginCredentials,
  ): Promise<void> {
    const loginStrategy = new LoginAsAdmin()
    await loginStrategy.LoginAction(credentials)
  }

  public async createUserLoginWithEmailAndPassword(credentials: LoginCredentials): Promise<void> {
    const loginStrategy = new LoginWithEmail()
    await loginStrategy.LoginAction(credentials)
  }

  public async createUserLoginWithUsernameAndPassword(
    credentials: LoginCredentials,
  ): Promise<void> {
    const loginStrategy = new LoginWithUserName()
    await loginStrategy.LoginAction(credentials)
  }
}

export const loginFactory = new LoginFactoryImpl()
