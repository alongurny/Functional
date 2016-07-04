package fun.algebra;

import fun.Bottom;
import fun.Foldable;

public interface Monoid<M extends Monoid<M>> {

	public static <M extends Monoid<M>> M concat(Foldable<M> foldable) {
		return foldable.foldLeft(M::append).getOrDefault(Bottom.error("must have at least one value"));
	}

	M append(M other);

	M identity();

}
