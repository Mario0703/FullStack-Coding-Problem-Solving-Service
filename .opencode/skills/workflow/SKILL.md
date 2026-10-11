---
name: Feature Workflow
description: Minimal workflow for implementing a feature — write a test first, then code the feature until the test passes.
---
The workflow follows the Test-Driven Development (TDD) approach, which emphasizes writing tests before implementing the feature. This ensures that the code meets the requirements and behaves as expected.

1. Add a test.
2. Code the feature 
3. Add another test
4. Run the tests and repeat steps 2-4 until all tests pass.

# Adding a test
When adding a test it is should be descriptoive, even when the mehtod can be long and it should use the given -when-then format.
The test should be written in a way that clearly describes the expected behavior of the feature being implemented.
```java
@Test
public void givenAuserWhenTheUserSignsInWithAnInValidEmailFormatTheyGetAnError() {
    // given a user
    User user = new User("invalid-email-format");
    // when the user signs in with an invalid email format
    boolean result = user.signIn();
    // then they get an error
    assertFalse(result);
}
```
now run the test and see it fail. This is an important step in TDD, as it ensures that the test is valid and that the feature has not been implemented yet.
Add an idea of what the code might look like to make the test pass. When done run the test and see if it fails. If it does, then implement the code to make the test pass. 
If it passes, then add another test and repeat the process until all tests pass. **The purpose of the second test is to ensure we abstract correct**

**DO NOT ADD CODE BEFORE ADDING A TEST**
This defeats the purpose of doing this approch in the first place. 
The goal is to write tests that define the expected behavior of the feature before implementing it.
This ensures that the code meets the requirements and behaves as expected.

**Consider adding neagtive test where it makes sense**
Adding a negative test means to expect failure or an error condition. 
This helps to ensure that the feature behaves correctly in all scenarios, including edge cases and unexpected inputs
. It also helps to identify potential bugs and improve the overall quality of the code.

**DO NOT ADD HELPER METHODS OR CLASSES INSIDE THE TEST FOLDER**
Use the before each fiscture to define common objects that may be needed during testing
This helps keep the test structure consistent and maintainable, and ensures that the tests are focused on the behavior of the feature being implemented.