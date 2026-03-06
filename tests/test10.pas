program Test10;
var
  x: Integer;
  y: Integer;

procedure Inner;
var
  x: Integer;
begin
  x := 999;
  WriteLn('Inner x = ', x);
  WriteLn('Inner sees global y = ', y)
end;

procedure Outer;
var
  x: Integer;
begin
  x := 50;
  WriteLn('Outer x = ', x);
  Inner;
  WriteLn('Outer x after Inner = ', x)
end;

begin
  x := 10;
  y := 20;
  WriteLn('Global x = ', x);
  WriteLn('Global y = ', y);
  WriteLn;

  Outer;
  WriteLn;

  WriteLn('Global x after calls = ', x);

  WriteLn;
  WriteLn('=== Loop Scoping ===');
  x := 100;
  for x := 1 to 3 do
    WriteLn('Loop x = ', x);
  WriteLn('x after loop = ', x)
end.
