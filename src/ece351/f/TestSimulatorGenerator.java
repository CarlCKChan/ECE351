package ece351.f;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ece351.f.ast.FProgram;
import ece351.util.CommandLine;
import ece351.util.CommandLine.FSimplifierOptions;
import ece351.util.ExaminableProperties;
import ece351.util.TestInputs351;


@RunWith(Parameterized.class)
public final class TestSimulatorGenerator {

	private final File f;

	public TestSimulatorGenerator(final File f) {
		this.f = f;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> files() {
		return TestInputs351.formulaFiles("cse.*");
	}
	
	@Test
	public void simgen() throws IOException {
		final String inputSpec = f.getAbsolutePath();
		final CommandLine c = new CommandLine("-p", "-o4", inputSpec);
		final String input = c.readInputSpec();
		System.out.println("processing " + inputSpec);
		System.out.println("input: ");
		System.out.println(input);
		
		// parse from the file to construct first AST
		final FProgram original = FParser.parse(c);
		FProgram simplified = original;
    	if (c.simplifierOpts.contains(FSimplifierOptions.STANDARDIZE)) {
    		simplified = simplified.standardize();
    	}
        simplified = simplified.simplify(c.simplifierOpts);
    	System.out.println("simplified:");
    	System.out.println(simplified.toString());
		
		// check that the two ASTs are NOT isomorphic (the optimization should have done something)
		assertFalse("ASTs do not differ for " + inputSpec, original.isomorphic(simplified));
		
		// check examinable sanity
		ExaminableProperties.checkAllUnary(original);
		ExaminableProperties.checkAllUnary(simplified);
		ExaminableProperties.checkAllBinary(original, simplified);

		// render the output
		final StringWriter sw = new StringWriter();
		final SimulatorGenerator sg = new SimulatorGenerator();
		sg.generate(c.getInputName(), simplified, new PrintWriter(sw));
		sw.close();
		final String javasim = sw.toString();
		System.out.println("output:");
		System.out.println(javasim);

		// compile the output
//		final JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
		// test the compiled output
		
		
		// success!
		System.out.println("so far, so good. still need to compile and run.  " + inputSpec);
	}

}
