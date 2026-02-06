$ErrorActionPreference = "Stop"

Write-Host "==> Executando Gradle clean build..."
./gradlew clean --no-daemon

Write-Host "==> Subindo containers com Docker Compose..."
docker compose up --build -d

Write-Host "==> Containers em execução:"
docker ps
