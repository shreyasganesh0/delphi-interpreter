grammar delphi;

options {
    caseInsensitive = true;
}

// Parser

program
    : programHeading block DOT EOF
    ;

programHeading
    : PROGRAM identifier (LPAREN identifierList RPAREN)? SEMI
    | UNIT identifier SEMI
    ;

identifier
    : IDENT
    | RESULT
    ;

block
    : (
        labelDeclarationPart
        | constantDefinitionPart
        | typeDefinitionPart
        | variableDeclarationPart
        | procedureAndFunctionDeclarationPart
        | usesUnitsPart
        | IMPLEMENTATION
    )* compoundStatement
    ;

usesUnitsPart
    : USES identifierList SEMI
    ;

labelDeclarationPart
    : LABEL label (COMMA label)* SEMI
    ;

label
    : unsignedInteger
    ;

constantDefinitionPart
    : CONST (constantDefinition SEMI)+
    ;

constantDefinition
    : identifier EQUAL constant
    ;

constantChr
    : CHR LPAREN unsignedInteger RPAREN
    ;

constant
    : unsignedNumber
    | sign unsignedNumber
    | identifier
    | sign identifier
    | string
    | constantChr
    ;

unsignedNumber
    : unsignedInteger
    | unsignedReal
    ;

unsignedInteger
    : NUM_INT
    ;

unsignedReal
    : NUM_REAL
    ;

sign
    : PLUS
    | MINUS
    ;

bool_
    : TRUE
    | FALSE
    ;

string
    : STRING_LITERAL
    ;

typeDefinitionPart
    : TYPE (typeDefinition SEMI)+
    ;

typeDefinition
    : identifier EQUAL (type_ | functionType | procedureType)
    ;

functionType
    : FUNCTION (formalParameterList)? COLON resultType
    ;

procedureType
    : PROCEDURE (formalParameterList)?
    ;

type_
    : simpleType
    | structuredType
    | pointerType
    | classType
    | interfaceType
    ;

simpleType
    : scalarType
    | subrangeType
    | typeIdentifier
    | stringtype
    ;

scalarType
    : LPAREN identifierList RPAREN
    ;

subrangeType
    : constant DOTDOT constant
    ;

typeIdentifier
    : identifier
    | CHAR
    | BOOLEAN
    | INTEGER
    | REAL
    | STRING
    ;

structuredType
    : PACKED unpackedStructuredType
    | unpackedStructuredType
    ;

unpackedStructuredType
    : arrayType
    | recordType
    | setType
    | fileType
    ;

stringtype
    : STRING LBRACK (identifier | unsignedNumber) RBRACK
    ;

arrayType
    : ARRAY LBRACK typeList RBRACK OF componentType
    | ARRAY LBRACK2 typeList RBRACK2 OF componentType
    ;

typeList
    : indexType (COMMA indexType)*
    ;

indexType
    : simpleType
    ;

componentType
    : type_
    ;

recordType
    : RECORD fieldList? END
    ;

fieldList
    : fixedPart (SEMI variantPart)?
    | variantPart
    ;

fixedPart
    : recordSection (SEMI recordSection)*
    ;

recordSection
    : identifierList COLON type_
    ;

variantPart
    : CASE tag OF variant (SEMI variant)*
    ;

tag
    : identifier COLON typeIdentifier
    | typeIdentifier
    ;

variant
    : constList COLON LPAREN fieldList RPAREN
    ;

setType
    : SET OF baseType
    ;

baseType
    : simpleType
    ;

fileType
    : FILE OF type_
    | FILE
    ;

pointerType
    : POINTER typeIdentifier
    ;

classType
    : CLASS (LPAREN identifierList RPAREN)? classBody END
    ;

classBody
    : classMemberSection*
    ;

classMemberSection
    : visibility classMember*
    | classMember+
    ;

classMember
    : variableDeclaration SEMI
    | methodDecl SEMI
    ;

visibility
    : STRICT PRIVATE
    | STRICT PROTECTED
    | PRIVATE
    | PROTECTED
    | PUBLIC
    | PUBLISHED
    ;

methodDecl
    : methodKind identifier (formalParameterList)? (COLON resultType)?
      (SEMI methodQualifier)?
    ;

methodQualifier
    : VIRTUAL
    | OVERRIDE
    | ABSTRACT
    | DYNAMIC
    ;

methodKind
    : PROCEDURE
    | FUNCTION
    | CONSTRUCTOR
    | DESTRUCTOR
    ;


interfaceType
    : INTERFACE (LPAREN identifierList RPAREN)? interfaceGuid? interfaceBody END
    ;

interfaceGuid
    : LBRACK string RBRACK
    ;

interfaceBody
    : (methodDecl SEMI)*
    ;

variableDeclarationPart
    : VAR variableDeclaration (SEMI variableDeclaration)* SEMI
    ;

variableDeclaration
    : identifierList COLON type_
    ;

procedureAndFunctionDeclarationPart
    : procedureOrFunctionDeclaration SEMI
    ;

procedureOrFunctionDeclaration
    : procedureDeclaration
    | functionDeclaration
    | constructorDeclaration
    | destructorDeclaration
    ;

procedureDeclaration
    : PROCEDURE qualifiedMethodName (formalParameterList)? SEMI block
    ;

functionDeclaration
    : FUNCTION qualifiedMethodName (formalParameterList)? COLON resultType SEMI block
    ;

constructorDeclaration
    : CONSTRUCTOR qualifiedMethodName (formalParameterList)? SEMI block
    ;

destructorDeclaration
    : DESTRUCTOR qualifiedMethodName (formalParameterList)? SEMI block
    ;

qualifiedMethodName
    : identifier (DOT identifier)?
    ;

formalParameterList
    : LPAREN formalParameterSection (SEMI formalParameterSection)* RPAREN
    ;

formalParameterSection
    : parameterGroup
    | VAR parameterGroup
    | FUNCTION parameterGroup
    | PROCEDURE parameterGroup
    | CONST parameterGroup
    ;

parameterGroup
    : identifierList COLON typeIdentifier
    ;

identifierList
    : identifier (COMMA identifier)*
    ;

constList
    : constant (COMMA constant)*
    ;

resultType
    : typeIdentifier
    ;

statement
    : label COLON unlabelledStatement
    | unlabelledStatement
    ;

unlabelledStatement
    : simpleStatement
    | structuredStatement
    ;

simpleStatement
    : assignmentStatement
    | ioStatement
    | inheritedStatement
    | procedureStatement
    | gotoStatement
    | emptyStatement_
    ;

assignmentStatement
    : variable ASSIGN expression
    ;

ioStatement
    : WRITELN (LPAREN expressionList? RPAREN)?
    | WRITE   (LPAREN expressionList? RPAREN)?
    | READLN  (LPAREN variable (COMMA variable)* RPAREN)?
    | READ    (LPAREN variable (COMMA variable)* RPAREN)?
    ;

expressionList
    : expression (COMMA expression)*
    ;

inheritedStatement
    : INHERITED (identifier (LPAREN parameterList RPAREN)?)?
    ;

procedureStatement
    : variable (LPAREN parameterList RPAREN)?
    ;

actualParameter
    : expression parameterwidth*
    ;

parameterwidth
    : COLON expression
    ;

parameterList
    : actualParameter (COMMA actualParameter)*
    ;

gotoStatement
    : GOTO label
    ;

emptyStatement_
    :
    ;

empty_
    :
    ;

structuredStatement
    : compoundStatement
    | conditionalStatement
    | repetetiveStatement
    | withStatement
    ;

compoundStatement
    : BEGIN statements END
    ;

statements
    : statement (SEMI statement)*
    ;

conditionalStatement
    : ifStatement
    | caseStatement
    ;

ifStatement
    : IF expression THEN statement (ELSE statement)?
    ;

caseStatement
    : CASE expression OF caseListElement (SEMI caseListElement)* (SEMI ELSE statements SEMI?)? END
    ;

caseListElement
    : constList COLON statement
    ;

repetetiveStatement
    : whileStatement
    | repeatStatement
    | forStatement
    ;

whileStatement
    : WHILE expression DO statement
    ;

repeatStatement
    : REPEAT statements UNTIL expression
    ;

forStatement
    : FOR identifier ASSIGN forList DO statement
    ;

forList
    : initialValue (TO | DOWNTO) finalValue
    ;

initialValue
    : expression
    ;

finalValue
    : expression
    ;

withStatement
    : WITH recordVariableList DO statement
    ;

recordVariableList
    : variable (COMMA variable)*
    ;

expression
    : simpleExpression (relationaloperator simpleExpression)?
    ;

relationaloperator
    : EQUAL
    | NOT_EQUAL
    | LT
    | LE
    | GE
    | GT
    | IN
    | IS
    | AS
    ;

simpleExpression
    : term (additiveoperator simpleExpression)?
    ;

additiveoperator
    : PLUS
    | MINUS
    | OR
    | XOR
    ;

term
    : signedFactor (multiplicativeoperator term)?
    ;

multiplicativeoperator
    : STAR
    | SLASH
    | DIV
    | MOD
    | AND
    | SHL
    | SHR
    ;

signedFactor
    : (PLUS | MINUS)? factor
    ;

factor
    : LPAREN expression RPAREN
    | functionDesignator
    | unsignedConstant
    | set_
    | NOT factor
    | bool_
    | NIL
    | variable
    ;

unsignedConstant
    : unsignedNumber
    | constantChr
    | string
    ;

functionDesignator
    : variable LPAREN parameterList? RPAREN
    ;

variable
    : (AT identifier | identifier | SELF) variableSuffix*
    ;

variableSuffix
    : LBRACK expression (COMMA expression)* RBRACK
    | LBRACK2 expression (COMMA expression)* RBRACK2
    | DOT identifier
    | POINTER
    ;

set_
    : LBRACK elementList RBRACK
    | LBRACK2 elementList RBRACK2
    ;

elementList
    : element (COMMA element)*
    |
    ;

element
    : expression (DOTDOT expression)?
    ;

// Lexer

ABSTRACT     : 'ABSTRACT' ;
AND          : 'AND' ;
ARRAY        : 'ARRAY' ;
AS           : 'AS' ;
BEGIN        : 'BEGIN' ;
BOOLEAN      : 'BOOLEAN' ;
CASE         : 'CASE' ;
CHAR         : 'CHAR' ;
CHR          : 'CHR' ;
CLASS        : 'CLASS' ;
CONST        : 'CONST' ;
CONSTRUCTOR  : 'CONSTRUCTOR' ;
DESTRUCTOR   : 'DESTRUCTOR' ;
DIV          : 'DIV' ;
DO           : 'DO' ;
DOWNTO       : 'DOWNTO' ;
DYNAMIC      : 'DYNAMIC' ;
ELSE         : 'ELSE' ;
END          : 'END' ;
FALSE        : 'FALSE' ;
FILE         : 'FILE' ;
FOR          : 'FOR' ;
FUNCTION     : 'FUNCTION' ;
GOTO         : 'GOTO' ;
IF           : 'IF' ;
IMPLEMENTATION : 'IMPLEMENTATION' ;
IN           : 'IN' ;
INHERITED    : 'INHERITED' ;
INTEGER      : 'INTEGER' ;
INTERFACE    : 'INTERFACE' ;
IS           : 'IS' ;
LABEL        : 'LABEL' ;
MOD          : 'MOD' ;
NIL          : 'NIL' ;
NOT          : 'NOT' ;
OF           : 'OF' ;
OR           : 'OR' ;
OVERRIDE     : 'OVERRIDE' ;
PACKED       : 'PACKED' ;
PRIVATE      : 'PRIVATE' ;
PROCEDURE    : 'PROCEDURE' ;
PROGRAM      : 'PROGRAM' ;
PROTECTED    : 'PROTECTED' ;
PUBLIC       : 'PUBLIC' ;
PUBLISHED    : 'PUBLISHED' ;
READ         : 'READ' ;
READLN       : 'READLN' ;
REAL         : 'REAL' ;
RECORD       : 'RECORD' ;
REPEAT       : 'REPEAT' ;
RESULT       : 'RESULT' ;
SELF         : 'SELF' ;
SET          : 'SET' ;
SHL          : 'SHL' ;
SHR          : 'SHR' ;
STRICT       : 'STRICT' ;
STRING       : 'STRING' ;
THEN         : 'THEN' ;
TO           : 'TO' ;
TRUE         : 'TRUE' ;
TYPE         : 'TYPE' ;
UNIT         : 'UNIT' ;
UNTIL        : 'UNTIL' ;
USES         : 'USES' ;
VAR          : 'VAR' ;
VIRTUAL      : 'VIRTUAL' ;
WHILE        : 'WHILE' ;
WITH         : 'WITH' ;
WRITE        : 'WRITE' ;
WRITELN      : 'WRITELN' ;
XOR          : 'XOR' ;

PLUS         : '+' ;
MINUS        : '-' ;
STAR         : '*' ;
SLASH        : '/' ;
ASSIGN       : ':=' ;
COMMA        : ',' ;
SEMI         : ';' ;
COLON        : ':' ;
EQUAL        : '=' ;
NOT_EQUAL    : '<>' ;
LT           : '<' ;
LE           : '<=' ;
GE           : '>=' ;
GT           : '>' ;
LPAREN       : '(' ;
RPAREN       : ')' ;
LBRACK       : '[' ;
LBRACK2      : '(.' ;
RBRACK       : ']' ;
RBRACK2      : '.)' ;
POINTER      : '^' ;
AT           : '@' ;
DOTDOT       : '..' ;
DOT          : '.' ;

IDENT
    : [A-Z_] [A-Z0-9_]*
    ;

STRING_LITERAL
    : '\'' ('\'\'' | ~['\r\n])* '\''
    ;

NUM_INT
    : [0-9]+
    ;

NUM_REAL
    : [0-9]+ ('.' [0-9]+ EXPONENT? | EXPONENT)
    ;

fragment EXPONENT
    : [E] [+\-]? [0-9]+
    ;

WS
    : [ \t\r\n]+ -> skip
    ;

COMMENT_1
    : '(*' .*? '*)' -> skip
    ;

COMMENT_2
    : '{' .*? '}' -> skip
    ;

COMMENT_3
    : '//' ~[\r\n]* -> skip
    ;
