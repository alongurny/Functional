package fun;

import java.util.function.BiFunction;

public final class Pair<F, S> {

	public static <F, S> Pair<F, S> pair(F first, S second) {
		return new Pair<>(first, second);
	}

	private final F first;
	private final S second;

	private Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}

	public F getFirst() {
		return first;
	}

	public S getSecond() {
		return second;
	}

	public <X> X match(BiFunction<F, S, X> pattern) {
		return pattern.apply(first, second);
	}

}
