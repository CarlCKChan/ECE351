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
public final class TestWRegexReject {

	private final File wave;

	public TestWRegexReject(final File wave) {
		this.wave = wave;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> badWaveFiles() {
		return TestInputs351.badWaveFiles();
	}

	@Test
	public void reject() {
		TestWRegexSimple.process(false, TestWRegexAccept.REGEX, wave.getAbsolutePath());
	}
	
}
