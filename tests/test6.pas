{ test6.pas - Combined: Inheritance, Interfaces, I/O, Constructors/Destructors }
program Test6;

type
  { ── Interface ── }
  ILogger = interface
    procedure Log(Message: String);
  end;

  { ── Base class ── }
  TVehicle = class
  private
    FMake:  String;
    FModel: String;
    FYear:  Integer;
    FSpeed: Integer;
  public
    constructor Create(Make: String; Model: String; Year: Integer);
    destructor  Destroy;
    procedure   Accelerate(Amount: Integer); virtual;
    procedure   Brake(Amount: Integer);
    function    GetSpeed: Integer;
    function    GetMake: String;
    function    GetModel: String;
    function    GetYear: Integer;
    procedure   PrintInfo;
    procedure   Log(Message: String);
  end;

  { ── Derived: Electric car ── }
  TElectricCar = class(TVehicle)
  private
    FBattery: Integer;
  public
    constructor Create(Make: String; Model: String; Year: Integer; Battery: Integer);
    destructor  Destroy;
    procedure   Accelerate(Amount: Integer); override;
    procedure   Charge(Amount: Integer);
    function    GetBattery: Integer;
    procedure   PrintInfo;
  end;

  { ── Derived: Truck ── }
  TTruck = class(TVehicle)
  private
    FPayload: Integer;
  public
    constructor Create(Make: String; Model: String; Year: Integer; Payload: Integer);
    procedure   Accelerate(Amount: Integer); override;
    function    GetPayload: Integer;
    procedure   PrintInfo;
  end;

{ ────────────────── TVehicle ────────────────── }
constructor TVehicle.Create(Make: String; Model: String; Year: Integer);
begin
  FMake  := Make;
  FModel := Model;
  FYear  := Year;
  FSpeed := 0
end;

destructor TVehicle.Destroy;
begin
  WriteLn('[LOG] ', FMake, ' ', FModel, ' destroyed.')
end;

procedure TVehicle.Accelerate(Amount: Integer);
begin
  FSpeed := FSpeed + Amount;
  WriteLn(FMake, ' ', FModel, ' accelerates to ', FSpeed, ' km/h')
end;

procedure TVehicle.Brake(Amount: Integer);
begin
  FSpeed := FSpeed - Amount;
  if FSpeed < 0 then FSpeed := 0;
  WriteLn(FMake, ' ', FModel, ' brakes to ', FSpeed, ' km/h')
end;

function TVehicle.GetSpeed: Integer;
begin
  Result := FSpeed
end;

function TVehicle.GetMake: String;
begin
  Result := FMake
end;

function TVehicle.GetModel: String;
begin
  Result := FModel
end;

function TVehicle.GetYear: Integer;
begin
  Result := FYear
end;

procedure TVehicle.PrintInfo;
begin
  WriteLn(FYear, ' ', FMake, ' ', FModel, ' | Speed: ', FSpeed, ' km/h')
end;

procedure TVehicle.Log(Message: String);
begin
  WriteLn('[LOG] ', Message)
end;

{ ────────────────── TElectricCar ────────────────── }
constructor TElectricCar.Create(Make: String; Model: String; Year: Integer; Battery: Integer);
begin
  inherited Create(Make, Model, Year);
  FBattery := Battery
end;

destructor TElectricCar.Destroy;
begin
  WriteLn('[LOG] Electric car battery at ', FBattery, '% on shutdown.');
  inherited Destroy
end;

procedure TElectricCar.Accelerate(Amount: Integer);
begin
  FBattery := FBattery - Amount;
  if FBattery < 0 then FBattery := 0;
  inherited Accelerate(Amount)
end;

procedure TElectricCar.Charge(Amount: Integer);
begin
  FBattery := FBattery + Amount;
  if FBattery > 100 then FBattery := 100;
  WriteLn('Charged to ', FBattery, '%')
end;

function TElectricCar.GetBattery: Integer;
begin
  Result := FBattery
end;

procedure TElectricCar.PrintInfo;
begin
  inherited PrintInfo;
  WriteLn('  Battery: ', FBattery, '%')
end;

{ ────────────────── TTruck ────────────────── }
constructor TTruck.Create(Make: String; Model: String; Year: Integer; Payload: Integer);
begin
  inherited Create(Make, Model, Year);
  FPayload := Payload
end;

procedure TTruck.Accelerate(Amount: Integer);
var
  adjusted: Integer;
begin
  { Trucks accelerate slower under load }
  adjusted := Amount div 2;
  inherited Accelerate(adjusted)
end;

function TTruck.GetPayload: Integer;
begin
  Result := FPayload
end;

procedure TTruck.PrintInfo;
begin
  inherited PrintInfo;
  WriteLn('  Payload capacity: ', FPayload, ' kg')
end;

{ ── Main Program ── }
var
  ev:    TElectricCar;
  truck: TTruck;
  n:     Integer;

begin
  WriteLn('==============================');
  WriteLn(' Vehicle Fleet Management     ');
  WriteLn('==============================');
  WriteLn;

  ev    := TElectricCar.Create('Tesla', 'Model 3', 2023, 80);
  truck := TTruck.Create('Volvo', 'FH16', 2022, 20000);

  WriteLn('--- Initial State ---');
  ev.PrintInfo;
  truck.PrintInfo;
  WriteLn;

  WriteLn('--- Accelerating ---');
  ev.Accelerate(30);
  truck.Accelerate(30);   { truck gets only 15 }
  WriteLn;

  WriteLn('--- Electric Car Battery ---');
  WriteLn('Battery: ', ev.GetBattery, '%');
  ev.Charge(15);
  WriteLn('Battery after charge: ', ev.GetBattery, '%');
  WriteLn;

  WriteLn('--- Braking ---');
  ev.Brake(10);
  truck.Brake(5);
  WriteLn;

  WriteLn('--- Final State ---');
  ev.PrintInfo;
  truck.PrintInfo;
  WriteLn;

  WriteLn('--- Interactive: Enter extra speed for EV ---');
  WriteLn('How much to accelerate? ');
  ReadLn(n);
  ev.Accelerate(n);
  WriteLn('Speed after user input: ', ev.GetSpeed, ' km/h');
  WriteLn;

  WriteLn('--- Cleanup ---');
  ev.Destroy;
  truck.Destroy
end.
