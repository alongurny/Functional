package fun;

@FunctionalInterface
public interface Comparator<T> {
	Ordering compare(T a, T b);
}
