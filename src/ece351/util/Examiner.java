package ece351.util;

import java.util.BitSet;
import java.util.List;

import org.parboiled.common.ImmutableList;

public enum Examiner {

	Equals {
		public boolean examine(final Examinable a, final Examinable b) {
			if (a == null && b == null) return true;
			if (a == null) return false;
			if (b == null) return false;
			return a.equals(b);
		}

	},
	
	Equivalent {
		public boolean examine(final Examinable a, final Examinable b) {
			if (a == null && b == null) return true;
			if (a == null) return false;
			if (b == null) return false;
			return a.equivalent(b);
		}
	},

	Isomorphic {
		public boolean examine(final Examinable a, final Examinable b) {
			if (a == null && b == null) return true;
			if (a == null) return false;
			if (b == null) return false;
			return a.isomorphic(b);
		}
	};
	
	public abstract boolean examine(final Examinable a, final Examinable b);

	/**
	 * Returns true if the two lists have the same elements 
	 * in the same order.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T extends Examinable> boolean orderedExamination(final Examiner e, final List<T> a, final List<T> b) {
		if (a == null && b == null) return true;
		if (a == null) return false;
		if (b == null) return false;
		// now we know that both are not null
		final int size = b.size();
		if (a.size() != size) return false;
		for (int i = 0; i < size; i++) {
			if (!e.examine(a.get(i), b.get(i))) {
				return false;
			}
		}
		// no significant differences found
		return true;
	}
	
	/**
	 * Returns true if the two lists have the same elements, 
	 * possibly in a different order.
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T extends Examinable> boolean unorderedExamination(final Examiner e, final List<T> a, final List<T> b) {
		if (a == null && b == null) return true;
		if (a == null) return false;
		if (b == null) return false;
		// now we know that both are not null
		final int size = b.size();
		if (a.size() != size) return false;
		final BitSet bits = new BitSet(size);
		for (final T x : a) {
			boolean matched = false;
			for (int i = 0; i < size; i++) {
				if (!bits.get(i)) {
					// we haven't matched this index yet, try to do so now
					final Examinable y = b.get(i);
					if (e.examine(x, y)) {
						// matched!
						matched = true;
						bits.set(i);
						break;
					}
				}
			}
			if (!matched) return false;
		}
		return true;
	}
	
	/**
	 * Wrapper for types that do not extend Examinable.
	 * For example, could be used to compare two lists of strings.
	 * If an ordered equality of lists is needed, just call list.equals(otherlist).
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T> boolean unorderedEquals(final List<T> a, final List<T> b) {
		ImmutableList<EqualsExaminer<T>> la = ImmutableList.of();
		ImmutableList<EqualsExaminer<T>> lb = ImmutableList.of();
		for (final T item : a) { la = la.append(new EqualsExaminer<T>(item)); }
		for (final T item : b) { lb = lb.append(new EqualsExaminer<T>(item)); }
		return unorderedExamination(Examiner.Equals, la, lb);
	}

	/**
	 * You could use this method, or just call list.equals(otherList)
	 * @param a
	 * @param b
	 * @return
	 */
	public static <T> boolean orderedEquals(final List<T> a, final List<T> b) {
		ImmutableList<EqualsExaminer<T>> la = ImmutableList.of();
		ImmutableList<EqualsExaminer<T>> lb = ImmutableList.of();
		for (final T item : a) { la = la.append(new EqualsExaminer<T>(item)); }
		for (final T item : b) { lb = lb.append(new EqualsExaminer<T>(item)); }
		return orderedExamination(Examiner.Equals, la, lb);
	}
	
	private static class EqualsExaminer<T> implements Examinable {
		private final T item;
		
		public EqualsExaminer (final T item) { this.item = item; }
		
		@Override
		public boolean equals(final Object obj) {
			if (obj == null) return false;
			if (!obj.getClass().equals(this.getClass())) return false;
			final EqualsExaminer<?> that = (EqualsExaminer<?>) obj;
			return this.item.equals(that.item);
		}
		@Override
		public boolean isomorphic(Examinable obj) { return equals(obj); }
		@Override
		public boolean equivalent(Examinable obj) { return equals(obj);	}
	}

}
