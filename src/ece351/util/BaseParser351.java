package ece351.util;

import static org.parboiled.errors.ErrorUtils.printParseErrors;
import static org.parboiled.support.ParseTreeUtils.printNodeTree;
import static org.parboiled.trees.GraphUtils.printTree;

import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import org.parboiled.support.ToStringFormatter;
import org.parboiled.trees.GraphNode;

/**
 * Base class for all Parboiled parsers and recognizers in ECE351.
 * Adds some utility methods, most for output and debugging, to the normal
 * Parboiled BaseParser class.
 * 
 * @author drayside
 *
 */
@BuildParseTree
public abstract class BaseParser351 extends BaseParser<Object> {

	/**
	 * By convention we name the top production in the grammar "Program".
	 */
	abstract public Rule Program();

	/**
	 * Definition of WhiteSpace.
	 * 
	 * @see https://github.com/sirthias/parboiled/wiki/Handling-Whitespace
	 * @return
	 */
	protected Rule W() {
		return AnyOf(" \t\f\n\r");
	}
	
	/**
	 * Optional WhiteSpace (zero or more).
	 * 
	 * @see https://github.com/sirthias/parboiled/wiki/Handling-Whitespace
	 * @return
	 */
	protected Rule W0() {
		return ZeroOrMore(W());
	}
	
	/**
	 * Mandatory WhiteSpace (one or more).
	 * 
	 * @see https://github.com/sirthias/parboiled/wiki/Handling-Whitespace
	 * @return
	 */
	protected Rule W1() {
		return OneOrMore(W());
	}
	
	

	/**
	 * We redefine the rule creation for string literals to automatically match
	 * trailing whitespace if the string literal ends with a space character,
	 * this way we don't have to insert extra whitespace() rules after each
	 * character or string literal
	 */
	@Override
	protected Rule fromStringLiteral(String string) {
		return string.endsWith(" ") ? Sequence(
				String(string.substring(0, string.length() - 1)), W0())
				: String(string);
	}

	/**
	 * Print the value on the top of the stack to the debug output (System.err).
	 * @return
	 */
	public boolean debugStack() {
		debugmsg(String.format("%s: %s", peek().getClass().toString(), peek().toString()));
		return true;
	}


	/**
	 * Print a debug message to standard error (System.err).
	 * @param msg the message to print
	 * @return true, so that this can be called inside a Parboiled Sequence()
	 */
	public boolean debugmsg(final Object msg) {
		Debug.msg(msg);
		return true;
	}

	/**
	 * Check if an object is an instance of the given type.
	 * @param obj
	 * @param expectedType
	 * @throws IllegalArgumentException if expectedType is null
	 * @return
	 */
	public boolean checkType(final Object obj, final Class<?> expectedType) {
		if (obj == null) return false;
		if (expectedType == null) throw new IllegalArgumentException("expectedType cannot be null");
		return expectedType.isAssignableFrom(obj.getClass());
	}

	/**
	 * Check if an object is an instance of the given type, throw an exception if it isn't.
	 * @param obj
	 * @param expectedType
	 * @throws IllegalArgumentException if expectedType is null
	 * @throws RuntimeExcpetion if obj is null
	 * @throws RuntimeException if obj is not of the expected type
	 * @return
	 */
	public boolean checkTypeThrow(final Object obj, final Class<?> expectedType) {
		if (obj == null) throw new RuntimeException("obj==null and null does not have a type");
		if (!checkType(obj, expectedType)) {
			throw new RuntimeException("Object not of expected type: " + expectedType.getSimpleName() + "  actual type: " + obj.getClass().getSimpleName());			
		} else {
			return true;
		}
	}

	/**
	 * Check if an object's type is exactly the same as the argument.
	 * @param obj
	 * @param expectedType
	 * @throws IllegalArgumentException if expectedType is null
	 * @return
	 */
	public boolean checkExactType(final Object obj, final Class<?> expectedType) {
		if (obj == null) return false;
		if (expectedType == null) throw new IllegalArgumentException("expectedType cannot be null");
		return expectedType.equals(obj.getClass());
	}

	/**
	 * Check if an object is an instance of the given type, throw an exception if it isn't.
	 * @param obj
	 * @param expectedType
	 * @throws IllegalArgumentException if expectedType is null
	 * @throws RuntimeExcpetion if obj is null
	 * @throws RuntimeException if obj is not of the expected type
	 * @return
	 */
	public boolean checkExactTypeThrow(final Object obj, final Class<?> expectedType) {
		if (obj == null) throw new RuntimeException("obj==null and null does not have a type");
		if (!checkExactType(obj, expectedType)) {
			throw new RuntimeException("Object not of expected type: " + expectedType.getSimpleName() + "  actual type: " + obj.getClass().getSimpleName());			
		} else {
			return true;
		}
	}

	

	/**
	 * Run a parser. This method is usually called by some subclass that passes itself in as the cls
	 * parameter and the extracts and casts the AST from the ParsingResult object returned by this method.
	 * @param cls The class of the parser to run.
	 * @param input The input string.
	 * @return The result of the parse. The AST can be extracted from the object returned by this method.
	 */
	@SuppressWarnings("rawtypes")
	protected static ParsingResult process(final Class<? extends BaseParser351> cls, final String input) {
		final BaseParser351 parser = Parboiled.createParser(cls);
    	final ParsingResult result = new ReportingParseRunner(parser.Program()).run(input);
    	if (result.hasErrors()) {
    		System.out.println("Parse errors:");
    		System.out.println(printParseErrors(result));
    		throw new RuntimeException("parse error(s) encountered");
    	}
    	return result;
	}

	/**
	 * Print the parse tree for the given input. The parse tree contains all of the lexical information for
	 * the input, whereas the abstract syntax tree (AST) does not. This method is just used for debugging.
	 * @param cls
	 * @param input
	 */
	@SuppressWarnings("rawtypes")
	protected static void printParseTree(final Class<? extends BaseParser351> cls, final String input) {
		final ParsingResult result = process(cls, input);
    	final Object value = result.parseTreeRoot.getValue();
    	if (value instanceof GraphNode) {
    		final GraphNode root = (GraphNode)value;
    		final String s = printTree(root, new ToStringFormatter(null));
    		System.out.println("AST: " + s);
    	} else {
    		System.out.println("parse tree: " + printNodeTree(result));
    	}
	}
	
}
