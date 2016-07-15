package fun;

import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class Recursion {

	public static <X, T> X recurse(X startValue, T startStructure, BiFunction<X, T, X> f, UnaryOperator<T> nextStruct,
			Predicate<T> base) {
		X value = startValue;
		T struct = startStructure;
		while (!base.test(struct)) {
			value = f.apply(value, struct);
			struct = nextStruct.apply(struct);
		}
		return value;
	}

}
