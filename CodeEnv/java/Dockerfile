FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY submission.java /app/Main.java
RUN chown -R 1000:1000 /app

COPY TestSuite /tests/TestSuite
COPY TestCases /tests/TestCases

ENV COMPILE_CMD="javac /app/Main.java"

CMD ["sh", "/tests/TestSuite/runCommandTest.bs", "/tests/TestCases", "java", "-cp", "/app", "Main"]
