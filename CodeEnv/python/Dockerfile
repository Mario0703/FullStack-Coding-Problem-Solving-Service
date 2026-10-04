FROM python:3.12-slim

WORKDIR /app


COPY submission.py /app/submission.py
COPY TestSuite /app/TestSuite
COPY TestCases /app/TestCases

ENV COMPILE_CMD="python3 -m py_compile /app/submission.py"

CMD ["bash", "/app/TestSuite/runCommandTest.bs", "/app/TestCases", "python3", "/app/submission.py"]
