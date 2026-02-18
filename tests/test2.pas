{ test2.pas - Encapsulation: private fields with public accessors }
program Test2;

type
  TBankAccount = class
  private
    FOwner:   String;
    FBalance: Integer;
  protected
    procedure InternalDeposit(Amount: Integer);
  public
    constructor Create(Owner: String; InitBalance: Integer);
    procedure   Deposit(Amount: Integer);
    procedure   Withdraw(Amount: Integer);
    function    GetBalance: Integer;
    function    GetOwner: String;
    procedure   PrintStatement;
  end;

constructor TBankAccount.Create(Owner: String; InitBalance: Integer);
begin
  FOwner   := Owner;
  FBalance := InitBalance
end;

procedure TBankAccount.InternalDeposit(Amount: Integer);
begin
  FBalance := FBalance + Amount
end;

procedure TBankAccount.Deposit(Amount: Integer);
begin
  if Amount > 0 then
    InternalDeposit(Amount)
  else
    WriteLn('Deposit amount must be positive.')
end;

procedure TBankAccount.Withdraw(Amount: Integer);
begin
  if Amount > FBalance then
    WriteLn('Insufficient funds.')
  else
    FBalance := FBalance - Amount
end;

function TBankAccount.GetBalance: Integer;
begin
  Result := FBalance
end;

function TBankAccount.GetOwner: String;
begin
  Result := FOwner
end;

procedure TBankAccount.PrintStatement;
begin
  WriteLn('Account owner:   ', FOwner);
  WriteLn('Current balance: ', FBalance)
end;

{ ── Main Program ── }
var
  acc: TBankAccount;
  bal: Integer;

begin
  acc := TBankAccount.Create('Alice', 500);
  acc.PrintStatement;

  WriteLn('--- Depositing 200 ---');
  acc.Deposit(200);
  acc.PrintStatement;

  WriteLn('--- Withdrawing 100 ---');
  acc.Withdraw(100);
  acc.PrintStatement;

  WriteLn('--- Withdrawing 1000 (overdraft) ---');
  acc.Withdraw(1000);

  bal := acc.GetBalance;
  WriteLn('Final balance: ', bal);

  WriteLn('Owner: ', acc.GetOwner)
end.
