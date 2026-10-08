---
name: Adding New Feature Skill
description: This skill focuses on the process of adding new features to an existing software application. It covers the steps involved in feature development, including requirement analysis, design, implementation, testing, and deployment.
---

Before diving into the implementation of a new feature, it is important to understand how we scale it.
Consider any kind of design pattern before implementing a new feature.
The most commonly used pattern that will be used when implementing a new feature is the Strategy pattern.
Always consider this pattern when implementing a new feature. Use this pattern when it is necessary to define a series of algorithms.
If you take a class that does something specific in a lot of different ways, extract all of these algorithms into separate classes called strategies.

See `designPatteren-skill.md` for more information about the Strategy pattern.

DO NOT CONSIDER A CODE COPY PASTE APPROACH WHEN IMPLEMENTING A NEW FEATURE.
The risk here is introducing more slop code, and there is a high risk of introducing bugs and errors.

DO NOT CONSIDER A PARAMETERIZED APPROACH WHEN IMPLEMENTING A NEW FEATURE.
This includes long switch statements, and if-else statements. The risk here is introducing more slop code, and there is a high risk of introducing bugs and errors.
The parameterized approach is not scalable, and it is not maintainable, since the growth of the switch statement can be exponential.

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
