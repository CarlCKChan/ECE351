package ece351.w.rdescent;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ece351.util.CommandLine;
import ece351.util.TestInputs351;
import ece351.util.Utils351;

@RunWith(Parameterized.class)
public final class TestWRDParserReject {

	private final File wave;

	public TestWRDParserReject(final File wave) {
		this.wave = wave;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> badWaveFiles() {
		return TestInputs351.badWaveFiles();
	}

	@Test
	public void reject() {
		String inputSpec = wave.getAbsolutePath();
		final CommandLine c = new CommandLine(inputSpec);
		final String input = c.readInputSpec();
		try {
			WRecursiveDescentParser.parse(input);
			fail("should have rejected but didn't:  " + inputSpec);
		} catch (final Exception e) {
			System.out.println("rejected, as expected:  " + inputSpec);
		}
	}
	
}
