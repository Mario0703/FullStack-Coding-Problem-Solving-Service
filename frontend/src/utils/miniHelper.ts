import { baseUrl } from '@/utils/api'
import { readErrorMessage } from '@/utils/apiError'


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

  const reposeData: MiniHelperResponse = await response.json()
  return reposeData.answer
}

export async function clearMiniHelperConversation(conversationId: string): Promise<void> {
  await fetch(`${baseUrl}/api/ai/conversation/${encodeURIComponent(conversationId)}`, {
    method: 'DELETE',
    credentials: 'include',
    keepalive: true,
  })
}
