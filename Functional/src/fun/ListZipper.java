package fun;

import java.util.function.Function;

public abstract class ListZipper<A> {

	public interface Pattern<A, X> {
		X matchZipper(List<A> left, List<A> right);
	}

	public static <A> ListZipper<A> fromList(List<A> list) {
		return zipper(list, List.empty());
	}

	public static <A> ListZipper<A> zipper(List<A> left, List<A> right) {
		return new ListZipper<A>() {
			@Override
			public <X> X match(Pattern<A, X> pattern) {
				return pattern.matchZipper(left, right);
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

	public abstract <X> X match(Pattern<A, X> pattern);

	public Option<ListZipper<A>> moveLeft() {
		return getRight().matchCons((h, t) -> Option.some(zipper(List.cons(h, getLeft()), t)), Option::none);
	}

	public Option<ListZipper<A>> moveRight() {
		return getLeft().matchCons((h, t) -> Option.some(zipper(t, List.cons(h, getRight()))), Option::none);
	}

	@Override
	public String toString() {
		return match((left, right) -> String.format("zipper(%s, %s)", left, right));
	}

}
