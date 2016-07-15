package fun;

import java.util.function.UnaryOperator;

public class IORef<A> {

	public static <A> IO<IORef<A>> create(A value) {
		return IO.wrap(() -> new IORef<A>(value));
	}

	private A value;

	private IORef(A value) {
		this.value = value;
	}

	public IO<A> get() {
		return IO.pure(value);
	}

	public IO<Unit> modify(UnaryOperator<A> operator) {
		return IO.wrap(() -> {
			this.value = operator.apply(this.value);
			return Unit.UNIT;
		});
	}

	public IO<Unit> set(A value) {
		return IO.wrap(() -> {
			this.value = value;
			return Unit.UNIT;
		});
	}

}
