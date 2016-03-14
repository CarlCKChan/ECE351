package ece351.f;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ece351.f.ast.FProgram;
import ece351.f.rdescent.FRecursiveDescentParser;
import ece351.util.CommandLine;
import ece351.util.CommandLine.FSimplifierOptions;
import ece351.util.ExaminableProperties;
import ece351.util.TestInputs351;


@RunWith(Parameterized.class)
public final class TestSimplifier {

	private final File f;

	public TestSimplifier(final File f) {
		this.f = f;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> files() {
		return TestInputs351.formulaFiles("opt.*");
	}

	@Test
	public void simplify() throws IOException {
		final String inputSpec = f.getAbsolutePath();
		final CommandLine c = new CommandLine("-h", "-o4", inputSpec);
		final String input = c.readInputSpec();
		System.out.println("processing " + inputSpec);
		System.out.println("input: ");
		System.out.println(input);
		
		// parse from the file to construct first AST
		final FProgram original = FRecursiveDescentParser.parse(input);
		System.out.println("input after parsing + pretty-printing:");
		System.out.println(original.toString());
		FProgram simplified = original;
    	if (c.simplifierOpts.contains(FSimplifierOptions.STANDARDIZE)) {
    		simplified = simplified.standardize();
    	}
    	System.out.println("standardized:");
    	System.out.println(simplified.toString());
        simplified = simplified.simplify(c.simplifierOpts);
		System.out.println("ouput: ");
		System.out.println(simplified.toString());
		
		// check that the two ASTs are NOT isomorphic (the optimization should have done something)
		assertFalse("ASTs do not differ for " + inputSpec, original.isomorphic(simplified));

		// ok, something has happened
		// was it the right thing?

		final String s = File.separator;
		final String staffSpec = inputSpec.replace( s + "f" + s, s + "f" + s + "staff.out" + s + "simplified" + s );
		
		final CommandLine c3 = new CommandLine(staffSpec);
		final FProgram staff = FRecursiveDescentParser.parse(staffSpec).standardize();
		System.out.println("expected output:");
		System.out.println(staff.toString());
		assertTrue("ASTs differ for " + inputSpec, simplified.isomorphic(staff));
		
		// check examinable sanity
		ExaminableProperties.checkAllUnary(original);
		ExaminableProperties.checkAllUnary(simplified);
		ExaminableProperties.checkAllUnary(staff);
		ExaminableProperties.checkAllBinary(original, simplified);
		ExaminableProperties.checkAllBinary(original, staff);
		ExaminableProperties.checkAllBinary(simplified, staff);
		ExaminableProperties.checkAllTernary(original, simplified, staff);
		
		// success!
		System.out.println("success!  " + inputSpec);
	}

}
