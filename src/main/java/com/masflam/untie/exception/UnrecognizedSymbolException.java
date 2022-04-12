package com.masflam.untie.exception;

public class UnrecognizedSymbolException extends Exception {
	private final String symbol;
	
	public UnrecognizedSymbolException(String symbol) {
		this.symbol = symbol;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	@Override
	public String toString() {
		return "Unrecognized symbol: " + symbol;
	}
}
