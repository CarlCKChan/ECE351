package ece351.w.rdescent;

import org.parboiled.common.ImmutableList;

import ece351.util.Lexer;
import ece351.w.ast.WProgram;
import ece351.w.ast.Waveform;

public final class WRecursiveDescentParser {
    private final Lexer lexer;

    public WRecursiveDescentParser(final Lexer lexer) {
        this.lexer = lexer;
    }

    public static WProgram parse(final String input) {
    	final WRecursiveDescentParser p = new WRecursiveDescentParser(new Lexer(input));
        return p.parse();
    }

    public WProgram parse() {
    	
    	/** Use the program() function to get the AST (by convention the top-level production
    	 *  method is called program) */
    	return ( program() );
    }
    
    
    public WProgram program() {
    	
    	/** This is a list of waveforms which will be used to generate the WProgram object */
    	ImmutableList<Waveform> waveforms = ImmutableList.of();
    	
    	/**
    	 * 	Until reaching the end of the file, generate the sub-AST for each individual
    	 *  waveform and append it to the list.  This is done with calls to the "waveform"
    	 *  production method
    	 */
        while (!lexer.inspectEOF()) {
            waveforms = waveforms.append( waveform() );
        }
        lexer.consumeEOF();
        
        /** Use the waveform list to make the WProgram object to return */
        WProgram ret = new WProgram( waveforms );        
        return (ret);
    }

    public Waveform waveform() {
    	
    	String myName = lexer.consumeID();		/** Recognizing the ID production does not have its own method since it is small */
    	lexer.consume(":");						/** Check for the : separator */
    	ImmutableList<String> myBits = bits();	/** Check the bits production */
    	lexer.consume(";");						/** Check that the line is delimited by a semi-colon */
    	
    	Waveform ret = new Waveform(myBits, myName);
    	return (ret);
    }
    
    /**
     * This method will parse the production "bits" by looking at the bits of the wave file
     * */
    public ImmutableList<String> bits() {
    	
    	ImmutableList<String> myBits = ImmutableList.of();
    	
    	/** The bits should be a stream of tokens with values "0" or "1", ending in ";" */
    	while ( !lexer.inspect(";")) {
    		myBits = myBits.append( lexer.consume("0","1") );
    	}
    	
    	return (myBits);
    }
    
}
