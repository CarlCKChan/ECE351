package ece351.w.svg;

import static org.junit.Assert.assertTrue;

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
@Deprecated
@RunWith(Parameterized.class)
public class TestW2SVGequivalent {

	private final File studentsvgfile;
	private final Examiner examiner;
	private final boolean compareInferredW;

	protected TestW2SVGequivalent(final File f, final Examiner e, final boolean b) {
		this.studentsvgfile = f;
		this.examiner = e;
		this.compareInferredW = b;
	}

	protected TestW2SVGequivalent(final File f, final Examiner e) {
		this(f, e, false);
	}

	public TestW2SVGequivalent(final File f) {
		this(f, Examiner.Equivalent, false);
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

		if (!compareInferredW) {
			/*
			 * Not sure why each element in studentSVGfiles is an Object array with
			 * only 1 element. This code is quite possibly broken with respect to the
			 * rest.
			 */
			for (Object[] os : TestInputs351.studentSVGfiles()) {
				File svgfile = (File) os[0];
				final String staffPath = svgfile.getAbsolutePath().replace("student.out", "staff.out");
				final URI staffURI = (new File(staffPath)).toURI();

				System.out.println("reading      " + staffPath);
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
					System.out.println(examiner.name());
				}
				else if ( (studentsvgfile.getName().equals("r2.svg") || studentsvgfile.getName().equals("r3.svg") || studentsvgfile.getName().equals("level2.svg"))
						  && (svgfile.getName().equals("r2.svg") || svgfile.getName().equals("r3.svg") || svgfile.getName().equals("level2.svg"))
						){
					// ignored
				}
				else {
					assertTrue(studentsvgfile.getName() + " is " + examiner.name() + " to staff's " + svgfile.getName(), !examiner.examine(studentwsvg, staffwsvg));
					System.out.println("Not" + examiner.name());
				}
			}
		}
		else {
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

			// read original W file
			final String originalWPath = studentPath.replace("student.out" + File.separator, "").replace(".svg", ".wave");

			final CommandLine c = new CommandLine(originalWPath);
			final String originalWtxt = c.readInputSpec();
			final WProgram originalWP = WRecursiveDescentParser.parse(originalWtxt);

			// SVG -> W
			final WProgram staffwp = TransformSVG2W.transform(staffwsvg);
			final WProgram studentwp = TransformSVG2W.transform(studentwsvg);

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
}
