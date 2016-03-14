package ece351.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Methods for testing Examinable objects.
 * 
 * @author drayside
 *
 */
public final class ExaminableProperties {

	private ExaminableProperties() {
		throw new UnsupportedOperationException();
	}
	
	public static void checkAllUnary(final Examinable e) {
		reflexive(e);
	}

	public static void checkAllBinary(final Examinable x, final Examinable y) {
		symmetric(x, y);
		equalsImpliesIsomorphic(x, y);
		isomorphicImpliesEquivalent(x, y);
	}

	public static void checkAllTernary(final Examinable x, final Examinable y, final Examinable z) {
		transitive(x, y, z);
	}

	public static void reflexive(final Examinable x) {
		assertTrue(x.equals(x));
		assertTrue(x.equivalent(x));
		assertTrue(x.isomorphic(x));
	}
	
	public static void symmetric(final Examinable x, final Examinable y) {
		assertEquals(x.equals(y), y.equals(x));
		assertEquals(x.isomorphic(y), y.isomorphic(x));
		assertEquals(x.equivalent(y), y.equivalent(x));
	}

	public static void symmetricHOF(final Examinable x, final Examinable y) {
		symmetricHOF(Examiner.Equals, x, y);
		symmetricHOF(Examiner.Isomorphic, x, y);
		symmetricHOF(Examiner.Equivalent, x, y);
	}
	
	public static void symmetricHOF(final Examiner e, final Examinable x, final Examinable y) {
		assertEquals(e.examine(x,y), e.examine(y,x));
	}
	
	public static void equalsImpliesIsomorphic(final Examinable x, final Examinable y) {
		if (x.equals(y)) {
			assertTrue(x.isomorphic(y));
			assertTrue(x.equivalent(y));
		}
	}

	public static void isomorphicImpliesEquivalent(final Examinable x, final Examinable y) {
		if (x.isomorphic(y)) {
			assertTrue(x.equivalent(y));
		}
	}

	public static void transitive(final Examinable x, final Examinable y, final Examinable z) {
		transitivityAll(Examiner.Equals, x, y, z);
		transitivityAll(Examiner.Isomorphic, x, y, z);
		transitivityAll(Examiner.Equivalent, x, y, z);
	}
	
	public static <T extends Examinable> void transitivityAll(final Examiner e, final T x, final T y, final T z) {
		// x,y,z rotations
		transitivity(e,x,y,z);
		transitivity(e,y,z,x);
		transitivity(e,z,x,y);
		// y,x,z rotations
		transitivity(e,y,x,z);
		transitivity(e,x,z,y);
		transitivity(e,z,y,x);
	}

	public static <T extends Examinable> void transitivity(final Examiner e, final T x, final T y, final T z) {
		if (e.examine(x, y)) {
			if (e.examine(y, z)) {
				assertTrue(e.examine(x, z));
			} else {
				assertFalse(e.examine(x, z));
			}
		}
	}

}
