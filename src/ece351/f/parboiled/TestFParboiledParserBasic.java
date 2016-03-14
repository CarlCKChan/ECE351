package ece351.f.parboiled;

import ece351.f.AbstractTestFParserBasic;
import ece351.f.ast.FProgram;


public final class TestFParboiledParserBasic extends AbstractTestFParserBasic {

	public TestFParboiledParserBasic() { super(); }

	@Override
	protected FProgram parse(final String input) {
		return FParboiledParser.parse(input);
	}

}
