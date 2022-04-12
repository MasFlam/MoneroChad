grammar Untie;

input: expr EOF;

expr: where;

where
	: where WHERE SYMBOL EQ plusMinus
	| plusMinus
;

plusMinus
	: plusMinus (ops+=(PLUS | MINUS) multDiv)+
	| multDiv
;

multDiv
	: multDiv (ops+=(MULT | DIV) unaryPlusMinus)+
	| unaryPlusMinus
;

unaryPlusMinus
	: op=(PLUS | MINUS) pow
	| pow
;

pow
	: pow (POW atom)+
	| atom
;

atom: LPAREN expr RPAREN | literal;

literal: SYMBOL | NUMBER;

PLUS: '+';
MINUS: '-';
MULT: '*';
DIV: '/';
POW: '^';
LPAREN: '(';
RPAREN: ')';
EQ: '=';
WHERE: 'where';
NUMBER: [0-9]+;
SYMBOL: [a-z]+;

WS: [\t\n\r ] -> skip;
