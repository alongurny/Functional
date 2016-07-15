package fun;

import java.util.stream.IntStream;

public class Numbers {

	public static long streamFactorial(int n) {
		return IntStream.rangeClosed(1, n).reduce(1, (a, b) -> a * b);
	}

	public static long recursiveFactorial(int n) {
		return n == 0 ? 1 : n * recursiveFactorial(n - 1);
	}

	public static long iterativeFactorial(int n) {
		long prd = 1;
		while (n-- > 0) {
			prd *= n;
		}
		return prd;
	}
}
