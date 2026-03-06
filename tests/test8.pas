program Test8;
var
  i: Integer;
  j: Integer;
  found: Integer;
begin
  WriteLn('=== For with Break ===');
  found := 0;
  for i := 1 to 20 do
  begin
    if i * i > 50 then
    begin
      found := i;
      break
    end
  end;
  WriteLn('First i where i*i > 50: ', found);

  WriteLn('=== For with Continue ===');
  for i := 1 to 10 do
  begin
    if i mod 3 = 0 then
      continue;
    Write(i, ' ')
  end;
  WriteLn;

  WriteLn('=== Nested Loops with Break ===');
  for i := 1 to 5 do
  begin
    for j := 1 to 5 do
    begin
      if j > i then
        break;
      Write(j, ' ')
    end;
    WriteLn
  end;

  WriteLn('=== Repeat with Break ===');
  i := 0;
  repeat
    i := i + 1;
    if i = 3 then
      break
  until i = 10;
  WriteLn('Stopped at i = ', i)
end.
