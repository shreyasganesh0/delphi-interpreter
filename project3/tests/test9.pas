program Test9;
var
  n: Integer;

function Factorial(n: Integer): Integer;
begin
  if n <= 1 then
    Result := 1
  else
    Result := n * Factorial(n - 1)
end;

function Fibonacci(n: Integer): Integer;
begin
  if n <= 0 then
    Result := 0
  else if n = 1 then
    Result := 1
  else
    Result := Fibonacci(n - 1) + Fibonacci(n - 2)
end;

function Max(a: Integer; b: Integer): Integer;
begin
  if a > b then
    Result := a
  else
    Result := b
end;

procedure PrintLine(msg: String);
begin
  WriteLn('>> ', msg)
end;

begin
  WriteLn('=== Factorial ===');
  for n := 0 to 7 do
    WriteLn('Factorial(', n, ') = ', Factorial(n));

  WriteLn;
  WriteLn('=== Fibonacci ===');
  for n := 0 to 10 do
    Write(Fibonacci(n), ' ');
  WriteLn;

  WriteLn;
  WriteLn('=== Max ===');
  WriteLn('Max(3, 7) = ', Max(3, 7));
  WriteLn('Max(10, 2) = ', Max(10, 2));

  WriteLn;
  PrintLine('Hello from a procedure!')
end.
