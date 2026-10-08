
export async function readErrorMessage(
  response: Response,
  fallback = `Request failed (${response.status})`,
): Promise<string> {
  try {
    const body = await response.text()
    if (!body) {
      return fallback
    }

    try {
      const parsed = JSON.parse(body) as { message?: string; detail?: string; error?: string }
      return parsed.message ?? parsed.detail ?? parsed.error ?? body
    } catch {
      return body
    }
  } catch {
    return fallback
  }
}
