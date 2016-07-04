package fun;

public abstract class Pair<F, S> {

	public interface Pattern<F, S, X> {
		X matchPair(F first, S second);
	}

	public static <F, B> Pair<F, B> pair(F first, B second) {
		return new Pair<F, B>() {
			@Override
			public <X> X match(Pattern<F, B, X> pattern) {
				return pattern.matchPair(first, second);
			}
		};
	}

	private Pair() {
	}

	public F getFirst() {
		return match((f, s) -> f);
	}

	public S getSecond() {
		return match((f, s) -> s);
	}

	public abstract <X> X match(Pattern<F, S, X> pattern);

}
