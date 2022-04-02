package com.masflam.untie.expr;

import java.util.List;

public class Product implements Expression {
	private List<Expression> factors;
	
	public Product(List<Expression> factors) {
		this.factors = factors;
	}
	
	public List<Expression> getFactors() {
		return factors;
	}
	
	public void setFactors(List<Expression> factors) {
		this.factors = factors;
	}
	
	@Override
	public String toString() {
		if (factors.size() == 1) {
			return factors.get(0).toString();
		}
		var sb = new StringBuilder();
		sb.append('(');
		sb.append(factors.get(0));
		for (int i = 1; i < factors.size(); ++i) {
			sb.append('*');
			sb.append(factors.get(i));
		}
		sb.append(')');
		return sb.toString();
	}
}
