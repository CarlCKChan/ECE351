package ece351.common.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import ece351.util.CommandLine.FSimplifierOptions;
import ece351.vhdl.VConstants;

public final class NaryOrExpr extends NaryExpr {

	public NaryOrExpr(final Expr... exprs) {
		super(exprs);
	}
	
	public NaryOrExpr(final List<Expr> children) {
		super(children);
	}
	
	@Override
	public Expr accept(ExprVisitor v) { v.visit(this); return this;}

	@Override
	public String operator() {
		return VConstants.OR;
	}

	@Override
	protected String displayName() {
		return "NaryOr";
	}

	@Override
	protected Expr simplifySelf(final Set<FSimplifierOptions> opts) {
		assert repOk();
		
		//Return immediately if don't want absorption simplifications
		if( !(opts.contains(FSimplifierOptions.ABSORPTION)) ) { return this; }
		

		// make a mutable temporary copy of children to work with
		ArrayList<Expr> tempList = new ArrayList<Expr>(this.children);
		ArrayList<NaryAndExpr> conjunctions = new ArrayList<NaryAndExpr>();
		ArrayList<Expr> otherExpr = new ArrayList<Expr>();
		
		// (x.y) + x ... = x ...
		// classify expressions into two groups: (1) conjunctions (2) others
		for ( Expr localexpr : tempList )
		{
			if (localexpr.getClass().equals(NaryAndExpr.class)) { conjunctions.add(((NaryAndExpr)localexpr)); }
			else { otherExpr.add(localexpr); }
		}
		
		//System.out.println(tempList.toString());
		
		// check if there are any conjunctions that can be removed
		for ( NaryAndExpr localexpr : conjunctions )
		{
			// remove all possible conjunctions whose children match the non-disjunction children
			for ( Expr checkmatch : otherExpr )
			{
				if( localexpr.children.contains(checkmatch) ) { tempList.remove(localexpr); }
			}
			
		}
		
		// Cover the more difficult case when there exists N-ary and expressions within 
		// the conjunctions that may match a subset of children
		// Partition children into two lists: one containing all n-ary and expressions, and one containing the rest
		// For each n-ary and expression child, determine if the n-ary and expression has any n-ary or expressions and search 
		// other children to see if there's a 'nested' match
		conjunctions.clear();
		for ( Expr localexpr : tempList )
		{
			if (localexpr.getClass().equals(NaryAndExpr.class))
			{
				conjunctions.add(((NaryAndExpr)localexpr));
			}
		}
		
		// Aggregate list of n-ary or expressions within the n-ary and expression to check
		// For each n-ary or expression found within n-ary and expression, search for match 
		// in subset of this.children that are not n-ary and expressions
		for ( int i=0; i<(conjunctions.size()); i++ )
		{
			for ( int j=(i+1); j<(conjunctions.size()); j++ )
			{				
				// Check for match				
				if ( conjunctions.get(i).children.containsAll(conjunctions.get(j).children) )
				{
					// There's a match
					// Remove the applicable n-ary and expressions
					tempList.remove(conjunctions.get(i));
				}
				else if (conjunctions.get(j).children.containsAll(conjunctions.get(i).children))
				{
					// There's a match
					// Remove the applicable n-ary and expressions
					tempList.remove(conjunctions.get(j));
				}
			}
		}
		
		//If there is only one child left, just return that since not Nary anymore.
		//If no changes to tempList (matches original children) return the current object itself.
		//Otherwise, create a new NaryOrExpr, with the new children.
		Collections.sort(tempList);
		if ( tempList.size() == 1 ) { return tempList.get(0); }
		else if ( tempList.containsAll(this.children) )	{ return this; }
		else { return new NaryOrExpr(tempList); }

		
// TODO: 120 lines snipped
		//return this; // TODO: replace with useful code
	}

	@Override
	protected Class<? extends BinaryExpr> getCorrespondingBinaryExprClass() {
		//return null; // TODO: replace this with something useful
		return( OrExpr.class );
// TODO: 1 lines snipped
	}

	@Override
	public ConstantExpr getAbsorbingElement() {
// TODO: 1 lines snipped
		//return null; // TODO: return the correct value
		return( ConstantExpr.TrueExpr );
	}

	@Override
	public ConstantExpr getIdentityElement() {
// TODO: 1 lines snipped
		//return null; // TODO: return the correct value
		return( ConstantExpr.FalseExpr );
	}
	
	@Override
	public NaryExpr newNaryExpr(final List<Expr> children) {
		return new NaryOrExpr(children);
	}

}
