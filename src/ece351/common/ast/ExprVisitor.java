package ece351.common.ast;

import ece351.common.ast.AndExpr;
import ece351.common.ast.BinaryExpr;
import ece351.common.ast.ConstantExpr;
import ece351.common.ast.EqualExpr;
import ece351.common.ast.Expr;
import ece351.common.ast.NAndExpr;
import ece351.common.ast.NOrExpr;
import ece351.common.ast.NotExpr;
import ece351.common.ast.OrExpr;
import ece351.common.ast.UnaryExpr;
import ece351.common.ast.VarExpr;
import ece351.common.ast.XNOrExpr;
import ece351.common.ast.XOrExpr;

public abstract class ExprVisitor {
	public abstract Expr visit(ConstantExpr e);
	public abstract Expr visit(VarExpr e);
	public abstract Expr visit(NotExpr e);
	public abstract Expr visit(AndExpr e);
	public abstract Expr visit(OrExpr e);
	public abstract Expr visit(XOrExpr e);
	public abstract Expr visit(NAndExpr e);
	public abstract Expr visit(NOrExpr e);
	public abstract Expr visit(XNOrExpr e);
	public abstract Expr visit(EqualExpr e);
	public abstract Expr visit(NaryAndExpr e);
	public abstract Expr visit(NaryOrExpr e);


	public Expr traverse(final Expr e) {
		if (e instanceof BinaryExpr) {
			return traverse( (BinaryExpr) e );
		} else if (e instanceof NotExpr) {
			return traverse( (NotExpr) e );
		} else {
			return e.accept(this);
		}
	}
	
	public abstract Expr traverse(final BinaryExpr e);
	public abstract Expr traverse(final UnaryExpr e);
		
	
}
