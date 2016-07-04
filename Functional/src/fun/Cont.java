package fun;

import java.util.function.Function;

public abstract class Cont<R, A> {

	public static <R, A> Cont<R, A> cont(Function<Function<A, R>, R> cont) {
		return new Cont<R, A>() {
			@Override
			public R run(Function<A, R> f) {
				return cont.apply(f);
			}
		};
	}

	public static <R, A> Cont<R, A> join(Cont<R, Cont<R, A>> c) {
		return cont(f -> c.run(d -> d.run(f)));
	}

	public static <R, A> Cont<R, A> pure(A value) {
		return new Cont<R, A>() {
			@Override
			public R run(Function<A, R> f) {
				return f.apply(value);
			}
		};
	}

	private Cont() {
	}

	public <B> Cont<R, B> andThen(Cont<R, B> c) {
		return bind(x -> c);
	}

	public <B> Cont<R, B> bind(Function<A, Cont<R, B>> f) {
		return join(map(f));
	}

	public <B> Cont<R, B> map(Function<A, B> f) {
		return cont(g -> run(g.compose(f)));
	}

	public abstract R run(Function<A, R> f);
}
