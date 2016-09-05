package fun.algebra;

import java.util.function.BinaryOperator;

import fun.Foldable;

public final class Monoid<M> {
	public static <M> Monoid<M> monoid(M identity, Semigroup<M> semigroup) {
		return new Monoid<>(identity, semigroup);
	}

	public static <M> Monoid<M> monoid(M identity, BinaryOperator<M> operator) {
		return new Monoid<>(identity, Semigroup.semigroup(operator));
	}

	private M identity;
	private Semigroup<M> semigroup;

	private Monoid(M identity, Semigroup<M> semigroup) {
		this.semigroup = semigroup;
		this.identity = identity;
	}

	public M identity() {
		return identity;
	}

	public Semigroup<M> asSemigroup() {
		return semigroup;
	}

	public M operate(M a, M b) {
		return semigroup.operate(a, b);
	}

	public M concat(Foldable<M> foldable) {
		return foldable.foldLeft(identity, this::operate);
	}
}
