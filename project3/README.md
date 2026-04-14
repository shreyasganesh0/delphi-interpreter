# Delphi/Pascal → LLVM IR Compiler

This project converts a procedural subset of Delphi/Pascal into LLVM IR (`.ll`)
text files. The same ANTLR4 grammar from Project 2 is reused; the
evaluation visitor is replaced by `LLVMCodeGenerator`, which walks the parse
tree and emits IR. The resulting IR can then be turned into a native binary
(via `clang` or `lli`) or a WebAssembly module (via `emcc` / `llc`) and run in
a browser.

## Implemented language subset (~70%)

| Feature | Status |
|---|---|
| `program ... begin ... end.` skeleton | ✓ |
| Global + local variables (`Integer`, `Real`, `Boolean`, `String`) | ✓ |
| Arithmetic (`+`, `-`, `*`, `/`, `div`, `mod`) | ✓ |
| Relational (`=`, `<>`, `<`, `<=`, `>`, `>=`) | ✓ |
| Boolean (`and`, `or`, `xor`, `not`) | ✓ |
| `if … then … else` | ✓ |
| `while … do`, `for … to/downto … do`, `repeat … until` | ✓ |
| `break`, `continue` | ✓ |
| User-defined `procedure` / `function` (with recursion) | ✓ |
| `var` (pass-by-reference) parameters | ✓ |
| Static scoping (functions see only globals + locals) | ✓ |
| Constants + constant propagation (BONUS, prints `[FOLDED]` traces) | ✓ |
| `Write`, `WriteLn`, `Read`, `ReadLn` | ✓ |
| Classes, interfaces, inheritance, records, arrays, sets, files, `goto` | ✗ |

## Project layout

```
ganeshshreyasProject3/
├── README.md
├── grammar/
│   └── delphi.g4               ANTLR4 grammar (verbatim from Project 2)
├── src/
│   ├── Main.java               entry point: parse → fold → codegen → write .ll
│   ├── LLVMCodeGenerator.java  the new visitor that emits LLVM IR text
│   └── ConstantFolder.java     reused; drives the constant-propagation bonus
├── lib/
│   └── antlr-4.13.2-complete.jar
├── tests/
│   ├── test7..test13.pas       Pascal source (covers the procedural subset)
│   ├── test*.ll                generated LLVM IR (one per test, checked in)
│   └── expected_test*.txt      golden output for diff-based regression checks
├── web/                        BROWSER (extra-credit walk-through)
│   ├── index.html              HTML loader
│   └── loader.js               instantiates output.wasm and calls main
├── build.sh                    runs ANTLR + javac
├── compile.sh                  .pas → .ll
├── run_native.sh               .pas → .ll → native binary → run
└── run_tests.sh                runs the full test suite, diffs against expected
```

## Prerequisites

- Java JDK 11 or later (tested with 17 / 21)
- `clang` (Apple's clang on macOS works fine; LLVM 14+ on Linux)
- For the WASM extra credit: either Emscripten (`emcc`) **or** raw LLVM tools
  (`llc`, `wasm-ld`) — see the WASM section below.
- The ANTLR jar is bundled in `lib/`.

## Build the compiler

```bash
chmod +x build.sh compile.sh run_native.sh run_tests.sh
./build.sh
```

This runs ANTLR on `grammar/delphi.g4` (output goes to `gen/`) and compiles
all `.java` sources into `bin/`.

## Generate LLVM IR for a single program

```bash
./compile.sh tests/test7.pas
```

Writes `tests/test7.ll`. Any `[FOLDED]` lines from constant propagation are
printed to stdout while compiling.

## Compile and run (native, via clang)

```bash
./run_native.sh tests/test7.pas
```

This is equivalent to:

```bash
./compile.sh tests/test7.pas
clang tests/test7.ll -o tests/test7.out
./tests/test7.out
```

Or use the LLVM interpreter directly (if `lli` is on your PATH; on macOS with
Homebrew LLVM installed, that's `/opt/homebrew/opt/llvm/bin/lli`):

```bash
lli tests/test7.ll
```

## Run the full test suite

```bash
./run_tests.sh
```

For each `tests/test*.pas`, the script:

1. Runs the compiler to produce `.ll`.
2. Builds a native binary with `clang`.
3. Runs the binary, captures `stdout`, prepends any `[FOLDED]` lines from
   step 1, and `diff`s the combined output against `tests/expected_testN.txt`
   (golden output captured from the Project 2 interpreter).

A passing run prints `SUMMARY: 7 passed, 0 failed`.

## Test programs

| File | Features exercised |
|---|---|
| `test7.pas`  | `while` + `break` + `continue`, `mod`, `WriteLn` |
| `test8.pas`  | `for to`/`downto`, nested loops, `repeat … until` + `break` |
| `test9.pas`  | recursive `Factorial` / `Fibonacci`, `Max(a,b)`, `procedure PrintLine(msg: String)` |
| `test10.pas` | static scoping — `Inner`/`Outer` with shadowed locals + global access; for-loop variable retains final value |
| `test11.pas` | `const` declarations used in expressions and as for-loop bounds |
| `test12.pas` | `var` parameters: `Swap(var x,y: Integer)`, `DoubleIt(var n: Integer)` |
| `test13.pas` | constant propagation: `2*(A+B)`, `A*B+C`, etc. — `[FOLDED]` traces emitted |

## Compile to WebAssembly (extra credit)

There are two supported toolchains. **Emscripten** is recommended because it
provides a working `printf`/`scanf` automatically; the raw LLVM path requires
writing JavaScript stubs for any libc calls the program makes.

### Path A — Emscripten (recommended)

```bash
brew install emscripten   # or follow https://emscripten.org/docs/getting_started/

# Compile Pascal → LLVM IR → wasm + html + js (one shot)
./compile.sh tests/test7.pas
emcc tests/test7.ll -o web/output.html

# Serve and open
python3 -m http.server --directory web 8000
open http://localhost:8000/output.html
```

`emcc` produces `web/output.html`, `web/output.js`, and `web/output.wasm`.
Open `output.html` to run the program with full `printf` support.

### Path B — raw LLVM (`llc` + `wasm-ld`)

```bash
brew install llvm                              # ships llc and wasm-ld
export PATH="/opt/homebrew/opt/llvm/bin:$PATH"

./compile.sh tests/test7.pas
llc -march=wasm32 -filetype=obj tests/test7.ll -o tests/test7.o
wasm-ld --no-entry --export=main \
        --allow-undefined \
        tests/test7.o -o web/output.wasm

python3 -m http.server --directory web 8000
open http://localhost:8000/index.html
```

`web/index.html` and `web/loader.js` (provided in this project) fetch
`output.wasm`, instantiate it, and invoke the exported `main`. Because the
raw LLVM path leaves `printf` undefined, `loader.js` ships a tiny shim that
captures format-string output (it does **not** parse `%d`/`%g` arguments —
only the format string itself is logged). For full I/O fidelity, use Path A.

## Architecture

```
.pas
 │
 │  ANTLR4 (delphi.g4)
 ▼
parse tree  ──►  ConstantFolder  ──►  foldedValues map + [FOLDED] log
 │                                          │
 │                                          ▼
 └──────────►  LLVMCodeGenerator  ──►  .ll file
                                            │
                                            ▼
                                clang / lli / llc → wasm
```

- `ConstantFolder` is a `delphiBaseVisitor<Object>` that pre-evaluates pure
  constant expressions and stores their values in a
  `Map<ParserRuleContext, Object>`.
- `LLVMCodeGenerator` is also a `delphiBaseVisitor`. At every expression
  visit it consults the folded-values map first; on a hit it inlines the
  constant as an IR literal (this is the bonus's "constant propagation made
  visible in the generated IR").

### Code generation strategy

- **Variables**: every variable is addressable. Locals get `alloca` slots in
  the function's `entry` block; globals are emitted as `@g_<name>`.
- **VAR parameters**: the formal becomes a pointer (`i32*`), so the callee
  reads/writes the caller's storage directly. No copying.
- **Functions**: pre-declared in pass B of `visitProgram` so calls work
  regardless of declaration order. The result variable is an alloca named
  `%result.addr`, loaded just before `ret`. Both `Result := ...` and
  `Funcname := ...` resolve to the same slot.
- **Static scoping**: every function's local `Scope` has the global scope
  as its only parent. Functions see globals + their own locals — never the
  caller's locals.
- **Loops**: each loop pushes a `LoopCtx{continue, break}` so `break` /
  `continue` know where to jump. Every `br` / `ret` opens a fresh basic
  block before the next instruction so LLVM's "exactly one terminator per
  block" rule is never violated.
- **Pascal `for`**: after the loop body, a post-block compares the loop
  variable to the final value. If equal, jump to `end` (the variable
  retains its final value, matching Pascal semantics — see test10). Only
  if not equal does the variable get incremented.
- **I/O**: `WriteLn` builds a single `printf` format string from the typed
  arguments (`%d` for `Integer`, `%g` for `Real`, `%s` for `String`,
  booleans rendered via a `select i1` between `"TRUE"`/`"FALSE"`). All
  format strings are interned in a string-pool to deduplicate.
- **Constant propagation bonus**: handled by `ConstantFolder`. Each fold
  prints a line like `A+B => 21 [FOLDED]` and the `LLVMCodeGenerator`
  later emits the literal `21` instead of an `add`. The `.ll` for
  `test13.pas` shows folded literals in the `printf` arguments.

## Limitations

- Strings are limited to literals (no concatenation, no string variables
  beyond by-value parameters that hold a literal pointer).
- No classes, interfaces, inheritance, records, arrays, sets, files, or
  `goto`. Programs using these features will throw a `RuntimeException` at
  compile time.
- Real arithmetic uses `double`; `WriteLn` formats reals with `%g`.
- The WASM Path B loader (`web/loader.js`) ships a stub `printf` for
  demonstration only. Use Emscripten for real output.
