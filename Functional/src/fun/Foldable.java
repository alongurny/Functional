package fun;

import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Predicate;

public interface Foldable<A> {
	public static boolean and(Foldable<Boolean> foldable) {
		return foldable.foldLeft(true, Boolean::logicalAnd);
	}

	public static <A extends Comparable<A>> Option<A> maximum(Foldable<A> foldable) {
		return foldable.foldLeft((x, y) -> x.compareTo(y) >= 0 ? x : y);
	}

	public static <A extends Comparable<A>> Option<A> minimum(Foldable<A> foldable) {
		return foldable.foldLeft((x, y) -> x.compareTo(y) <= 0 ? x : y);
	}

	public static boolean or(Foldable<Boolean> foldable) {
		return foldable.foldLeft(false, Boolean::logicalOr);
	}

	public static int product(Foldable<Integer> foldable) {
		return foldable.foldLeft(1, (a, b) -> a * b);
	}

	public static int sum(Foldable<Integer> foldable) {
		return foldable.foldLeft(0, (a, b) -> a + b);
	}

	default boolean allMatch(Predicate<A> predicate) {
		return foldLeft(true, (r, t) -> r && predicate.test(t));
	}

	default boolean anyMatch(Predicate<A> predicate) {
		return foldLeft(false, (r, t) -> r || predicate.test(t));
	}

	default boolean contains(A object) {
		return anyMatch(object::equals);
	}

	default Option<A> foldLeft(BinaryOperator<A> f) {
		return foldLeft(Option.none(), (r, t) -> r.matchSome(v -> Option.some(f.apply(v, t)), () -> Option.some(t)));
	}

	<X> X foldLeft(X initialValue, BiFunction<X, A, X> f);

	default boolean isEmpty() {
		return foldLeft(false, (r, t) -> true);
	}

	default int length() {
		return foldLeft(0, (r, t) -> 1 + r);
	}

}
