package fun;

import java.util.function.Supplier;

public class LazyTask<T> {

	public static <T> LazyTask<T> init(Supplier<T> supplier) {
		return new LazyTask<>(supplier);
	}

	private Option<T> result;
	private Thread thread;
	private boolean started;

	private LazyTask(Supplier<T> supplier) {
		result = Option.none();
		thread = new Thread(() -> {
			started = true;
			this.result = Option.some(supplier.get());
		});
	}

	public T get() {
		try {
			startComputation();
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result.get();
	}

	private synchronized void startComputation() {
		if (!started) {
			started = true;
			thread.start();
		}
	}

	public IO<Option<T>> getResult() {
		return IO.wrap(() -> {
			startComputation();
			return result;
		});
	}

	public IO<Boolean> isDone() {
		return getResult().map(Option::isSome);
	}

}
