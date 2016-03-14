package ece351.w.parboiled;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.parboiled.common.ImmutableList;

import ece351.util.ExaminableProperties;
import ece351.w.ast.WProgram;
import ece351.w.ast.Waveform;

public class TestWParboiledParserBasic {

	@Test
	public void test() {
		// explicitly construct an AST
		final Waveform x = new Waveform(ImmutableList.of("0"), "X");
		final Waveform y = new Waveform(ImmutableList.of("1"), "Y");
		final WProgram wp1 = new WProgram(ImmutableList.of(x).append(y));
		
		// now parse a string that should construct it
		final WProgram wp2 = WParboiledParser.parse("X: 0; Y: 1;");
		
		// compare
		assertTrue("ASTs not equals", wp1.equals(wp2));
		assertTrue("ASTs not isomorphic", wp1.isomorphic(wp2));
		assertTrue("ASTs not equivalent", wp1.equivalent(wp2));

		// sanity
		ExaminableProperties.checkAllUnary(wp1);
		ExaminableProperties.checkAllUnary(wp2);
		ExaminableProperties.checkAllBinary(wp1, wp2);
	}

}
