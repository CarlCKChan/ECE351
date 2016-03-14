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
import ece351.util.Examiner;
import ece351.util.TestInputs351;
import ece351.util.Utils351;

/** 
 * Test that your SVG files are isomorphic to the staff SVG files.
 * Run this after TestW2SVGtransform and TestW2SVGequivalent.
 */
@Deprecated
@RunWith(Parameterized.class)
public final class TestW2SVGisomorphic extends TestW2SVGequivalent {

	public TestW2SVGisomorphic(final File f) {
		super(f, Examiner.Isomorphic);
	}

}
