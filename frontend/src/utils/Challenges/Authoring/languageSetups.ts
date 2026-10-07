import type { Language } from '../Types/types'
import type { LanguageSetup } from '../Types/authoringTypes'


export interface LanguageTempleteSetup {
  getTemplate(): string
  getExplanation(): string
  getRunner(): string
  getRunnerExplanation(): string
}

export class Python implements LanguageTempleteSetup {
  public getTemplate(): string {
    return `def add(a, b):
    # Write your solution here.
    pass
`
  }

  public getExplanation(): string {
    return 'The user only changes the function body. The function name and arguments must stay compatible with the runner.'
  }

  public getRunner(): string {
    return `a, b = map(int, input().split())
    print(add(a, b))
    `
  }

  public getRunnerExplanation(): string {
    return 'The runner reads two integers from standard input, calls add(a, b), and prints the returned value. It is appended by the server when the submission runs.'
  }
}

export class Java implements LanguageTempleteSetup {
  public getTemplate(): string {
    return `class Solution {
    public static int add(int a, int b) {
        // Write your solution here.
        return 0;
    }
}
`
  }

  public getExplanation(): string {
    return 'The user implements Solution.add. The class name, method name, and argument types must stay compatible with the runner.'
  }

  public getRunner(): string {
    return `import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int a = scanner.nextInt();
        int b = scanner.nextInt();
        System.out.println(Solution.add(a, b));
    }
}
`
  }

  public getRunnerExplanation(): string {
    return 'The runner reads two integers, calls Solution.add(a, b), and prints the result. The server combines it with the user template before compiling.'
  }
}

export class CPlusPlus implements LanguageTempleteSetup {
  public getTemplate(): string {
    return `class Solution {
public:
    int add(int a, int b) {
        // Write your solution here.
        return 0;
    }
};
`
  }

  public getExplanation(): string {
    return 'The user implements Solution::add. The class name, method name, and argument types must stay compatible with the runner.'
  }

  public getRunner(): string {
    return `#include <iostream>

int main() {
    int a, b;
    std::cin >> a >> b;

    Solution solution;
    std::cout << solution.add(a, b) << '\\n';
    return 0;
}
`
  }

  public getRunnerExplanation(): string {
    return 'The runner reads two integers, calls Solution::add, and prints the result. The server combines it with the user template before compiling.'
  }
}

export class CSharp implements LanguageTempleteSetup {
  public getTemplate(): string {
    return `public class Solution {
    public int Add(int a, int b) {
        // Write your solution here.
        return 0;
    }
}
`
  }

  public getExplanation(): string {
    return 'The user implements Solution.Add. The class name, method name, and argument types must stay compatible with the runner.'
  }

  public getRunner(): string {
    return `using System;

public class Main {
    public static void Main(string[] args) {
        string[] values = Console.ReadLine()!.Split();
        int a = int.Parse(values[0]);
        int b = int.Parse(values[1]);

        Solution solution = new Solution();
        Console.WriteLine(solution.Add(a, b));
    }
}
`
  }

  public getRunnerExplanation(): string {
    return 'The runner reads two integers, calls Solution.Add, and prints the result. The server combines it with the user template before compiling.'
  }
}

/** Abstract factory that constructs the `LanguageTempleteSetup` product per language. */
export interface LanguageSetupsFactory {
  constructPythonTemplate(): LanguageTempleteSetup
  constructJavaTemplate(): LanguageTempleteSetup
  constructCsharpTemplate(): LanguageTempleteSetup
  constructCPlusPlusTemplate(): LanguageTempleteSetup
}

export class LanguageSetupsFactoryImpl implements LanguageSetupsFactory {
  public constructPythonTemplate(): LanguageTempleteSetup {
    return new Python()
  }

  public constructJavaTemplate(): LanguageTempleteSetup {
    return new Java()
  }

  public constructCsharpTemplate(): LanguageTempleteSetup {
    return new CSharp()
  }

  public constructCPlusPlusTemplate(): LanguageTempleteSetup {
    return new CPlusPlus()
  }
}


const languageSetupsFactory: LanguageSetupsFactory = new LanguageSetupsFactoryImpl()

const languageTemplateBuilders: Record<Language, () => LanguageTempleteSetup> = {
  python: () => languageSetupsFactory.constructPythonTemplate(),
  java: () => languageSetupsFactory.constructJavaTemplate(),
  cplusplus: () => languageSetupsFactory.constructCPlusPlusTemplate(),
  csharp: () => languageSetupsFactory.constructCsharpTemplate(),
}

export function getSupportedLanguages(): Language[] {
  return Object.keys(languageTemplateBuilders) as Language[]
}

export function createLanguageSetup(language: Language): LanguageSetup {
  const setup = languageTemplateBuilders[language]()

  return {
    userTemplate: setup.getTemplate(),
    userTemplateExplanation: setup.getExplanation(),
    runner: setup.getRunner(),
    runnerExplanation: setup.getRunnerExplanation(),
  }
}
