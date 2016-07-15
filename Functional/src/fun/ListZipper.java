package fun;

import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class ListZipper<A> {
	public static <A> ListZipper<A> fromList(List<A> list) {
		return zipper(list, List.empty());
	}

	public static <A> ListZipper<A> zipper(List<A> left, List<A> right) {
		return new ListZipper<A>() {
			@Override
			public <X> X match(BiFunction<List<A>, List<A>, X> pattern) {
				return pattern.apply(left, right);
			}
		};
	}

	private ListZipper() {
	}

	public Option<A> getFocus() {
		return getLeft().tryHead();
	}

	public List<A> getLeft() {
		return match((left, right) -> left);
	}

	public List<A> getRight() {
		return match((left, right) -> right);
	}

	public <B> ListZipper<B> map(Function<A, B> f) {
		return match((left, right) -> zipper(left.map(f), right.map(f)));
	}

	public abstract <X> X match(BiFunction<List<A>, List<A>, X> pattern);

	public Option<ListZipper<A>> moveLeft() {
		return getRight().match((h, t) -> Option.some(zipper(List.cons(h, getLeft()), t)), Option::none);
	}

	public Option<ListZipper<A>> moveRight() {
		return getLeft().match((h, t) -> Option.some(zipper(t, List.cons(h, getRight()))), Option::none);
	}

	@Override
	public String toString() {
		return match((left, right) -> String.format("zipper(%s, %s)", left, right));
	}

}
