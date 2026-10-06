FROM mcr.microsoft.com/dotnet/sdk:8.0-alpine

WORKDIR /app

RUN dotnet new console --framework net8.0 --force

COPY submission.cs /app/Program.cs
RUN chown -R 1000:1000 /app

COPY TestSuite /tests/TestSuite
COPY TestCases /tests/TestCases

# dotnet needs a writable HOME for its NuGet/obj state. /app is writable by
# uid 1000; the test assets under /tests are not.
ENV HOME=/app \
    DOTNET_CLI_TELEMETRY_OPTOUT=1 \
    DOTNET_NOLOGO=1
ENV COMPILE_CMD="dotnet build --configuration Release"

CMD ["sh", "/tests/TestSuite/runCommandTest.bs", "/tests/TestCases", "dotnet", "/app/bin/Release/net8.0/app.dll"]
