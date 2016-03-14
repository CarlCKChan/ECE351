package ece351.util;

public final class Debug {

	private Debug() {
		throw new UnsupportedOperationException();
	}
	
	/** 
	 * Print a message if the global debug flag is on.
	 * @param s
	 */
	public static void msg(final Object s) {
		if (CommandLine.GLOBAL != null) {
			if (CommandLine.GLOBAL.debug) {
				System.err.println(s);
			}
		}
	}
	
	public static void barf() {
		throw new RuntimeException("something went wrong, probably bad input");
	}

	public static void barf(final String msg) {
		if (msg == null || msg.length() == 0) {
			barf();
		} else {
			throw new RuntimeException(msg);
		}
	}

}
