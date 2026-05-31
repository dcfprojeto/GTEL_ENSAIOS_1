#!/usr/bin/env bash
set -euo pipefail
APP_HOME="$(cd "$(dirname "$0")" && pwd -P)"
GRADLE_VERSION="8.10.2"
GRADLE_HOME="$APP_HOME/.gradle-local/gradle-$GRADLE_VERSION"
GRADLE_BIN="$GRADLE_HOME/bin/gradle"
if [ ! -x "$GRADLE_BIN" ]; then
  mkdir -p "$APP_HOME/.gradle-local"
  ZIP="$APP_HOME/.gradle-local/gradle-$GRADLE_VERSION-bin.zip"
  if [ ! -f "$ZIP" ]; then
    echo "Baixando Gradle $GRADLE_VERSION..."
    curl -L --retry 3 --output "$ZIP" "https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip"
  fi
  unzip -q -o "$ZIP" -d "$APP_HOME/.gradle-local"
fi
exec "$GRADLE_BIN" "$@"
