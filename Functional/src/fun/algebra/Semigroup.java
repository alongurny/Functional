package fun.algebra;

import java.util.function.BinaryOperator;

public final class Semigroup<S> {

	public static <S> Semigroup<S> semigroup(BinaryOperator<S> operator) {
		return new Semigroup<>(operator);
	}

	private BinaryOperator<S> operator;

	private Semigroup(BinaryOperator<S> operator) {
		this.operator = operator;
	}

	public S operate(S a, S b) {
		return operator.apply(a, b);
	}

}
