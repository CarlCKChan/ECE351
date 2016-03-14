package ece351.common.ast;

import ece351.util.Examinable;
import ece351.util.Examiner;

public abstract class UnaryExpr extends Expr {
	public final Expr expr;

    public UnaryExpr(final Expr e) { 
    	this.expr = e; 
		assert repOk();
    }

	@Override
	public final boolean repOk() {
		assert expr != null : "expr should not be null";
		return true;
	}

    public abstract UnaryExpr newUnaryExpr(final Expr expr);

	public Expr standardize() {
		return newUnaryExpr(this.expr.standardize());
	}

    private final boolean examine(final Examiner e, final Examinable obj) {
		// basics
		if (obj == null) return false;
		if (!obj.getClass().equals(this.getClass())) return false;
		final UnaryExpr that = (UnaryExpr) obj;
		// compare field values using e.examine(x,y)
		return ( e.examine(this.expr, that.expr) );
// TODO: 1 lines snipped
    }
    
    @Override
    public final int hashCode() {
    	return 17 + expr.hashCode();
    }

	@Override
	public final boolean equals(final Object obj) {
		if (!(obj instanceof Examinable)) return false;
		return examine(Examiner.Equals, (Examinable)obj);
	}

	@Override
	public final boolean isomorphic(final Examinable obj) {
		return examine(Examiner.Isomorphic, obj);
	}

	@Override
	public final boolean equivalent(final Examinable obj) {
		return examine(Examiner.Equivalent, obj);
	}
	
	@Override
	public final String toString() {
	    	return "( " + operator() + " ( "+this.expr+ " ) )";
	}

}
