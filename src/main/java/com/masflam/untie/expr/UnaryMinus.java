package com.masflam.untie.expr;

public class UnaryMinus implements Expression {
	private Expression expr;
	
	public UnaryMinus(Expression expr) {
		this.expr = expr;
	}
	
	public Expression getExpr() {
		return expr;
	}
	
	public void setExpr(Expression expr) {
		this.expr = expr;
	}
	
	@Override
	public String toString() {
		return "(-" + expr + ")";
	}
}
