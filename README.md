# Delphi Interpreter — ANTLR4 + Java

## What Is Implemented

- Classes & Objects 
- Constructors & Destructors
- Encapsulation (private / protected / public / published)
- Integer I/O via `WriteLn` / `ReadLn`
- Arithmetic & logic expressions
- If / While / Repeat / For / Case statements
- Standalone procedures & functions
- BONUS — Inheritance (`class(Parent)`, `inherited`, `override`)
- BONUS — Interfaces (`interface … end`)


## Project Layout

```
delphi-interpreter/
│
├── antlr-4.13.2-complete.jar
│
├── grammar/
│   └── delphi.g4           
│
├── tests/
│   ├── test1.pas   ← Classes, constructors, destructors, I/O
│   ├── test2.pas   ← Encapsulation (private fields, public API)
│   ├── test3.pas   ← Inheritance (Animal → Dog / Cat)   [BONUS]
│   ├── test4.pas   ← Interfaces (IShape, IPrintable)    [BONUS]
│   ├── test5.pas   ← Integer terminal I/O inside a class
│   ├── test5.input ← pre-canned stdin for test5
│   ├── test6.pas   ← Full combined: inheritance + interfaces + I/O
│   └── test6.input ← pre-canned stdin for test6
│
├── build.sh        ← generate + compile everything
└── run_tests.sh    ← run all test files
└── simple_build.sh ← run grammar via gui AST
```

---

## Prerequisites
- Java JDK: 11 or higher 
- ANTLR jar: 4.13.2

Download the ANTLR jar:
```
curl -O https://www.antlr.org/download/antlr-4.13.2-complete.jar
```
Place it in the **project root** (same directory as `build.sh`).

## How to Build

```bash
chmod +x build.sh run_tests.sh simple_build.sh
./build.sh
```

This will:
1. Run `java -jar antlr-4.13.2-complete.jar grammar/delphi.g4 -visitor -package gen -o gen`
2. Eventually Compile `gen/*.java` + `src/interpreter/*.java` → `bin/`


## How to Run Tests

```bash
./run_tests.sh
```

Each test prints its output and shows **PASS** or **FAIL**.
Tests that use `ReadLn` are fed from a `.input` file automatically.

---

## How to Run a Single File

```bash
java -cp "bin:antlr-4.13.2-complete.jar" interpreter.DelphiInterpreter tests/test1.pas
```

With stdin for interactive tests:
```bash
java -cp "bin:antlr-4.13.2-complete.jar" interpreter.DelphiInterpreter tests/test5.pas < tests/test5.input
```

---

## Test Case Descriptions

### test1.pas — Core OOP
Demonstrates `TCounter` with a private integer field, a constructor, destructor, `Increment`, `Decrement`, `GetValue`, `SetValue`, and `PrintValue`. Shows how encapsulation hides `FValue` and exposes it only through the public API.

**Expected output (trimmed):**
```
Counter value: 10
Counter value: 13
Counter value: 12
Value via getter: 100
Counter destroyed. Final value: 100
```

### test2.pas — Encapsulation
`TBankAccount` has `private` fields `FOwner` / `FBalance`, a `protected` internal deposit method, and a fully public API. Tests overdraft protection and field hiding.

**Expected output (trimmed):**
```
Account owner:   Alice
Current balance: 500
...
Insufficient funds.
...
Final balance: 600
```

### test3.pas — Inheritance (BONUS)
`TAnimal` base class → `TDog` and `TCat`. Demonstrates:
- `inherited Create(…)` in child constructors
- `virtual` / `override` method dispatch for `Speak`
- Inheriting `Describe` without re-implementing it

### test4.pas — Interfaces (BONUS)
Declares `IShape` and `IPrintable` interfaces.  
`TRectangle` and `TCircle` implement the shape API.  
The interpreter registers the interface methods and classes satisfy them structurally.

### test5.pas — Integer I/O Inside a Class
`TCalculator` accumulates results through `Add`, `Subtract`, `Multiply`. The `InteractiveMode` method reads two integers from the terminal using `ReadLn` and reports results with `WriteLn`.

### test6.pas — Combined (Full Demo)
`TVehicle` base → `TElectricCar` + `TTruck` with:
- `ILogger` interface
- Full inheritance chain with `inherited` calls in constructors and destructors
- Overridden `Accelerate` (truck is slower, EV drains battery)
- Interactive `ReadLn` to boost EV speed
- Destructor chain cleanup

