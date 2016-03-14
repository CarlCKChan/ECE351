package ece351.common.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ece351.util.CommandLine.FSimplifierOptions;
import ece351.vhdl.VConstants;


public final class NaryAndExpr extends NaryExpr {

	public NaryAndExpr(final Expr... exprs) {
		super(exprs);
	}

	public NaryAndExpr(final List<Expr> children) {
		super(children);
	}
	

	@Override
	public Expr accept(ExprVisitor v) { v.visit(this); return this; }

	@Override
	public String operator() {
		return VConstants.AND;
	}

	@Override
	protected String displayName() {
		return "NaryAnd";
	}

	@Override
	public ConstantExpr getIdentityElement() {
// TODO: 1 lines snipped
		//return null; // TODO: return the correct value
		return ( ConstantExpr.TrueExpr );
	}
	
	@Override
	public ConstantExpr getAbsorbingElement() {
// TODO: 1 lines snipped
		//return null; // TODO: return the correct value
		return ( ConstantExpr.FalseExpr );
	}
	
	@Override
	protected Expr simplifySelf(final Set<FSimplifierOptions> opts) {
		assert repOk();
		
		//Return immediately if don't want absorption simplifications
		if( !(opts.contains(FSimplifierOptions.ABSORPTION)) ) { return this; }
		

		// make a mutable temporary copy of children to work with
		ArrayList<Expr> tempList = new ArrayList<Expr>(this.children);
		ArrayList<NaryOrExpr> disjunctions = new ArrayList<NaryOrExpr>();
		ArrayList<Expr> otherExpr = new ArrayList<Expr>();
		
		// (x.y) + x ... = x ...
		// classify expressions into two groups: (1) disjunctions (2) others
		for ( Expr localexpr : tempList )
		{
			if (localexpr.getClass().equals(NaryOrExpr.class)) { disjunctions.add(((NaryOrExpr)localexpr)); }
			else { otherExpr.add(localexpr); }
		}
		
		//System.out.println(tempList.toString());
		
		// check if there are any disjunctions that can be removed
		for ( NaryOrExpr localexpr : disjunctions )
		{			
			// remove all possible disjunction whose children match the non-disjunction children
			for ( Expr checkmatch : otherExpr )
			{
				if( localexpr.children.contains(checkmatch) ) { tempList.remove(localexpr); }
			}
			
		}
		
		// Cover the more difficult case when there exists N-ary and expressions within 
		// the disjunctions that may match a subset of children
		// Partition children into two lists: one containing all n-ary and expressions, and one containing the rest
		// For each n-ary and expression child, determine if the n-ary and expression has any n-ary or expressions and search 
		// other children to see if there's a 'nested' match
		disjunctions.clear();
		for ( Expr localexpr : tempList )
		{
			if (localexpr.getClass().equals(NaryOrExpr.class))
			{
				disjunctions.add(((NaryOrExpr)localexpr));
			}
		}
		
		// Aggregate list of n-ary or expressions within the n-ary and expression to check
		// For each n-ary or expression found within n-ary and expression, search for match 
		// in subset of this.children that are not n-ary and expressions
		for ( int i=0; i<(disjunctions.size()); i++ )
		{
			for ( int j=(i+1); j<(disjunctions.size()); j++ )
			{				
				// Check for match				
				if ( disjunctions.get(i).children.containsAll(disjunctions.get(j).children) )
				{
					// There's a match
					// Remove the applicable n-ary and expressions
					tempList.remove(disjunctions.get(i));
				}
				else if (disjunctions.get(j).children.containsAll(disjunctions.get(i).children))
				{
					// There's a match
					// Remove the applicable n-ary and expressions
					tempList.remove(disjunctions.get(j));
				}
			}
		}
		
		//If there is only one child left, just return that since not Nary anymore.
		//If no changes to tempList (matches original children) return the current object itself.
		//Otherwise, create a new NaryOrExpr, with the new children.
		Collections.sort(tempList);
		if ( tempList.size() == 1 ) { return tempList.get(0); }
		else if ( tempList.containsAll(this.children) )	{ return this; }
		else { return new NaryAndExpr(tempList); }
// TODO: 120 lines snipped
		//return this; // TODO: replace with useful code
	}

	@Override
	protected Class<? extends BinaryExpr> getCorrespondingBinaryExprClass() {
		return ( AndExpr.class );
		//return null; // TODO: replace this with something useful
// TODO: 1 lines snipped
	}

	@Override
	public NaryExpr newNaryExpr(final List<Expr> children) {
		return new NaryAndExpr(children);
	}
	
}
