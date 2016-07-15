package fun.test;

import java.util.function.Supplier;

public class RunTest {

	private long repeats;
	private long avg;
	private long min;
	private long max;
	private long minIndex;
	private long maxIndex;

	public static void debug(Supplier<?> supplier, long repeats) {
		System.out.println(get(supplier, repeats));
	}

	public static RunTest get(Supplier<?> supplier, long repeats) {
		long counter = 0;
		long minIndex = 0, maxIndex = 0;
		long min = Long.MAX_VALUE, max = 0, sum = 0;
		while (counter++ < repeats) {
			long start = System.nanoTime();
			supplier.get();
			long end = System.nanoTime();
			long diff = end - start;
			if (diff < min) {
				min = diff;
				minIndex = counter;
			}
			if (diff > max) {
				max = diff;
				maxIndex = counter;
			}
			sum += diff;
		}
		return new RunTest(repeats, sum / repeats, min, max, minIndex, maxIndex);
	}

	@Override
	public String toString() {
		return String.format("Results: average[tests:%d]=%d, min[index:%d]=%d, max[index:%d]=%d", repeats, avg,
				minIndex, min, maxIndex, max);
	}

	public RunTest(long repeats, long avg, long min, long max, long minIndex, long maxIndex) {
		this.repeats = repeats;
		this.avg = avg;
		this.min = min;
		this.max = max;
		this.minIndex = minIndex;
		this.maxIndex = maxIndex;
	}

}
