package ece351.common.ast;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import ece351.util.CommandLine.FSimplifierOptions;
import ece351.util.Examinable;

public abstract class Expr implements Comparable<Expr>, Examinable {
	
	private static final AtomicInteger counter = new AtomicInteger();
	public final int id;
	
	public Expr() {
		id = counter.getAndIncrement();
	}
	
	/**
	 * Default implementation is to do nothing.
	 * @param opts
	 * @return
	 */
	protected Expr simplifyOnce(final Set<FSimplifierOptions> opts) {
		return this;
	}
	
	/**
	 * Keep applying the simplify method until no more changes occur.
	 * In other words, iterator to a fixed point.
	 * @param e
	 * @param opts
	 * @return
	 */
	public Expr simplify(final Set<FSimplifierOptions> opts) {
		Expr e = this;
    	while (true) {
    		final Expr simplified = e.simplifyOnce(opts);
    		if (simplified.equals(e)) {
    			return simplified;
    		} else {
    			e = simplified;
    		}
    	}
    }
	
	public final String nameID(){ return operator() + id; };
	public abstract Expr accept(final ExprVisitor exprVisitor);
//	public abstract Expr deepCopy();

	/**
     * Default implementation is no-op.
     */
	public Expr standardize() {
		return this;
	}

	
	@Override
	public final int compareTo(final Expr e) {
		if (getClass().equals(e.getClass())) {
			// same type
			return toString().compareTo(e.toString());
		} else {
			// different types
			return getClass().getName().compareTo(e.getClass().getName());
		}
	}

	public abstract String operator();
	
	/**
	 * Representation invariant. Assert at the beginning and end of public methods.
	 * @return true if this object is in a legal state
	 */
	public abstract boolean repOk();
}
