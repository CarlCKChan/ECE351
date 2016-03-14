package ece351.f;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import ece351.common.ast.*;
import ece351.f.ast.*;


public final class DetermineInputVars extends PostOrderFExprVisitor {
	private final Set<String> inputVars = new LinkedHashSet<String>();
	private DetermineInputVars(final AssignmentStatement f) { traverse(f.expr); }
	public static Set<String> inputVars(final AssignmentStatement f) {
		final DetermineInputVars div = new DetermineInputVars(f);
		return Collections.unmodifiableSet(div.inputVars);
	}
	public static Set<String> inputVars(final FProgram p) {
		final Set<String> vars = new TreeSet<String>();
		for (final AssignmentStatement f : p.formulas) {
			vars.addAll(DetermineInputVars.inputVars(f));
		}
		return Collections.unmodifiableSet(vars);
	}
	@Override public Expr visit(final ConstantExpr e) { return e; }
	@Override public Expr visit(final VarExpr e) { inputVars.add(e.identifier); return e; }
	@Override public Expr visit(final NotExpr e) { return e; }
	@Override public Expr visit(final AndExpr e) { return e; }
	@Override public Expr visit(final OrExpr e) { return e; }
	@Override public Expr visit(final XOrExpr e) { return e; }
	@Override public Expr visit(final NAndExpr e) { return e; }
	@Override public Expr visit(final NOrExpr e) { return e; }
	@Override public Expr visit(final XNOrExpr e) { return e; }
	@Override public Expr visit(final EqualExpr e) { return e; }
	@Override public Expr visit(NaryAndExpr e) { return e; }
	@Override public Expr visit(NaryOrExpr e) { return e; }
}
