package ece351.f.parboiled;

import org.parboiled.Rule;
import ece351.util.BaseParser351;

public abstract class FBase extends BaseParser351 {
	
	Rule Char() {
    	return FirstOf( CharRange('a', 'z'), CharRange('A', 'Z') );
	}
	
	Rule Digit() {
    	return CharRange('0', '9');
	}
	
	public Rule Keyword() {
		return FirstOf(
				AND(),
				OR(),
				NOT(),
				TestNot(FirstOf(Char(),Digit(), "_"))
			);
	}
	
	public Rule AND()	{ return Sequence(IgnoreCase("and"), Test(AnyOf("( \t\n\r\f")), W0()); }
	public Rule OR()	{ return Sequence(IgnoreCase("or"), Test(AnyOf("( \t\n\r\f")), W0()); }
	public Rule NOT()	{ return Sequence(IgnoreCase("not"), Test(AnyOf("( \t\n\r\f")), W0()); }

}
