package com.masflam.untie.expr;

public interface Expression {
	@Override
	String toString();
	
	public static double evaluate(Expression expr) {
		if (expr instanceof Sum sum) {
			double out = 0;
			for (var el : sum.getElements()) {
				out += evaluate(el);
			}
			return out;
		} else if (expr instanceof Product prod) {
			double out = 1;
			for (var el : prod.getFactors()) {
				out *= evaluate(el);
			}
			return out;
		} else if (expr instanceof Fraction fr) {
			return evaluate(fr.getNumerator()) / evaluate(fr.getDenominator());
		} else if (expr instanceof UnaryMinus um) {
			return -evaluate(um.getExpr());
		} else if (expr instanceof PowerTower pt) {
			double out = evaluate(pt.getTerms().get(0));
			for (int i = 1; i < pt.getTerms().size(); ++i) {
				out = Math.pow(out, evaluate(pt.getTerms().get(i)));
			}
			return out;
		} else if (expr instanceof IntegerLiteral lit) {
			return lit.getValue();
		} else {
			throw new IllegalArgumentException("Unknown Expression subtype");
		}
	}
}
