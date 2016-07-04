package fun.algebra;

import fun.Bottom;
import fun.Foldable;

public interface MultiplicativeMonoid<M extends MultiplicativeMonoid<M>> {

	public static <M extends MultiplicativeMonoid<M>> M sum(Foldable<M> foldable) {
		return foldable.foldLeft(M::multiply).getOrDefault(Bottom.error("must have at least one value"));
	}

	M multiply(M other);

	M unit();

}
