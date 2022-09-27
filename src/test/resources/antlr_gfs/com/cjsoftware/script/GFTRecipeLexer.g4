lexer grammar GFTRecipeLexer;

INCLUDE : 'include';

APPEND : 'append' ;

// Conditional
IF : 'if';
ELSE: 'else';

// Declaration
VAR : 'var';
FUN : 'fun';

// Scope traversal
OUTER : 'outer';

// Error handling
PROTECTED : 'protected';
ERRORSTATEMENT : 'error';

// function return
RETURN : 'return';

// Loop index association
AS : 'as';

// Model Loops
ITERATE : 'iterate';

WALK : 'walk';
BREADTH : 'breadth';
DEPTH : 'depth';
INORDER : 'inorder';
PREORDER : 'preorder';
POSTORDER : 'postorder';

// Loops
REPEAT : 'repeat';
WHILE : 'while';
UNTIL : 'until';

PRINT : 'print';

// Separators
COMMA : ',' ;

// Connection
GOESTO : '->';

// Assignment
TAKESVALUEOF : '=';

// Arithmetic
PLUS : '+';
MINUS : '-';
MULTIPLY : '*';
DIVIDE : '/';
MODULO : '%';
POWER : '^';

// Shift
SHL : '<<';
SHR : '>>';

NOT : 'not';
OR : 'or';
XOR : 'xor';
AND : 'and';
TRUE : 'true';
FALSE : 'false';

// comparisons
ISEQUAL : '==';
NOTEQUAL : '!=';
GREATER : '>' ;
LESS : '<' ;
GREATEROREQUAL : '>=';
LESSOREQUAL : '<=';

// enclosures
LPAREN : '(';
RPAREN : ')';
LBRACE : '{';
RBRACE : '}';
LSQ : '[';
RSQ : ']';


// Types
NUMBER : 'Number';
STRING : 'String';
BOOLEAN : 'Boolean';
MAP : 'Map';
ARRAY : 'Array';
NODE : 'Node';
PAIR : 'Pair';
FILE : 'File';
ERROR : 'Error';
DATE : 'Date';
ANY : 'Any';

DOUBLEQUOTE: '"' -> pushMode(QUOTED_STRING);

FLOAT :  DECIMALPART MANTISSA?;

fragment DECIMALPART : INT DECIMAL INT;

INT : DIGITS;

fragment MANTISSA : EXPONENT SIGN? INT;
fragment SIGN : '-' ;
fragment DIGITS : DIGIT+ ;
fragment EXPONENT : 'e' | 'E' ;

DECIMAL : '.' ;

IDENTIFIER : STARTIDCHAR (IDCHAR)* ;

STARTIDCHAR : LETTER;

IDCHAR : STARTIDCHAR
       | '_'
       | DIGIT
       ;

LETTER : [A-Z]
       | [a-z]
       ;

fragment DIGIT : [0-9] ;

// Comments and whitespace

WHITE_SPACE : [ \t\r\n\u000C]+ -> channel(HIDDEN);

BLOCK_COMMENT:      '/*' .*? '*/'    -> channel(HIDDEN);
LINE_COMMENT:       '//' ~[\r\n]*    -> channel(HIDDEN);

// Quoted strings
mode QUOTED_STRING;

ENDDOUBLEQUOTE : '"' -> popMode;

UNICODE_ESCAPE : '\\u' -> pushMode(UNICODE);

BACKSLASH : '\\' -> pushMode(ESCAPE_CHAR);

STRINGCHARSEQ : STRCHAR+
              ;

fragment STRCHAR: ~[\\"\r\n\t];

mode ESCAPE_CHAR;
ESCAPECHAR : ["\\rnt] -> popMode;

mode UNICODE;
CODEPOINT : HEXDIG HEXDIG HEXDIG HEXDIG -> popMode;

fragment HEXDIG : [A-Fa-f0-9];

