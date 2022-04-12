package com.masflam.untie.expr;

public class Where implements Expression {
	private Expression expr;
	private String sym;
	private Expression symExpr;
	
	public Where(Expression expr, String sym, Expression symExpr) {
		this.expr = expr;
		this.sym = sym;
		this.symExpr = symExpr;
	}
	
	public Expression getExpr() {
		return expr;
	}
	
	public void setExpr(Expression expr) {
		this.expr = expr;
	}
	
	public String getSym() {
		return sym;
	}
	
	public void setSym(String sym) {
		this.sym = sym;
	}
	
	public Expression getSymExpr() {
		return symExpr;
	}
	
	public void setSymExpr(Expression symExpr) {
		this.symExpr = symExpr;
	}
	
	@Override
	public String toString() {
		return "(" + expr + " where " + sym + " = " + symExpr + ")";
	}
}
