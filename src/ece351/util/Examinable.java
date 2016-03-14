package ece351.util;

/**
 * Defines three different kinds of equivalence comparisons: computational
 * substitutability (equals), semantic equivalence (equivalent), and
 * structural/syntactic equivalence (isomorphic).
 * 
 * @author drayside
 *
 */
public interface Examinable {

	/**
	 * Compares to objects to see if they can be substituted for each other in the
	 * computation [Liskov]. Should only ever be true of immutable objects.
	 * 
	 * @param obj
	 * @return
	 */
	@Override
	public boolean equals(Object obj);
	
	/**
	 * Structurally (syntactically) the same. Perhaps the ordering of some things has
	 * changed, but otherwise these two objects represent the same AST.
	 * 
	 * @param obj
	 * @return
	 */
	public boolean isomorphic(Examinable obj);
	
	/**
	 * Semantically the same, even if syntactically different.
	 * @param obj
	 * @return
	 */
	public boolean equivalent(Examinable obj);
	
}
