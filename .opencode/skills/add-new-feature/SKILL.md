---
name: Add New Feature
description: Process for adding a new feature to an existing application — requirement analysis, design, implementation, testing, and deployment — preferring composition and the Strategy pattern over copy-paste or long switch/if-else chains.
---

Before diving into the implementation of a new feature, it is important to understand how we scale it.
Consider any kind of design pattern before implementing a new feature.
The most commonly used pattern when implementing a new feature is the Strategy pattern.
Always consider this pattern when implementing a new feature. Use this pattern when it is necessary to define a series of algorithms.
If you take a class that does something specific in a lot of different ways, extract all of these algorithms into separate classes called strategies.

See the `design-pattern` skill for more information about the Strategy pattern.

**DO NOT CONSIDER A CODE COPY PASTE APPROACH WHEN IMPLEMENTING A NEW FEATURE.**
The risk here is introducing more slop code, and there is a high risk of introducing bugs and errors.
Changes in common code must be propagated to all copies, and this can be a nightmare to maintain.
Usually manual process, and many times the copies can get mixed up, and it can be hard to track down which copy is the correct one.

Example:
 Fixing the same bug in 5 nearly identical SAVOS production code
 bases at the same time

**DO NOT CONSIDER A PARAMETERIZED APPROACH WHEN IMPLEMENTING A NEW FEATURE.**
This includes long switch statements, and if-else statements. The risk here is introducing more slop code, and there is a high risk of introducing bugs and errors.
The parameterized approach is not scalable, and it is not maintainable, since the growth of the switch statement can be exponential.

The risk of using the parameterized approch is Reliability concerns,Readability concerns, Responsibility erosion and Composition problem
Each time we must add a new, we must extend the switch statement, and this can be a nightmare to maintain.

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

The benefit that we get when we are doing the approch os using the Strategy patteren
is that we get to deifne objext with a single responsibility, and we can add new features without changing the existing code.
This is called deligation and is allways something that we should consider when implementing a new feature. In delegeation two objecs collaborate to satisfy a request or fulfill a responsability
The bejhavor of teh reciveing objeci is partially hanlded by a subordinate object called a delegate.
