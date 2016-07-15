package fun;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import fun.algebra.Monoid;

public final class List<A> implements Foldable<A>, Monoid<List<A>> {

	private interface Constructor<A> {
		<X> X match(BiFunction<A, List<A>, X> matchCons, Supplier<X> matchEmpty);
	}

	public static <A> List<A> cons(A head, List<A> tail) {
		return new List<>(new Constructor<A>() {
			@Override
			public <X> X match(BiFunction<A, List<A>, X> matchCons, Supplier<X> matchEmpty) {
				return matchCons.apply(head, tail);
			}
		});
	}

	public static <A> List<A> empty() {
		return new List<>(new Constructor<A>() {
			@Override
			public <X> X match(BiFunction<A, List<A>, X> matchCons, Supplier<X> matchEmpty) {
				return matchEmpty.get();
			}
		});
	}

	public static List<Integer> inclusiveRange(int start, int end) {
		return start > end ? empty() : cons(start, inclusiveRange(start + 1, end));
	}

	public static <A> List<A> join(List<List<A>> list) {
		return Monoid.concat(list);
	}

	@SafeVarargs
	public static <A> List<A> list(A... arr) {
		return list(0, arr);
	}

	public static <A> List<A> pure(A value) {
		return cons(value, empty());
	}

	public static List<Integer> range(int start, int end) {
		return start >= end ? empty() : cons(start, range(start + 1, end));
	}

	private static <A> List<A> list(int i, A[] arr) {
		if (i >= arr.length) {
			return empty();
		}
		return cons(arr[i], list(i + 1, arr));
	}

	private Constructor<A> constructor;

	public List(Constructor<A> constructor) {
		this.constructor = constructor;
	}

	@Override
	public List<A> append(List<A> other) {
		return match((h, t) -> cons(h, t.append(other)), () -> other);
	}

	public <B> List<B> bind(Function<A, List<B>> f) {
		return join(map(f));
	}

	public List<A> dropWhile(Predicate<A> predicate) {
		return match((h, t) -> predicate.test(h) ? t.dropWhile(predicate) : this, List::empty);
	}

	public List<A> filter(Predicate<A> predicate) {
		return bind(a -> predicate.test(a) ? pure(a) : empty());
	}

	@Override
	public <X> X foldLeft(X startValue, BiFunction<X, A, X> f) {
		return match((head, tail) -> f.apply(tail.foldLeft(startValue, f), head), () -> startValue);
	}

	public A get(int index) {
		return index < 0 ? Bottom.error("negative index")
				: match((h, t) -> index > 0 ? t.get(index - 1) : h, () -> Bottom.error("index too large"));
	}

	public A head() {
		return match((head, tail) -> head, Bottom::undefined);
	}

	@Override
	public List<A> identity() {
		return empty();
	}

	public boolean isEmpty() {
		return match((h, t) -> false, () -> true);
	}

	public <B> List<B> map(Function<A, B> f) {
		return match((h, t) -> cons(f.apply(h), t.map(f)), List::empty);
	}

	public <X> X match(BiFunction<A, List<A>, X> matchCons, Supplier<X> matchEmpty) {
		return constructor.match(matchCons, matchEmpty);
	}

	public List<A> takeWhile(Predicate<A> predicate) {
		return match((h, t) -> predicate.test(h) ? cons(h, t.takeWhile(predicate)) : empty(), List::empty);
	}

	public String toString() {
		return "[" + inside() + "]";
	}

	public Option<A> tryGet(int index) {
		return index < 0 ? Option.none()
				: match((h, t) -> index > 0 ? t.tryGet(index - 1) : Option.some(h), Option::none);
	}

	public Option<A> tryHead() {
		return match((head, tail) -> Option.some(head), Option::none);
	}

	public Option<List<A>> tryTail() {
		return match((head, tail) -> Option.some(tail), Option::none);
	}

	private String inside() {
		return match((h, t) -> t.isEmpty() ? h.toString() : h + ", " + t.inside(), () -> "");
	}

}
