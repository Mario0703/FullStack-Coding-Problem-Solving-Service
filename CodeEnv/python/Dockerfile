FROM python:3.12-slim

WORKDIR /app


COPY submission.py /app/submission.py
COPY TestSuite /app/TestSuite
COPY TestCases /app/TestCases


CMD ["bash", "-c", "cp -R /app/TestCases /tmp/TestCases && cd /tmp && bash /app/TestSuite/runTestSub.bs /app/submission.py"]
