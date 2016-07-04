package fun;

import java.util.function.Function;
import java.util.function.Supplier;

public class Lazy<T> implements Supplier<T> {

	public static <T> Lazy<T> lazy(Supplier<T> supplier) {
		return new Lazy<>(supplier);
	}

	private Option<T> value;
	private final Supplier<T> supplier;

	private Lazy(Supplier<T> supplier) {
		this.value = Option.none();
		this.supplier = supplier;
	}

	public <S> Lazy<S> bind(Function<T, Lazy<S>> f) {
		return lazy(() -> f.apply(get()).get());
	}

	public T get() {
		if (value.isNone()) {
			value = Option.some(supplier.get());
		}
		return value.get();
	}

	public <S> Lazy<S> map(Function<T, S> f) {
		return lazy(() -> f.apply(get()));
	}

	@Override
	public String toString() {
		return "<<Lazy>>";
	}

}
