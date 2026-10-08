---
name: Design Pattern Skill
description: This skill focuses on understanding and applying design patterns in software development. It covers various design patterns, their use cases, and best practices for implementing them effectively.
---
Design patterns are typical solutions to common problems in software design. Each pattern is like a blueprint.
There are three different types of design patterns to consider when implementing a new feature.
1. Creational Patterns: These patterns provide various object creation mechanisms, which increase flexibility and reuse of existing code.


2. Structural Patterns: These patterns explain how to assemble objects and classes into larger structures while keeping these structures flexible and efficient.


3. Behavioral Patterns: These patterns are concerned with algorithms and the assignment of responsibilities between objects.

The most commonly used pattern that will be used when implementing a new feature is the Strategy pattern.
Always consider this pattern when implementing a new feature. Use this pattern when it is necessary to define a series of algorithms.
If you take a class that does something specific in a lot of different ways, extract all of these algorithms into separate classes called strategies.

```
interface Strategy is
    method execute(a, b)
    
class ConcreteStrategyAdd implements Strategy is
    method execute(a, b) is
        return a + b

class ConcreteStrategySubtract implements Strategy is
    method execute(a, b) is
        return a - b

class ConcreteStrategyMultiply implements Strategy is
    method execute(a, b) is
        return a * b
```

DO NOT ADD CODE TO THE INTERFACE WHEN IMPLEMENTING A NEW FEATURE.
The interface should say what a class should do, not how it should do it.
The interface should be a contract that defines the behavior of a class, and it should not contain any implementation details.

BAD
```
interface Strategy{
    method execute(a, b)
    method executeWithLogging(a, b) is
        log("Executing strategy with logging")
        return execute(a, b)
}
}
```
GOOD
```
interface Strategy{
    method execute(a, b)    
    }
```

