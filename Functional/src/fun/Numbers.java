package fun;

public class Numbers {

	public static int factorial(int n) {
		return Foldable.product(List.inclusiveRange(1, n));
	}

}
