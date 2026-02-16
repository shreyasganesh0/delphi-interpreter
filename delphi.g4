grammar delphi;

// LEXER

PROGRAM: 'program';
BEGIN: 'begin';
END: 'end';
TRY: 'try';
USES: 'uses';
RAISES: 'raises';
UNIT: 'unit';
CONST: 'const';
VAR: 'var';
TYPE: 'type';
STRING: 'string';
RESOURCESTRING: 'resourcestring';
ARRAY: 'array';
CLASS: 'class';
INHERITED: 'inherited';
INTERFACE: 'interface';
DISPINTERFACE: 'dispinterface';
IMPLEMENTATION: 'implementation';
INITIALIZATION: 'initialization';
FINALIZATION: 'finalization';
IS: 'is';
IN: 'in';
AS: 'as';
WHILE: 'while';
FOR: 'for';
OF: 'of';
TO: 'to';
IF: 'if';
THEN: 'then';
ELSE: 'else';
MOD: 'mod';
XOR: 'xor';
DIV: 'div';
NOT: 'not';
OR: 'or';
AND: 'and';
WITH: 'with';
GOTO: 'goto';
UNTIL: 'until';
DOWNTO: 'downto';
CONSTRUCTOR: 'constructor';
DESTRUCTOR: 'destructor';
INLINE: 'inline';
PROPERTY: 'property';
ASM: 'asm';
FILE: 'file';
LIBRARY: 'library';
SET: 'set';
EXPORTS: 'exports';
SHL: 'shl';
SHR: 'shr';
NIL: 'nil';
PACKED: 'packed';
THREADVAR: 'threadvar';
FINALLY: 'finally';

PLUS: '+';
MINUS: '-';
SLASH: '/';
CARAT: '^';
EQUAL: '=';
INEQ: '<>';
GREATER: '>';
GREATEREQ: '>=';
LESSER: '<';
LESSEREQ: '<=';
POUND: '#';
DOLLAR: '$';
AMPERSAND: '&';
AT: '@';
SEMI: ';';
ASSIGN: ':=';
DOT: '.';
COMMA: ',';
CURLO: '{';
CURLC: '}';
CIRCO: '(';
CIRCC: ')';
CIRCDOTO: '(.';
CIRCDOTC: '.)';
CIRCSTARO: '(*';
CIRCSTARC: '*)';
SQUAREO: '[';
SQUAREC: ']';
STAR: '*';
QOUTE: '\'';

ID: [a-zA-Z_][a-zA-Z0-9_]*;
INT: (PLUS|MINUS)?[0-9]+;

// PARSER 

programRule: PROGRAM ID SEMI block DOT EOF;

block: BEGIN statement* END;

statement: assignment SEMI;

assignment: ID ASSIGN expr;

expr: expr (STAR | SLASH) expr
    | expr (PLUS | MINUS) expr
    | INT
    | ID
    ; 
