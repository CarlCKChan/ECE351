package ece351.f.parboiled;

import java.io.File;

import ece351.f.AbstractTestFParser;
import ece351.f.ast.FProgram;


public final class TestFParboiledParser extends AbstractTestFParser {

	public TestFParboiledParser(final File f) {
		super(f);
	}

	@Override
	protected FProgram parse(final String input) {
		return FParboiledParser.parse(input);
	}

}
