FROM gcc:14-bookworm

WORKDIR /app

COPY submission.cpp /app/main.cpp
RUN chown -R 1000:1000 /app

COPY TestSuite /tests/TestSuite
COPY TestCases /tests/TestCases

ENV COMPILE_CMD="g++ -std=c++20 -O2 -o /app/main /app/main.cpp"

CMD ["sh", "/tests/TestSuite/runCommandTest.bs", "/tests/TestCases", "/app/main"]
