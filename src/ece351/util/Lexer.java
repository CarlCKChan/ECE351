package ece351.util;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.parboiled.common.FileUtils;

/**
 * Breaks a stream of input into tokens by wrapping the StreamTokenizer
 * JDK class. The JDK provides two tokenizer classes: StringTokenizer and
 * StreamTokenizer. The former is easier to use but not as powerful. The 
 * latter is more powerful but less easy to use. This class wraps 
 * StreamTokenizer for you to give you an easier interface.
 * 
 * @author drayside
 * @see java.util.StreamTokenizer
 * @see java.util.StringTokenizer
 */
public final class Lexer {

    protected final static Pattern ID = Pattern.compile("([A-Z]|[a-z])+");
    
	private final StreamTokenizer t;
	
	public Lexer(final String input) {
		this.t = new StreamTokenizer(new StringReader(input));
		t.ordinaryChar('.'); // otherwise it lexes . as a number
		t.ordinaryChars('0', '9'); // lex all digits as ordinary characters
		t.ordinaryChar('\''); // otherwise it lexes ' as a string quote
		t.wordChars('<', '='); // for assignment in F+VHDL
		t.eolIsSignificant(false); // semi-colons separate statements
		advance();
	}

	public String consume(final String s) {
		if (inspect(s)) {
			advance();
			return s;
		} else {
			err("expected : " + s);
			return null; // dead code
		}
	}

	public boolean inspectID() {
		if (t.ttype != StreamTokenizer.TT_WORD) return false;
        final Matcher m = ID.matcher(t.sval);
        return m.matches();
	}

	public String consumeID() {
		if (!inspectID()) err("expected ID but found: " + t);
		final String result = t.sval;
		advance();
		return result;
	}

	public String consume(final String... options) {
        for (final String s : options) {
            if (inspect(s)) {
                return consume(s);
            }
        }
        // didn't match any of the options
        err("expected one of " + Arrays.toString(options) + ", but had " + t);
        return null;
	}
	
	public void consumeEOF() {
		if (t.ttype != StreamTokenizer.TT_EOF) err("expected EOF");
		advance();
	}

	public boolean inspect(final String s) {
		if (t.ttype == StreamTokenizer.TT_WORD && t.sval.equals(s)) {
			// words
			return true;
		} else if (s.length() == 1 && s.charAt(0) == t.ttype) {
			// punctuation characters
			return true;
		} else if (t.ttype == StreamTokenizer.TT_NUMBER && Integer.toString((int)t.nval).equals(s)) {
			// numbers such as 0 or 1
			return true;
		} else {
			// no match
			return false;
		}
	}

	/**
	 * Returns true if any of the options match the current token.
	 * @param options
	 * @return
	 */
	public boolean inspect(final String... options) {
        for (final String s : options) {
            if (inspect(s)) {
                return true;
            }
        }
        // didn't match any of the options
        return false;
	}
	
	public void advance() {
		try {
			Debug.msg("consuming: " + t);
			t.nextToken();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean inspectEOF() {
		return t.ttype == StreamTokenizer.TT_EOF;
	}

	public String debugState() {
		return t.toString();
	}
    
    protected void err(final String msg) {
        Debug.barf(msg);
    }

}
