package ece351.w.svg;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.net.URI;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ece351.util.CommandLine;
import ece351.util.ExaminableProperties;
import ece351.util.Examiner;
import ece351.util.TestInputs351;
import ece351.w.ast.WProgram;
import ece351.w.rdescent.WRecursiveDescentParser;

/** 
 * Test that your SVG files are equivalent to the staff SVG files.
 * Run this after TestW2SVGtransform and before TestW2SVGisomorphic.
 */
@RunWith(Parameterized.class)
public class TestEquivalent {

	private final File studentsvgfile;
	private final Examiner examiner;

	protected TestEquivalent(final File f, final Examiner e) {
		this.studentsvgfile = f;
		this.examiner = e;
	}

	public TestEquivalent(final File f) {
		this(f, Examiner.Equivalent);
	}
	
	@Parameterized.Parameters
	public static Collection<Object[]> studentSVGfiles() {
		final Collection<Object[]> result = TestInputs351.studentSVGfiles();
		if (result == null || result.isEmpty()) {
			System.err.println("Couldn't find any svg files. Where are they? No tests run.");
		}
		return result;
	}

	@Test
	public void process() throws Exception {

		final String studentPath = studentsvgfile.getAbsolutePath();
		
		// read student SVG file
		System.out.println("reading      " + studentPath);
		final WSVG studentwsvg = WSVG.fromSVG(studentsvgfile.toURI(), false);

		// read staff SVG file
		final String staffPath = studentPath.replace("student.out", "staff.out");
		final URI staffURI = (new File(staffPath)).toURI();
		System.out.println("             " + staffPath);
		final WSVG staffwsvg = WSVG.fromSVG(staffURI, false);
		
		// overall sanity properties
		ExaminableProperties.checkAllUnary(studentwsvg);
		ExaminableProperties.checkAllUnary(staffwsvg);
		ExaminableProperties.checkAllBinary(staffwsvg, studentwsvg);
		
		// comparison of SVG
		assertTrue(examiner.examine(studentwsvg, staffwsvg));
		System.out.println("             " + examiner);
		
	}
}
