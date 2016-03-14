package ece351.f;

import java.io.PrintWriter;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
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
import ece351.f.ast.PostOrderFExprVisitor;

public final class TechnologyMapper extends PostOrderFExprVisitor {

	/** Where we will write the output to. */
	private final PrintWriter out;
	
	/** 
	 * Table of substitutions for common subexpression elimination.
	 * We use a LinkedHashMap instead of a HashMap to get
	 * deterministic ordering.
	 */
	private final IdentityHashMap<Expr,Expr> substitutions = new IdentityHashMap<Expr,Expr>();
	//private final LinkedHashMap<Expr,Expr> substitutions = new LinkedHashMap<Expr,Expr>();

	/**
	 * The set of nodes in our circuit diagram. (We'll produce a node
	 * for each .) We could just print
	 * the nodes directly to the output stream instead of building up
	 * this set, but then we might output the same node twice. The set
	 * uniqueness property ensure that we will ultimately print each
	 * node exactly once.
	 */
	private final Set<String> nodes = new LinkedHashSet<String>();
	
	/**
	 * The set of edges in our circuit diagram. We could just print
	 * the edges directly to the output stream instead of building up
	 * this set, but then we might output the same edge twice. The set
	 * uniqueness property ensure that we will ultimately print each
	 * edge exactly once.
	 */
	private final Set<String> edges = new LinkedHashSet<String>();
	
	public TechnologyMapper(final PrintWriter out) {
		this.out = out;
	}
	
	public TechnologyMapper() {
		 this(new PrintWriter(System.out));
	}
	
	public static void main(final String arg) {
		main(new String[]{arg});
	}
	public static void main(final String[] args) {
		render(FParser.parse(args), new PrintWriter(System.out));
	}
	
	/**
	 * Translate an FProgram to Graphviz format.
	 */
	public static void render(final FProgram program, final PrintWriter out) {
		final TechnologyMapper tm = new TechnologyMapper(out);
		tm.render(program);
	}

	/** Where the real work happens. */
	public void render(final FProgram program) {
		header(out);
		
		// build a set of all of the exprs in the program
		Set<Expr> myExpr = ExtractAllExprs.allExprs(program);
		
		
		// build substitutions by determining equivalences of exprs
		// This should end with each key (expr) associated with the last equivalent expr
		// that appears in the list of all expressions (myExpr)
		for (Expr keyExpr : myExpr)
		{
			for ( Expr mapExpr : myExpr )
			{
				if ( keyExpr.equivalent(mapExpr))
				{
					substitutions.put(keyExpr, mapExpr);
				}				
			}
		}
		
		// attach images to gates
		// Variables and Constants just have a label, while actual gates will use the images
		// without any leads (to accommodate N-ary gates)
		for ( Expr keyExpr : substitutions.keySet() )
		{
			Expr mapped = substitutions.get(keyExpr);
			
			if( mapped instanceof VarExpr || mapped instanceof ConstantExpr )
			{
				node( mapped.nameID(), mapped.toString() );
			}
			else
			{
				node( mapped.nameID(), "", mapped.operator() + "_noleads.png" );
			}
			
		}
		
		// Traverse the trees of all the formulas to assign the edges in topological order
		// Then also draw the edge from the top-level expressions to the output variables.
		for (final AssignmentStatement f : program.formulas) {
		    final Expr expr = substitutions.get(f.expr);
		    traverse(expr);
		    edge(expr,substitutions.get(f.outputVar));
		 }
		
		
		// print nodes
		for( String printNode : nodes )
		{
			out.println( printNode );
		}		
		
		// print edges
		for( String printEdge : edges )
		{
			out.println( printEdge );
		}	
		
// TODO: 50 lines snipped
		// print footer
		footer(out);
		out.flush();
		
		// release memory
		substitutions.clear();
		edges.clear();
	}

	
	private static void header(final PrintWriter out) {
		out.println("digraph g {");
		out.println("    // header");
		out.println("    rankdir=LR;");
		out.println("    margin=0.01;");
		out.println("    node [shape=\"plaintext\"];");
		out.println("    edge [arrowhead=\"diamond\"];");
		out.println("    // circuit ");
	}

	private static void footer(final PrintWriter out) {
		out.println("}");
	}

	@Override
	public Expr visit(final ConstantExpr e) {
		node(e.nameID(), e.toString());
		return e;
	}

	@Override
	public Expr visit(final VarExpr e) {
		final Expr e2 = substitutions.get(e);
		assert e2 != null : "no substitution for " + e + " " + e.nameID();
		node(e2.nameID(), e2.toString());
		return e;
	}

	@Override
	public Expr visit(final NotExpr e) {
		edge(e.expr, e);
		return e;
	}

	@Override
	public Expr visit(final AndExpr e) {
// TODO: 2 lines snipped
		edge(e.left,e);
		edge(e.right,e);
		return e;
	}

	@Override
	public Expr visit(final OrExpr e) {
// TODO: 2 lines snipped
		edge(e.left,e);
		edge(e.right,e);
		return e;
	}
	
	@Override public Expr visit(final NaryAndExpr e) {
// TODO: 3 lines snipped
		for ( Expr temp : e.children ) {
			edge( temp, e );
		}
		return e;
	}

	@Override public Expr visit(final NaryOrExpr e) { 
// TODO: 3 lines snipped
		for ( Expr temp : e.children ) {
			edge( temp, e );
		}
		return e;
	}


	private void node(final String name, final String label) {
		nodes.add("    " + name + "[label=\"" + label + "\"];");
	}

	private void node(final String name, final String label, final String image) {
		nodes.add(String.format("    %s [label=\"%s\", image=\"%s\"];", name, label, image));
	}

	private void edge(final Expr source, final Expr target) {
		edge(substitutions.get(source).nameID(), substitutions.get(target).nameID());
	}
	
	private void edge(final String source, final String target) {
		edges.add("    " + source + " -> " + target + " ;");
	}
}
