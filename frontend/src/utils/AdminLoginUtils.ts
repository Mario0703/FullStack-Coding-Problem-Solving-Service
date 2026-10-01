export interface AdminLoginCredentials {
  adminUserName: string
  adminPassword: string
}

const ADMIN_LOGIN_URL = 'http://localhost:8082/api/admin/login'

export function loginAdmin(credentials: AdminLoginCredentials): Promise<Response> {
  return fetch(ADMIN_LOGIN_URL, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify(credentials),
  })
}
