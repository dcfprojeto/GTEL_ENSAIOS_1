@echo off
setlocal
set APP_HOME=%~dp0
set GRADLE_VERSION=8.10.2
set GRADLE_BIN=%APP_HOME%.gradle-local\gradle-%GRADLE_VERSION%\bin\gradle.bat
if not exist "%GRADLE_BIN%" (
  echo Este gradlew.bat simplificado requer Gradle instalado ou execução via GitHub Actions/Linux.
  echo Para Windows local, instale o Gradle ou abra o projeto no Android Studio.
  exit /b 1
)
"%GRADLE_BIN%" %*
