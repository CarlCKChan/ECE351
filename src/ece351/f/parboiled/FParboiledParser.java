package ece351.f.parboiled;

import java.util.List;

import org.parboiled.Rule;
import org.parboiled.common.ImmutableList;

import ece351.common.ast.AndExpr;
import ece351.common.ast.AssignmentStatement;
import ece351.common.ast.ConstantExpr;
import ece351.common.ast.Expr;
import ece351.common.ast.NotExpr;
import ece351.common.ast.OrExpr;
import ece351.common.ast.VarExpr;
import ece351.f.ast.FProgram;
import ece351.util.CommandLine;
import ece351.vhdl.VConstants;

// Parboiled requires that this class not be final
public /*final*/ class FParboiledParser extends FBase implements VConstants {

	
	public static void main(final String[] args) {
    	final CommandLine c = new CommandLine(args);
    	final String input = c.readInputSpec();
    	final FProgram fprogram = parse(input);
    	final String output = fprogram.toString();
    	
    	// if we strip spaces and parens input and output should be the same
    	if (strip(input).equals(strip(output))) {
    		// success: return quietly
    		return;
    	} else {
    		// failure: make a noise
    		System.err.println("parsed value not equal to input:");
    		System.err.println("    " + strip(input));
    		System.err.println("    " + strip(output));
    		System.exit(1);
    	}
    }
	
	private static String strip(final String s) {
		return s.replaceAll("\\s", "").replaceAll("\\(", "").replaceAll("\\)", "");
	}
	
	public static FProgram parse(final String inputText) {
		return (FProgram) process(FParboiledParser.class, inputText).resultValue;
	}

	@Override
	public Rule Program() {
		return( Sequence(
					push( ImmutableList.of() ),
					W0(),
					ZeroOrMore(
						Sequence(
							Formula(),
							swap(),
							push( ((ImmutableList<AssignmentStatement>)pop()).append((AssignmentStatement)pop() ) ),
							W0() )
							
					),
					push( new FProgram( (ImmutableList<AssignmentStatement>)pop() ) ),
					EOI
				)
			);
		}
		
		public Rule Formula() {
			return Sequence(
						Var(),
						W0(), "<=", W0(),
						Expr(),
						W0(), ";",
						swap(),
						push( new AssignmentStatement((VarExpr)pop(), (Expr)pop() ) )
					);
		}
		
		public Rule Expr() {
			return Sequence(
					Term(),
					ZeroOrMore(
						Sequence(
							W0(), OR, TestNot(NotALetter()), W0(),
							Term(),
							swap(),
							push( new OrExpr((Expr)pop(), (Expr)pop() ) )
						)
					)
				);
		}
		
		public Rule Term() {
			return Sequence(
						Factor(),
						ZeroOrMore(
							Sequence(
								W0(), AND, TestNot(NotALetter()), W0(),
								Factor(),
								swap(),
								push( new AndExpr((Expr)pop(), (Expr)pop() ) )
							)
						)
					);
		}
		
		public Rule Factor() {
			return FirstOf(
						Sequence( NOT, TestNot(NotALetter()), W0(), Factor(), push(new NotExpr( (Expr)pop() )) ),
						Sequence( "(", W0(), Expr(), W0(), ")" ),
						Var(),
						Constant()
					);
		}
		
		public Rule Constant() {
			return Sequence(
						"'",
						FirstOf("0","1"),
						push(match().equals("1") ? ConstantExpr.TrueExpr : ConstantExpr.FalseExpr),
						"'"
					);
		}
		
		public Rule Var() {
			return Sequence(
						Sequence( Letter(), ZeroOrMore( FirstOf(Letter(), Number()) ) ),
						push(new VarExpr(match())),
						!(match().equals(AND)),
						!(match().equals(NOR)),
						!(match().equals(NOT)),
						!(match().equals(NAND)),
						!(match().equals(OR)),
						!(match().equals(XNOR)),
						!(match().equals(XOR))
					);
		}
		
	    public Rule Letter() {
	    	return( FirstOf( CharRange('A','Z'), CharRange('a','z') ) );
	    }
	    
	    public Rule Number() {
	    	return( CharRange('0','9') );
	    }
	    
	    
	    //This should only be passed to TestNot() to test if something is not a letter,
	    //since this actually tests for whether something is a letter.
	    public Rule NotALetter() {
	    	return( Letter() );
	    }
}
