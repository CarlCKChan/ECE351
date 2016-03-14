package ece351.w.parboiled;

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
public final class TestWParboiledRecognizerAccept {

	private final File wave;

	public TestWParboiledRecognizerAccept(final File wave) {
		this.wave = wave;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> waveFiles() {
		return TestInputs351.waveFiles();
	}

	@Test
	public void accept() {
		final String inputSpec = wave.getAbsolutePath();
		final CommandLine c = new CommandLine(inputSpec);
		final String input = c.readInputSpec();
		WParboiledRecognizer.printParseTree(input);
		System.out.println("accepted, as expected:  " + inputSpec);
	}

}
