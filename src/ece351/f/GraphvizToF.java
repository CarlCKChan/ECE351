package ece351.f;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.parboiled.common.ImmutableList;

import ece351.common.ast.AssignmentStatement;
import ece351.common.ast.ConstantExpr;
import ece351.common.ast.Expr;
import ece351.common.ast.NaryAndExpr;
import ece351.common.ast.NaryOrExpr;
import ece351.common.ast.NotExpr;
import ece351.common.ast.VarExpr;
import ece351.f.ast.FProgram;
import ece351.util.Tuple;


/**
 * A logic unit that converts a graphviz .dot file to
 * its corresponding FProgram. 
 */
public final class GraphvizToF {

	private GraphvizToF() {
		throw new UnsupportedOperationException();
	}

	public static Tuple<FProgram,Integer> graphvizToF(final String dotFilePath) throws IOException {
		final FileInputStream fstream = new FileInputStream(dotFilePath);
		final DataInputStream in = new DataInputStream(fstream);
		final BufferedReader br = new BufferedReader(new InputStreamReader(in));
		final Tuple<FProgram,Integer> result = graphvizToF(br);
		br.close();
		return result;
	}
	
	public static Tuple<FProgram,Integer> graphvizToF(final BufferedReader br) throws IOException {

		// A map of nodes, nameID <--> label.
		final Map<String, String> nodeMap = new HashMap<String, String>();

		// A list of edges parsed from the input .dot file.
		final ArrayList<Tuple<String, String>> edges = new ArrayList<Tuple<String, String>>();
		
		// Read and parse input file line by line.
		String strLine = null;
		while ((strLine = br.readLine()) != null) {
			int i;
			strLine = strLine.trim();
			if((i = strLine.indexOf("->")) >= 0) {
				// An edge.
				final String source = strLine.substring(0, i-1).trim();
				final String target = strLine.substring(i+3).replace(';', ' ').trim();

				edges.add(new Tuple<String, String>(source, target));
			} else {
				// A node.
				if(strLine.trim().startsWith("var") || strLine.trim().startsWith("Const")) {
					if((i = strLine.indexOf("label")) >= 0) {
						final String nameID = strLine.substring(0, i-1).trim();
						final String label = strLine.substring(i+7, strLine.lastIndexOf("\"")).trim();
						
						nodeMap.put(nameID, label);
					}
				}
			}
		}

		// A map of nameID to their corresponding exprs.
		final Map<String, Expr> exprMap = new HashMap<String, Expr>();

		// Extract exprs from the nodes.
		for(final Map.Entry<String, String> entry: nodeMap.entrySet()) {
			final String key = entry.getKey();
			final String val = entry.getValue();
			if(key.startsWith("var")) {
				exprMap.put(key, new VarExpr(val));
			} else {
				exprMap.put(key, ConstantExpr.make(val.replace('\'', ' ').trim()));
			}
		}

		ImmutableList<AssignmentStatement> formulas = ImmutableList.of();

		// Iterate through every edge, this is based on the assumption that
		// the edges are sorted in post-order from the TechnologyMapper.
		final ArrayList<Expr> children = new ArrayList<Expr>();
		String previousTarget = "";
		int gates = 0;
		for(int i = 0; i < edges.size(); i++) {
			Tuple<String, String> edge = edges.get(i);
			String source = edge.x;
			String target = edge.y;

			if (target.equals(previousTarget) && !target.startsWith("var")) {
				// new formula
				children.add(exprMap.get(source));
			} else {
				if(previousTarget.startsWith("or")) {
					exprMap.put(previousTarget, new NaryOrExpr(children));
					gates++;
				} else if (previousTarget.startsWith("and")) {
					exprMap.put(previousTarget, new NaryAndExpr(children));
					gates++;
				} else if (previousTarget.startsWith("not")) {
					exprMap.put(previousTarget, new NotExpr(children.get(0)));
					gates++;
				}

				children.clear();

				if(target.startsWith("var")) {
					final Expr sourceExpr = (Expr)exprMap.get(source);
					assert sourceExpr != null : "sourceExpr is null for " + source + "\n" + exprMap;
					final VarExpr targetExpr = (VarExpr)exprMap.get(target);
					assert targetExpr != null : "targetExpr is null for " + target + "\n" + exprMap;
					formulas = formulas.append(new AssignmentStatement(targetExpr, sourceExpr));
				} else {
					children.add(exprMap.get(source));
				}

				previousTarget = target;
			}
		}

		return new Tuple<FProgram,Integer>(new FProgram(formulas), gates);
	}
}
