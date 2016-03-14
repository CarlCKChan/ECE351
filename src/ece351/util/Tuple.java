package ece351.util;

/*
 * Generic tuple.*/
public final class Tuple<X, Y> {
	public final X x;
	public final Y y;

	public Tuple(final X x, final Y y) {
		this.x = x;
		this.y = y;
	}
}
