package ece351.common.ast;

import ece351.util.Examinable;
import ece351.util.Examiner;

public abstract class CommutativeBinaryExpr extends BinaryExpr {

	public CommutativeBinaryExpr(final Expr left, final Expr right) {
		super(left, right);
	}

	/**
	 * Allow for arguments to be commuted (swapped).
	 * So either left=left + right=right OR
	 * left=right + right=left.
	 */
	@Override
	public final boolean isomorphic(final Examinable obj) {
		return examine(Examiner.Isomorphic, obj);
	}

	/**
	 * Allow for arguments to be commuted (swapped).
	 * So either left=left + right=right OR
	 * left=right + right=left.
	 */
	@Override
	public final boolean equivalent(final Examinable obj) {
		return examine(Examiner.Equivalent, obj);
	}

	private boolean examine(final Examiner e, final Examinable obj) {
		// basics
		if (obj == null) return false;
		if (!this.getClass().equals(obj.getClass())) return false;
		final CommutativeBinaryExpr cbe = (CommutativeBinaryExpr) obj;
		
		// compare field values, both ways, using e.examine(x,y)
		if ( e.examine(this.left, cbe.left) && e.examine(this.right, cbe.right) ) { return true; }
		else if ( e.examine(this.left, cbe.right) && e.examine(this.right, cbe.left) ) { return true; }
		else { return false; }
// TODO: 6 lines snipped
		//return false;
	}


}
