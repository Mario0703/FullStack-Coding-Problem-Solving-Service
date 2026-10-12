---
name: Refactor
description: Refactor code to improve its structure, readability, and maintainability without changing its external behavior.
---

The main purpose of a refactor is to improve the existing code, both its coupling and its cohesion.
When doing a refactor, consider the following goals:
1. Code should become cleaner and more readable; see the [Clean Code](../clean-code/SKILL.MD) skill for more information.
2. New functionality should NOT be added during the refactor.
3. All existing tests must pass after the refactor.

**DO NOT MODIFY EXISTING CODE IN A WAY THAT INTRODUCES NEW FUNCTIONALITY**
The point of a refactor is to improve the existing code base, not to add new features or change the behavior of the code.
Any changes made during a refactor should focus on improving the structure, readability, and maintainability of the code without altering its external behavior.

**IF A REFACTOR LEAVES ANY TEST FAILING, AN ERROR WAS MADE**
Return to the original code and check what went wrong. Tests can fail after refactoring for two reasons:
1. An error was made during refactoring. In this case, fix the error.
2. The tests were too low-level. For example, they tested private methods of classes.
