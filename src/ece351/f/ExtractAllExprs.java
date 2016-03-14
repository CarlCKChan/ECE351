package ece351.f;

import java.util.Set;

import kodkod.util.collections.IdentityHashSet;
import ece351.common.ast.AndExpr;
import ece351.common.ast.AssignmentStatement;
import ece351.common.ast.ConstantExpr;
import ece351.common.ast.EqualExpr;
import ece351.common.ast.Expr;
import ece351.common.ast.NAndExpr;
import ece351.common.ast.NOrExpr;
import ece351.common.ast.NaryAndExpr;
import ece351.common.ast.NaryOrExpr;
import ece351.common.ast.NotExpr;
import ece351.common.ast.OrExpr;
import ece351.common.ast.VarExpr;
import ece351.common.ast.XNOrExpr;
import ece351.common.ast.XOrExpr;
import ece351.f.ast.FProgram;
import ece351.f.ast.PostOrderFExprVisitor;

/**
 * Returns a set of all Expr objects in a given FProgram or AssignmentStatement.
 * The result is returned in an IdentityHashSet, which defines object identity
 * by memory address. A regular HashSet defines object identity by the equals()
 * method. Consider two VarExpr objects, X1 and X2, both naming variable X. If
 * we tried to add both of these to a regular HashSet the second add would fail
 * because the regular HashSet would say that it already held a VarExpr for X.
 * The IdentityHashSet, on the other hand, will hold both X1 and X2.
 */
public final class ExtractAllExprs extends PostOrderFExprVisitor {
	
	private final IdentityHashSet<Expr> exprs = new IdentityHashSet<Expr>();
	
	private ExtractAllExprs(final Expr e) { traverse(e); }
	
	public static IdentityHashSet<Expr> allExprs(final AssignmentStatement f) {
		final ExtractAllExprs cae = new ExtractAllExprs(f.expr);
		return cae.exprs;
	}
	
	public static IdentityHashSet<Expr> allExprs(final FProgram p) {
		final IdentityHashSet<Expr> allExprs = new IdentityHashSet<Expr>();
		for (final AssignmentStatement f : p.formulas) {
			allExprs.add(f.outputVar);
			allExprs.addAll(ExtractAllExprs.allExprs(f));
		}
		return allExprs;
	}
	
	@Override public Expr visit(ConstantExpr e) { exprs.add(e); return e; }
	@Override public Expr visit(VarExpr e) { exprs.add(e); return e; }
	@Override public Expr visit(NotExpr e) { exprs.add(e); return e; }
	@Override public Expr visit(AndExpr e) { exprs.add(e); return e; }
	@Override public Expr visit(OrExpr e) { exprs.add(e); return e; }
	@Override public Expr visit(XOrExpr e) { exprs.add(e); return e; }
	@Override public Expr visit(NAndExpr e) { exprs.add(e); return e; }
	@Override public Expr visit(NOrExpr e) { exprs.add(e); return e; }
	@Override public Expr visit(XNOrExpr e) { exprs.add(e); return e; }
	@Override public Expr visit(EqualExpr e) { exprs.add(e); return e; }
	@Override public Expr visit(NaryAndExpr e) { exprs.add(e); return e; }
	@Override public Expr visit(NaryOrExpr e) { exprs.add(e); return e; }
}
