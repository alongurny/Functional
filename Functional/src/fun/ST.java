package fun;

import java.util.concurrent.Callable;
import java.util.function.Function;

public abstract class ST<S, A> {

	public interface STSupplier<A> {
		<S> ST<S, A> get();
	}

	public static <A> A run(STSupplier<A> supplier) {
		return supplier.get().run();
	}

	public static <S, A> ST<S, A> join(ST<S, ST<S, A>> action) {
		return new ST<S, A>() {
			@Override
			public A run() {
				return action.run().run();
			}
		};
	}

	public static <S, A> ST<S, A> unit(A value) {
		return wrap(() -> value);
	}

	public static <S> ST<S, Unit> when(ST<S, Boolean> condition, ST<S, ?> action) {
		return condition.bind(b -> b ? action.makeVoid() : unit(Unit.UNIT));
	}

	public static <S, A> ST<S, A> wrap(Callable<A> operation) {
		return new ST<S, A>() {
			public A run() {
				try {
					return operation.call();
				} catch (Exception e) {
					return Bottom.error(e.getMessage());
				}
			}
		};
	}

	private ST() {
	}

	public <B> ST<S, B> andThen(ST<S, B> x) {
		return bind(Functions.constant(x));
	}

	public <B> ST<S, B> bind(Function<A, ST<S, B>> f) {
		return join(map(f));
	}

	public ST<S, Unit> makeVoid() {
		return andThen(unit(Unit.UNIT));
	}

	public <B> ST<S, B> map(Function<A, B> f) {
		return new ST<S, B>() {
			public B run() {
				return f.apply(ST.this.run());
			}
		};
	}

	@Override
	public String toString() {
		return "<<ST action>>";
	}

	protected abstract A run();
}
