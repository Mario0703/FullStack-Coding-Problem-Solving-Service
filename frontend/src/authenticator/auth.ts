import { ref } from "vue";

export type UserData = {
  userId: string;
  userName: string;
  email: string;
};

export const currentUser = ref<UserData | null>(null);
export const isAdmin = ref(false);
export const isLoadingUser = ref(true);
export const hasCheckedUser = ref(false);
export const authError = ref("");

export async function loadCurrentUser(): Promise<UserData | null> {
  isLoadingUser.value = true;
  authError.value = "";

  try {
    const response = await fetch("http://localhost:8082/api/auth/me", {
      method: "GET",
      credentials: "include",
    });

    if (response.status === 401) {
      currentUser.value = null;
      return null;
    }

    if (!response.ok) {
      throw new Error("Unable to check authentication");
    }

    currentUser.value = (await response.json()) as UserData;
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

export async function loadCurrentAdmin(): Promise<void> {
  const response = await fetch("http://localhost:8082/api/auth/admin", {
    credentials: "include",
  });

  if (!response.ok) {
    isAdmin.value = false;
    throw new Error("Admin is not authenticated");
  }

  isAdmin.value = true;
}

export async function isAdminAuthenticated(): Promise<boolean> {
  try {
    await loadCurrentAdmin();
    return true;
  } catch {
    isAdmin.value = false;
    return false;
  }
}


export function clearCurrentUser(): void {
  currentUser.value = null;
  hasCheckedUser.value = true;
}
