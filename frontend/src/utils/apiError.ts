/**
 * Reads a useful message from a failed API response.
 *
 * Errors handled by the backend's `ApiGlobalHandler` come back as plain text
 * (the message). Errors that never reach it (e.g. filter-chain 401/403) are
 * still Spring's default JSON body. This handles both, so components can show
 * `error.message` directly.
 */
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
