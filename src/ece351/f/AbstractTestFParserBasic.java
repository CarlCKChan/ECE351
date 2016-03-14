package ece351.f;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;
import org.parboiled.common.ImmutableList;

import ece351.common.ast.AndExpr;
import ece351.common.ast.AssignmentStatement;
import ece351.common.ast.Expr;
import ece351.common.ast.NotExpr;
import ece351.common.ast.OrExpr;
import ece351.common.ast.VarExpr;
import ece351.f.ast.FProgram;
import ece351.f.rdescent.FRecursiveDescentParser;
import ece351.util.CommandLine;
import ece351.util.ExaminableProperties;
import ece351.util.TestInputs351;


public abstract class AbstractTestFParserBasic {

	public AbstractTestFParserBasic() { super(); }

	abstract protected FProgram parse(final String input);
	
	/**
	 * x <= a and b;
	 */
	@Test
	public void testAnd() {
		// parse
		final FProgram fp = parse("x <= a and b;");
		// compare
		compareExpectSame(fp, constructAnd());
		compareExpectDifferent(fp, constructOr());
		compareExpectDifferent(fp, constructNot());
		compareExpectDifferent(fp, constructAnd("p", "q"));
		// check isomorphism up to commutativity
		assertTrue("isomorphic() should allow arguments to commute", fp.isomorphic(constructAnd("b", "a")));
	}

	/**
	 * x <= a or b;
	 */
	@Test
	public void testOr() {
		final FProgram fp = parse("x <= a or b;");
		// compare
		compareExpectSame(fp, constructOr());
		compareExpectDifferent(fp, constructAnd());
		compareExpectDifferent(fp, constructNot());
		compareExpectDifferent(fp, constructOr("p", "q"));
		// check isomorphism up to commutativity
		assertTrue("isomorphic() should allow arguments to commute", fp.isomorphic(constructOr("b", "a")));
	}

	/**
	 * x <= not a;
	 */
	@Test
	public void testNot() {
		// parse
		final FProgram fp = parse("x <= not a;");
		// compare
		compareExpectSame(fp, constructNot());
		compareExpectDifferent(fp, constructAnd());
		compareExpectDifferent(fp, constructOr());
		compareExpectDifferent(fp, constructNot("p"));
	}

	private FProgram constructNot() {
		return constructNot("a");
	}
	
	private FProgram constructNot(final String a) {
		final Expr e = new NotExpr(new VarExpr(a));
		final AssignmentStatement s = new AssignmentStatement(new VarExpr("x"), e);
		final FProgram fp = new FProgram(ImmutableList.of(s));
		return fp;
	}

	private static FProgram constructAnd() {
		return constructAnd("a", "b");
	}
	
	private static FProgram constructAnd(final String a, final String b) {
		final Expr e = new AndExpr(new VarExpr(a), new VarExpr(b));
		final AssignmentStatement s = new AssignmentStatement(new VarExpr("x"), e);
		final FProgram fp = new FProgram(ImmutableList.of(s));
		return fp;
	}

	private static FProgram constructOr() {
		return constructOr("a", "b");
	}
	
	private static FProgram constructOr(final String a, final String b) {
		final Expr e = new OrExpr(new VarExpr(a), new VarExpr(b));
		final AssignmentStatement s = new AssignmentStatement(new VarExpr("x"), e);
		final FProgram fp = new FProgram(ImmutableList.of(s));
		return fp;
	}


	private static void compareExpectSame(final FProgram fp1, final FProgram fp2) {
		assertTrue("unexpectedly not equals: " + fp1 + " " + fp2, fp1.equals(fp2));
		assertTrue("unexpectedly not isomorphic: " + fp1 + " " + fp2, fp1.isomorphic(fp2));
		assertTrue("unexpectedly not equivalent: " + fp1 + " " + fp2, fp1.equivalent(fp2));
	}

	private static void compareExpectDifferent(final FProgram fp1, final FProgram fp2) {
		assertFalse("unexpectedly equals: " + fp1 + " " + fp2, fp1.equals(fp2));
		assertFalse("unexpectedly isomorphic: " + fp1 + " " + fp2, fp1.isomorphic(fp2));
		assertFalse("unexpectedly equivalent: " + fp1 + " " + fp2, fp1.equivalent(fp2));
	}

}
