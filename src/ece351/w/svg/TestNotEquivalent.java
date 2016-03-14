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
public class TestNotEquivalent {

	private final File studentsvgfile;
	private final Examiner examiner;

	protected TestNotEquivalent(final File f, final Examiner e) {
		this.studentsvgfile = f;
		this.examiner = e;
	}

	public TestNotEquivalent(final File f) {
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
		System.out.println(studentPath);
		final WSVG studentwsvg = WSVG.fromSVG(studentsvgfile.toURI(), false);

		for (final Object[] os : TestInputs351.studentSVGfiles()) {
			final File svgfile = (File) os[0];
			final String staffPath = svgfile.getAbsolutePath().replace("student.out", "staff.out");
			final URI staffURI = (new File(staffPath)).toURI();

			System.out.print("    comparing to " + staffPath);
			final WSVG staffwsvg = WSVG.fromSVG(staffURI, false);

			if (  (studentsvgfile.equals(svgfile))
			   || (studentsvgfile.getName().equals("gates0.svg") || (studentsvgfile.getName().equals("or.svg")))
			      && (svgfile.getName().equals("gates0.svg") || svgfile.getName().equals("or.svg"))
			   || (studentsvgfile.getName().equals("r2.svg") || studentsvgfile.getName().equals("r3.svg"))
			      && (svgfile.getName().equals("r2.svg") || svgfile.getName().equals("r3.svg"))
			   || (studentsvgfile.getName().equals("r6.svg") || studentsvgfile.getName().equals("r7.svg") || studentsvgfile.getName().equals("r8.svg"))
			      && (svgfile.getName().equals("r6.svg") || svgfile.getName().equals("r7.svg") || svgfile.getName().equals("r8.svg"))
			   ) {
				assertTrue(studentsvgfile.getName() + " is Not" + examiner.name() + " to staff's " + svgfile.getName(), examiner.examine(studentwsvg, staffwsvg));
				System.out.println(" --> " + examiner.name() + ", as expected.");
			}
			else if ( (studentsvgfile.getName().equals("r2.svg") || studentsvgfile.getName().equals("r3.svg") || studentsvgfile.getName().equals("level2.svg"))
					  && (svgfile.getName().equals("r2.svg") || svgfile.getName().equals("r3.svg") || svgfile.getName().equals("level2.svg"))
					){
				// ignored
			}
			else {
				assertFalse(studentsvgfile.getName() + " is " + examiner.name() + " to staff's " + svgfile.getName(), examiner.examine(studentwsvg, staffwsvg));
				System.out.println(" --> Not " + examiner.name() + ", as expected.");
			}
		}
	}
}
