package fun;

import java.util.function.Supplier;

public class Task<T> {

	public static <T> Task<T> init(Supplier<T> supplier) {
		return new Task<>(supplier);
	}

	private Option<T> result;

	private Task(Supplier<T> supplier) {
		result = Option.none();
		new Thread(() -> this.result = Option.some(supplier.get())).start();
	}

	public IO<Option<T>> getResult() {
		return IO.wrap(() -> result);
	}

	public IO<Boolean> isDone() {
		return getResult().map(Option::isSome);
	}

}
