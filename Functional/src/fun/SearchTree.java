package fun;

import java.util.function.Function;

public abstract class SearchTree<A> {

	public interface Pattern<A, X> {
		default X matchEmpty() {
			return Bottom.undefined();
		}

		default X matchNode(A value, SearchTree<A> left, SearchTree<A> right) {
			return Bottom.undefined();
		}
	}

	public static <A extends Comparable<A>> SearchTree<A> add(SearchTree<A> tree, A addedValue) {
		return tree.match(new Pattern<A, SearchTree<A>>() {
			@Override
			public SearchTree<A> matchEmpty() {
				return node(addedValue, empty(), empty());
			}

			@Override
			public SearchTree<A> matchNode(A value, SearchTree<A> left, SearchTree<A> right) {
				return addedValue.compareTo(value) <= 0 ? node(value, add(left, addedValue), right)
						: node(value, left, add(right, addedValue));
			}
		});
	}

	public static <A> SearchTree<A> empty() {
		return new SearchTree<A>() {
			@Override
			public <R> R match(Pattern<A, R> pattern) {
				return pattern.matchEmpty();
			}
		};
	}

	public static <A extends Comparable<A>> SearchTree<A> fromList(List<A> list) {
		return list.foldLeft(empty(), SearchTree::add);
	}

	public static <A> List<A> leftOrder(SearchTree<A> tree) {
		return tree.match(new Pattern<A, List<A>>() {
			@Override
			public List<A> matchEmpty() {
				return List.empty();
			}

			@Override
			public List<A> matchNode(A value, SearchTree<A> left, SearchTree<A> right) {
				return leftOrder(left).append(List.pure(value)).append(leftOrder(right));
			}
		});
	}

	public static <A> SearchTree<A> node(A value, SearchTree<A> left, SearchTree<A> right) {
		return new SearchTree<A>() {
			@Override
			public <R> R match(Pattern<A, R> pattern) {
				return pattern.matchNode(value, left, right);
			}
		};
	}

	private SearchTree() {
	}

	public <B> SearchTree<B> map(Function<A, B> f) {
		return match(new Pattern<A, SearchTree<B>>() {
			@Override
			public SearchTree<B> matchEmpty() {
				return SearchTree.empty();
			}

			@Override
			public SearchTree<B> matchNode(A value, SearchTree<A> left, SearchTree<A> right) {
				return SearchTree.node(f.apply(value), left.map(f), right.map(f));
			}
		});
	}

	public abstract <X> X match(Pattern<A, X> pattern);

	@Override
	public String toString() {
		return toString("");
	}

	public String toString(String prefix) {
		return match(new Pattern<A, String>() {
			@Override
			public String matchEmpty() {
				return prefix + "-";
			}

			@Override
			public String matchNode(A value, SearchTree<A> left, SearchTree<A> right) {
				return prefix + value + "\n" + left.toString(prefix + " ") + "\n" + right.toString(prefix + " ");
			}
		});
	}

}
