package ece351.f.rdescent;

import java.io.File;

import ece351.f.AbstractTestFParser;
import ece351.f.ast.FProgram;


public final class TestFRDParser extends AbstractTestFParser {

	public TestFRDParser(final File f) {
		super(f);
	}

	@Override
	protected FProgram parse(final String input) {
		return FRecursiveDescentParser.parse(input);
	}
	
}
