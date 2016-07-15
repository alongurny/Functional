package mutable;

import fun.Option;

public class NodeStack<T> implements Stack<T> {

	private Option<Node<T>> top;

	public NodeStack() {
		top = Option.none();
	}

	@Override
	public void push(T value) {
		top = Option.some(new Node<>(value, top));
	}

	@Override
	public Option<T> pop() {
		Option<T> value = top.map(Node::getValue);
		top = top.bind(Node::getNext);
		return value;
	}

	@Override
	public Option<T> peek() {
		return top.map(Node::getValue);
	}

	@Override
	public boolean isEmpty() {
		return top.isNone();
	}

}
