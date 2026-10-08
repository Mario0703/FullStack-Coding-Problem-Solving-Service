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

The intent of the Strategy pattern is to define a family of business rules or algorithms, encapsulate each one, and make them interchangeable.
Strategy lets the algorithms vary independently from the clients that use them.

Use the pattern when the product must support variable algorithms or business rules and you want a flexible and reliable way of controlling that variability.

Strategy specifies the responsibility and interface of the algorithm. ConcreteStrategies define concrete behavior that fulfills the responsibility. Context performs its work for the client by delegating to an instance of type Strategy.

The benefits are: strategies eliminate conditional statements, are an alternative to subclassing, facilitate separate testing of Context and ConcreteStrategy, and can be changed at run-time.

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
The next good patteren to consider is the State patteren.
The state is a behavioral design pattern that lets an object alter its behavior when its internal state changes. It appears as if the object changed its class.
The state patteren is used to create an interface STATE that encapsulates the behavior associated with a particular state of the context.
State specifies
responsibilities of the
behavior that varies
according to state
