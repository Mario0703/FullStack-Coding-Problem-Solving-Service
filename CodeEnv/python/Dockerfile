FROM python:3.12-alpine

WORKDIR /app

COPY submission.py /app/submission.py
# The runner compiles the submission at run time (see COMPILE_CMD) and the
# container runs as the unprivileged uid 1000, so /app must be owned by it.
# Test assets live under /tests instead, where they stay root-owned and
# therefore read-only to the submission.
RUN chown -R 1000:1000 /app

COPY TestSuite /tests/TestSuite
COPY TestCases /tests/TestCases

ENV COMPILE_CMD="python3 -m py_compile /app/submission.py"

CMD ["sh", "/tests/TestSuite/runCommandTest.bs", "/tests/TestCases", "python3", "/app/submission.py"]
