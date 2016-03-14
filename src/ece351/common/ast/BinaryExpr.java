package ece351.common.ast;

import java.util.Set;

import ece351.util.CommandLine.FSimplifierOptions;
import ece351.util.Examinable;
import ece351.util.Examiner;

public abstract class BinaryExpr extends Expr {

	public final Expr left, right;
	
	public abstract BinaryExpr newBinaryExpr(final Expr left, final Expr right);
	
	public BinaryExpr(final Expr left, final Expr right){
		this.left = left;
		this.right = right;
		assert repOk();
	}
	
	@Override
	public final boolean repOk() {
		assert left != null : "left should not be null";
		assert right != null : "right should not be null";
		return true;
	}

	@Override 
	public final String toString() {
		final StringBuilder b = new StringBuilder();
		b.append("( ");
		b.append(this.left);
		b.append(" ");
		b.append(operator());
		b.append(" ");
		b.append(this.right);
		b.append(" )");
		return b.toString();
	}
	
	public final int hashCode() {
		int hash = 17;
		hash = hash * 13 + left.hashCode();
		hash = hash * 13 + right.hashCode();
		return hash;
	}
	
	/**
	 * left equals left and right equals right.
	 */
	@Override
	public final boolean equals(final Object obj) {
		return examine(Examiner.Equals, obj);
	}

	/**
	 * Subclasses may override this if order can be changed.
	 */
	@Override
	public boolean isomorphic(final Examinable obj) {
		return examine(Examiner.Isomorphic, obj);
	}

	/**
	 * Subclasses may override this if order can be changed.
	 */
	@Override
	public boolean equivalent(final Examinable obj) {
		return examine(Examiner.Equivalent, obj);
	}

	private boolean examine(final Examiner e, final Object obj) {
		// basics
		if (obj == null) return false;
		if (!this.getClass().equals(obj.getClass())) return false;
		final BinaryExpr be = (BinaryExpr) obj;
		
		// compare field values
		if (!e.examine(left, be.left)) return false;
		if (!e.examine(right, be.right)) return false;
		
		// no differences
		return true;
	}
	
	/**
	 * Default implementation is to do nothing.
	 * @return this
	 */
	@Override
	protected final Expr simplifyOnce(final Set<FSimplifierOptions> opts) {
		return this;
	}
}
