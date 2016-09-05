package fun;

import java.util.function.BiFunction;

public final class Pair<F, S> {

	private interface Constructor<F, S> {
		<X> X match(BiFunction<F, S, X> pattern);
	}

	public static <F, S> Pair<F, S> pair(F first, S second) {
		return new Pair<F, S>(new Constructor<F, S>() {
			@Override
			public <X> X match(BiFunction<F, S, X> pattern) {
				return pattern.apply(first, second);
			}
		});
	}

	private Constructor<F, S> constructor;

	private Pair(Constructor<F, S> constructor) {
		this.constructor = constructor;
	}

	public F getFirst() {
		return match((first, second) -> first);
	}

	public S getSecond() {
		return match((first, second) -> second);
	}

	public <X> X match(BiFunction<F, S, X> pattern) {
		return constructor.match(pattern);
	}

}
