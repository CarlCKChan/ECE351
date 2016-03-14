package ece351.f.parboiled;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ece351.f.ast.FProgram;
import ece351.f.rdescent.FRecursiveDescentParser;
import ece351.util.CommandLine;
import ece351.util.ExaminableProperties;
import ece351.util.TestInputs351;


@RunWith(Parameterized.class)
public final class TestFParserComparison {

	public boolean checkEquivalent = false;
	
	private final File f;

	public TestFParserComparison(final File f) {
		this.f = f;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> files() {
		return TestInputs351.formulaFiles();
	}

	@Test
	public void testComparison() {
		final String inputSpec = f.getAbsolutePath();
		final CommandLine c = new CommandLine(inputSpec);
		final String input = c.readInputSpec();
		System.out.println("processing " + inputSpec);
		System.out.println("input: ");
		System.out.println(input);

		// parse from both Parboiled and Recursive Descent
		final FProgram fp1 = FParboiledParser.parse(input);
		final FProgram fp2 = FRecursiveDescentParser.parse(input);

		// should be isomorphic
		assertTrue("Different parsers giving non-isomorphic results for " + inputSpec, fp1.isomorphic(fp2));
		
		// check examinable sanity
		ExaminableProperties.checkAllUnary(fp1);
		ExaminableProperties.checkAllUnary(fp2);
		ExaminableProperties.checkAllBinary(fp1, fp2);
		
	}
}
