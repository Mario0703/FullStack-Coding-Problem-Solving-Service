import { ref } from "vue";

export type UserData = {
  userId: string;
  userName: string;
  email: string;
};

export const currentUser = ref<UserData | null>(null);
export const isLoadingUser = ref(false);

export async function loadCurrentUser(): Promise<UserData | null> {
  isLoadingUser.value = true;

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
  } finally {
    isLoadingUser.value = false;
  }
}

export function clearCurrentUser(): void {
  currentUser.value = null;
}
