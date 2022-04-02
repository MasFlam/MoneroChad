package com.masflam.untie.expr;

public class Fraction implements Expression {
	private Expression numerator;
	private Expression denominator;
	
	public Fraction(Expression numerator, Expression denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}
	
	public Expression getNumerator() {
		return numerator;
	}
	
	public void setNumerator(Expression numerator) {
		this.numerator = numerator;
	}
	
	public Expression getDenominator() {
		return denominator;
	}
	
	public void setDenominator(Expression denominator) {
		this.denominator = denominator;
	}
	
	@Override
	public String toString() {
		return "(" + numerator + "/" + denominator + ")";
	}
}
