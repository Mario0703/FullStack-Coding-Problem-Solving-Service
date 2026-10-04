FROM gcc:14

WORKDIR /app

COPY submission.cpp /app/main.cpp
COPY TestSuite /app/TestSuite
COPY TestCases /app/TestCases

ENV COMPILE_CMD="g++ -std=c++20 -O2 -o /app/main /app/main.cpp"

CMD ["bash", "/app/TestSuite/runCommandTest.bs", "/app/TestCases", "/app/main"]
