package fun;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Option<A> {

	public interface ConsPattern<A, X> extends Pattern<A, X> {
		default X matchNone() {
			return Bottom.undefined();
		}
	}

	public interface Pattern<A, X> {
		X matchNone();

		X matchSome(A value);
	}

	public static <A> Option<A> join(Option<Option<A>> x) {
		return x.getOrDefault(Option::none);
	}

	public static <A> Option<A> none() {
		return new Option<A>() {
			@Override
			public <X> X match(Pattern<A, X> pattern) {
				return pattern.matchNone();
			}
		};
	}

	public static <A> Option<A> some(A value) {
		return new Option<A>() {
			@Override
			public <X> X match(Pattern<A, X> pattern) {
				return pattern.matchSome(value);
			}
		};
	}

	private Option() {
	}

	public <B> Option<B> bind(Function<A, Option<B>> f) {
		return join(map(f));
	}

	public A get() {
		return match(new ConsPattern<A, A>() {
			public A matchSome(A value) {
				return value;
			}
		});
	}

	public A getOrDefault(Supplier<A> defaultSupplier) {
		return matchSome(value -> value, defaultSupplier);
	}

	public boolean isNone() {
		return matchSome(value -> false, () -> true);
	}

	public boolean isSome() {
		return matchSome(value -> true, () -> false);
	}

	public <X> Option<X> map(Function<A, X> f) {
		return matchSome(value -> Option.some(f.apply(value)), Option::none);
	}

	public abstract <X> X match(Pattern<A, X> pattern);

	public <X> X matchSome(Function<A, X> ifSome, Supplier<X> otherwise) {
		return match(new Pattern<A, X>() {
			@Override
			public X matchNone() {
				return otherwise.get();
			}

			@Override
			public X matchSome(A value) {
				return ifSome.apply(value);
			}
		});
	}

	@Override
	public String toString() {
		return matchSome(value -> "Option.some(" + value + ")", () -> "Option.none()");
	}

}
