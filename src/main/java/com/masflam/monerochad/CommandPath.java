package com.masflam.monerochad;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Qualifier;

@Documented
@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
public @interface CommandPath {
	String value();
	
	@SuppressWarnings("all")
	public static abstract class Literal extends AnnotationLiteral<CommandPath> implements CommandPath {
		public static Literal of(String path) {
			return new Literal() {
				@Override
				public String value() {
					return path;
				}
			};
		}
	}
}
