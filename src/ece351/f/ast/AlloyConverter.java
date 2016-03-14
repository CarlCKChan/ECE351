package ece351.f.ast;

import java.util.Set;
import java.util.TreeSet;

import ece351.common.ast.AndExpr;
import ece351.common.ast.AssignmentStatement;
import ece351.common.ast.BinaryExpr;
import ece351.common.ast.ConstantExpr;
import ece351.common.ast.Expr;
import ece351.common.ast.NaryAndExpr;
import ece351.common.ast.NaryExpr;
import ece351.common.ast.NaryOrExpr;
import ece351.common.ast.NotExpr;
import ece351.common.ast.OrExpr;
import ece351.common.ast.UnaryExpr;
import ece351.common.ast.VarExpr;
import ece351.f.DetermineInputVars;

public final class AlloyConverter extends FExprVisitor {

	private final static String linesep = System.getProperty("line.separator");
	
	private final StringBuilder b = new StringBuilder();

	private AlloyConverter(final Expr e) {
		traverse(e);
	}
	
	public static String convert(final FProgram fp1, final FProgram fp2) {
		final StringBuilder m = new StringBuilder();
		
		m.append(linesep);
		// signatures
		m.append("abstract sig Boolean {}");
		m.append(linesep);
		m.append("one sig true extends Boolean {}");
		m.append(linesep);
		m.append("abstract sig Var { v : lone Boolean }");
		m.append(linesep);
		m.append("one sig ");
		final Set<String> inputVars = new TreeSet<String>();
		inputVars.addAll(DetermineInputVars.inputVars(fp1));
		inputVars.addAll(DetermineInputVars.inputVars(fp2));
		final int inputVarSize1 = inputVars.size()-1;
		int counter = 0;
		for (final String v : inputVars) {
			m.append(v);
			if (counter < inputVarSize1) {
				m.append(", ");
				counter++;
			}
		}
		if (inputVars.isEmpty()) {
			m.append("NOVARS");
		}
		m.append(" extends Var {}");
		m.append(linesep);
		
		// predicates
		pred(fp1, m, "1");
		pred(fp2, m, "2");
		
		// check
		m.append("assert equivalent {");
		m.append(linesep);
		for (final AssignmentStatement a : fp1.formulas) {
			m.append("    ");
			m.append(a.outputVar);
			m.append("1 <=> ");
			m.append(a.outputVar);
			m.append("2");
			m.append(linesep);
		}
		m.append("}");
		m.append(linesep);
		m.append("check equivalent");
		m.append(linesep);
		
		// return result
		return m.toString();
	}

	private static void pred(final FProgram fp, final StringBuilder m, final String suffix) {
		for (final AssignmentStatement a : fp.formulas) {
			m.append("pred ");
			m.append(a.outputVar);
			m.append(suffix);
			m.append("[] {");
			m.append((new AlloyConverter(a.expr)).toString());
			m.append("}");
			m.append(linesep);
		}
	}
	
	@Override
	public String toString() {
		return b.toString();
	}
	
	@Override
	public Expr visit(final ConstantExpr e) {
		if (e.b) {
			b.append(" (some Boolean) ");
		} else {
			b.append(" (no Boolean) ");
		}
		return e;
	}

	@Override
	public Expr visit(final VarExpr e) {
		b.append(" (some ");
		b.append(e.identifier);
		b.append(".v) ");
		return e;
	}

	@Override
	public Expr visit(final NotExpr e) {
		b.append(" (not ");
		e.expr.accept(this);
		b.append(") ");
		return e;
	}

	@Override
	public Expr visit(final AndExpr e) {
		helperBE(e);
		return e;
	}

	@Override
	public Expr visit(final OrExpr e) {
		helperBE(e);
		return e;
	}

	private void helperBE(final BinaryExpr e) {
		b.append(" ( ");
		e.left.accept(this);
		b.append(" ");
		b.append(e.operator());
		b.append(" ");
		e.right.accept(this);
		b.append(" ) ");
	}

	@Override
	public Expr visit(final NaryAndExpr e) {
		helperNE(e);
		return e;
	}

	private void helperNE(final NaryExpr e) {
		b.append(" ( ");
		final int size1 = e.children.size()-1;
		for (int i = 0; i < size1; i++) {
			e.children.get(i).accept(this);
			b.append(" ");
			b.append(e.operator());
			b.append(" ");
		}
		e.children.get(size1).accept(this);
		b.append(" ) ");
	}

	@Override
	public Expr visit(final NaryOrExpr e) {
		helperNE(e);
		return e;
	}

	@Override
	public Expr traverse(final NaryExpr e) {
		e.accept(this);
		return e;
	}

	@Override
	public Expr traverse(final BinaryExpr e) {
		e.accept(this);
		return e;
	}

	@Override
	public Expr traverse(final UnaryExpr e) {
		e.accept(this);
		return e;
	}
	
}