program Test12;
var
  a: Integer;
  b: Integer;

procedure Swap(var x: Integer; var y: Integer);
var
  temp: Integer;
begin
  temp := x;
  x := y;
  y := temp
end;

procedure DoubleIt(var n: Integer);
begin
  n := n * 2
end;

begin
  a := 10;
  b := 20;
  WriteLn('Before swap: a=', a, ' b=', b);
  Swap(a, b);
  WriteLn('After swap:  a=', a, ' b=', b);

  WriteLn;
  a := 7;
  WriteLn('Before double: a=', a);
  DoubleIt(a);
  WriteLn('After double:  a=', a)
end.
