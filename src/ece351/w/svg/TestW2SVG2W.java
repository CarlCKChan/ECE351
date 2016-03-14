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
import ece351.util.ExaminableProperties;
import ece351.util.Examiner;
import ece351.util.TestInputs351;
import ece351.util.Utils351;
import ece351.w.ast.WProgram;
import ece351.w.rdescent.WRecursiveDescentParser;

/** 
 * Test that your SVG files are equivalent to the staff SVG files.
 * Run this after TestW2SVGtransform and before the other SVG equivalence tests.
 * This is likely to be the most accurate of the SVG equivalence tests.
 */
@RunWith(Parameterized.class)
public final class TestW2SVG2W {

	private final File studentsvgfile;

	public TestW2SVG2W(final File f) {
		this.studentsvgfile = f;
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
		System.out.println("reading      " + staffPath);
		final WSVG staffwsvg = WSVG.fromSVG(staffURI, false);
		
		// overall sanity properties
		ExaminableProperties.checkAllUnary(studentwsvg);
		ExaminableProperties.checkAllUnary(staffwsvg);
		ExaminableProperties.checkAllBinary(staffwsvg, studentwsvg);
		
		
		// infer W from the SVG and then compare the W

		// SVG -> W
		final WProgram staffwp = TransformSVG2W.transform(staffwsvg);
		final WProgram studentwp = TransformSVG2W.transform(studentwsvg);

		// read original W file
		final String originalWPath = studentPath.replace("student.out" + File.separator, "").replace(".svg", ".wave");
		final CommandLine c = new CommandLine(originalWPath);
		final String originalWtxt = c.readInputSpec();
		final WProgram originalWP = WRecursiveDescentParser.parse(originalWtxt);

		// Overall sanity check
		ExaminableProperties.checkAllUnary(originalWP);
		ExaminableProperties.checkAllUnary(studentwp);
		ExaminableProperties.checkAllUnary(staffwp);
		ExaminableProperties.checkAllBinary(staffwp, studentwp);
		ExaminableProperties.checkAllBinary(originalWP, staffwp);
		ExaminableProperties.checkAllBinary(originalWP, studentwp);
		ExaminableProperties.checkAllTernary(originalWP, staffwp, studentwp);

		// W equivalence checks
		assertTrue(originalWP.equivalent(studentwp));
		assertTrue(originalWP.equivalent(staffwp));
		assertTrue(staffwp.equivalent(studentwp));
	}

}
