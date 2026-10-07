import {
  type LoginCredentials,
  LoginWithEmail,
  LoginWithUserName
} from "@/utils/Login/loginStrategies.ts";
import {type AdminLoginCredentials, loginAdmin} from "@/utils/Login/Admin/AdminLoginUtils.ts";

interface LoginFactory {
  createLoginAdminWithUserNameAndPassword(credentials: AdminLoginCredentials): Promise<Response>
  createUserLoginWithEmailAndPassword(credentials: LoginCredentials): Promise<void>
  createUserLoginWithUsernameAndPassword(credentials: LoginCredentials): Promise<void>
}

export class LoginFactoryImpl implements LoginFactory {
  public async createLoginAdminWithUserNameAndPassword(credentials: AdminLoginCredentials): Promise<Response> {
    return loginAdmin(credentials)
  }

  public async createUserLoginWithEmailAndPassword(credentials: LoginCredentials): Promise<void> {
    const loginStrategy = new LoginWithEmail()
    await loginStrategy.LoginAction(credentials)
  }

  public async createUserLoginWithUsernameAndPassword(credentials: LoginCredentials): Promise<void> {
    const loginStrategy = new LoginWithUserName()
    await loginStrategy.LoginAction(credentials)
  }
}

export const loginFactory = new LoginFactoryImpl()
