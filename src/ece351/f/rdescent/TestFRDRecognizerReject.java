package ece351.f.rdescent;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
public final class TestFRDRecognizerReject {

	private final File f;

	public TestFRDRecognizerReject(final File f) {
		this.f = f;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> files() {
		return TestInputs351.badFormulaFiles();
	}

	@Test
	public void accept() {
		final String inputSpec = f.getAbsolutePath();
		final CommandLine c = new CommandLine(inputSpec);
		final String input = c.readInputSpec();
		try {
			FRecursiveDescentRecognizer.main(input);
			fail("should have rejected but didn't:  " + inputSpec);
		} catch (final Exception e) {
			System.out.println("rejected, as expected:  " + inputSpec);
		}
	}


}
