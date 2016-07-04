package fun;

import java.util.function.Function;

public abstract class State<S, A> {

	public static <S, A> State<S, A> pure(A a) {
		return new State<S, A>() {
			@Override
			public Pair<S, A> run(S state) {
				return Pair.pair(state, a);
			}
		};
	}

	public static <S, A> State<S, A> state(Function<S, Pair<S, A>> f) {
		return new State<S, A>() {
			@Override
			public Pair<S, A> run(S state) {
				return f.apply(state);
			}
		};
	}

	public <B> State<S, B> bind(Function<A, State<S, B>> f) {
		return new State<S, B>() {
			@Override
			public Pair<S, B> run(S state) {
				Pair<S, A> pair = State.this.run(state);
				return f.apply(pair.getSecond()).run(pair.getFirst());
			}
		};
	}

	public A eval(S state) {
		return run(state).getSecond();
	}

	public S exec(S state) {
		return run(state).getFirst();
	}

	public abstract Pair<S, A> run(S state);

}
