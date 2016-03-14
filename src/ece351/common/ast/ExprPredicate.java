package ece351.common.ast;

public abstract class ExprPredicate {
	public abstract boolean eval(final Expr e);
	
	public final static class ExprTypeTestPredicate extends ExprPredicate {
		private final Class<?> c;
		public ExprTypeTestPredicate(final Class<?> c) {
			this.c = c;
		}
		@Override
		public boolean eval(final Expr e) {
			return c.isAssignableFrom(e.getClass());
		}
		
	}
	
	public final static ExprPredicate IsConstantExpr = new ExprTypeTestPredicate(ConstantExpr.class);
	public final static ExprPredicate IsNotExpr = new ExprTypeTestPredicate(NotExpr.class);
	public final static ExprPredicate IsAndExpr = new ExprTypeTestPredicate(AndExpr.class);
	public final static ExprPredicate IsOrExpr = new ExprTypeTestPredicate(OrExpr.class);
	public final static ExprPredicate IsNaryAndExpr = new ExprTypeTestPredicate(NaryAndExpr.class);
	public final static ExprPredicate IsNaryOrExpr = new ExprTypeTestPredicate(NaryOrExpr.class);
}
