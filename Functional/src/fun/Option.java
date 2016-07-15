package fun;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Option<A> {

	private interface Constructor<A> {
		<X> X match(Function<A, X> matchSome, Supplier<X> matchNone);
	}

	public static <A> Option<A> join(Option<Option<A>> x) {
		return x.getOrDefault(Option::none);
	}

	public static <A> Option<A> none() {
		return new Option<>(new Constructor<A>() {
			public <X> X match(Function<A, X> matchSome, Supplier<X> matchNone) {
				return matchNone.get();
			}
		});
	}

	public static <A> Option<A> some(A value) {
		return new Option<>(new Constructor<A>() {
			public <X> X match(Function<A, X> matchSome, Supplier<X> matchNone) {
				return matchSome.apply(value);
			}
		});
	}

	private Constructor<A> constructor;

	private Option(Constructor<A> constructor) {
		this.constructor = constructor;
	}

	public <B> Option<B> bind(Function<A, Option<B>> f) {
		return join(map(f));
	}

	public A get() {
		return match((value -> value), Bottom::undefined);
	}

	public A getOrDefault(Supplier<A> defaultSupplier) {
		return match(value -> value, defaultSupplier);
	}

	public boolean isNone() {
		return match(value -> false, () -> true);
	}

	public boolean isSome() {
		return match(value -> true, () -> false);
	}

	public <X> Option<X> map(Function<A, X> f) {
		return match(value -> Option.some(f.apply(value)), Option::none);
	}

	public <X> X match(Function<A, X> matchSome, Supplier<X> matchNone) {
		return constructor.match(matchSome, matchNone);
	}

	public void ifSome(Consumer<A> matchSome) {
		constructor.match(Action.wrap(matchSome), () -> Unit.UNIT);
	}

	@Override
	public String toString() {
		return match(value -> "Option.some(" + value + ")", () -> "Option.none()");
	}

}
