package ece351.common.ast;

import java.util.Set;

import ece351.util.Examinable;
import ece351.util.CommandLine.FSimplifierOptions;
import ece351.vhdl.ast.Statement;

public final class AssignmentStatement extends Statement implements Examinable {

	public final VarExpr outputVar;
	public final Expr expr;
	
	public AssignmentStatement(String var, Expr expr)
	{
		this.outputVar = new VarExpr(var);
		this.expr = expr;
		assert repOk();
	}
	
	public AssignmentStatement(VarExpr var, Expr expr) {
		this.outputVar = var;
		this.expr = expr;
		assert repOk();
	}

	public boolean repOk() {
		assert outputVar != null;
		assert expr != null : "expr is null for " + outputVar;
		return true;
	}
	
    @Override
    public String toString() {
        return outputVar + " <= " + expr + ";" + System.getProperty("line.separator");
    }

	@Override
	public boolean equals(final Object obj) {
		// basics
		if (obj == null) return false;
		if (!obj.getClass().equals(getClass())) return false;
		final AssignmentStatement that = (AssignmentStatement) obj;

		// compare fields
		if (!this.outputVar.equals(that.outputVar)) return false;
		if (!this.expr.equals(that.expr)) return false;

		// no significant differences
		return true;
	}

	@Override
	public boolean isomorphic(final Examinable obj) {
		// basics
		if (obj == null) return false;
		if (!obj.getClass().equals(this.getClass())) return false;
		final AssignmentStatement that = (AssignmentStatement) obj;
		
		return( this.outputVar.equals(that.outputVar) && this.expr.isomorphic(that.expr) );
	}

	@Override
	public boolean equivalent(final Examinable obj) {
		return isomorphic(obj);
	}
	
	public AssignmentStatement simplify(final Set<FSimplifierOptions> opts) {
		return new AssignmentStatement(outputVar, expr.simplify(opts));
	}

	public AssignmentStatement standardize() {
		return new AssignmentStatement(outputVar, expr.standardize());
	}
}
