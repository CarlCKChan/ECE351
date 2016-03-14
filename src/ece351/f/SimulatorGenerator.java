package ece351.f;

import java.io.PrintWriter;
import java.util.Set;

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
import ece351.f.ast.FProgram;
import ece351.f.ast.PreOrderFExprVisitor;
import ece351.util.CommandLine;

public final class SimulatorGenerator extends PreOrderFExprVisitor {

//	private final String fName;
	private PrintWriter out = new PrintWriter(System.out);
	private String indent = "";

	public static void main(final String arg) {
		main(new String[]{arg});
	}
	
	public static void main(final String[] args) {
		final CommandLine c = new CommandLine(args);
		final SimulatorGenerator s = new SimulatorGenerator();
		final PrintWriter pw = new PrintWriter(System.out);
		s.generate(c.getInputName(), FParser.parse(c), pw);
		pw.flush();
	}

	private void println(final String s) {
		out.print(indent);
		out.println(s);
	}
	
	private void println() {
		out.println();
	}
	
	private void print(final String s) {
		out.print(s);
	}

	private void indent() {
		indent = indent + "    ";
	}
	
	private void outdent() {
		indent = indent.substring(0, indent.length() - 4);
	}
	
	public void generate(final String fName, final FProgram program, final PrintWriter out) {
		this.out = out;

		// header
		println("import java.util.*;");
		println("import ece351.w.ast.*;");
		println("import ece351.w.parboiled.*;");
		println("import static ece351.util.Boolean351.*;");
		println();
		println("public final class Simulator_" + fName + " {");
		indent();
		println("public static void main(final String[] args) {");
		indent();
		
		println("// read input WProgram");
		println("// construct storage for output");
		println("// loop over each time step");
		println("// values of input variables at this time step");
		println("// values of output variables at this time step");
		println("// store outputs");
		// end the time step loop
		println("// write the input");
		println("// write the output");
// TODO: 38 lines snipped
		// end main method
		outdent();
		println("}");
		
		println("// methods to compute values for output pins");
// TODO: 10 lines snipped
		// end class
		outdent();
		println("}");

	}

	@Override
	public Expr traverse(final NaryExpr e) {
		e.accept(this);
		final int size = e.children.size();
		for (int i = 0; i < size; i++) {
			final Expr c = e.children.get(i);
			traverse(c);
			if (i < size - 1) {
				// common case
				out.print(", ");
			}
		}
		out.print(") ");
		return e;
	}

	@Override
	public Expr traverse(final BinaryExpr e) {
		e.accept(this);
		traverse(e.left);
		out.print(", ");
		traverse(e.right);
		out.print(") ");
		return e;
	}

	@Override
	public Expr traverse(final UnaryExpr e) {
		e.accept(this);
		traverse(e.expr);
		out.print(") ");
		return e;
	}

	@Override
	public Expr visit(final ConstantExpr e) {
		out.print(Boolean.toString(e.b));
		return e;
	}

	@Override
	public Expr visit(final VarExpr e) {
		out.print(e.identifier);
		return e;
	}

	@Override
	public Expr visit(final NotExpr e) {
		out.print("not(");
		return e;
	}

	@Override
	public Expr visit(final AndExpr e) {
		out.print("and(");
		return e;
	}

	@Override
	public Expr visit(final OrExpr e) {
		out.print("or(");
		return e;
	}
	
	@Override public Expr visit(NaryAndExpr e) { 
		out.print("and(");
		return e; 
	}
	@Override public Expr visit(NaryOrExpr e) { 
		out.print("or(");
		return e; 
	}
	
	
	public String generateSignature(final AssignmentStatement f) {
		return generateList(f, true);
	}
	
	public String generateCall(final AssignmentStatement f) {
		return generateList(f, false);
	}

	private String generateList(final AssignmentStatement f, final boolean signature) {
		final StringBuilder b = new StringBuilder();
		if (signature) {
			b.append("public static boolean ");
		}
		b.append(f.outputVar);
		b.append("(");
		// loop over f's input variables
// TODO: 13 lines snipped
		b.append(")");
		return b.toString();
	}

}
