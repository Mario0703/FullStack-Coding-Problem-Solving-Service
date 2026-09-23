export type ChallengeDifficulty = "EASY" | "MEDIUM" | "HARD";

export type ChallengeCategory = "GENERAL" | "ARRAYS" | "STRINGS" | "TREES";

export type Challenge = {
  id: string;
  name: string;
  description: string;
  difficulty: ChallengeDifficulty;
  catagory: ChallengeCategory;
};

export type ChallengeOption<T extends string> = {
  title: string;
  value: T;
};

export const difficultyOptions: ChallengeOption<ChallengeDifficulty>[] = [
  { title: "Easy", value: "EASY" },
  { title: "Medium", value: "MEDIUM" },
  { title: "Hard", value: "HARD" },
];

export const categoryOptions: ChallengeOption<ChallengeCategory>[] = [
  { title: "General", value: "GENERAL" },
  { title: "Arrays", value: "ARRAYS" },
  { title: "Strings", value: "STRINGS" },
  { title: "Trees", value: "TREES" },
];

export function formatChallengeValue(value: string): string {
  return value.charAt(0) + value.slice(1).toLowerCase();
}
