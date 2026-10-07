import OpenAI from 'openai'
import { ref } from 'vue'

const apiKey = import.meta.env.VITE_OPENAI_API_KEY
const model = import.meta.env.VITE_OPENAI_MODEL || 'gpt-5.6-luna'


export const client = new OpenAI({
  apiKey: apiKey || 'missing-api-key',
  dangerouslyAllowBrowser: true,
})

export const greetings = ref<string[]>([
  'Hi, I am MiniCodeAI, your coding assistant. How can I help you today? ' +
    'I can help you with hints and guides towards the given challenge. ' +
    'I can also help you with code snippets and explanations. Please provide me ' +
    'with the challenge details and I will do my best to assist you.',
])


const challengeTools: OpenAI.Responses.FunctionTool[] = [
  {
    type: 'function',
    name: 'getChallengeDescription',
    description: 'Get the description of the challenge',
    parameters: {
      type: 'object',
      properties: {
        challengeId: { type: 'string' },
      },
      required: ['challengeId'],
      additionalProperties: false,
    },
    strict: true,
  },
  {
    type: 'function',
    name: 'getChallengeSolution',
    description: 'Get the solution of the challenge',
    parameters: {
      type: 'object',
      properties: {
        challengeId: { type: 'string' },
      },
      required: ['challengeId'],
      additionalProperties: false,
    },
    strict: true,
  },
  {
    type: 'function',
    name: 'getChallengeHint',
    description: 'Get the hint of the challenge',
    parameters: {
      type: 'object',
      properties: {
        challengeId: { type: 'string' },
      },
      required: ['challengeId'],
      additionalProperties: false,
    },
    strict: true,
  },
]

export async function responseFormatter(response: string): Promise<string> {
  if (!apiKey) {
    throw new Error(
      'Missing VITE_OPENAI_API_KEY. Add it to the .env file to use the AI helper.',
    )
  }

  const userQuestion = await client.responses.create({
    model,
    instructions:
      'You are MiniCodeAI, a helpful coding assistant embedded in a coding challenge ' +
      'platform. Give hints and guidance about the challenge the user is working on.',
    input: response,
    tools: challengeTools,
  })

  if(!userQuestion.output_text) {
    throw new Error('No response from MiniCodeAI. Please try again later.')
  }

  return userQuestion.output_text
}
