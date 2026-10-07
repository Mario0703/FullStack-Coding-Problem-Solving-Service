import OpenAI from "openai";
import { tool } from "langchain/tools";

const apiKey = import.meta.env.VITE_OPENAI_API_KEY;

if (!apiKey) {
  throw new Error(
    "Missing VITE_OPENAI_API_KEY. Add it to the .Env file to Add AI helper to challenges
  );
}

export const client = new OpenAI({
  apiKey,
  dangerouslyAllowBrowser: true,
});

let greetings = [
  "Hi, i am MiniCodeAI, your coding assistant. How can I help you today? I can help you with hints and guids to" +
  "towards the given challenge. I can also help you with code snippets and explanations. Please provide me with the challenge details and I will do my best to assist you.",
]

const getChallangeDescriptionTool = tool({
  name: "getChallengeDescription",
  description: "Get the description of the challenge",
  func: async (challengeName: string) => {
  }
})

const getChallangeSolutionTool = tool({
  name: "getChallengeSolution",
  description: "Get the solution of the challenge",
  func: async (challengeName: string) => {
  }
})

const getChallangeHintTool = tool({
  name: "getChallengeHint",
  description: "Get the hint of the challenge",
  func: async (challengeName: string) => {
  }
})





