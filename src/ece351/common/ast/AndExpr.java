package ece351.common.ast;

import ece351.vhdl.VConstants;

public final class AndExpr extends CommutativeBinaryExpr {

	public AndExpr(Expr left, Expr right) {
		super(left, right);
	}

	public AndExpr() {this(null, null);}
    
    public Expr accept(final ExprVisitor v) { return v.visit(this); }
    
	@Override
	public Expr standardize() {
		return new NaryAndExpr(left, right).standardize();
	}
	@Override
	public String operator() {
		return VConstants.AND;
	}
	@Override
	public BinaryExpr newBinaryExpr(final Expr left, final Expr right) {
		return new AndExpr(left, right);
	}
}
