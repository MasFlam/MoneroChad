package com.masflam.monerochad.command;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import com.masflam.monerochad.CommandHandler;
import com.masflam.monerochad.CommandPath;
import com.masflam.untie.UntieBaseVisitor;
import com.masflam.untie.UntieLexer;
import com.masflam.untie.UntieParser;
import com.masflam.untie.UntieParser.AtomContext;
import com.masflam.untie.UntieParser.ExprContext;
import com.masflam.untie.UntieParser.InputContext;
import com.masflam.untie.UntieParser.LiteralContext;
import com.masflam.untie.UntieParser.MultDivContext;
import com.masflam.untie.UntieParser.PlusMinusContext;
import com.masflam.untie.UntieParser.PowContext;
import com.masflam.untie.UntieParser.UnaryPlusMinusContext;
import com.masflam.untie.expr.Expression;
import com.masflam.untie.expr.Fraction;
import com.masflam.untie.expr.IntegerLiteral;
import com.masflam.untie.expr.PowerTower;
import com.masflam.untie.expr.Product;
import com.masflam.untie.expr.Sum;
import com.masflam.untie.expr.UnaryMinus;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.InteractionHook;

@ApplicationScoped
@CommandPath("calc")
public class CalcCommand implements CommandHandler {
	
	@Override
	public void handle(SlashCommandInteractionEvent event, InteractionHook ihook) throws Exception {
		var exprString = event.getOption("expression").getAsString();
		var charStream = CharStreams.fromString(exprString);
		var lexer = new UntieLexer(charStream);
		var tokenStream = new CommonTokenStream(lexer);
		var parser = new UntieParser(tokenStream);
		
		var visitor = new UntieBaseVisitor<Expression>() {
			
			@Override
			public Expression visitInput(InputContext ctx) {
				return visit(ctx.expr());
			}
			
			@Override
			public Expression visitExpr(ExprContext ctx) {
				return visit(ctx.plusMinus());
			}
			
			@Override
			public Expression visitPlusMinus(PlusMinusContext ctx) {
				if (ctx.plusMinus() == null) {
					return visit(ctx.multDiv(0));
				} else {
					List<Expression> elements = new ArrayList<>();
					elements.add(visit(ctx.plusMinus()));
					for (int i = 0; i < ctx.ops.size(); ++i) {
						boolean plus = ctx.ops.get(i).getType() == UntieLexer.PLUS;
						Expression expr = visit(ctx.multDiv(i));
						elements.add(plus ? expr : new UnaryMinus(expr));
					}
					return new Sum(elements);
				}
			}
			
			@Override
			public Expression visitMultDiv(MultDivContext ctx) {
				if (ctx.multDiv() == null) {
					return visit(ctx.unaryPlusMinus(0));
				} else {
					List<Expression> numeratorFactors = new ArrayList<>();
					List<Expression> denominatorFactors = new ArrayList<>();
					numeratorFactors.add(visit(ctx.multDiv()));
					for (int i = 0; i < ctx.ops.size(); ++i) {
						boolean numerator = ctx.ops.get(i).getType() == UntieLexer.MULT;
						Expression expr = visit(ctx.unaryPlusMinus(i));
						(numerator ? numeratorFactors : denominatorFactors).add(expr);
					}
					if (denominatorFactors.isEmpty()) {
						return new Product(numeratorFactors);
					} else {
						return new Fraction(
							new Product(numeratorFactors),
							new Product(denominatorFactors)
						);
					}
				}
			}
			
			@Override
			public Expression visitUnaryPlusMinus(UnaryPlusMinusContext ctx) {
				if (ctx.op == null || ctx.op.getType() == UntieLexer.PLUS) {
					return visit(ctx.pow());
				} else {
					return new UnaryMinus(visit(ctx.pow()));
				}
			}
			
			@Override
			public Expression visitPow(PowContext ctx) {
				if (ctx.pow() == null) {
					return visit(ctx.atom(0));
				} else {
					List<Expression> terms = new ArrayList<>();
					terms.add(visit(ctx.pow()));
					for (int i = 0; i < ctx.atom().size(); ++i) {
						terms.add(visit(ctx.atom(i)));
					}
					return new PowerTower(terms);
				}
			}
			
			@Override
			public Expression visitAtom(AtomContext ctx) {
				if (ctx.expr() != null) {
					return visit(ctx.expr());
				} else {
					return visit(ctx.literal());
				}
			}
			
			@Override
			public Expression visitLiteral(LiteralContext ctx) {
				int val = ctx.NUMBER() != null ? Integer.parseInt(ctx.NUMBER().getText()) : -42;
				return new IntegerLiteral(val);
			}
		};
		
		Expression expr = visitor.visit(parser.input());
		double result = Expression.evaluate(expr);
		
		ihook.sendMessage("```\n" + expr + " = " + result + "\n```").queue();
	}
}
