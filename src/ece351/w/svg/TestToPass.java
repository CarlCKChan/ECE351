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
 * Edit this test to run the test suite that you want. 
 * The default is to run the original tests of the isomorphic method.
 */
@RunWith(Parameterized.class)
public final class TestToPass extends TestIsomorphic {

	public TestToPass(final File f) {
		super(f);
	}

}
