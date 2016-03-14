package ece351.f.ast;

import ece351.common.ast.*;

public abstract class PostOrderFExprVisitor extends FExprVisitor {

	@Override
	public Expr traverse(final NaryExpr e) {
// TODO: 4 lines snipped
		for (final Expr c : e.children)
		{
			traverse(c);
		}
		e.accept(this);
		
		return e;
	}

	@Override
	public Expr traverse(final BinaryExpr b) {
// TODO: 3 lines snipped
		traverse(b.left);
		traverse(b.right);
		b.accept(this);
		
		return b;
	}

	@Override
	public Expr traverse(final UnaryExpr u) {
// TODO: 2 lines snipped
		traverse(u.expr);
		u.accept(this);
		
		return u;
	}
}
