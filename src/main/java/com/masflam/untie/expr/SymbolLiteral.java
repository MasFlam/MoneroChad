package com.masflam.untie.expr;

public class SymbolLiteral implements Expression {
	private String name;
	
	public SymbolLiteral(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
