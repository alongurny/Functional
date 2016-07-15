package mutable;

import fun.Option;

public interface Queue<T> {

	Option<T> dequeue();

	void enqueue(T value);

	default boolean isEmpty() {
		return peek().isNone();
	}

	Option<T> peek();

}
