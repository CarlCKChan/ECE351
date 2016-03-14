package ece351.f;

import ece351.f.ast.FProgram;

/**
 * Command line interface for standardizer.
 * Not used for ordinary testing.
 *
 */
public final class Standardizer {

	/**
	 * @param args
	 */
    public static void main(final String[] args) {
    	FProgram program = FParser.parse(args);
        program = program.standardize();
        System.out.println(program);
    }
    
}
