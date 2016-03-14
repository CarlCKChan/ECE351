package ece351.f.ast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.parboiled.common.ImmutableList;

import ece351.common.ast.AssignmentStatement;
import ece351.common.ast.VarExpr;
import ece351.util.CommandLine.FSimplifierOptions;
import ece351.util.Examinable;
import ece351.util.Examiner;
import ece351.util.RunAlloy351;


public final class FProgram implements Examinable {
	
    public final ImmutableList<AssignmentStatement> formulas;
    
    public FProgram(final List<AssignmentStatement> formulas) {
    	this.formulas = ImmutableList.copyOf(formulas);
    	assert repOk();
    }
    
    public boolean repOk() {
    	// some formulas
    	assert formulas != null;
    	assert !formulas.isEmpty();
    	// no duplicate output vars
    	assert formulas.size() == outputVars().size();
    	// TODO staff: output vars are not used as input vars?
    	// representation is ok
    	return true;
    }
    
    public void simplify() {
    	EnumSet<FSimplifierOptions> defaultOpts = EnumSet.of(FSimplifierOptions.STANDARDIZE);
    	simplify(defaultOpts);
    }
    
    public FProgram simplify(final Set<FSimplifierOptions> opts) {
    	final List<AssignmentStatement> newformulas = new ArrayList<AssignmentStatement>(formulas.size());
    	for (final AssignmentStatement f : formulas) {
    		newformulas.add(f.simplify(opts));
    	}
    	return new FProgram(newformulas);
    }
    
	public FProgram standardize() {
		final List<AssignmentStatement> newfs = new ArrayList<AssignmentStatement>(formulas.size());
    	for (final AssignmentStatement f : formulas) {
    		newfs.add(f.standardize());
    	}
    	return new FProgram(newfs);
	}

    public Set<VarExpr> outputVars() {
    	final Set<VarExpr> vars = new TreeSet<VarExpr>();
    	for (final AssignmentStatement f : formulas) {
    		vars.add(f.outputVar);
    	}
    	return Collections.unmodifiableSet(vars);
    }

    @Override
    public String toString() {
		if (formulas == null || formulas.isEmpty()) return "";
		final String sep = System.getProperty("line.separator");
    	final StringBuilder b = new StringBuilder();
    	for (final AssignmentStatement f : formulas) {
    		b.append(f.toString());
			b.append(sep);
    	}
    	return b.substring(0, b.length() - sep.length());
    }
    
	@Override
	public boolean equals(final Object obj) {
		// basics
		if (obj == null) return false;
		if (!obj.getClass().equals(this.getClass())) return false;
		final FProgram that = (FProgram) obj;
		Examiner equalExaminer = Examiner.Equals;
		
		// compare field values using Examiner.orderedExamination()
		if (Examiner.orderedExamination(equalExaminer, this.formulas, that.formulas)) {
			return true;
		} else {
			return false;
		}
			

	}
	
	@Override
	public boolean isomorphic(final Examinable obj) {
		// basics
		if (obj == null) return false;
		if (!obj.getClass().equals(this.getClass())) return false;
		final FProgram that = (FProgram) obj;
		Examiner isoExaminer = Examiner.Isomorphic;
		
		// compare field values using Examiner.unorderedExamination()
		if (Examiner.unorderedExamination(isoExaminer, this.formulas, that.formulas)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean equivalent(final Examinable obj) {
		// basics
		if (obj == null) return false;
		if (!obj.getClass().equals(this.getClass())) return false;
		final FProgram that = (FProgram) obj;
		
		// check that we have the same output variables
		final int size = this.formulas.size();
		if (size != that.formulas.size()) return false;
		final Set<VarExpr> thisOutputVars = this.outputVars();
		final Set<VarExpr> thatOutputVars = that.outputVars();
		if (!thisOutputVars.equals(thatOutputVars)) return false;
		// input variables could be different, because some
		// of them might be effectively don't care
		// so don't need to check input vars
		
		// now the hard part ...
		boolean result = false;
		try {
			result = !RunAlloy351.check(AlloyConverter.convert(this, that));
		} catch (final Exception e) {
			result = isomorphic(obj);
		}
		return result;
	}
}
