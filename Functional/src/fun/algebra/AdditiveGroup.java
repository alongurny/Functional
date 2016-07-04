package fun.algebra;

public interface AdditiveGroup<G extends AdditiveGroup<G>> extends AdditiveMonoid<G> {

	G negate();

	default G subtract(G other) {
		return add(other.negate());
	}

}
