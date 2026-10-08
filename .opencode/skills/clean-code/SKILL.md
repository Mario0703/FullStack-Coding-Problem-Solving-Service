---
name: Clean Code
description: Apply Clean Code principles when writing, reviewing, or refactoring code — small functions that do one thing, descriptive names, few arguments, no flag arguments or hidden side effects, no duplication, no magic values, shallow nesting, and named boolean expressions.
---

The code should be self-documented, and should be clean according to the clean code principles.

1. Small
2. Do one thing
3. One level of abstraction per function
4. Use descriptive names
5. Keep the number of arguments to a minimum: 0, 1, or 2; maybe even 3 is okay. DO NOT use more than 3 arguments in a function. This will blow up the function signature and make it hard to read and understand. If you need more arguments, consider using helper functions.

DO NOT HAVE FLAG ARGUMENTS IN FUNCTION PARAMETERS
Boolean arguments say "do more than one thing!"

BAD
```
 produceWebPage(true, true, false)
```

GOOD
```
 produceWebPage()
 produceWebPageWithHeader()
 produceWebPageWithHeaderAndFooter()
```

**DO NOT LET FUNCTIONS GROW TOO BIG**
Functions should be small and focused on a single task. If a function is growing too large,
consider breaking it down into smaller helper functions. This will improve readability and maintainability.

**DO NOT LET FUNCTIONS HAVE HIDDEN SIDE EFFECTS**
Functions should not have hidden side effects that can affect the state of the program in unexpected ways.

**DO NOT DUPLICATE CODE**
Avoid duplicating code by creating reusable functions or classes. This will make your code more maintainable.
If you find yourself copying and pasting code, consider refactoring it into a separate function or class.

**DO NOT USE MAGIC NUMBERS OR STRINGS**
Avoid using magic numbers or strings in your code. Instead, use constants or enums to give them meaningful names.
This will make your code more readable and easier to maintain.

**DO NOT USE DEEP NESTING**
Don't use nested if statements or loops that are more than 2 levels deep. This can make your code hard to read and understand.

BAD
```
if (condition1) {
    if (condition2) {
        // do something
    }
}
```
Instead, you should try to bail out fast and use a return statement to avoid deep nesting.
```
if (!condition1) {
    return;
}
if (!condition2) {
    return;
}
// do something
```
DO NOT INTRODUCE ARGUMENT NAMES IN FUNCTION SIGNATURES
This is a bad practice and should be avoided. This is the sign of a function that is doing too much and should be broken down into smaller functions.
This also indicates the function is hardcoded to one parameter. One important exception is test cases, where it is okay to have arguments in the function signature.


DO NOT CREATE LONG BOOLEAN EXPRESSIONS
Avoid creating long boolean expressions that are hard to read and understand.
This includes long ANDs and ORs, and nested boolean expressions. Instead, break them down into smaller functions or use descriptive variable names to make them more readable.

BAD
```
if (isUserLoggedIn && isUserAdmin && hasPermission) {
    // do something
}
```
GOOD
```
Boolean isUserAuthorized = isUserLoggedIn && isUserAdmin && hasPermission;
if (isUserAuthorized) {
    // do something
}
```

DO NOT WRITE SLOP CODE THAT CONTAINS NO SPACES
This simply cannot be read and understood.

BAD
```java
public class X{private int y;public X(){y=0;}public int z(){return y;}public void z1(int z0){y+=z0;}public static void main(String[]args){X y=new X();y.z1(200);y.z1(3400);System.out.println("Result is "+y.z());}}
```

**ALWAYS, WHEN INSPECTING A PIECE OF CODE, CHECK WHETHER THESE HOLD**

1. Small
2. Do One Thing
3. One Level of Abstraction
4. Use Descriptive Names
5. Keep Number of Arguments Low
6. Avoid Flag Arguments
7. Have No Side Effects
8. Command Query Separation
9. Prefer Exceptions to Error Codes
10. Don't Repeat Yourself
11. Do the Same Thing, the Same Way
12. Name Boolean Expressions
13. Bail Out Fast
14. Arguments in Argument Lists

If one fails, the code is not clean and should be refactored.
