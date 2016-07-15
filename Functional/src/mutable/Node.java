package mutable;

import fun.Option;

public class Node<T> {

	private T value;
	private Option<Node<T>> next;

	public Node(T value, Option<Node<T>> next) {
		this.value = value;
		this.next = next;
	}

	public Option<Node<T>> getNext() {
		return next;
	}

	public T getValue() {
		return value;
	}

}
