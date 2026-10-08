---
name: Code Smells
description: Code smells are a set of points to avoid when implementing a new feature or doing a refactor.
---

**WATCH OUT FOR BLOATERS**
Bloaters are classes that are too large to maintain; they have grown to such lengths that they are no longer maintainable.
A class is a bloater if it has any of these points:
1. Long methods
2. Large class
3. Primitive obsession
4. Long parameter list
5. Data clumps

# Long methods
A method contains too many lines of code. In general, any method longer than ten lines should raise questions.
If a method contains more lines than this, split the main method into helper methods. Each created method should adhere to the clean code principles; see [Clean Code](../clean-code/SKILL.MD).

## Solution
Use the Extract Method technique to solve this problem; see [Clean Code](../clean-code/SKILL.MD).

# Large class
A class is large when it contains many fields, methods, or lines of code. Classes usually start small, but over time they get bloated as the program grows.
The problem is that when one class does the work of two, awkwardness results.

## Solution
Use the Extract Class technique. Create a new class and place the fields and methods responsible for the relevant functionality in it. See [Clean Code](../clean-code/SKILL.MD).

# Primitive obsession
Primitive obsession comes from creating too many fields for storing data.
Creating a primitive field is much easier than making a whole new class, so this is often what happens.

## Solution
When there is a large variety of primitive fields, it may be possible to logically group some of them into their own class.

# Long parameter list
More than three or four parameters for a method. See [Clean Code](../clean-code/SKILL.MD).

# Data clumps
A data clump is any group of variables that is passed around together (in a clump) throughout various parts of the program.
