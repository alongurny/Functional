package fun;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Action {
	public static Supplier<Unit> wrap(Runnable runnable) {
		return () -> {
			runnable.run();
			return Unit.UNIT;
		};
	}

	public static <A> Function<A, Unit> wrap(Consumer<A> consumer) {
		return a -> {
			consumer.accept(a);
			return Unit.UNIT;
		};
	}
}
