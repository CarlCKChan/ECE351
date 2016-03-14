package ece351.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public final class TestInputs351 {

	public static Collection<Object[]> formulaFiles() {
		return formulaFiles(".*");
	}
	
	public static Collection<Object[]> formulaFiles(String filter) {
		filter = "^" + filter + "\\.f$";
		return oneD2twoD(Utils351.files("tests/f", filter));
	}
	
	public static Collection<Object[]> badFormulaFiles() {
		return oneD2twoD(Utils351.files("tests/f/ungrammatical", "^.*\\.f$"));
	}
	
	public static Collection<Object[]> waveFiles() {
		return oneD2twoD(Utils351.files("tests/wave", "^.*\\.wave$"));
	}
	
	public static Collection<Object[]> badWaveFiles() {
		return oneD2twoD(Utils351.files("tests/wave", "^.*\\.badwave$"));
	}

	public static Collection<Object[]> studentSVGfiles() {
		return oneD2twoD(Utils351.files("tests/wave/student.out", "^.*\\.svg$"));
	}
	
	public static Collection<Object[]> vhdlFiles() {
		return vhdlFiles(".*");
	}
	
	public static Collection<Object[]> vhdlFiles(String filter) {
		filter = "^" + filter + "\\.vhd$";
		return oneD2twoD(Utils351.files("tests/vhdl", filter));
	}
	
	public static Collection<Object[]> badVhdlFiles() {
		return oneD2twoD(Utils351.files("tests/vhdl/ungrammatical", "^.*\\.vhd$"));
	}
	
	public static Collection<Object[]> badDefVhdlFiles() {
		return oneD2twoD(Utils351.files("tests/vhdl/undefined-var", "^.*\\.vhd$"));
	}
	
	public static Collection<Object[]> desugaredVhdlFiles() {
		return oneD2twoD(Utils351.files("tests/vhdl/staff.out/desugared", "^.*\\.vhd$"));
	}
	
	public static Collection<Object[]> elaboratedVhdlFiles() {
		return oneD2twoD(Utils351.files("tests/vhdl/staff.out/elaborated", "^.*\\.vhd$"));
	}
	
	public static Collection<Object[]> processSplitVhdlFiles() {
		return oneD2twoD(Utils351.files("tests/vhdl/staff.out/split", "^.*\\.vhd$"));
	}
	
	public static Collection<Object[]> synthesizedFFiles() {
		return oneD2twoD(Utils351.files("tests/vhdl/staff.out/synthesize", "^.*\\.f$"));
	}
	

	public static Collection<Object[]> oneD2twoD(final Object[] in) {
		final Collection<Object[]> out = new ArrayList<Object[]>(in.length);
		for (final Object obj : in) {
			out.add(new Object[] {obj});
		}
		return out;
	}

	
	public final static boolean knownIsomorphicFormulas(final String f1, final String f2) {
		final Set<String> s = ISOMORPHIC_FORMULAS.get(f1);
		if (s == null) {
			return false;
		} else {
			return s.contains(f2);
		}
	}
	
	public final static boolean knownEquivalentFormulas(final String f1, final String f2) {
		final Set<String> s = EQUIVALENT_FORMULAS.get(f1);
		if (s == null) {
			return false;
		} else {
			return s.contains(f2);
		}
	}
	
	public final static Map<String,Set<String>> ISOMORPHIC_FORMULAS;
	public final static Map<String,Set<String>> EQUIVALENT_FORMULAS;

	static {
		final Map<String,Set<String>> m = new TreeMap<String,Set<String>>();

		// isomorphic
		m.put("ex02.f", s("ex04.f"));
		m.put("ex05.f", s("ex11.f"));
		m.put("opt0-left-parens.f", s("opt0-no-parens.f"));
		m.put("opt1-and-false1.f", s("opt1-and-false2.f"));
		m.put("opt1-and-true1.f", s("opt1-and-true2.f"));
		m.put("opt1-false-and-true.f", s("opt1-true-and-false.f"));
		m.put("opt1-false-or-true.f", s("opt1-true-or-false.f"));
		m.put("opt1-or-false1.f", s("opt1-or-false2.f"));
		m.put("opt1-or-true1.f", s("opt1-or-true2.f"));
		m.put("opt2-and1.f", s("opt2-and2.f"));
		m.put("opt2-or1.f", s("opt2-or2.f"));
		m.put("opt4-and-or.f", s("opt4-and-or2.f"));
		m.put("opt4-or-no-paren.f", s("opt4-or-and.f"));
		m.put("z02.f", s("z06.f", "z07.f", "z08.f"));
		m.put("z10.f", s("z11.f", "z12.f"));
		invert(m);
		ISOMORPHIC_FORMULAS = freeze(m);

		// equivalent
		madd(m, "ex00.f", s(
				"opt1-and-false1.f",
				"opt1-false-and-false.f",
				"opt1-false-and-true.f",
				"opt1-not-true-and-false.f",
				"opt1-not-true-or-false.f",
				"opt1-not-true.f",
				"opt1-false-or-false.f",
				"opt2-and1.f"
				));
		madd(m, "ex01.f", s(
				"opt1-false-or-true.f",
				"opt1-not-false-and-true.f",
				"opt1-not-false-or-false.f",
				"opt1-not-false-or-true.f",
				"opt1-not-false.f",
				"opt1-or-true1.f",
				"opt1-true-and-true.f",
				"opt1-true-or-true.f",
				"opt2-or1.f"
				));
		madd(m, "ex02.f", s(
				"opt1-and-true1.f",
				"opt1-or-false1.f",
				"opt3-and-dup.f",
				"opt3-or-dup.f",
				"opt4-and-or.f",
				"opt4-or-and.f",
				"nary-or.f"
				));
		madd(m, "ex03.f", s(
				"opt5-not-and.f"
				));
		madd(m, "ex05.f", s(
				"opt4-big2.f"
				));
		madd(m, "opt0-left-parens.f", s(
				"opt0-right-parens.f",
				"opt5-fixed-point.f"
				));
		invert(m);
		EQUIVALENT_FORMULAS = freeze(m);
	}

	private static Map<String,Set<String>> freeze(final Map<String,Set<String>> m) {
		final Map<String,Set<String>> n = new TreeMap<String,Set<String>>();
		for (final Map.Entry<String, Set<String>> e : m.entrySet()) {
			final Set<String> s = new TreeSet<String>(e.getValue());
			n.put(e.getKey(), s);
		}
		return Collections.unmodifiableMap(n);
	}
	
	private static void madd(final Map<String, Set<String>> m, final String s, final Set<String> ss) {
		final Set<String> existingSet = m.get(s);
		if (existingSet == null) {
			m.put(s, ss);
		} else {
			existingSet.addAll(ss);
		}
	}
	
	private static void invert(final Map<String, Set<String>> m) {
		final Map<String,Set<String>> toAdd = new TreeMap<String,Set<String>>();

		// make sure the inverse keys exist
		for (final Map.Entry<String, Set<String>> e : m.entrySet()) {
			final String k = e.getKey();
			final Set<String> s = e.getValue();
			for (final String k2 : s) {
				if (!m.containsKey(k2)) {
					toAdd.put(k2, s(k));
				}
			}
		}
		m.putAll(toAdd);

		// make sure the inverse sets are complete
		for (final Map.Entry<String, Set<String>> e : m.entrySet()) {
			final String k = e.getKey();
			final Set<String> s = e.getValue();
			for (final String k2 : s) {
				final Set<String> s2 = m.get(k2);
				s2.add(k);
				s2.addAll(s);
			}
		}
	}

	private static Set<String> s(final String...args) {
		final Set<String> s = new TreeSet<String>();
		for (final String a : args) {
			s.add(a);
		}
		return s;
	}

}
