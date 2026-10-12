---
name: Add New Feature
description: Process for adding a new feature to an existing application — requirement analysis, design, implementation, testing, and deployment — preferring composition and the Strategy pattern over copy-paste or long switch/if-else chains.
---

Before implementing a new feature, it is important to understand how it scales.
Consider any kind of design pattern before implementing a new feature.
The most commonly used pattern when implementing a new feature is the Strategy pattern.
Always consider this pattern when implementing a new feature. Use this pattern when it is necessary to define a series of algorithms.
When a class does something specific in many different ways, extract each of those algorithms into separate classes called strategies.

See the `design-pattern` skill for more information about the Strategy pattern.

**DO NOT CONSIDER A CODE COPY PASTE APPROACH WHEN IMPLEMENTING A NEW FEATURE.**
The risk here is introducing more slop code, and there is a high risk of introducing bugs and errors.
Changes in common code must be propagated to all copies, and this can be a nightmare to maintain.
This is usually a manual process, and the copies can easily get mixed up, making it hard to track down which copy is correct.

Example: fixing the same bug in 5 nearly identical SAVOS production code bases at the same time.

**DO NOT CONSIDER A PARAMETERIZED APPROACH WHEN IMPLEMENTING A NEW FEATURE.**
This includes long switch statements, and if-else statements. The risk here is introducing more slop code, and there is a high risk of introducing bugs and errors.
The parameterized approach is not scalable, and it is not maintainable, since the growth of the switch statement can be exponential.

The risk of using the parameterized approach is reliability concerns, readability concerns, responsibility erosion, and composition problems.
Each time a new operation is added, the switch statement must be extended, and this can be a nightmare to maintain.

BAD
```java
switch (operation) {
    case "add":
        return a + b;
    case "subtract":
        return a - b;
    case "multiply":
        return a * b;
    default:
        throw new IllegalArgumentException("Invalid operation: " + operation);
}
```

GOOD
```java
interface Strategy {
    int execute(int a, int b);
}

public class ConcreteStrategyAdd implements Strategy {
    @Override
    public int execute(int a, int b) {
        return a + b;
    }
}

public class ConcreteStrategySubtract implements Strategy {
    @Override
    public int execute(int a, int b) {
        return a - b;
    }
}

public class ConcreteStrategyMultiply implements Strategy {
    @Override
    public int execute(int a, int b) {
        return a * b;
    }
}
```
The examples are in Java, but they can be applied to any programming language.
The key is to avoid long switch statements and if-else statements.

The benefit of using the Strategy pattern is that it defines objects with a single responsibility and allows new features to be added without changing existing code.
This is called delegation and should always be considered when implementing a new feature. In delegation, two objects collaborate to satisfy a request or fulfill a responsibility.
The behavior of the receiving object is partially handled by a subordinate object called a delegate.
