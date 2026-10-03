import { ref } from "vue";
import { baseUrl } from '@/utils/api';


export type UserData = {
  userId: string;
  userName: string;
  email: string;
  role: string
};

export const currentUser = ref<UserData | null>(null);
export const currentRole = ref<string | null>(null);
export const isLoadingUser = ref(true);
export const hasCheckedUser = ref(false);
export const authError = ref("");

export async function loadCurrentUser(): Promise<UserData | null> {
  isLoadingUser.value = true;
  authError.value = "";

  try {
    const response = await fetch(`${baseUrl}/api/auth/me`, {
      method: "GET",
      credentials: "include",
    });

    if (response.status === 401) {
      currentUser.value = null;
      currentRole.value = null;
      return null;
    }

    if (!response.ok) {
      throw new Error("Unable to check authentication");
    }

    currentUser.value = await response.json();
    currentRole.value = currentUser.value?.role ?? null;
    console.log("Current user:", currentUser.value);
    return currentUser.value;
  } catch (error) {
    currentUser.value = null;
    authError.value = "Unable to check your login status. Please try again later.";
    throw error;
  } finally {
    hasCheckedUser.value = true;
    isLoadingUser.value = false;
  }
}

export async function loadCurrentAdmin(): Promise<string> {
  const response = await fetch(`${baseUrl}/api/auth/admin`, {
    credentials: "include",
  });

  if (!response.ok) {
    currentRole.value = null;
    throw new Error("Admin is not authenticated");
  }

  const role = (await response.text()).trim();
  currentRole.value = role;
  return role;
}


export function clearCurrentUser(): void {
  currentUser.value = null;
  currentRole.value = null;
  hasCheckedUser.value = true;
}
