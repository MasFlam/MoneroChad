package com.masflam.untie.expr;

import java.util.HashMap;
import java.util.Map;

import com.masflam.untie.exception.UnrecognizedSymbolException;

public interface Expression {
	@Override
	String toString();
	
	public static double evaluate(Expression expr, Map<String, Double> ctx) throws UnrecognizedSymbolException {
		if (expr instanceof Where where) {
			Map<String, Double> newCtx = new HashMap<>(ctx);
			newCtx.put(where.getSym(), evaluate(where.getSymExpr(), ctx));
			return evaluate(where.getExpr(), newCtx);
		} else if (expr instanceof Sum sum) {
			double out = 0;
			for (var el : sum.getElements()) {
				out += evaluate(el, ctx);
			}
			return out;
		} else if (expr instanceof Product prod) {
			double out = 1;
			for (var el : prod.getFactors()) {
				out *= evaluate(el, ctx);
			}
			return out;
		} else if (expr instanceof Fraction fr) {
			return evaluate(fr.getNumerator(), ctx) / evaluate(fr.getDenominator(), ctx);
		} else if (expr instanceof UnaryMinus um) {
			return -evaluate(um.getExpr(), ctx);
		} else if (expr instanceof PowerTower pt) {
			double out = evaluate(pt.getTerms().get(0), ctx);
			for (int i = 1; i < pt.getTerms().size(); ++i) {
				out = Math.pow(out, evaluate(pt.getTerms().get(i), ctx));
			}
			return out;
		} else if (expr instanceof SymbolLiteral lit) {
			Double val = ctx.get(lit.getName());
			if (val != null) return val;
			throw new UnrecognizedSymbolException(lit.getName());
		} else if (expr instanceof IntegerLiteral lit) {
			return lit.getValue();
		} else {
			throw new IllegalArgumentException("Unknown Expression subtype");
		}
	}
}
