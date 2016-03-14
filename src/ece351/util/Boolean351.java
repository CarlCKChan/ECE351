package ece351.util;

public final class Boolean351 {

	private Boolean351() {
		throw new UnsupportedOperationException();
	}
	
	public static boolean and(final boolean b1, final boolean b2) {
		return b1 && b2;
	}
	
	public static boolean or(final boolean b1, final boolean b2) {
		return b1 || b2;
	}
	
	public static boolean not(final boolean b) {
		return !b;
	}

	public static boolean xor(final boolean b1, final boolean b2) {
		return or(b1,b2) && not(and(b1,b2));
	}
	
	public static boolean implies(final boolean antecedant, final boolean consequent) {
		return or(antecedant, not(consequent));
	}

	public static boolean nand(final boolean b1, final boolean b2) {
		return not(and(b1,b2));
	}

	public static boolean nor(final boolean b1, final boolean b2) {
		return not(or(b1,b2));
	}
}
