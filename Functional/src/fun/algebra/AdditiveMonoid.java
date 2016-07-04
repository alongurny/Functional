package fun.algebra;

import fun.Bottom;
import fun.Foldable;

public interface AdditiveMonoid<M extends AdditiveMonoid<M>> {

	public static <M extends AdditiveMonoid<M>> M sum(Foldable<M> foldable) {
		return foldable.foldLeft(M::add).getOrDefault(Bottom.error("must have at least one value"));
	}

	M add(M other);

	M zero();

}
