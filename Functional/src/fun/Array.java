package fun;

import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Array<T> {

	@SafeVarargs
	public static <T> Array<T> array(T... values) {
		return create(values.length, i -> values[i]);
	}

	public static <T> Array<T> create(int length, IntFunction<T> initializer) {
		return new Array<>(length, initializer);
	}

	private java.util.List<T> list;

	private Array(int length, IntFunction<T> initializer) {
		list = IntStream.range(0, length).mapToObj(initializer).collect(Collectors.toList());
	}

	public T get(int index) {
		return index < 0 ? Bottom.error("negative index")
				: index >= list.size() ? Bottom.error("index too large") : list.get(index);
	}

	public int length() {
		return list.size();
	}

	public Option<T> tryGet(int index) {
		return index >= 0 && index < list.size() ? Option.some(list.get(index)) : Option.none();
	}

}
