{ test3.pas - Inheritance: Animal -> Dog / Cat hierarchy }
program Test3;

type
  { ── Base class ── }
  TAnimal = class
  private
    FName: String;
    FAge:  Integer;
  public
    constructor Create(Name: String; Age: Integer);
    function    GetName: String;
    function    GetAge:  Integer;
    procedure   Speak; virtual;
    procedure   Describe;
  end;

  { ── Derived class 1 ── }
  TDog = class(TAnimal)
  private
    FBreed: String;
  public
    constructor Create(Name: String; Age: Integer; Breed: String);
    procedure   Speak; override;
    procedure   Fetch;
    function    GetBreed: String;
  end;

  { ── Derived class 2 ── }
  TCat = class(TAnimal)
  private
    FIndoor: Boolean;
  public
    constructor Create(Name: String; Age: Integer; Indoor: Boolean);
    procedure   Speak; override;
    procedure   Purr;
  end;

{ ── TAnimal implementations ── }
constructor TAnimal.Create(Name: String; Age: Integer);
begin
  FName := Name;
  FAge  := Age
end;

function TAnimal.GetName: String;
begin
  Result := FName
end;

function TAnimal.GetAge: Integer;
begin
  Result := FAge
end;

procedure TAnimal.Speak;
begin
  WriteLn(FName, ' makes a generic animal sound.')
end;

procedure TAnimal.Describe;
begin
  WriteLn('Name: ', FName, ', Age: ', FAge)
end;

{ ── TDog implementations ── }
constructor TDog.Create(Name: String; Age: Integer; Breed: String);
begin
  inherited Create(Name, Age);
  FBreed := Breed
end;

procedure TDog.Speak;
begin
  WriteLn(GetName, ' says: Woof! Woof!')
end;

procedure TDog.Fetch;
begin
  WriteLn(GetName, ' fetches the ball!')
end;

function TDog.GetBreed: String;
begin
  Result := FBreed
end;

{ ── TCat implementations ── }
constructor TCat.Create(Name: String; Age: Integer; Indoor: Boolean);
begin
  inherited Create(Name, Age);
  FIndoor := Indoor
end;

procedure TCat.Speak;
begin
  WriteLn(GetName, ' says: Meow!')
end;

procedure TCat.Purr;
begin
  WriteLn(GetName, ' purrs contentedly...')
end;

{ ── Main Program ── }
var
  dog: TDog;
  cat: TCat;

begin
  dog := TDog.Create('Rex', 3, 'Labrador');
  cat := TCat.Create('Whiskers', 5, True);

  WriteLn('=== Dog ===');
  dog.Describe;
  dog.Speak;
  dog.Fetch;
  WriteLn('Breed: ', dog.GetBreed);

  WriteLn;
  WriteLn('=== Cat ===');
  cat.Describe;
  cat.Speak;
  cat.Purr;

  WriteLn;
  WriteLn('=== Polymorphic Speak ===');
  dog.Speak;
  cat.Speak
end.
