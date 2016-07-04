package fun;

import java.util.function.Function;
import java.util.function.Supplier;

public abstract class IO<A> {

	public static <A> IO<A> wrap(Supplier<A> operation) {
		return new IO<A>() {
			public A unsafeExec() {
				return operation.get();
			}
		};
	}

	private IO() {
	}

	public <B> IO<B> andThen(IO<B> x) {
		return bind(Functions.constant(x));
	}

	public <B> IO<B> bind(Function<A, IO<B>> f) {
		return f.apply(unsafeExec());
	}

	public <B> IO<B> map(Function<A, B> f) {
		return new IO<B>() {
			public B unsafeExec() {
				return f.apply(IO.this.unsafeExec());
			}
		};
	}

	@Override
	public String toString() {
		return "<<IO action>>";
	}

	public abstract A unsafeExec();
}
