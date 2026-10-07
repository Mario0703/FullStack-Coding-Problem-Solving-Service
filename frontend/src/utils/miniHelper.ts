import { baseUrl } from '@/utils/api'
import { readErrorMessage } from '@/utils/apiError'

/**
 * Request sent to the backend miniHelper endpoint. The OpenAI call and the API
 * key live entirely on the server; the browser only sends the question and the
 * code the user is currently editing.
 */
export type MiniHelperContext = {
  challengeId: string
  question: string
  code?: string
  language?: string
  conversationId?: string
}

type MiniHelperResponse = {
  answer: string
}

export async function askMiniHelper(context: MiniHelperContext): Promise<string> {
  const response = await fetch(`${baseUrl}/api/ai/hint`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify({
      challengeId: context.challengeId,
      question: context.question,
      code: context.code,
      language: context.language,
      conversationId: context.conversationId,
    }),
  })

  if (!response.ok) {
    throw new Error(
      await readErrorMessage(response, 'miniHelper could not generate a hint. Please try again.'),
    )
  }

  const data = (await response.json()) as MiniHelperResponse
  return data.answer
}

/**
 * Tells the backend to forget a conversation. Best-effort: called when the
 * user leaves the challenge page or closes the tab.
 */
export async function clearMiniHelperConversation(conversationId: string): Promise<void> {
  await fetch(`${baseUrl}/api/ai/conversation/${encodeURIComponent(conversationId)}`, {
    method: 'DELETE',
    credentials: 'include',
    keepalive: true,
  })
}
