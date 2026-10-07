import type { Language } from '../Types/types'
import type { LanguageSetup } from '../Types/authoringTypes'

export const languageSetups: Record<Language, LanguageSetup> = {
  python: {
    userTemplate: `def add(a, b):
    # Write your solution here.
    pass
`,
    userTemplateExplanation:
      'The user only changes the function body. The function name and arguments must stay compatible with the runner.',
    runner: `a, b = map(int, input().split())
print(add(a, b))
`,
    runnerExplanation:
      'The runner reads two integers from standard input, calls add(a, b), and prints the returned value. It is appended by the server when the submission runs.',
  },
  java: {
    userTemplate: `class Solution {
    public static int add(int a, int b) {
        // Write your solution here.
        return 0;
    }
}
`,
    userTemplateExplanation:
      'The user implements Solution.add. The class name, method name, and argument types must stay compatible with the runner.',
    runner: `import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        System.out.println(Solution.add(a, b));
    }
}
`,
    runnerExplanation:
      'The runner reads two integers, calls Solution.add(a, b), and prints the result. The server combines it with the user template before compiling.',
  },
  cplusplus: {
    userTemplate: `class Solution {
public:
    int add(int a, int b) {
        // Write your solution here.
        return 0;
    }
};
`,
    userTemplateExplanation:
      'The user implements Solution::add. The class name, method name, and argument types must stay compatible with the runner.',
    runner: `#include <iostream>

int main() {
    int a, b;
    std::cin >> a >> b;

    Solution solution;
    std::cout << solution.add(a, b) << '\\n';
    return 0;
}
`,
    runnerExplanation:
      'The runner reads two integers, calls Solution::add, and prints the result. The server combines it with the user template before compiling.',
  },
  csharp: {
    userTemplate: `public class Solution {
    public int Add(int a, int b) {
        // Write your solution here.
        return 0;
    }
}
`,
    userTemplateExplanation:
      'The user implements Solution.Add. The class name, method name, and argument types must stay compatible with the runner.',
    runner: `using System;

public class Main {
    public static void Main(string[] args) {
        string[] values = Console.ReadLine()!.Split();
        int a = int.Parse(values[0]);
        int b = int.Parse(values[1]);

        Solution solution = new Solution();
        Console.WriteLine(solution.Add(a, b));
    }
}
`,
    runnerExplanation:
      'The runner reads two integers, calls Solution.Add, and prints the result. The server combines it with the user template before compiling.',
  },
}
