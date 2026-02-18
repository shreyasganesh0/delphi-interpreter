{ test1.pas - Classes, Constructors, Encapsulation, I/O }
program Test1;

type
  TCounter = class
  private
    FValue: Integer;
  public
    constructor Create(InitVal: Integer);
    destructor  Destroy;
    procedure   Increment;
    procedure   Decrement;
    function    GetValue: Integer;
    procedure   SetValue(NewVal: Integer);
    procedure   PrintValue;
  end;

{ -- Constructor -- }
constructor TCounter.Create(InitVal: Integer);
begin
  FValue := InitVal
end;

{ -- Destructor -- }
destructor TCounter.Destroy;
begin
  WriteLn('Counter destroyed. Final value: ', FValue)
end;

{ -- Methods -- }
procedure TCounter.Increment;
begin
  FValue := FValue + 1
end;

procedure TCounter.Decrement;
begin
  FValue := FValue - 1
end;

function TCounter.GetValue: Integer;
begin
  Result := FValue
end;

procedure TCounter.SetValue(NewVal: Integer);
begin
  FValue := NewVal
end;

procedure TCounter.PrintValue;
begin
  WriteLn('Counter value: ', FValue)
end;

{ ── Main Program ── }
var
  c: TCounter;
  v: Integer;

begin
  c := TCounter.Create(10);

  c.PrintValue;           { Counter value: 10 }

  c.Increment;
  c.Increment;
  c.Increment;
  c.PrintValue;           { Counter value: 13 }

  c.Decrement;
  c.PrintValue;           { Counter value: 12 }

  c.SetValue(100);
  v := c.GetValue;
  WriteLn('Value via getter: ', v);  { 100 }

  c.Destroy
end.
