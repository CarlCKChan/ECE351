package ece351.f.rdescent;

import ece351.f.AbstractTestFParserBasic;
import ece351.f.ast.FProgram;


public final class TestFRDParserBasic extends AbstractTestFParserBasic {

	public TestFRDParserBasic() { super(); }

	@Override
	protected FProgram parse(final String input) {
		return FRecursiveDescentParser.parse(input);
	}

}
