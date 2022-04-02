grammar Untie;

input: expr EOF;

expr: plusMinus;

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
NUMBER: [0-9]+;
SYMBOL: [a-z]+;
