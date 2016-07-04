package fun;

import java.util.function.Function;

public abstract class Either<L, R> {

	public interface Pattern<L, R, X> {
		default X matchLeft(L value) {
			return Bottom.undefined();
		}

		default X matchRight(R value) {
			return Bottom.undefined();
		}
	}

	public static <L, R> Either<L, R> left(L value) {
		return new Either<L, R>() {
			public <X> X match(Pattern<L, R, X> pattern) {
				return pattern.matchLeft(value);
			}
		};
	}

	public static <L, R> Either<L, R> right(R value) {
		return new Either<L, R>() {
			public <X> X match(Pattern<L, R, X> pattern) {
				return pattern.matchRight(value);
			}
		};
	}

	private Either() {
	}

	public <T, S> Either<T, S> bimap(Function<L, T> f, Function<R, S> g) {
		return matchEither(value -> left(f.apply(value)), value -> right(g.apply(value)));
	}

	public boolean isLeft() {
		return matchEither(value -> true, value -> false);
	}

	public boolean isRight() {
		return matchEither(value -> false, value -> true);
	}

	public abstract <X> X match(Pattern<L, R, X> pattern);

	public <X> X matchEither(Function<L, X> leftMatch, Function<R, X> rightMatch) {
		return match(new Pattern<L, R, X>() {
			public X matchLeft(L value) {
				return leftMatch.apply(value);
			}

			public X matchRight(R value) {
				return rightMatch.apply(value);
			}
		});
	}

	@Override
	public String toString() {
		return matchEither(value -> "Either.left(" + value + ")", value -> "Either.right(" + value + ")");
	}

}
