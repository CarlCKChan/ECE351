package ece351.w.rdescent;

import ece351.util.Lexer;

public final class WRecursiveDescentRecognizer {
    private final Lexer lexer;

    public WRecursiveDescentRecognizer(final Lexer lexer) {
        this.lexer = lexer;
    }

    public static void recognize(final String input) {
    	final WRecursiveDescentRecognizer r = new WRecursiveDescentRecognizer(new Lexer(input));
        r.recognize();
    }

    /**
     * Throws an exception to reject.
     */
    public void recognize() {
        program();
    }

    public void program() {
        while (!lexer.inspectEOF()) {
            waveform();
        }
        lexer.consumeEOF();
    }

    public void waveform() {
    	lexer.consumeID();		/** Recognizing the ID production does not have its own method since it is small */
    	lexer.consume(":");		/** Check for the : separator */
    	bits();					/** Check the bits production */
    	lexer.consume(";");		/** Check that the line is delimited by a semi-colon */
    }
    
    /**
     * This method will recognize the production "bits" by looking at the bits of the wave file
     * */
    public void bits() {
    	
    	/** The bits should be a stream of tokens with values "0" or "1", ending in ";" */
    	while ( !lexer.inspect(";")) {
    		lexer.consume("0","1");
    	}
    }

}
