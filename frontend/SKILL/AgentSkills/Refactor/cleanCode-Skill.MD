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
