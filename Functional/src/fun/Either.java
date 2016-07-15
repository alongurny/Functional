package fun;

import java.util.function.Function;

public final class Either<L, R> {

	private interface Constructor<L, R> {
		<X> X match(Function<L, X> matchLeft, Function<R, X> matchRight);
	}

	public static <L, R> Either<L, R> left(L value) {
		return new Either<L, R>(new Constructor<L, R>() {
			public <X> X match(Function<L, X> matchLeft, Function<R, X> matchRight) {
				return matchLeft.apply(value);
			}
		});
	}

	public static <L, R> Either<L, R> right(R value) {
		return new Either<L, R>(new Constructor<L, R>() {
			public <X> X match(Function<L, X> matchLeft, Function<R, X> matchRight) {
				return matchRight.apply(value);
			}
		});
	}

	private Constructor<L, R> constructor;

	private Either(Constructor<L, R> constructor) {
		this.constructor = constructor;
	}

	public <T, S> Either<T, S> bimap(Function<L, T> leftMapper, Function<R, S> rightMapper) {
		return match(value -> left(leftMapper.apply(value)), value -> right(rightMapper.apply(value)));
	}

	public boolean isLeft() {
		return match(value -> true, value -> false);
	}

	public boolean isRight() {
		return match(value -> false, value -> true);
	}

	public <T> Either<T, R> mapLeft(Function<L, T> mapper) {
		return match(Functions.compose(Either::left, mapper), Either::right);
	}

	public <T> Either<T, R> bindLeft(Function<L, Either<T, R>> f) {
		return match(f, Either::right);
	}

	public <T> Either<L, T> mapRight(Function<R, T> mapper) {
		return match(Either::left, Functions.compose(Either::right, mapper));
	}

	public <T> Either<L, T> bindRight(Function<R, Either<L, T>> f) {
		return match(Either::left, f);
	}

	public <X> X match(Function<L, X> matchLeft, Function<R, X> matchRight) {
		return constructor.match(matchLeft, matchRight);
	}

	@Override
	public String toString() {
		return match(value -> "Either.left(" + value + ")", value -> "Either.right(" + value + ")");
	}

}
