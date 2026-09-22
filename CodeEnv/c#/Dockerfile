FROM mcr.microsoft.com/dotnet/sdk:8.0

WORKDIR /app

RUN dotnet new console --framework net8.0 --force

COPY Main.cs Program.cs

RUN dotnet build --configuration Release

CMD ["dotnet", "/app/bin/Release/net8.0/app.dll"]
