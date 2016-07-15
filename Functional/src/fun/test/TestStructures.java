package fun.test;

import mutable.NodeStack;
import mutable.Stack;

public class TestStructures {

	public static void main(String[] args) {
		Stack<Integer> s = new NodeStack<>();
		s.push(3);
		s.push(5);
		System.out.println(s);
	}
}
