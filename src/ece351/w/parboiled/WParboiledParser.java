package ece351.w.parboiled;
import java.io.File;

import org.parboiled.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.common.FileUtils;
import org.parboiled.common.ImmutableList;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import ece351.util.BaseParser351;
import ece351.w.ast.WProgram;
import ece351.w.ast.Waveform;

@BuildParseTree
//Parboiled requires that this class not be final
public /*final*/ class WParboiledParser extends BaseParser351 {

	/**
	 * Run this parser, exit with error code 1 for malformed input.
	 * Called by wave/Makefile.
	 * @param args
	 */
	public static void main(final String[] args) {
    	process(WParboiledParser.class, FileUtils.readAllText(args[0]));
    }

	/**
	 * Construct an AST for a W program. Use this for debugging.
	 */
	public static WProgram parse(final String inputText) {
		return (WProgram) process(WParboiledParser.class, inputText).resultValue;
	}

	/**
	 * By convention we name the top production in the grammar "Program".
	 */
	@Override
    public Rule Program() {
        		// push empty ImmutableList, which will grow to hold the waveform objects
// TODO: 6 lines snipped
		//return NOTHING; // TODO: replace this stub
		return( Sequence(
					push( ImmutableList.of() ),						//Immutable list for waveforms
					ZeroOrMore(Waveform()),							//Get waveforms
					checkType(peek(), ImmutableList.class),			//Check waveform list now on top
					push( new WProgram( (ImmutableList<Waveform>)pop() ) ),	//Make WProgram from waveform list
					EOI
				)				
		);
    }

	/**
	 * Each line of the input W file represents a "pin" in the circuit.
	 */
    public Rule Waveform() {
    			// peek() == pin name
    			// peek() == immutable list of bits
    			// push the new Waveform on the stack
// TODO: 16 lines snipped
		//return null; // TODO: replace this stub
    	return( 
    		Sequence(
    			checkType( peek(), ImmutableList.class ),	//Check waveform list on top of stack
    			W0(), Name(),								//Get name
    			checkType( peek(), String.class ),			//Make sure name is on top of stack
    			
    			W0(),':', W0(),
    			
    			push(ImmutableList.of()),					//Push on list for the bits
    			BitString(), 								//Get the bit string list
    			checkType( peek(), ImmutableList.class ),	//Make sure bit list is on top of stack
    			
    			push( new Waveform( (ImmutableList<String>)pop(), (String)pop() ) ), //Put new waveform on stack
    			swap(),
    			checkType( peek(), ImmutableList.class ),			//Check waveform list on top of stack after swap
    			push( ((ImmutableList<Waveform>)pop()).append((Waveform)pop()) ),		//Append new waveform to waveform list
    			
    			W0(), ';', W0() 
    		)
    	);
    }

    /**
     * The first token on each line is the name of the pin that line represents.
     */
    public Rule Name() {
// TODO: 4 lines snipped
		//return null; // TODO: replace this stub
    	return( 
    		Sequence(
    			OneOrMore( Letter() ),			//Get the letters
    			push(match())					//Push the full name to stack
    		)
    	);
    		
    }
    
    /**
     * A Name is composed of a sequence of Letters. 
     * Recall that PEGs incorporate lexing into the parser.
     */
    public Rule Letter() {
// TODO: 1 lines snipped
		//return null; // TODO: replace this stub
    	return( FirstOf( CharRange('A','Z'), CharRange('a','z') ) );
    }

    /**
     * A BitString is the sequence of values for a pin.
     */
    public Rule BitString() {
// TODO: 4 lines snipped
		//return null; // TODO: replace this stub
    	return(
    		Sequence(
    			checkType(peek(), ImmutableList.class),							//Check bit list is on top of stack
    			Bit(),															//Get first bit
    			checkType(peek(),String.class),
    			swap(),
        		checkType(peek(), ImmutableList.class),							//Make sure bit list is on top of stack after swap
        		push( ((ImmutableList<String>)pop()).append((String)pop()) ),	//Append new bit to bit list
        		
    			ZeroOrMore(
    				Sequence(
    					W1(),
    					Bit(),
    					checkType(peek(),String.class),
    	    			swap(),
    	        		checkType(peek(), ImmutableList.class),							//Make sure bit list is on top of stack after swap
    	        		push( ((ImmutableList<String>)pop()).append((String)pop()) ),	//Append new bit to bit list						//Get any bits after that
    	        		checkType(peek(), ImmutableList.class)							//Check that bit list still on top of stack
    				)
    			)
    		)
    	);
    }
    
    /**
     * A BitString is composed of a sequence of Bits. 
     * Recall that PEGs incorporate lexing into the parser.
     */
    public Rule Bit() {   
        //return null; // TODO: replace this stub
    	return(
        	Sequence(
        		AnyOf("01"),										//Get the bit
        		push(match())										//Push bit to stack
        	)
        );
// TODO: 5 lines snipped
    }

}

