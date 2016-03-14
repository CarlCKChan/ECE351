package ece351.f.parboiled;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ece351.f.ast.FProgram;
import ece351.util.CommandLine;
import ece351.util.TestInputs351;
import ece351.w.ast.WProgram;
import ece351.w.rdescent.WRecursiveDescentParser;
import ece351.w.rdescent.WRecursiveDescentRecognizer;


@RunWith(Parameterized.class)
public final class TestFParboiledRecognizer {

	private final File f;

	public TestFParboiledRecognizer(final File f) {
		this.f = f;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> files() {
		return TestInputs351.formulaFiles();
	}

	@Test
	public void accept() {
		final String inputSpec = f.getAbsolutePath();
		final CommandLine c = new CommandLine(inputSpec);
		final String input = c.readInputSpec();
		FParboiledRecognizer.main(input);
		System.out.println("accepted, as expected:  " + inputSpec);
	}


}
