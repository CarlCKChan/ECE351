package ece351.common.ast;

import ece351.vhdl.VConstants;


public final class OrExpr extends CommutativeBinaryExpr{
	
	public OrExpr(Expr left, Expr right) {
		super(left,right);
	}
	public OrExpr() {this(null, null);}
    
    public Expr accept(final ExprVisitor v){
    	return v.visit(this);
    }

	@Override
	public Expr standardize() {
		return new NaryOrExpr(left, right).standardize();
	}
	@Override
	public String operator() {
		return VConstants.OR;
	}
	@Override
	public BinaryExpr newBinaryExpr(final Expr left, final Expr right) {
		return new OrExpr(left, right);
	}
}
