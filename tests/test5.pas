{ test5.pas - Integer terminal I/O through class methods }
program Test5;

type
  TCalculator = class
  private
    FAccumulator: Integer;
    FHistory:     Integer;
  public
    constructor Create;
    procedure   Add(Value: Integer);
    procedure   Subtract(Value: Integer);
    procedure   Multiply(Value: Integer);
    procedure   Reset;
    function    GetResult: Integer;
    procedure   PrintResult;
    procedure   InteractiveMode;
  end;

constructor TCalculator.Create;
begin
  FAccumulator := 0;
  FHistory     := 0
end;

procedure TCalculator.Add(Value: Integer);
begin
  FHistory     := FAccumulator;
  FAccumulator := FAccumulator + Value
end;

procedure TCalculator.Subtract(Value: Integer);
begin
  FHistory     := FAccumulator;
  FAccumulator := FAccumulator - Value
end;

procedure TCalculator.Multiply(Value: Integer);
begin
  FHistory     := FAccumulator;
  FAccumulator := FAccumulator * Value
end;

procedure TCalculator.Reset;
begin
  FHistory     := FAccumulator;
  FAccumulator := 0
end;

function TCalculator.GetResult: Integer;
begin
  Result := FAccumulator
end;

procedure TCalculator.PrintResult;
begin
  WriteLn('Accumulator = ', FAccumulator)
end;

procedure TCalculator.InteractiveMode;
var
  n: Integer;
begin
  WriteLn('Enter a number to add: ');
  ReadLn(n);
  Add(n);
  WriteLn('After adding ', n, ':');
  PrintResult;

  WriteLn('Enter a number to multiply by: ');
  ReadLn(n);
  Multiply(n);
  WriteLn('After multiplying by ', n, ':');
  PrintResult
end;

{ ── Main Program ── }
var
  calc: TCalculator;
  r:    Integer;

begin
  calc := TCalculator.Create;

  WriteLn('=== Calculator Demo ===');

  calc.Add(10);
  calc.PrintResult;    { 10 }

  calc.Add(5);
  calc.PrintResult;    { 15 }

  calc.Subtract(3);
  calc.PrintResult;    { 12 }

  calc.Multiply(4);
  calc.PrintResult;    { 48 }

  r := calc.GetResult;
  WriteLn('Result via getter: ', r);

  calc.Reset;
  calc.PrintResult;    { 0 }

  WriteLn('=== Interactive Mode ===');
  calc.InteractiveMode
end.
