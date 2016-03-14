package ece351.f.ast;

import ece351.common.ast.*;

public abstract class FExprVisitor extends ExprVisitor {

	// Any FExprVisitor subclass must implement the following methods
	public abstract Expr visit(ConstantExpr e);
	public abstract Expr visit(VarExpr e);
	public abstract Expr visit(NotExpr e);
	public abstract Expr visit(AndExpr e);
	public abstract Expr visit(OrExpr e);
	public abstract Expr visit(NaryAndExpr e);
	public abstract Expr visit(NaryOrExpr e);
	
	// F does not include these types of expressions; stubbed out
	@Override public Expr visit(XOrExpr e) { return e; }
	@Override public Expr visit(NAndExpr e) { return e; }
	@Override public Expr visit(NOrExpr e) { return e; }
	@Override public Expr visit(XNOrExpr e) { return e; }
	@Override public Expr visit(EqualExpr e) { return e; }
	
	/** 
	 * Dispatch to traverse(NaryExpr) and traverse(BinaryExpr) 
	 * and traverse(UnaryExpr). 
	 * */
	public Expr traverse(final Expr e) {
		if (e instanceof NaryExpr) {
			traverse( (NaryExpr) e );
		} else if (e instanceof BinaryExpr) {
			traverse( (BinaryExpr) e );
		} else if (e instanceof UnaryExpr) {
			traverse( (UnaryExpr) e );
		} else {
			e.accept(this);
		}
		return e;
	}
	public abstract Expr traverse(final NaryExpr e);
	public abstract Expr traverse(final BinaryExpr e);
	public abstract Expr traverse(final UnaryExpr e);

}
