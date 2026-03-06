program Test11;
const
  PI_APPROX = 3;
  MAX_SIZE = 100;
  GREETING = 'Hello, Constants!';
  OFFSET = 10;
var
  area: Integer;
  radius: Integer;
begin
  WriteLn(GREETING);
  WriteLn('MAX_SIZE = ', MAX_SIZE);
  WriteLn('PI_APPROX = ', PI_APPROX);

  radius := 5;
  area := PI_APPROX * radius * radius;
  WriteLn('Approximate area of circle with r=5: ', area);

  WriteLn('MAX_SIZE + OFFSET = ', MAX_SIZE + OFFSET);

  WriteLn;
  WriteLn('=== Constants in loops ===');
  for radius := 1 to PI_APPROX do
    WriteLn('r=', radius, ' area~=', PI_APPROX * radius * radius)
end.
