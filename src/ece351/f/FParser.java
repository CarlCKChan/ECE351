package ece351.f;

import ece351.f.ast.FProgram;
import ece351.f.parboiled.FParboiledParser;
import ece351.f.rdescent.FRecursiveDescentParser;
import ece351.util.CommandLine;
import ece351.util.Lexer;

public final class FParser {
	
    public static FProgram parse(final String[] args) {
    	final CommandLine c = new CommandLine(args);
    	return parse(c);
    }

	public static FProgram parse(final CommandLine c) {
		if (c.handparser) {
    		return handParse(c.readInputSpec());
    	} else {
    		return parboiledParse(c.readInputSpec());
    	}
	}
    
    private static FProgram handParse(final String input) {
        final Lexer lexer = new Lexer(input);
        final FRecursiveDescentParser parser = new FRecursiveDescentParser(lexer);
        return parser.parse();
    }
    
    private static FProgram parboiledParse(final String input) {
    	return FParboiledParser.parse(input);
    }
}
