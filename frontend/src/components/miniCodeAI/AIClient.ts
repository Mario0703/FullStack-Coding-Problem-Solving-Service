import { ref } from 'vue'
import { askMiniHelper } from '@/utils/miniHelper'

export type ResponseContext = {
  challengeId: string
  currentCode?: string
  currentLanguage?: string
  conversationId?: string
}

export const greetings = ref<string[]>([
  'Hi, I am miniHelper, your coding assistant. How can I help you today? ' +
    'I can help you with hints and guides towards the given challenge. ' +
    'I can also help you with code snippets and explanations. Please provide me ' +
    'with the challenge details and I will do my best to assist you.',
])


export async function responseFormatter(
  question: string,
  context: ResponseContext,
): Promise<string> {
  return askMiniHelper({
    challengeId: context.challengeId,
    question,
    code: context.currentCode,
    language: context.currentLanguage,
    conversationId: context.conversationId,
  })
}
