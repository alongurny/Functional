package fun;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import fun.algebra.Monoid;

public abstract class List<A> implements Foldable<A>, Monoid<List<A>> {

	public interface Pattern<A, X> {
		default X cons(A head, List<A> tail) {
			return Bottom.undefined();
		}

		default X empty() {
			return Bottom.undefined();
		}
	}

	public static <A> List<A> cons(A head, List<A> tail) {
		return new List<A>() {
			@Override
			public <X> X match(Pattern<A, X> pattern) {
				return pattern.cons(head, tail);
			}
		};
	}

	public static <A> List<A> empty() {
		return new List<A>() {
			@Override
			public <X> X match(Pattern<A, X> pattern) {
				return pattern.empty();
			}
		};
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
		return start >= end ? empty() : cons(start, inclusiveRange(start + 1, end));
	}

	private static <A> List<A> list(int i, A[] arr) {
		if (i >= arr.length) {
			return empty();
		}
		return cons(arr[i], list(i + 1, arr));
	}

	private List() {
	}

	@Override
	public List<A> append(List<A> other) {
		return matchCons((h, t) -> cons(h, t.append(other)), () -> other);
	}

	public <B> List<B> bind(Function<A, List<B>> f) {
		return join(map(f));
	}

	public List<A> dropWhile(Predicate<A> predicate) {
		return matchCons((h, t) -> predicate.test(h) ? t.dropWhile(predicate) : this, List::empty);
	}

	public List<A> filter(Predicate<A> predicate) {
		return bind(a -> predicate.test(a) ? pure(a) : empty());
	}

	@Override
	public <X> X foldLeft(X startValue, BiFunction<X, A, X> f) {
		return matchCons((head, tail) -> f.apply(tail.foldLeft(startValue, f), head), () -> startValue);
	}

	public A get(int index) {
		return index < 0 ? Bottom.error("negative index")
				: matchCons((h, t) -> index > 0 ? t.get(index - 1) : h, () -> Bottom.error("index too large"));
	}

	public A head() {
		return match(new Pattern<A, A>() {
			@Override
			public A cons(A head, List<A> tail) {
				return head;
			}
		});
	}

	@Override
	public List<A> identity() {
		return empty();
	}

	public boolean isEmpty() {
		return matchEmpty(() -> true, () -> false);
	}

	public <B> List<B> map(Function<A, B> f) {
		return matchCons((h, t) -> cons(f.apply(h), t.map(f)), List::empty);
	}

	public abstract <X> X match(Pattern<A, X> patternMatching);

	public <X> X matchCons(BiFunction<A, List<A>, X> ifCons, Supplier<X> otherwise) {
		return match(new Pattern<A, X>() {
			public X cons(A head, List<A> tail) {
				return ifCons.apply(head, tail);
			}

			@Override
			public X empty() {
				return otherwise.get();
			}
		});
	}

	public <X> X matchEmpty(Supplier<X> ifEmpty, Supplier<X> otherwise) {
		return match(new Pattern<A, X>() {
			public X cons(A head, List<A> tail) {
				return otherwise.get();
			}

			@Override
			public X empty() {
				return ifEmpty.get();
			}
		});
	}

	public List<A> takeWhile(Predicate<A> predicate) {
		return matchCons((h, t) -> predicate.test(h) ? cons(h, t.takeWhile(predicate)) : empty(), List::empty);
	}

	public String toString() {
		return "[" + inside() + "]";
	}

	public Option<A> tryGet(int index) {
		return index < 0 ? Option.none()
				: matchCons((h, t) -> index > 0 ? t.tryGet(index - 1) : Option.some(h), Option::none);
	}

	public Option<A> tryHead() {
		return matchCons((head, tail) -> Option.some(head), Option::none);
	}

	public Option<List<A>> tryTail() {
		return matchCons((head, tail) -> Option.some(tail), Option::none);
	}

	private String inside() {
		return matchCons((h, t) -> t.isEmpty() ? h.toString() : h + ", " + t.inside(), () -> "");
	}

}
