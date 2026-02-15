grammar delphi;

program: INT EOF;

INT: [0-9]+;
WS: [ \t\r\n]+ -> skip;
