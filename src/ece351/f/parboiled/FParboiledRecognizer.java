package ece351.f.parboiled;

import org.parboiled.Rule;

import ece351.util.CommandLine;
import ece351.vhdl.VConstants;

//Parboiled requires that this class not be final
public /*final*/ class FParboiledRecognizer extends FBase implements VConstants {

	
	public static void main(final String... args) {
		final CommandLine c = new CommandLine(args);
    	process(FParboiledRecognizer.class, c.readInputSpec());
    }

	@Override
	public Rule Program() {
		//return NOTHING; // TODO: replace this stub
		// For the grammar production Id, ensure that the Id does not match any of the keywords specified
		// in the rule, 'Keyword'
// TODO: 39 lines snipped
		return( Sequence(W0(), ZeroOrMore( Formula(), W0() ), EOI ) );
	}
	
	public Rule Formula() {
		return Sequence(Var(), W0(), "<=", W0(), Expr(), W0(), ";");
	}
	
	public Rule Expr() {
		return Sequence( Term(), ZeroOrMore( W0(), OR, TestNot(NotALetter()), W0(), Term() ) );
	}
	
	public Rule Term() {
		return Sequence( Factor(), ZeroOrMore( W0(), AND, TestNot(NotALetter()), W0(), Factor() ) );
	}
	
	public Rule Factor() {
		return FirstOf(
					Sequence( NOT, TestNot(NotALetter()), W0(), Factor() ),
					Sequence( "(", W0(), Expr(), W0(), ")" ),
					Var(),
					Constant()
				);
	}
	
	public Rule Constant() {
		return Sequence( "'", FirstOf("0","1"), "'" );
	}
	
	public Rule Var() {
		return Sequence(
					Sequence( Letter(), ZeroOrMore( FirstOf(Letter(), Number()) ) ),
					!(match().equals(AND)),
					!(match().equals(NOR)),
					!(match().equals(NOT)),
					!(match().equals(NAND)),
					!(match().equals(OR)),
					!(match().equals(XNOR)),
					!(match().equals(XOR))
				);
	}
	
    public Rule Letter() {
    	return( FirstOf( CharRange('A','Z'), CharRange('a','z') ) );
    }
    
    public Rule Number() {
    	return( CharRange('0','9') );
    }
    
    
    //This should only be passed to TestNot() to test if something is not a letter,
    //since this actually tests for whether something is a letter.
    public Rule NotALetter() {
    	return( Letter() );
    }

}
