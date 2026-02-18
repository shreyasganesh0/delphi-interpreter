#!/bin/bash
set -e

ANTLR_JAR="antlr-4.13.2-complete.jar"
GEN_DIR="gen"
SRC_DIR="src"
BIN_DIR="bin"

if [ ! -f "$ANTLR_JAR" ]; then
  echo "ERROR: $ANTLR_JAR not found in $(pwd)"
  echo "Download it from: https://www.antlr.org/download/antlr-4.13.2-complete.jar"
  exit 1
fi

mkdir -p "$GEN_DIR"
java -jar "$ANTLR_JAR" grammar/delphi.g4 \
     -visitor \
     -package gen \
     -o "$GEN_DIR"

mkdir -p "$BIN_DIR"

SOURCES=$(find "$GEN_DIR" "$SRC_DIR" -name "*.java" 2>/dev/null | tr '\n' ' ')

javac -cp ".:$ANTLR_JAR" \
      -d "$BIN_DIR" \
      $SOURCES

echo ""
echo "Run tests with:  ./run_tests.sh"
echo "Run a file with: java -cp bin:$ANTLR_JAR interpreter.DelphiInterpreter <file.pas>"
