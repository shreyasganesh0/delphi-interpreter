#!/bin/bash
set -e

ANTLR_JAR="lib/antlr-4.13.2-complete.jar"
GEN_DIR="gen"
SRC_DIR="src"
BIN_DIR="bin"

if [ ! -f "$ANTLR_JAR" ]; then
  echo "ERROR: $ANTLR_JAR not found in $(pwd)"
  exit 1
fi

mkdir -p "$GEN_DIR"
java -jar "$ANTLR_JAR" grammar/delphi.g4 \
     -visitor \
     -package gen \
     -o "$GEN_DIR"

mkdir -p "$BIN_DIR"
SOURCES=$(find "$GEN_DIR" "$SRC_DIR" -name "*.java" | tr '\n' ' ')

javac -cp ".:$ANTLR_JAR" \
      -d "$BIN_DIR" \
      $SOURCES

echo ""
echo "Build successful."
echo "Compile a file:  ./compile.sh tests/test7.pas"
echo "Run all tests :  ./run_tests.sh"
