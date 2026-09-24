FROM gcc:14

WORKDIR /app

COPY submission.cpp /app/main.cpp
COPY TestSuite /app/TestSuite
COPY TestCases /app/TestCases

RUN g++ -std=c++20 -O2 -o main main.cpp

CMD ["bash", "/app/TestSuite/runCommandTest.bs", "/app/TestCases", "/app/main"]
