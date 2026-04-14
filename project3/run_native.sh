#!/bin/bash
# Compile a single .pas file all the way to a native binary and run it.
set -e
if [ -z "$1" ]; then
  echo "Usage: $0 <file.pas>"
  exit 1
fi
ANTLR_JAR="lib/antlr-4.13.2-complete.jar"
src="$1"
ll="${src%.pas}.ll"
exe="${src%.pas}.out"

java -cp "bin:$ANTLR_JAR" Main "$src" > /dev/null
clang "$ll" -o "$exe" 2>/dev/null
"$exe"
