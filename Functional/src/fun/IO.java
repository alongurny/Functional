package fun;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.concurrent.Callable;
import java.util.function.Function;

public abstract class IO<A> {

	private static final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
	private static final PrintStream output = System.out;

	public static <A> IO<A> join(IO<IO<A>> action) {
		return new IO<A>() {
			@Override
			public A unsafeExec() {
				return action.unsafeExec().unsafeExec();
			}
		};
	}

	public static IO<Unit> println(Object object) {
		return wrap(() -> {
			output.println(object);
			return Unit.UNIT;
		});
	}

	public static <A> IO<A> pure(A value) {
		return wrap(() -> value);
	}

	public static IO<String> readLine() {
		return wrap(input::readLine);
	}

	public static IO<Unit> when(IO<Boolean> condition, IO<?> action) {
		return condition.bind(b -> b ? action.makeVoid() : pure(Unit.UNIT));
	}

	public static IO<Unit> whileM_(IO<Boolean> condition, IO<?> action) {
		return new IO<Unit>() {
			@Override
			public Unit unsafeExec() {
				while (condition.unsafeExec()) {
					action.unsafeExec();
				}
				return Unit.UNIT;
			}
		};
	}

	public static <A> IO<A> wrap(Callable<A> operation) {
		return new IO<A>() {
			public A unsafeExec() {
				try {
					return operation.call();
				} catch (Exception e) {
					return Bottom.error(e.getMessage());
				}
			}
		};
	}

	private IO() {
	}

	public <B> IO<B> andThen(IO<B> x) {
		return bind(Functions.constant(x));
	}

	public <B> IO<B> bind(Function<A, IO<B>> f) {
		return join(map(f));
	}

	public IO<Unit> makeVoid() {
		return andThen(pure(Unit.UNIT));
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

	public IO<Unit> whileM_(IO<Boolean> condition) {
		return whileM_(condition, this);
	}
}
