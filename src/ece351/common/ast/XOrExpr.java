package ece351.common.ast;

import ece351.vhdl.VConstants;

public final class XOrExpr extends CommutativeBinaryExpr {

	public XOrExpr(Expr left, Expr right) {
		super(left,right);
	}
    
    public Expr accept(final ExprVisitor v){
    	return v.visit(this);
    }
    
	@Override
	public String operator() {
		return VConstants.XOR;
	}
	@Override
	public BinaryExpr newBinaryExpr(final Expr left, final Expr right) {
		return new XOrExpr(left, right);
	}
}
