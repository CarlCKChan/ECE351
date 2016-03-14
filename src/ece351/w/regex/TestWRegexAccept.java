package ece351.w.regex;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ece351.util.CommandLine;
import ece351.util.TestInputs351;
import ece351.util.Utils351;

@RunWith(Parameterized.class)
public final class TestWRegexAccept {

	/**
	 * Now you have a fairly general regex. Copy and paste it into this static
	 * variable and let's try to match it against all of the test wave files.
	 */
	final static String REGEX = "(([A-Za-z])+( )*:((( )*;)|(( )*(0|1)(( )+(0|1))*( )*;))( )*\\s*)*";
// TODO: 2 lines snipped

	
	private final File wave;

	public TestWRegexAccept(final File wave) {
		this.wave = wave;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> waveFiles() {
		return TestInputs351.waveFiles();
	}

	@Test
	public void accept() {
		TestWRegexSimple.process(true, REGEX, wave.getAbsolutePath());
	}
	
}
