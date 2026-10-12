import { baseUrl } from '@/utils/api'
import { readErrorMessage } from '@/utils/apiError'
import type { UserData } from '@/utils/Users/types'

export type StaffRole = 'ADMIN' | 'MODERATOR'

export type StaffAccountData = {
  userName: string
  email: string
  password: string
  staffRole: StaffRole
}

export async function createStaffMember(staff: StaffAccountData): Promise<UserData> {
  const response = await fetch(`${baseUrl}/api/admin/staff`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify(staff),
  })

  if (!response.ok) {
    throw new Error(await readErrorMessage(response, 'The staff account could not be created.'))
  }

  return (await response.json()) as UserData
}