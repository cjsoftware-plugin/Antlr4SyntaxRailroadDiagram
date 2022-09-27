// ANTLR G4
parser grammar GFTRecipeParser ;
options {
    tokenVocab='com/cjsoftware/script/GFTRecipeLexer';
}

recipe : recipeStatement * EOF ;

recipeStatement : includeStatement
                | blockStatement
                | variableDeclaration
                | functionDeclaration
                | assignment
                | expressionStatement
                | conditionalStatement
                | modelIteration
                | modelWalk
                | repeatStatement
                | protectedBlock
                | print
                | returnStatement
                ;


includeStatement : INCLUDE name=expression;

variableDeclaration : VAR lhs=IDENTIFIER TAKESVALUEOF rhs=expression #typedVarDecl
               | VAR lhs=IDENTIFIER ANY #untypedVarDecl
               ;

functionDeclaration : FUN (appliedType=type (AS receiverVar = IDENTIFIER)? DECIMAL)? lhs=IDENTIFIER LPAREN argumentList? RPAREN (returnType=type)? body=recipeStatement;

returnStatement : RETURN expression;

argumentList : args+=argumentDeclaration (COMMA args+=argumentDeclaration)*;
argumentDeclaration : argName=IDENTIFIER argType=type;

type : NUMBER
     | STRING
     | BOOLEAN
     | MAP
     | ARRAY
     | NODE
     | PAIR
     | FILE
     | DATE
     | ANY
     ;


simpleAssignment : lhs=identifierRef TAKESVALUEOF rhs=expression;


assignment : simpleAssignment
           ;

conditionalStatement : IF condition=expression truepart=recipeStatement (ELSE falsepart=recipeStatement)?;

modelIteration : ITERATE modelsource=expression AS iterateVar=IDENTIFIER body=recipeStatement;
modelWalk : WALK modelSource=expression walkStrategy AS walkVar=IDENTIFIER body=recipeStatement;

walkStrategy : DEPTH
             | BREADTH
             | INORDER
             | PREORDER
             | POSTORDER
             ;


repeatStatement : REPEAT limit=expression (AS loopCounter=IDENTIFIER)? loopBody=recipeStatement #simpleRepeat
                | WHILE condition=expression loopBody=recipeStatement #repeatWhile
                | REPEAT loopBody=recipeStatement UNTIL condition=expression #repeatUntil
                ;

blockStatement : LBRACE recipeStatement+ RBRACE ;

protectedBlock : PROTECTED recipeStatement errorPart?;

errorPart : ERRORSTATEMENT (AS errorVar=IDENTIFIER)? recipeStatement;

expressionStatement : expression;

expression : literal #literalTerm
            | receiver=expression DECIMAL funRef LPAREN parameterList? RPAREN #functionCall
            | funRef LPAREN parameterList? RPAREN #functionCall
            | identifierRef #varTerm
            | prefix=NOT rhs=expression #prefixNot
            | prefix=MINUS rhs=expression #prefixNegate
            | (LPAREN expression RPAREN) #parenTerm
            | lhs=expression bOp=POWER rhs=expression #powerTerm
            | lhs=expression bOp=MULTIPLY rhs=expression #mulTerm
            | lhs=expression bOp=DIVIDE rhs=expression #divTerm
            | lhs=expression bOp=MODULO rhs=expression #modTerm
            | lhs=expression bOp=PLUS rhs=expression #addTerm
            | lhs=expression bOp=MINUS rhs=expression #subTerm
            | lhs=expression bOp=SHL rhs=expression #shlTerm
            | lhs=expression bOp=SHR rhs=expression #shrTerm
            | lhs=expression bOp=GREATER rhs=expression #greaterTerm
            | lhs=expression bOp=LESS rhs=expression #lessTerm
            | lhs=expression bOp=GREATEROREQUAL rhs=expression #greaterOrEqualTerm
            | lhs=expression bOp=LESSOREQUAL rhs=expression #lessOrEqualTerm
            | lhs=expression bOp=ISEQUAL rhs=expression #equalityTerm
            | lhs=expression bOp=NOTEQUAL rhs=expression #inequalityTerm
            | lhs=expression bOp=AND rhs=expression #andTerm
            | lhs=expression bOp=XOR rhs=expression #xorTerm
            | lhs=expression bOp=OR rhs=expression #orTerm
            ;

literal : stringLiteral
        | numericLiteral
        | booleanLiteral
        | mapLiteral
        | arrayLiteral
        | nodeLiteral
        | pairLiteral
        | fileLiteral
        | dateLiteral
        ;

parameterList : params+=expression (COMMA params+=expression)* ;

booleanLiteral : (TRUE | FALSE);

mapLiteral : LESS (elementList+=expression COMMA?)* GREATER ;

arrayLiteral : LSQ (elementList+=expression COMMA?)* RSQ ;

pairLiteral : LPAREN key=expression GOESTO value=expression? RPAREN ;

nodeLiteral : LPAREN nodeValue=expression GOESTO leftChild=expression? COMMA rightChild=expression? RPAREN ;

fileLiteral : FILE LPAREN path=expression RPAREN;

dateLiteral : DATE LPAREN date=expression RPAREN;

funRef : scopeSpec? funName=IDENTIFIER;

identifierRef : scopeSpec? identifier=IDENTIFIER index? ;

index : LSQ indexVal=expression RSQ;

scopeSpec : scope+=outerScope+;

outerScope : OUTER DECIMAL;

print : PRINT (err=ERROR)? noNewline=APPEND? parameterList? ;


numericLiteral : FLOAT
               | INT
               ;

stringLiteral : DOUBLEQUOTE stringParts+=stringPart* ENDDOUBLEQUOTE;

stringPart : escapedChar
           | unicodeChar
           | stringCharSeq
           ;

stringCharSeq: STRINGCHARSEQ ;

unicodeChar : UNICODE_ESCAPE CODEPOINT;

escapedChar : BACKSLASH ESCAPECHAR;


