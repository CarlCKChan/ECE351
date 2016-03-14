package ece351.common.ast;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.parboiled.common.ImmutableList;

import ece351.f.ast.FProgram;
import ece351.util.CommandLine.FSimplifierOptions;
import ece351.util.Examinable;
import ece351.util.Examiner;

/**
 * An expression with multiple children. Must be commutative.
 * @author ece351
 *
 */
public abstract class NaryExpr extends Expr {

	public final ImmutableList<Expr> children;

	public abstract String operator();
	protected abstract String displayName();
	protected abstract Expr simplifySelf(final Set<FSimplifierOptions> opts);
	
	public NaryExpr(final Expr... exprs) {
		Arrays.sort(exprs);
		ImmutableList<Expr> c = ImmutableList.of();
		for (final Expr e : exprs) {
			c = c.append(e);
		}
    	this.children = c;
    	assert repOk();
	}
	
	public NaryExpr(final List<Expr> children) {
		final ArrayList<Expr> a = new ArrayList<Expr>(children);
		Collections.sort(a);
		this.children = ImmutableList.copyOf(a);
		assert repOk();
	}
	
	public boolean repOk() {
		return repOk(this.children);
	}
	
	public static boolean repOk(final List<Expr> children) {
		// programming sanity
		assert children != null;
		// should not have a single child: indicates a bug in simplification
		assert children.size() > 1 : "should have more than one child, probably a bug in simplification";
		// check that children is sorted
		int i = 0;
		for (int j = 1; j < children.size(); i++, j++) {
			final Expr x = children.get(i);
			assert x != null : "null children not allowed in NaryExpr";
			final Expr y = children.get(j);
			assert y != null : "null children not allowed in NaryExpr";
			assert x.compareTo(y) <= 0 : "NaryExpr.children must be sorted";
		}
		return true;
	}
	
	protected abstract Class<? extends BinaryExpr> getCorrespondingBinaryExprClass();
	
	@Override
	public final Expr standardize() {
    	assert repOk();
    	
    	
    	// construct an ArrayList temporary to work with
    	ArrayList<Expr> tempList = new ArrayList<Expr>();
    	//Set<Expr> tempTree = new TreeSet<Expr>();	//Use this to temporarily store Expr to avoid duplicates
    	Expr tempExpr;
    	
    	
    	for( Expr myExpr : children )
    	{
    	// standardize children
    		tempExpr = myExpr.standardize();
    	
    	
    	// now examine new standardized children
		// identify children to be absorbed
    		if( tempExpr.getClass().equals(this.getClass()) )
    		{
			// binary child of the right type
			// child of my type
    			tempList.addAll( ((NaryExpr)tempExpr).children );
			
			// we had some children to absorb
    		}
    		else
    		{
    			// we had no children to absorb.  Just add the standardize expression itself
    			tempList.add(tempExpr);
    		}
    	}
    	
    	//Sort the elements and return them in a new object of the same type as this object
    	Collections.sort(tempList);
    	if (tempList.containsAll(this.children)) { return this;}
    	else { return newNaryExpr(tempList); }

// TODO: 44 lines snipped
    	
    	//return this; // TODO: replace with code that works
	}

	public abstract NaryExpr newNaryExpr(final List<Expr> children);
	
    @Override 
    public final String toString() {
    	final StringBuilder b = new StringBuilder();
    	b.append("(");
    	int count = 0;
    	for (final Expr c : children) {
    		b.append(c);
    		if (++count  < children.size()) {
    			b.append(" ");
    			b.append(operator());
    			b.append(" ");
    		}
    		
    	}
    	b.append(")");
    	return b.toString();
    }
    
	@Override
	protected final Expr simplifyOnce(final Set<FSimplifierOptions> opts) {
		assert repOk();
		// make a temporary mutable ArrayList to work with
		final List<Expr> newchildren = new ArrayList<Expr>(children.size());
		// simplify all children in the n-ary expression
		for (final Expr e : children) {
			newchildren.add(e.simplify(opts));
		}
		// need to re-sort newchildren since they may have changed during simplification
		Collections.sort(newchildren);
		assert repOk(newchildren);

		
		if (opts.contains(FSimplifierOptions.CONSTANT)) {
			
			//List<Expr> complementChildren = new ArrayList<Expr>( newchildren );
			
			for (Expr localExpr : newchildren)
			{
				// absorbing element: 0.x=1 and 1+x=1
				if (localExpr.equals(this.getAbsorbingElement())) { return this.getAbsorbingElement(); }
				
				// identity element: 1.x=x and 0+x=x
				// just remove the identity elements from the children
				// (but not if the identity element is the only child)
					// exactly one child
					// multiple children
					// if we now have no children it's because the identity element was all of them
					// if we're down to a single child then simply return it
				if (localExpr.equals(this.getIdentityElement())) { newchildren.remove(localExpr); }
			}
			if( newchildren.size() == 0 ) { return this.getIdentityElement(); }
			if( newchildren.size() == 1 ) { return newchildren.get(0); }
			
		}

		assert repOk(newchildren);
		
		if (opts.contains(FSimplifierOptions.DEDUPLICATION))
		{
			Set<Expr> tempTree = new TreeSet<Expr>();
			// remove duplicate children: x.x=x and x+x=x.  Just use a tree to remove duplicates.

			// since children are sorted this is fairly easy
			tempTree.addAll(newchildren);
			newchildren.clear();
			newchildren.addAll(tempTree);
			Collections.sort(newchildren);
			
			// if we're down to a single child then simply return it
			if( newchildren.size() == 1 ) {  return newchildren.get(0); }
		}
		
// TODO: 51 lines snipped
		assert repOk(newchildren);

		if (opts.contains(FSimplifierOptions.COMPLEMENT)) {
			// !x . x . ... = 0 and !x + x + ... = 1
			// x op !x = absorbing element
			// find all negations
			final ArrayList<NotExpr> negations = new ArrayList<NotExpr>();
			for (final Expr c : newchildren) {
				if (c instanceof NotExpr) {
					negations.add( (NotExpr)c );
				}
			}
			// for each negation, see if we find its complement
			for (final NotExpr n : negations) {
				for (final Expr c : newchildren) {
					if (c.equivalent(n.expr)) {
						// found matching negation and its complement
						// return absorbing element
						return getAbsorbingElement();
					}
				}
			}
		}

		
		// identities that differ for NaryAndExpr and NaryOrExpr
		// are newchildren any different than children?
		if (children.equals(newchildren)) {
			// newchildren are equal to children
			return simplifySelf(opts).standardize();
		} else {
			// newchildren have changed
			return newNaryExpr(newchildren).simplifySelf(opts).standardize();
		}
	}

	private boolean examine(final Examiner e, final Examinable obj) {
		// basics
		if (obj == null) return false;
		if (!this.getClass().equals(obj.getClass())) return false;
		final NaryExpr that = (NaryExpr) obj;
		assert repOk();
		assert that.repOk();
		
		// if the number of children are different, consider them not equivalent
		if (this.children.size() != that.children.size()) { return false; }
		
		// since the n-ary expressions have the same number of children and they are sorted, just iterate and check
		return ( Examiner.orderedExamination(e, this.children, that.children) );
// TODO: 12 lines snipped
		// no significant differences
		//return true;
		
	}

	@Override
	public final int hashCode() {
		return 17 + children.hashCode();
	}

	@Override
	public final boolean equals(final Object obj) {
		if (!(obj instanceof Examinable)) return false;
		return examine(Examiner.Equals, (Examinable)obj);
	}

	@Override
	public final boolean isomorphic(final Examinable obj) {
		return examine(Examiner.Isomorphic, obj);
	}

	@Override
	public final boolean equivalent(final Examinable obj) {
		return examine(Examiner.Isomorphic, obj);
	}


	
	/**
     * e op x = e for absorbing element e and operator op.
     * @return
     */
	public abstract ConstantExpr getAbsorbingElement();

    /**
     * e op x = x for identity element e and operator op.
     * @return
     */
	public abstract ConstantExpr getIdentityElement();


}
