package fun;

import java.util.function.Supplier;

public class Task<T> {

	public static <T> Task<T> init(Supplier<T> supplier) {
		return new Task<>(supplier);
	}

	private Option<T> result;

	private Task(Supplier<T> supplier) {
		result = Option.none();
		new Thread(() -> {
			T value = supplier.get();
			this.result = Option.some(value);
		}).start();
	}

	public IO<T> getResult() {
		return IO.wrap(() -> result.get());
	}

	public IO<Boolean> isFinished() {
		return IO.wrap(() -> result.isSome());
	}

}
