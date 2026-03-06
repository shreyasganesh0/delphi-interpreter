program Test13;
const
  A = 10;
  B = 11;
  C = 5;
var
  x: Integer;
begin
  WriteLn('=== Constant Propagation Demo ===');
  x := 2 * (A + B);
  WriteLn('2 * (A + B) = ', x);

  x := A * B + C;
  WriteLn('A * B + C = ', x);

  x := (A + B) * (A - B);
  WriteLn('(A + B) * (A - B) = ', x);

  if A + B > 15 then
    WriteLn('A + B > 15 is True')
  else
    WriteLn('A + B > 15 is False')
end.
