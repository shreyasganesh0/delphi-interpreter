#!/bin/bash

ANTLR_JAR="antlr-4.13.2-complete.jar"
BIN_DIR="bin"
TEST_DIR="tests"
PASS=0
FAIL=0

if [ ! -d "$BIN_DIR" ]; then
  echo "ERROR: bin/ not found. Run ./build.sh first."
  exit 1
fi

run_test() {
  local file="$1"
  local name=$(basename "$file")
  local input_file="${file%.pas}.input"

  echo "──────────────────────────────────────────────────────"
  echo "TEST: $name"
  echo "──────────────────────────────────────────────────────"

  if [ -f "$input_file" ]; then
    output=$(java -cp "$BIN_DIR:$ANTLR_JAR" interpreter.DelphiInterpreter "$file" \
                  < "$input_file" 2>&1)
  else
    output=$(echo "42" | java -cp "$BIN_DIR:$ANTLR_JAR" interpreter.DelphiInterpreter "$file" 2>&1)
  fi

  echo "$output"

  if echo "$output" | grep -qi "\[parse error\]\|\[runtime error\]\|exception"; then
    echo ">>> RESULT: FAIL "
    FAIL=$((FAIL + 1))
  else
    echo ">>> RESULT: PASS"
    PASS=$((PASS + 1))
  fi
  echo
}

for f in "$TEST_DIR"/test*.pas; do
  run_test "$f"
done

echo "======================================================"
echo " SUMMARY:  $PASS passed,  $FAIL failed"
echo "======================================================"
