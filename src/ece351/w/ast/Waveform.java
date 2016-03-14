package ece351.w.ast;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import org.parboiled.common.ImmutableList;

import ece351.util.Examinable;

public final class Waveform implements Examinable {
	public final String name;
	public final ImmutableList<String> bits;
	
	public Waveform(final ImmutableList<String> bits, final String name) {
		this.name = name;
		this.bits = bits;
		assert repOk();
	}

	public boolean repOk() {
		assert name != null;
		assert !name.isEmpty();
		assert bits != null;
		assert !bits.isEmpty();
		for (final String b : bits) {
			assert b != null;
			assert !b.isEmpty();
			assert LEGAL_BITS.contains(b);
		}
		return true;
	}

	public final static SortedSet<String> LEGAL_BITS;
	static {
		final SortedSet<String> s = new TreeSet<String>();
		s.add("0");
		s.add("1");		
		s.add("U");
		LEGAL_BITS = Collections.unmodifiableSortedSet(s);
	}
	
	
	@Override
	public String toString() {
		
		/** Construct the raw string and then remove all instances of 
		 *  "[", "]", and ",", which are added by bits.toString() */
		String temp = name + " : " + bits.toString() + ";";
		temp = temp.replace(",", "");
		temp = temp.replace("[", "");
		temp = temp.replace("]", "");
		
		return (temp); // TODO: Waveform.toString()		
// TODO: 1 lines snipped
	}

	/**
	 * If we override equals() then we must override hashCode().
	 */
	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 13 + name.hashCode();
		hash = hash * 13 + bits.hashCode();
		return hash;
	}

	/**
	 * Waveform objects are immutable, so compare state.
	 */
	@Override
	public boolean equals(final Object obj) {
		// basics
		if (obj == null) return false;
		if (obj == this) return true;
		if (!obj.getClass().equals(this.getClass())) return false;		
		final Waveform other = (Waveform) obj;
		assert repOk();
		assert other.repOk();
		// compare fields
		if (!name.equals(other.name)) return false;
		if (!bits.equals(other.bits)) return false;
		// no differences found, so must be same
		return true;
	}

	/**
	 * The order of the bits always matters, so we simply
	 * define isomorphic to be equals.
	 */
	@Override
	public boolean isomorphic(final Examinable obj) {
		return equals(obj);
	}

	/**
	 * Defined to be equals().
	 */
	@Override
	public boolean equivalent(final Examinable obj) {
		return equals(obj);
	}

}
