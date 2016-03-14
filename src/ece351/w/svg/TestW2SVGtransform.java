package ece351.w.svg;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ece351.util.CommandLine;
import ece351.util.TestInputs351;
import ece351.util.Utils351;

/**
 * All this does is run your transformer and parse the output. This
 * is a basic sanity test, not a complete correctness test.
 */
@RunWith(Parameterized.class)
public final class TestW2SVGtransform {

	private final File wave;

	public TestW2SVGtransform(final File wave) {
		this.wave = wave;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> waveFiles() {
		final Collection<Object[]> result = TestInputs351.waveFiles();
		if (result == null || result.isEmpty()) {
			System.err.println("Couldn't find any wave files. Where are they? No tests run.");
		}
		return result;
	}

	@Test
	public void process() throws Exception {

		final String inputSpec = wave.getAbsolutePath();
		
		final String studentOut = svgpath(inputSpec, "student.out");
		System.out.println("transforming " + inputSpec);

		// produce the SVG
		final CommandLine c = new CommandLine("-h", "-f", studentOut, inputSpec);
		TransformW2SVG.main(c);

		// read student SVG file
		System.out.println("reading      " + studentOut);
		final WSVG studentwsvg = WSVG.fromSVG(c.getOutputFile().toURI(), c.parseDOM);
		System.out.println("parsed       " + studentOut);
	}

	private final static String FS = System.getProperty("file.separator");

	private static String svgpath(final String inputSpec, final String dir) {
		final int lastSlash = inputSpec.lastIndexOf(FS);
		return inputSpec.substring(0, lastSlash) + FS + dir
				+ inputSpec.substring(lastSlash).replace(".wave", ".svg");
	}

}
