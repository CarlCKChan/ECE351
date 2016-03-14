package ece351.common.ast;

import java.util.Set;

import ece351.util.CommandLine.FSimplifierOptions;
import ece351.vhdl.VConstants;

public final class NotExpr extends UnaryExpr{
	public NotExpr(Expr argument) {
		super(argument);
	}

	public NotExpr() { this(null); }
	
	@Override
    protected final Expr simplifyOnce(final Set<FSimplifierOptions> opts) {		
    	// simplify our child
    	final Expr localexpr = expr.simplify(opts);
    	
		// !true = false (Constant folding)
		// !false = true (Constant folding)
    	if (opts.contains(FSimplifierOptions.CONSTANT))
    	{
	    	if ( localexpr.equals(ConstantExpr.FalseExpr) ) { return ConstantExpr.TrueExpr; }
	    	if ( localexpr.equals(ConstantExpr.TrueExpr) ) { return ConstantExpr.FalseExpr; }
    	}
    	
    	// !!x = x  (Complement)
    	if (opts.contains(FSimplifierOptions.COMPLEMENT))
    	{
    		if (localexpr.getClass().equals(NotExpr.class)) { return( ((NotExpr)localexpr).expr); }
    	}
    		
// TODO: 12 lines snipped
    	//If not simplifications wanted or possible, return itself.
    	//return this;
    	return (new NotExpr(localexpr));

    }
	
    public Expr accept(final ExprVisitor v){
    	return v.visit(this);
    }
	
	@Override
	public String operator() {
		return VConstants.NOT;
	}
	@Override
	public UnaryExpr newUnaryExpr(final Expr expr) {
		return new NotExpr(expr);
	}
	@Override
	public Expr standardize() {
		return new NotExpr( expr.standardize() );
	}

}
