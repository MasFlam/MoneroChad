package com.masflam.untie.expr;

import java.util.List;

public class Sum implements Expression {
	private List<Expression> elements;
	
	public Sum(List<Expression> elements) {
		this.elements = elements;
	}
	
	public List<Expression> getElements() {
		return elements;
	}
	
	public void setElements(List<Expression> elements) {
		this.elements = elements;
	}
	
	@Override
	public String toString() {
		if (elements.size() == 1) {
			return elements.get(0).toString();
		}
		var sb = new StringBuilder();
		sb.append('(');
		sb.append(elements.get(0));
		for (int i = 1; i < elements.size(); ++i) {
			sb.append('+');
			sb.append(elements.get(i));
		}
		sb.append(')');
		return sb.toString();
	}
}
