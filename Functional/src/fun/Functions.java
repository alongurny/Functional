package fun;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Functions {

	public static <A, B> Function<A, B> constant(B value) {
		return a -> value;
	}

	public static <A, B, X> Function<A, Function<B, X>> curry(BiFunction<A, B, X> f) {
		return a -> b -> f.apply(a, b);
	}

	public static <A, B, R> BiFunction<B, A, R> flip(BiFunction<A, B, R> f) {
		return (b, a) -> f.apply(a, b);
	}

	public static <A> A identity(A value) {
		return value;
	}

	public static <A, B, X> BiFunction<A, B, X> uncurry(Function<A, Function<B, X>> f) {
		return (a, b) -> f.apply(a).apply(b);
	}

	public static <A, B, C> Function<A, C> compose(Function<B, C> g, Function<A, B> f) {
		return g.compose(f);
	}
}
