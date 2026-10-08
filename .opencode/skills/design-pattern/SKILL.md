---
name: Design Patterns
description: Understand and apply software design patterns (creational, structural, behavioral), with emphasis on the Strategy and State patterns, when designing or implementing a new feature.
---

Design patterns are typical solutions to common problems in software design. Each pattern is like a blueprint.
There are three different types of design patterns to consider when implementing a new feature.
1. Creational Patterns: These patterns provide various object creation mechanisms, which increase flexibility and reuse of existing code.
2. Structural Patterns: These patterns explain how to assemble objects and classes into larger structures while keeping these structures flexible and efficient.
3. Behavioral Patterns: These patterns are concerned with algorithms and the assignment of responsibilities between objects.

The most commonly used pattern when implementing a new feature is the Strategy pattern.
Always consider this pattern when implementing a new feature. Use this pattern when it is necessary to define a series of algorithms.
When a class does something specific in many different ways, extract each of those algorithms into separate classes called strategies. Example of a pattern in Java:

```java
public interface PayStrategy {
    boolean pay(int paymentAmount);
    void collectPaymentDetails();
}

public class PayByPayPal implements PayStrategy {
    ...
}
```

An example of a pattern in TypeScript:

```typescript
interface Strategy {
    doAlgorithm(data: string[]): string[];
}

class ConcreteStrategyA implements Strategy {
    doAlgorithm(data: string[]): string[] {
        ...
    }
}
```

The intent of the Strategy pattern is to define a family of business rules or algorithms, encapsulate each one, and make them interchangeable.
Strategy lets the algorithms vary independently from the clients that use them.
Use the pattern when the product must support variable algorithms or business rules and requires a flexible and reliable way to control that variability.
Strategy specifies the responsibility and interface of the algorithm. ConcreteStrategies define concrete behavior that fulfills the responsibility.
Context performs its work for the client by delegating to an instance of type Strategy.

The benefits are: strategies eliminate conditional statements, are an alternative to subclassing, facilitate separate testing of Context and ConcreteStrategy, and can be changed at run-time.

For a complete pattern catalog, see the [Pattern Catalog](../PatterenCatalog/SKILL.MD) skill.
When implementing a new feature, start by considering a design pattern, and identify it using the following steps:

1. Start by abstracting and programming to an interface, not an implementation.
2. Identify the varying behavior in the code and separate it from the fixed behavior.
3. Identify the different strategies that can be used to implement the varying behavior.
4. Implement the strategies as separate classes that implement the same interface.

**DO NOT PROVIDE CONCRETE IMPLEMENTATIONS IN THE INTERFACE**
The interface should only define the contract for the behavior, not provide any concrete implementations.
The rule of the interface is that it should tell a class how to behave, but not how to implement that behavior.

**DO NOT USE ABSTRACT CLASSES TO IMPLEMENT STRATEGIES**
Abstract classes should not be used to implement strategies; these use polymorphic solutions.
And there is a limit to how many classes can extend an abstract class, but a class can implement multiple interfaces.

**DO NOT USE STRATEGY PATTERN FOR SIMPLE BEHAVIOR**
Remember design patterns are patterns that are used to solve a concrete problem, and not all problems require a design pattern.
If the behavior is simple and does not vary, it may be better to implement it directly in the class rather than using a strategy pattern.

**DO NOT OVERCOMMENT THE STRATEGY PATTERN**
Do not overcomment the interface or the classes with the strategy pattern; the code should be self-documenting and clean according to the clean code principles.
