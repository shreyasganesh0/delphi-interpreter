#!/bin/bash
set -e
ANTLR_JAR="lib/antlr-4.13.2-complete.jar"
BIN_DIR="bin"
if [ -z "$1" ]; then
  echo "Usage: $0 <file.pas>"
  exit 1
fi
java -cp "$BIN_DIR:$ANTLR_JAR" Main "$1"
