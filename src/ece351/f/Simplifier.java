package ece351.f;

import ece351.f.ast.FProgram;
import ece351.util.CommandLine;
import ece351.util.CommandLine.FSimplifierOptions;

/**
 * Command line interface for simplifier.
 * Not used for ordinary testing.
 *
 */
public final class Simplifier {

	/**
	 * @param args
	 */
    public static void main(final String[] args) {
    	final CommandLine c = new CommandLine(args);
    	FProgram program = FParser.parse(args);
    	if (c.simplifierOpts.contains(FSimplifierOptions.STANDARDIZE)) {
    		program = program.standardize();
    	}
        final FProgram simplified = program.simplify(c.simplifierOpts);
        System.out.println(simplified);
    }
}
