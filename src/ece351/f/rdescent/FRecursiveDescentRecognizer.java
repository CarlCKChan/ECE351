package ece351.f.rdescent;

import ece351.util.CommandLine;
import ece351.util.Lexer;
import ece351.vhdl.VConstants;

public final class FRecursiveDescentRecognizer implements VConstants {
   
    private final Lexer lexer;

    public static void main(final String arg) {
    	main(new String[]{arg});
    }
    
    public static void main(final String[] args) {
    	final CommandLine c = new CommandLine(args);
        final Lexer lexer = new Lexer(c.readInputSpec());
        final FRecursiveDescentRecognizer r = new FRecursiveDescentRecognizer(lexer);
        r.recognize();
    }

    public FRecursiveDescentRecognizer(final Lexer lexer) {
        this.lexer = lexer;
    }

    public void recognize() {
        program();
    }

    void program() {
        while (!lexer.inspectEOF()) {
            formula();
        }
        lexer.consumeEOF();
    }

    void formula() {
        var();
        lexer.consume("<=");
        expr();
        lexer.consume(";");
    }
    
    
    void var() {
    	lexer.consumeID();
    	return;
    } // TODO: replace this stub
    
    
    void expr() {
    	term();
    	
    	if( lexer.inspect(OR) )
    	{
    		lexer.consume(OR);
    		term();
    		
    		if( lexer.inspect(OR) )
        	{
        		lexer.consume(OR);
        		expr();
        	}
        	return;
    	}
    	
    } // TODO: replace this stub
    
    
    void term() {
    	factor();
    	if( lexer.inspect(AND) )
    	{
    		lexer.consume(AND);
    		factor();
    		
    		if( lexer.inspect(AND) )
        	{
        		lexer.consume(AND);
        		term();
        	}
    	}
    	
    	
    }
    
    void factor(){
    	if( lexer.inspect(NOT) )
    	{
    		lexer.consume(NOT);
    		factor();
    	}
    	else if( lexer.inspect("(") )
    	{
    		lexer.consume("(");
    		expr();
    		lexer.consume(")");
    	}
    	else if( lexer.inspectID() )
    	{
    		var();
    	}
    	else if ( lexer.inspect("'") )
    	{
    		constant();
    	}
    	else
    	{
    		lexer.consume(NOT,"(", "'");
    	}
    	
    }
    
    
    void constant() {
    	lexer.consume("'");
    	lexer.consume("0","1");
    	lexer.consume("'");
    }

// TODO: 45 lines snipped

    // helper functions
    private boolean peekConstant() {
        final boolean result = lexer.inspect("'"); //constants start (and end) with single quote
    	return result;
    }

}

