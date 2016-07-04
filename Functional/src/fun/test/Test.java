package fun.test;

import fun.Bottom;
import fun.Lazy;

public class Test {
	public static void main(String[] args) {
		Lazy<Integer> x = Lazy.lazy(() -> 3 + 4);
		System.out.println(x.bind(y -> Bottom.undefined()));
	}
}
