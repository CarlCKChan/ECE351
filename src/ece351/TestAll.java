package ece351;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import ece351.f.parboiled.TestFParboiledParser;
import ece351.f.parboiled.TestFParboiledRecognizer;
import ece351.f.parboiled.TestFParserComparison;
import ece351.f.rdescent.TestFRDParser;
import ece351.f.rdescent.TestFRDParserBasic;
import ece351.f.rdescent.TestFRDRecognizerAccept;
import ece351.f.rdescent.TestFRDRecognizerReject;
import ece351.w.parboiled.TestWParboiledParserAccept;
import ece351.w.parboiled.TestWParboiledParserBasic;
import ece351.w.parboiled.TestWParboiledParserReject;
import ece351.w.parboiled.TestWParboiledRecognizerAccept;
import ece351.w.parboiled.TestWParboiledRecognizerReject;
import ece351.w.rdescent.TestWRDParserAccept;
import ece351.w.rdescent.TestWRDParserReject;
import ece351.w.rdescent.TestWRDRecognizerAccept;
import ece351.w.rdescent.TestWRDRecognizerReject;
import ece351.w.regex.TestWRegexAccept;
import ece351.w.regex.TestWRegexReject;
import ece351.w.regex.TestWRegexSimple;
import ece351.w.svg.TestEquivalent;
import ece351.w.svg.TestIsomorphic;
import ece351.w.svg.TestNotEquivalent;
import ece351.w.svg.TestNotIsomorphic;
import ece351.w.svg.TestW2SVGtransform;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	// W Regex
	TestWRegexSimple.class,
	TestWRegexAccept.class,
	TestWRegexReject.class,
	// W Recursive Descent
	TestWRDRecognizerAccept.class,
	TestWRDRecognizerReject.class,
	TestWRDParserAccept.class,
	TestWRDParserReject.class,
	// W Parboiled
	TestWParboiledRecognizerAccept.class,
	TestWParboiledRecognizerReject.class,
	TestWParboiledParserBasic.class,
	TestWParboiledParserAccept.class,
	TestWParboiledParserReject.class,
	// W to SVG
	TestW2SVGtransform.class,
	TestEquivalent.class,
	TestIsomorphic.class,
	TestNotEquivalent.class,
	TestNotIsomorphic.class,
	// F recursive descent
	TestFRDRecognizerAccept.class,
	TestFRDRecognizerReject.class,
	TestFRDParserBasic.class,
	TestFRDParser.class,
	// F Parboiled
	TestFParboiledRecognizer.class,
	TestFParboiledParser.class,
	TestFParserComparison.class,
})
public class TestAll {}
