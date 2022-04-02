package com.masflam.untie.expr;

import java.util.List;

public class PowerTower implements Expression {
	private List<Expression> terms;
	
	public PowerTower(List<Expression> terms) {
		this.terms = terms;
	}
	
	public List<Expression> getTerms() {
		return terms;
	}
	
	public void setTerms(List<Expression> terms) {
		this.terms = terms;
	}
	
	@Override
	public String toString() {
		if (terms.size() == 1) {
			return terms.get(0).toString();
		}
		var sb = new StringBuilder();
		sb.append('(');
		sb.append(terms.get(0));
		for (int i = 1; i < terms.size(); ++i) {
			sb.append('^');
			sb.append(terms.get(i));
		}
		sb.append(')');
		return sb.toString();
	}
}
