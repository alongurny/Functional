package mutable;

import fun.Option;

public interface Stack<T> {

	default boolean isEmpty() {
		return peek().isNone();
	}

	default Option<T> peek() {
		Option<T> value = pop();
		value.ifSome(v -> push(v));
		return value;
	}

	Option<T> pop();

	void push(T value);

}
