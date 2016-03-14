package ece351.w.regex;

import static org.junit.Assert.*;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import ece351.util.CommandLine;
import ece351.util.Utils351;

public final class TestWRegexSimple {

	/**
	 * This regular expression is exactly the same as the input,
	 * so the test passes. 
	 */
	@Test
	public void testr1() {
		final String regex = "A: 0;\\s*";
		recognize(regex, "tests/wave/r1.wave");
	}

	/**
	 * Copy the regex from testr1 and paste it in the TODO of this test.
	 * Run the test suite to confirm that it fails on the new input.
	 * Generalize the regular expression so that it accepts the new input.
	 */
	@Test
	public void testr2() {
		final String regex = "A:( )*(( )*(0|1))+;\\s*";
// TODO: 2 lines snipped
		recognize(regex, "tests/wave/r2.wave");
	}

	/**
	 * Copy the regex from above and paste it in the TODO of this test.
	 * Run the test suite to confirm that it fails on the new input.
	 * Generalize the regular expression so that it accepts the new input.
	 */
	@Test
	public void testr3() {
		final String regex = "A:( )*(( )*(0|1))+( )*;( )*\\s*";
// TODO: 2 lines snipped
		recognize(regex, "tests/wave/r3.wave");
	}

	/**
	 * Copy the regex from above and paste it in the TODO of this test.
	 * Run the test suite to confirm that it fails on the new input.
	 * Generalize the regular expression so that it accepts the new input.
	 */
	@Test
	public void testr4() {
		final String regex = "([A-Za-z])+( )*:( )*(( )*(0|1))+( )*;( )*\\s*";
// TODO: 2 lines snipped
		recognize(regex, "tests/wave/r4.wave");
	}

	/**
	 * Copy the regex from above and paste it in the TODO of this test.
	 * Run the test suite to confirm that it fails on the new input.
	 * Generalize the regular expression so that it accepts the new input.
	 */
	@Test
	public void testr5() {
		final String regex = "(([A-Za-z])+( )*:( )*(( )*(0|1))+( )*;( )*\\s*)*";
// TODO: 2 lines snipped
		recognize(regex, "tests/wave/r5.wave");
	}

	/**
	 * Copy the regex from above and paste it in the TODO of this test.
	 * Run the test suite to confirm that it fails on the new input.
	 * Generalize the regular expression so that it accepts the new input.
	 */
	@Test
	public void testr6() {
		final String regex = "(([A-Za-z])+( )*:( )*(( )*(0|1))+( )*;( )*\\s*)*";
// TODO: 2 lines snipped
		recognize(regex, "tests/wave/r6.wave");
	}

	/**
	 * Copy the regex from above and paste it in the TODO of this test.
	 * Run the test suite to confirm that it fails on the new input.
	 * Generalize the regular expression so that it accepts the new input.
	 */
	@Test
	public void testr7() {
		final String regex = "(([A-Za-z])+( )*:( )*(( )*(0|1))+( )*;( )*\\s*)*";
// TODO: 2 lines snipped
		recognize(regex, "tests/wave/r7.wave");
	}

	/**
	 * Copy the regex from above and paste it in the TODO of this test.
	 * Run the test suite to confirm that it fails on the new input.
	 * Generalize the regular expression so that it accepts the new input.
	 */
	@Test
	public void testr8() {
		final String regex = "(([A-Za-z])+( )*:( )*(( )*(0|1))+( )*;( )*\\s*)*";
// TODO: 2 lines snipped
		recognize(regex, "tests/wave/r8.wave");
	}

	/**
	 * Copy the regex from above and paste it in the TODO of this test.
	 * Run the test suite to confirm that it fails on the new input.
	 * Generalize the regular expression so that it accepts the new input.
	 */
	@Test
	public void testr9() {
		final String regex = "(([A-Za-z])+( )*:( )*(( )*(0|1))+( )*;( )*\\s*)*";
// TODO: 2 lines snipped
		recognize(regex, "tests/wave/r9.wave");
	}


	
	protected static void recognize(final String regex, final String inputSpec) {
		process(true, regex, inputSpec);
	}
	
	/**
	 * Evaluate regex on inputSpec. InputSpec can be either a string of a
	 * putative W program or it can be the name of a file that contains a
	 * putative W program.
	 * @param regex
	 * @param inputSpec
	 */
	protected static void process(final boolean expect, final String regex, final String inputSpec) {
		final CommandLine c = new CommandLine(inputSpec);
		final Pattern p = Pattern.compile(regex, Pattern.DOTALL);
		final String input = c.readInputSpec();
		final Matcher m = p.matcher(input);
		if (expect) {
			assertTrue("\"" + input + "\" from " + c.getInputFile() + " doesn't match " + regex, m.matches());
			System.out.println("accepted, as expected:  " + inputSpec);
		} else {
			assertFalse(m.matches());
			System.out.println("rejected, as expected:  " + inputSpec);
		}
	}
}
