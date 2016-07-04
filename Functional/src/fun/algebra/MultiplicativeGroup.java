package fun.algebra;

public interface MultiplicativeGroup<G extends MultiplicativeGroup<G>> extends MultiplicativeMonoid<G> {

	default G divide(G other) {
		return multiply(other.inverse());
	}

	G inverse();

}
