FROM mcr.microsoft.com/dotnet/sdk:8.0

WORKDIR /app

RUN dotnet new console --framework net8.0 --force

COPY submission.cs /app/Program.cs
COPY TestSuite /app/TestSuite
COPY TestCases /app/TestCases

RUN dotnet build --configuration Release

CMD ["bash", "/app/TestSuite/runCommandTest.bs", "/app/TestCases", "dotnet", "/app/bin/Release/net8.0/app.dll"]
