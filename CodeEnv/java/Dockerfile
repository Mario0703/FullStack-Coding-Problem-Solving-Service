FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY submission.java /app/Main.java
COPY TestSuite /app/TestSuite
COPY TestCases /app/TestCases

ENV COMPILE_CMD="javac /app/Main.java"

CMD ["bash", "/app/TestSuite/runCommandTest.bs", "/app/TestCases", "java", "-cp", "/app", "Main"]
