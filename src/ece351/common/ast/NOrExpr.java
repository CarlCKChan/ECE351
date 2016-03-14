package ece351.common.ast;

import ece351.vhdl.VConstants;

public final class NOrExpr extends CommutativeBinaryExpr {

	public NOrExpr(Expr left, Expr right) {
		super(left,right);
	}
    
    public Expr accept(final ExprVisitor v){
    	return v.visit(this);
    }

	@Override
	public String operator() {
		return VConstants.NOR;
	}
	@Override
	public BinaryExpr newBinaryExpr(final Expr left, final Expr right) {
		return new NOrExpr(left, right);
	}
}
