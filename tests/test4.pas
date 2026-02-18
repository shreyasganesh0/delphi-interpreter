{ test4.pas - Interfaces: IShape, IPrintable }
program Test4;

type
  { ── Interface declarations ── }
  IShape = interface
    function  Area: Integer;
    function  Perimeter: Integer;
    procedure PrintInfo;
  end;

  IPrintable = interface
    procedure PrintToConsole;
  end;

  { ── TRectangle implements IShape + IPrintable ── }
  TRectangle = class(TObject)
  private
    FWidth:  Integer;
    FHeight: Integer;
  public
    constructor Create(Width: Integer; Height: Integer);
    function    Area: Integer;
    function    Perimeter: Integer;
    procedure   PrintInfo;
    procedure   PrintToConsole;
  end;

  { ── TCircle implements IShape ── }
  TCircle = class(TObject)
  private
    FRadius: Integer;
  public
    constructor Create(Radius: Integer);
    function    Area: Integer;
    function    Perimeter: Integer;
    procedure   PrintInfo;
  end;

{ ── TRectangle implementations ── }
constructor TRectangle.Create(Width: Integer; Height: Integer);
begin
  FWidth  := Width;
  FHeight := Height
end;

function TRectangle.Area: Integer;
begin
  Result := FWidth * FHeight
end;

function TRectangle.Perimeter: Integer;
begin
  Result := 2 * (FWidth + FHeight)
end;

procedure TRectangle.PrintInfo;
begin
  WriteLn('Rectangle ', FWidth, 'x', FHeight,
          ' | Area=', Area, ' | Perimeter=', Perimeter)
end;

procedure TRectangle.PrintToConsole;
begin
  WriteLn('[Console] ', FWidth, ' x ', FHeight)
end;

{ ── TCircle implementations ── }
constructor TCircle.Create(Radius: Integer);
begin
  FRadius := Radius
end;

function TCircle.Area: Integer;
begin
  { approximate: 3 * r^2 }
  Result := 3 * FRadius * FRadius
end;

function TCircle.Perimeter: Integer;
begin
  { approximate: 6 * r }
  Result := 6 * FRadius
end;

procedure TCircle.PrintInfo;
begin
  WriteLn('Circle r=', FRadius,
          ' | Area~=', Area, ' | Perimeter~=', Perimeter)
end;

{ ── Main Program ── }
var
  rect: TRectangle;
  circ: TCircle;

begin
  WriteLn('=== Interface Demo ===');
  WriteLn;

  rect := TRectangle.Create(4, 7);
  circ := TCircle.Create(5);

  WriteLn('--- Shapes ---');
  rect.PrintInfo;
  circ.PrintInfo;

  WriteLn;
  WriteLn('--- Printable ---');
  rect.PrintToConsole;

  WriteLn;
  WriteLn('--- Individual values ---');
  WriteLn('Rectangle area:      ', rect.Area);
  WriteLn('Rectangle perimeter: ', rect.Perimeter);
  WriteLn('Circle area:         ', circ.Area);
  WriteLn('Circle perimeter:    ', circ.Perimeter)
end.
