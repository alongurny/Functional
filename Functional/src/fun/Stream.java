package fun;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import fun.algebra.Monoid;

public abstract class Stream<A> implements Foldable<A>, Monoid<Stream<A>> {

	public static <A> Stream<A> cons(Supplier<A> head, Supplier<Stream<A>> tail) {
		return new Stream<A>() {
			@Override
			public <X> X match(BiFunction<A, Stream<A>, X> matchCons, Supplier<X> matchEmpty) {
				return matchCons.apply(head.get(), tail.get());
			}
		};
	}

	public static <A> Stream<A> empty() {
		return new Stream<A>() {
			@Override
			public <X> X match(BiFunction<A, Stream<A>, X> matchCons, Supplier<X> matchEmpty) {
				return matchEmpty.get();
			}
		};
	}

	public static Stream<Integer> inclusiveRange(int start, int end) {
		return start > end ? empty() : cons(() -> start, () -> inclusiveRange(start + 1, end));
	}

	public static <A> Stream<A> join(Stream<Stream<A>> list) {
		return Monoid.concat(list);
	}

	public static <A> Stream<A> pure(A value) {
		return cons(() -> value, Stream::empty);
	}

	public static Stream<Integer> range(int start, int end) {
		return start >= end ? empty() : cons(() -> start, () -> inclusiveRange(start + 1, end));
	}

	@SafeVarargs
	public static <A> Stream<A> stream(A... arr) {
		return stream(0, arr);
	}

	private static <A> Stream<A> stream(int i, A[] arr) {
		return i >= arr.length ? empty() : cons(() -> arr[i], () -> stream(i + 1, arr));
	}

	private Stream() {
	}

	@Override
	public Stream<A> append(Stream<A> other) {
		return match((h, t) -> cons(() -> h, () -> t.append(other)), () -> other);
	}

	public <B> Stream<B> bind(Function<A, Stream<B>> f) {
		return join(map(f));
	}

	public Stream<A> dropWhile(Predicate<A> predicate) {
		return match((h, t) -> predicate.test(h) ? t.dropWhile(predicate) : this, Stream::empty);
	}

	public Stream<A> filter(Predicate<A> predicate) {
		return bind(a -> predicate.test(a) ? pure(a) : empty());
	}

	@Override
	public <X> X foldLeft(X startValue, BiFunction<X, A, X> f) {
		return Recursion.recurse(startValue, this, (x, t) -> f.apply(x, t.head()), Stream::tail, Stream::isEmpty);
	}

	public Stream<A> tail() {
		return tryTail().get();
	}

	public A get(int index) {
		return index < 0 ? Bottom.error("negative index")
				: match((h, t) -> index > 0 ? t.get(index - 1) : h, () -> Bottom.error("index too large"));
	}

	public A head() {
		return match((head, tail) -> head, Bottom::undefined);
	}

	@Override
	public Stream<A> identity() {
		return empty();
	}

	public boolean isEmpty() {
		return match((h, t) -> false, () -> true);
	}

	public <B> Stream<B> map(Function<A, B> f) {
		return match((h, t) -> cons(() -> f.apply(h), () -> t.map(f)), Stream::empty);
	}

	public abstract <X> X match(BiFunction<A, Stream<A>, X> ifCons, Supplier<X> ifEmpty);

	public Stream<A> takeWhile(Predicate<A> predicate) {
		return match((h, t) -> predicate.test(h) ? cons(() -> h, () -> t.takeWhile(predicate)) : empty(),
				Stream::empty);
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

	public Option<Stream<A>> tryTail() {
		return match((head, tail) -> Option.some(tail), Option::none);
	}

	private String inside() {
		return match((h, t) -> t.isEmpty() ? h.toString() : h + ", " + t.inside(), () -> "");
	}

}
