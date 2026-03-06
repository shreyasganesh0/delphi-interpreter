# Delphi Interpreter — ANTLR4 + Java

A Delphi/Turbo Pascal interpreter built with ANTLR4 visitors and Java. The interpreter uses ANTLR's built-in parse tree generator with the visitor pattern to evaluate programs over the parse tree rather than using an evaluate-as-you-parse strategy.

## What Is Implemented

### Core Language (Project 1)
- Classes & Objects
- Constructors & Destructors
- Encapsulation (private / protected / public / published)
- I/O via `WriteLn` / `Write` / `ReadLn` / `Read`
- Arithmetic & logic expressions
- If / Case conditional statements
- Inheritance (`class(Parent)`, `inherited`, `override`)
- Interfaces (`interface … end`)

### Project 2 Features
- **While-do and for-do loops** with full break/continue support
- **Repeat-until loops** with break/continue
- **Break and continue keywords** implemented as exception-based control flow
- **User-defined procedures and functions** with recursion support
- **Static scoping** — each block (while, for) creates a new scope; functions see only globals + locals

### Bonuses
- **Constant propagation** — pure-constant expressions are folded at compile time before execution. The folded values are printed with `[FOLDED]` tags to demonstrate the optimization (e.g., `A+B => 21 [FOLDED]`).
- **Formal parameter passing** — procedures and functions accept formal parameters with correct scoping. VAR parameters enable pass-by-reference semantics (e.g., a `Swap` procedure that modifies caller variables).

## Project Layout

```
delphi-interpreter/
│
├── antlr-4.13.2-complete.jar
│
├── grammar/
│   └── delphi.g4            ← ANTLR4 grammar with lexer + parser rules
│
├── src/interpreter/
│   ├── DelphiInterpreter.java  ← entry point (parses, folds constants, evaluates)
│   ├── DelphiVisitor.java      ← main visitor (evaluates the parse tree)
│   ├── Environment.java        ← scoped variable/constant storage
│   ├── ClassDefinition.java    ← class metadata (fields, methods, visibility)
│   ├── ObjectInstance.java     ← runtime object with field storage
│   ├── ReturnException.java    ← control flow for function returns
│   ├── BreakException.java     ← control flow for break
│   ├── ContinueException.java  ← control flow for continue
│   ├── VarReference.java       ← pass-by-reference wrapper for VAR params
│   └── ConstantFolder.java     ← compile-time constant propagation pass
│
├── tests/
│   ├── test1.pas   ← Classes, constructors, destructors, I/O
│   ├── test2.pas   ← Encapsulation (private fields, public API)
│   ├── test3.pas   ← Inheritance (Animal → Dog / Cat)
│   ├── test4.pas   ← Interfaces (IShape, IPrintable)
│   ├── test5.pas   ← Integer terminal I/O inside a class
│   ├── test5.input ← pre-canned stdin for test5
│   ├── test6.pas   ← Combined: inheritance + interfaces + I/O
│   ├── test6.input ← pre-canned stdin for test6
│   ├── test7.pas   ← While-do with break and continue
│   ├── test8.pas   ← For-do with break/continue, nested loops, repeat-until
│   ├── test9.pas   ← Standalone procedures/functions (Factorial, Fibonacci)
│   ├── test10.pas  ← Static scoping (local vs global variable isolation)
│   ├── test11.pas  ← Constant definitions and usage in expressions
│   ├── test12.pas  ← VAR parameter passing (Swap, DoubleIt)       [BONUS]
│   └── test13.pas  ← Constant propagation demonstration            [BONUS]
│
├── build.sh         ← generate ANTLR sources + compile everything
├── run_tests.sh     ← run all test files
└── simple_build.sh  ← run grammar via GUI AST viewer
```

## Prerequisites
- Java JDK 11 or higher
- ANTLR jar 4.13.2

Download the ANTLR jar:
```
curl -O https://www.antlr.org/download/antlr-4.13.2-complete.jar
```
Place it in the project root (same directory as `build.sh`).

## How to Build

```bash
chmod +x build.sh run_tests.sh
./build.sh
```

This will:
1. Run ANTLR4 on `grammar/delphi.g4` to generate lexer, parser, and visitor classes into `gen/`
2. Compile all Java sources (`gen/*.java` + `src/interpreter/*.java`) into `bin/`

## How to Run a Single File

```bash
java -cp "bin:antlr-4.13.2-complete.jar" interpreter.DelphiInterpreter tests/test1.pas
```

With stdin for interactive tests:
```bash
java -cp "bin:antlr-4.13.2-complete.jar" interpreter.DelphiInterpreter tests/test5.pas < tests/test5.input
```

## How to Run All Tests

```bash
./run_tests.sh
```

Each test prints its output and shows **PASS** or **FAIL**. Tests that use `ReadLn` are fed from a `.input` file automatically.

## Test Case Descriptions

### test1.pas — Core OOP
`TCounter` with a private integer field, constructor, destructor, and public methods (`Increment`, `Decrement`, `GetValue`, `SetValue`, `PrintValue`).

### test2.pas — Encapsulation
`TBankAccount` with private fields, protected internal methods, and a public API. Tests overdraft protection and field hiding.

### test3.pas — Inheritance
`TAnimal` base class with `TDog` and `TCat` subclasses. Tests `inherited Create(…)`, `virtual`/`override` method dispatch.

### test4.pas — Interfaces
`IShape` and `IPrintable` interfaces implemented by `TRectangle` and `TCircle`.

### test5.pas — I/O Inside a Class
`TCalculator` with `Add`, `Subtract`, `Multiply` and an interactive mode using `ReadLn`.

### test6.pas — Combined Demo
`TVehicle` → `TElectricCar` + `TTruck` with inheritance chains, overridden methods, interface implementation, interactive I/O, and destructor cleanup.

### test7.pas — While-Do with Break and Continue
Tests `break` exiting a while loop early and `continue` skipping even iterations.

### test8.pas — For-Do with Break/Continue and Nested Loops
Tests `break` in for loops, `continue` skipping multiples of 3, nested loops where inner break doesn't affect outer, and repeat-until with break.

### test9.pas — Standalone Procedures and Functions
Recursive `Factorial`, recursive `Fibonacci`, multi-parameter `Max` function, and a standalone `PrintLine` procedure.

### test10.pas — Static Scoping
Demonstrates that functions create their own scopes with only globals visible. Local variables in `Inner` and `Outer` don't interfere with each other or with globals. Loop variables are scoped correctly.

### test11.pas — Constant Definitions
Defines constants (`PI_APPROX`, `MAX_SIZE`, `GREETING`, `OFFSET`) and uses them in expressions and loops. Constants cannot be reassigned.

### test12.pas — VAR Parameter Passing (Bonus)
`Swap(var x, y: Integer)` exchanges two caller variables by reference. `DoubleIt(var n: Integer)` modifies the caller's variable in place.

### test13.pas — Constant Propagation (Bonus)
Defines constants `A=10`, `B=11`, `C=5` and evaluates expressions like `2*(A+B)`, `A*B+C`, `(A+B)*(A-B)`. The constant folder pre-computes these at compile time and prints folded results (e.g., `A+B => 21 [FOLDED]`) before execution begins.

## Implementation Details

### Architecture
The interpreter runs in two passes:
1. **Constant Folding Pass** — `ConstantFolder` (a `delphiBaseVisitor`) walks the parse tree, collects constant definitions, and evaluates pure-constant expressions. Results are stored in a `Map<ParserRuleContext, Object>`.
2. **Evaluation Pass** — `DelphiVisitor` (the main visitor) evaluates the program. Expression visitors check the folded values map first, returning cached values for pre-computed expressions.

### Scoping
- **Global scope** — all top-level variables and procedure/function declarations
- **Function/procedure scope** — `new Environment(globalEnv)` so only globals + locals are visible
- **Loop scope** — while and for loops push `new Environment(currentEnv)` for block-level scoping
- **Constants** — tracked in `Environment.constants` set; assignment to a constant throws a runtime error

### Break/Continue
Implemented as lightweight exceptions (`BreakException`, `ContinueException`) with suppressed stack traces. Loop visitors catch them appropriately. `callMethod` catches and rethrows as `RuntimeException` to prevent leaking across function boundaries.

### VAR Parameters
`VarReference` holds an `Environment` reference and variable name. When a VAR parameter is bound, a `VarReference` is stored instead of the value. `resolveVariableBase` and `assignToVariable` transparently dereference these references, enabling true pass-by-reference semantics.
