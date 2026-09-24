FROM python:3.12-slim

WORKDIR /app

COPY main.py .
COPY runTestSub.bs .
COPY *.in .
COPY *.out .


CMD ["bash", "./runTestSub.bs", "./main.py"]