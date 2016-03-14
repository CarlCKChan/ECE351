package ece351.f;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ece351.f.ast.FProgram;
import ece351.util.CommandLine;
import ece351.util.ExaminableProperties;
import ece351.util.TestInputs351;


@RunWith(Parameterized.class)
public abstract class AbstractTestFParser {

	public boolean checkEquivalent = false;
	
	private final File f;

	public AbstractTestFParser(final File f) {
		this.f = f;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> files() {
		return TestInputs351.formulaFiles();
	}

	abstract protected FProgram parse(final String input);
	
	@Test
	public void test() {
		final String inputSpec = f.getAbsolutePath();
		final CommandLine c = new CommandLine(inputSpec);
		final String input = c.readInputSpec();
		System.out.println("processing " + inputSpec);
		System.out.println("input: ");
		System.out.println(input);
		// parse from the file to construct first AST
		final FProgram fp1 = parse(input);
		// pretty-print the first AST
		final String pp = fp1.toString();
		System.out.println("pretty-print: ");
		System.out.println(pp);
		// construct a second AST from the pretty-print
		final FProgram fp2 = parse(pp);
		// check that the two ASTs are isomorphic (syntactically the same)
		assertTrue("ASTs differ for " + inputSpec, fp1.isomorphic(fp2));
		// check examinable sanity
		ExaminableProperties.checkAllUnary(fp1);
		ExaminableProperties.checkAllUnary(fp2);
		ExaminableProperties.checkAllBinary(fp1, fp2);
		// success!
		System.out.println("accepted, as expected:  " + inputSpec);

		// now check that this formula is different from all of the other formulas
		for (final Object[] a : TestInputs351.formulaFiles()) {
			final File otherFile = (File) a[0];

			// don't compare to self, we already did that above
			if (f.equals(otherFile)) continue;


			final String otherName = otherFile.getName();
			final String thisName = f.getName();
			final String otherPath = otherFile.getAbsolutePath();
			final CommandLine otherC = new CommandLine(otherPath);
			final String otherContent = otherC.readInputSpec();
			final FProgram otherFP = parse(otherContent);

			// basic sanity
			ExaminableProperties.checkAllBinary(fp1, otherFP);

			if (TestInputs351.knownIsomorphicFormulas(otherName, thisName)) {
				// known cases of isomorphism
				// should be isomorphic and equivalent (all the files we have meet both criteria)
				assertTrue("Unexpectedly non-isomorphic: " + inputSpec + " " + otherPath, fp1.isomorphic(otherFP));
				if (checkEquivalent) {
					assertTrue("Unexpectedly non-equivalent: " + inputSpec + " " + otherPath, fp1.equivalent(otherFP));
				}
			} else {
				// it's a different file, not known isomorphic
				// check that they are different
				assertFalse("Unexpectedly equals: " + inputSpec + " " + otherPath, fp1.equals(otherFP));
				assertFalse("Unexpectedly isomorphic: " + inputSpec + " " + otherPath, fp1.isomorphic(otherFP));
				if (checkEquivalent) {
					if (TestInputs351.knownEquivalentFormulas(otherName, thisName)) {
						assertTrue("Unexpectedly non-equivalent: " + inputSpec + " " + otherPath, fp1.equivalent(otherFP));
					} else {
						assertFalse("Unexpectedly equivalent: " + inputSpec + " " + otherPath, fp1.equivalent(otherFP));
					}
				}
			}
		}
	}



}
