program Test7;
var
  i: Integer;
  sum: Integer;
begin
  WriteLn('=== While with Break ===');
  i := 1;
  sum := 0;
  while i <= 100 do
  begin
    if i > 5 then
      break;
    sum := sum + i;
    i := i + 1
  end;
  WriteLn('Sum 1..5 = ', sum);

  WriteLn('=== While with Continue ===');
  i := 0;
  sum := 0;
  while i < 10 do
  begin
    i := i + 1;
    if i mod 2 = 0 then
      continue;
    sum := sum + i
  end;
  WriteLn('Sum of odd 1..9 = ', sum)
end.
