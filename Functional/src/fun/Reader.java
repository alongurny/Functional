package fun;

import java.util.function.Function;

public abstract class Reader<R, A> {

	public static <R, A> Reader<R, A> pure(A value) {
		return new Reader<R, A>() {
			@Override
			public A run(R env) {
				return value;
			}
		};
	}

	public static <R, A> Reader<R, A> reader(Function<R, A> f) {
		return new Reader<R, A>() {
			@Override
			public A run(R env) {
				return f.apply(env);
			}
		};
	}

	private Reader() {
	}

	public <B> Reader<R, B> bind(Function<A, Reader<R, B>> f) {
		return new Reader<R, B>() {
			@Override
			public B run(R env) {
				return f.apply(Reader.this.run(env)).run(env);
			}
		};
	}

	public abstract A run(R env);

}
