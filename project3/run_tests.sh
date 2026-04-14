#!/bin/bash
# Compile each tests/testN.pas to LLVM IR, build a native binary with clang,
# run it, and diff against tests/expected_testN.txt.

ANTLR_JAR="lib/antlr-4.13.2-complete.jar"
BIN_DIR="bin"
TEST_DIR="tests"
PASS=0
FAIL=0

if [ ! -d "$BIN_DIR" ]; then
  echo "ERROR: bin/ not found. Run ./build.sh first."
  exit 1
fi

if ! command -v clang >/dev/null 2>&1; then
  echo "ERROR: clang not found on PATH."
  exit 1
fi

for src in "$TEST_DIR"/test*.pas; do
  name=$(basename "$src" .pas)
  ll="$TEST_DIR/${name}.ll"
  exe="$TEST_DIR/${name}.out"
  expected="$TEST_DIR/expected_${name}.txt"

  echo "──────────────────────────────────────────────────────"
  echo "TEST: ${name}"
  echo "──────────────────────────────────────────────────────"

  # Compile .pas -> .ll. Capture [FOLDED] traces (printed on stdout) so we can diff them.
  fold_out=$(java -cp "$BIN_DIR:$ANTLR_JAR" Main "$src" 2>/dev/null)

  # Compile .ll -> native binary.
  if ! clang "$ll" -o "$exe" 2>/dev/null; then
    echo "  clang failed"
    FAIL=$((FAIL+1)); continue
  fi

  # Combined output that mirrors the interpreter: [FOLDED] lines first, then runtime.
  if [ -n "$fold_out" ]; then
    actual=$(printf '%s\n%s' "$fold_out" "$("$exe")")
  else
    actual=$("$exe")
  fi

  echo "$actual"

  if [ -f "$expected" ]; then
    if diff -u <(printf '%s\n' "$actual") "$expected" >/dev/null; then
      echo ">>> RESULT: PASS"
      PASS=$((PASS+1))
    else
      echo ">>> RESULT: FAIL"
      diff -u <(printf '%s\n' "$actual") "$expected" | head -20
      FAIL=$((FAIL+1))
    fi
  else
    echo "  (no expected file; skipping diff)"
  fi
  echo
done

echo "======================================================"
echo " SUMMARY:  $PASS passed,  $FAIL failed"
echo "======================================================"
[ $FAIL -eq 0 ]
