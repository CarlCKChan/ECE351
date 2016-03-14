package ece351.f.rdescent;
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
import ece351.util.Lexer;
import ece351.vhdl.VConstants;



public final class FRecursiveDescentParser implements VConstants {
   
	// instance variables
	private final Lexer lexer;

    public FRecursiveDescentParser(String... args) {
    	final CommandLine c = new CommandLine(args);
        lexer = new Lexer(c.readInputSpec());
    }
    
    public FRecursiveDescentParser(final Lexer lexer) {
        this.lexer = lexer;
    }

    public static void main(final String arg) {
    	main(new String[]{arg});
    }
    
    public static void main(final String[] args) {
    	parse(args);
    }

    public static FProgram parse(final String... args) {
        final FRecursiveDescentParser p = new FRecursiveDescentParser(args);
        return p.parse();
    }
    
    public FProgram parse() {
        return program();
    }

    FProgram program() {
        ImmutableList<AssignmentStatement> formulas = ImmutableList.of();
        while (!lexer.inspectEOF()) {
        	formulas = formulas.append(formula());
        }
        lexer.consumeEOF();
        return new FProgram(formulas);
    }

    AssignmentStatement formula() {
        final VarExpr var = var();
        lexer.consume("<=");
        final Expr expr = expr();
        lexer.consume(";");
        return new AssignmentStatement(var, expr);
    }
    
    VarExpr var() {
    	//Return the variable name as an VarExpr
    	return new VarExpr(lexer.consumeID());
    } // TODO: replace this stub
    
    
    Expr expr() {
    	//Get the first term.  There might not be a second term, so don't set it now.
    	Expr first = term();
    	Expr second = null;

    	//If there is an OR, we must get the next term.  Get the second term.

    		while (lexer.inspect(OR))
    		{
    			lexer.consume(OR);
    		
	    		second = term();
	    		
	    		first = new OrExpr(first,second);
    		}

    		//If not second term for an OR, just return the first term found
    		return first;

    } // TODO: replace this stub
    
    
    Expr term() {
    	//Get the first factor.  There might not be a second factor, so don't set it now.
    	Expr first = factor();
    	Expr second = null;
    	
		while (lexer.inspect(AND))
		{
			lexer.consume(AND);
		
    		second = factor();
    		
    		first = new AndExpr(first,second);
		}

		//If not second term for an OR, just return the first term found
		return first;
    	
    }
    
    Expr factor(){
    	Expr retExpr = null;
    	
    	if( lexer.inspect(NOT) )
    	{
    		//If this is a NOT term, return whatever is after the "NOT" as a NotExpr
    		lexer.consume(NOT);
    		retExpr = new NotExpr(factor());
    	}
    	else if( lexer.inspect("(") )
    	{
    		//If reading from a set a brackets, just return back what is within the brackets
    		lexer.consume("(");
    		retExpr = expr();
    		lexer.consume(")");
    	}
    	else if( lexer.inspectID() )
    	{
    		//Just return what var() gives you to handle identifiers
    		retExpr = var();
    	}
    	else if ( lexer.inspect("'") )
    	{
    		//Just return what constant() gives you to handle identifiers
    		retExpr = constant();
    	}
    	else
    	{
    		//If none of the above cases match, try to consume one of the above symbols.  This
    		//will cause the lexer to reject the file
    		lexer.consume(NOT,"(", "'");
    	}
    	return retExpr;
    }
    
    
    ConstantExpr constant() {
    	lexer.consume("'");
    	ConstantExpr retConst = null;
    	
    	String saveConst = lexer.consume("0","1");
    	
    	//If read a "0", return a false constant.  Otherwise, return a true constant.
    	if (saveConst == "0") { retConst = ConstantExpr.FalseExpr; }
    	else { retConst = ConstantExpr.TrueExpr; }
    	
    	lexer.consume("'");
    	return retConst;
    }
    
// TODO: 51 lines snipped

    // helper functions
    private boolean peekConstant() {
        return lexer.inspect("'");
    }

}

