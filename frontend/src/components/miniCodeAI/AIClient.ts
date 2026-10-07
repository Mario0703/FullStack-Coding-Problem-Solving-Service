import { ref } from 'vue'
import { askMiniHelper } from '@/utils/miniHelper'

/**
 * The MiniCode AI client. It is deliberately thin: all OpenAI access happens on
 * the backend, so no API key (and no OpenAI SDK) ever reaches the browser.
 */

/** The context the chat window passes in; the question is passed separately. */
export type ResponseContext = {
  challengeId: string
  currentCode?: string
  currentLanguage?: string
}

export const greetings = ref<string[]>([
  'Hi, I am miniHelper, your coding assistant. How can I help you today? ' +
    'I can help you with hints and guides towards the given challenge. ' +
    'I can also help you with code snippets and explanations. Please provide me ' +
    'with the challenge details and I will do my best to assist you.',
])

/**
 * Sends the user's question to the backend, which assembles the challenge
 * context and asks OpenAI for a hint.
 */
export async function responseFormatter(
  question: string,
  context: ResponseContext,
): Promise<string> {
  return askMiniHelper({
    challengeId: context.challengeId,
    question,
    code: context.currentCode,
    language: context.currentLanguage,
  })
}
