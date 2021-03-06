package ece351.w.rdescent;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ece351.util.CommandLine;
import ece351.util.ExaminableProperties;
import ece351.util.TestInputs351;
import ece351.util.Utils351;
import ece351.w.ast.WProgram;

@RunWith(Parameterized.class)
public final class TestWRDParserAccept {

	private final File wave;

	public TestWRDParserAccept(final File wave) {
		this.wave = wave;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> waveFiles() {
		return TestInputs351.waveFiles();
	}

	@Test
	public void parse() {
		final String inputSpec = wave.getAbsolutePath();
		final CommandLine c = new CommandLine(inputSpec);
		final String input = c.readInputSpec();
		System.out.println("processing " + inputSpec);
		System.out.println("input: ");
		System.out.println(input);
		// parse from the file to construct first AST
		final WProgram wp1 = WRecursiveDescentParser.parse(input);
		// pretty-print the first AST
		final String pp = wp1.toString();
		System.out.println("pretty-print: ");
		System.out.println(pp);
		// construct a second AST from the pretty-print
		final WProgram wp2 = WRecursiveDescentParser.parse(pp);
		// check that the two ASTs are isomorphic (syntactically the same)
		assertTrue("ASTs differ for " + inputSpec, wp1.isomorphic(wp2));
		// check examinable sanity
		ExaminableProperties.checkAllUnary(wp1);
		ExaminableProperties.checkAllUnary(wp2);
		ExaminableProperties.checkAllBinary(wp1, wp2);
		// success!
		System.out.println("accepted, as expected:  " + inputSpec);
	}

}
