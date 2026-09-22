FROM gcc:14

WORKDIR /app

COPY main.cpp .

RUN g++ -std=c++20 -O2 -o main main.cpp

CMD ["./main"]
