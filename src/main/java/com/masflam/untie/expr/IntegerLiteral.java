package com.masflam.untie.expr;

public class IntegerLiteral implements Expression {
	private int value;
	
	public IntegerLiteral(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return Integer.toString(value);
	}
}
