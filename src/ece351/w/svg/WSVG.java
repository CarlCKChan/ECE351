package ece351.w.svg;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Comparator;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import ece351.util.Examinable;
import ece351.util.Examiner;



//This class is used as a comparator for Line objects.  In the context of Lab 2, it gets
//passed to Collections.sort() to facilitate sorting.
final class line_comparator implements Comparator
{
	public int compare(Object o1, Object o2)
	{
		Line line1 = (Line)o1;
		Line line2 = (Line)o2;
		
		if( (line1.x1 >= line2.x1) && (line1.x2 >= line2.x2) )  { return 1;	}
		else if( (line1.x1 <= line2.x1) && (line1.x2 <= line2.x2) )	{ return -1; }
		else { return 0; }		
	}
	
	public boolean equals(Object obj)
	{
		return false;
	}

}




final class WSVG implements Examinable {

	final List<Pin> pins;
	final List<Line> segments;

	private WSVG(final List<Pin> pins, final List<Line> segments) {
		this.pins = pins;
		this.segments = segments;
	}
	
	static WSVG fromSVG(final URI uri, final boolean parseDOM) throws Exception {
		final List<Pin> pins = new ArrayList<Pin>();
		final List<Line> segments = new ArrayList<Line>();
		try {
			final Document doc;
			if (parseDOM) {
			    doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(uri.toString());
			} else {
		    	SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(XMLResourceDescriptor.getXMLParserClassName());
		    	doc = f.createDocument(uri.toString());
			}
		    
		    for (Node node = doc.getDocumentElement().getFirstChild(); 
		    		node != null; 
		    		node = node.getNextSibling()) {
		    	if (node.getNodeType() == Node.ELEMENT_NODE) {
		    		final Pin p = Pin.fromSVG(node);
		    		if (p != null) {
		    			pins.add(p);
		    		}
		    		final Line line = Line.fromXML(node);
		    		if (line != null) {
						// ignore dots (i.e., lines of zero length)
						if (!(line.x1 == line.x2 && line.y1 == line.y2)) {
							// it's not a dot, so keep it
							segments.add(line);
						}
		    		}
		    	}
		    }
		} catch (IOException e) {
		    throw e;
		} catch (Exception e) {
			throw e;
		}
		return new WSVG(Collections.unmodifiableList(pins), Collections.unmodifiableList(segments));
	}
	
	/**
	 * If we override equals() then we must override hashCode().
	 */
	@Override
	public int hashCode() {
		int hash = 31;
		hash = hash * 13 + pins.hashCode();
		hash = hash * 13 + segments.hashCode();
		return hash;
	}

	/**
	 * WSVG objects are immutable, and so may be Liskov substitutable.
	 */
	@Override
	public boolean equals(final Object obj) {
		//return false;
		// basics
		boolean pin_match = false;
		boolean line_match = false;
		Examiner equal_Examiner = Examiner.Equals;
		
		// compare pins and lines with Examiner.orderedExamination()
		pin_match = Examiner.orderedExamination(equal_Examiner, this.pins, ((WSVG)obj).pins );
		line_match = Examiner.orderedExamination(equal_Examiner, this.segments, ((WSVG)obj).segments );
		
		// no significant differences, return true
		return( pin_match && line_match );
		
// TODO: 11 lines snipped
	}

	/**
	 * Same coordinates, possibly different orderings of elements in SVG.
	 */
	@Override
	public boolean isomorphic(final Examinable obj) {
		//return false;
		// basics
		boolean pin_match = false;
		boolean line_match = false;
		Examiner iso_Examiner = Examiner.Isomorphic;
		
		// compare pins and lines with Examiner.unorderedExamination()
		pin_match = Examiner.unorderedExamination(iso_Examiner, this.pins, ((WSVG)obj).pins );
		line_match = Examiner.unorderedExamination(iso_Examiner, this.segments, ((WSVG)obj).segments );

		// no significant differences, return true
		return( pin_match && line_match );
		
// TODO: 11 lines snipped
	}

	/**
	 * Allow geometric translations and stretching (different sized lines).  Equivalence will
	 * be defined based on the following formating requirements:
	 *      1. Pins will be at the rest position (midpoint) of their signals (Pin ids must match)
	 *      2. Each horizontal line must only represent 1 (one) bit, no matter its length
	 *      3. Each wave must start with a half-length vertical line that is the shortest
	 *         non-zero line in the file
	 *         
	 * Note: Notationally, "this" will refer to the student and the passed will be assumed to
	 *       be the staff.  It will work either way, this is just notational.
	 */
	@Override
	public boolean equivalent(final Examinable obj) {
		
		/** For the simple definition of equivalent
			//return false;
			// basics
			boolean pin_match = false;
			boolean line_match = false;
			Examiner equiv_Examiner = Examiner.Equivalent;
			
			// compare pins and lines with Examiner.unorderedExamination()
			pin_match = Examiner.unorderedExamination(equiv_Examiner, this.pins, ((WSVG)obj).pins );
			line_match = Examiner.unorderedExamination(equiv_Examiner, this.segments, ((WSVG)obj).segments );
			
			// no differences, return true
			return( pin_match && line_match );
		*/
		
		
		
		
		/**
		 *    This is for the more strict definition of equivalent
		 */
		
		// VARIABLES
		WSVG comp_svg = (WSVG)obj;				//Cast the passed object to a WSVG so we can work with it
		int min_staff_line = Integer.MAX_VALUE;	//Smallest line in the staff file
		int min_stud_line = Integer.MAX_VALUE;	//Smallest line in the studen file
		int pin_match = 0;						//Index of the staff pin that matches the current student pin
		List<Line> staff_wave = new LinkedList<Line>();	//Holds the current staff waveform
		List<Line> stud_wave = new LinkedList<Line>();	//Holds the current student waveform
		line_comparator line_comp = new line_comparator();
		
		
		//If the number of pins is wrong, it is automatically not a match
		if( this.pins.size() != comp_svg.pins.size() ) { return false; }
		
		
		// ************************************************************************************
		// Find the shortest non-zero vertical line for the staff and student SVG file; by definition, 
		// this will be the vertical starting line.
		for( Line l : this.segments )
		{
			//if ( (l.my_length() < min_stud_line) && (l.my_length() != 0) && (l.x1 == l.x2) && (l.y1 != l.y2) ) { min_stud_line = l.my_length(); }
			if ( (l.my_length() < min_stud_line) && (l.x1 == l.x2) && (l.y1 != l.y2) ) { min_stud_line = l.my_length(); }
		}
		
		for( Line l : comp_svg.segments )
		{
			//if ( (l.my_length() < min_staff_line) && (l.my_length() != 0) ) { min_staff_line = l.my_length(); }
			if ( (l.my_length() < min_staff_line) && (l.x1 == l.x2) && (l.y1 != l.y2) ) { min_staff_line = l.my_length(); }
		}
		// ************************************************************************************
		
		
		//Check that the waveforms for each pin are equivalent
		for( Pin p : this.pins )
		{
			//Clean out the lists used to store the current staff and student waveform
			staff_wave = new LinkedList<Line>();
			stud_wave = new LinkedList<Line>();
			
			
			// ****************************************************************************
			//Find the pin in the staff pin list that matches the current student pin.
			//Return false if no match since differing pins means not equivalent
			for( int count=0; count < comp_svg.pins.size(); count++ )
			{
				if( comp_svg.pins.get(count).id.equals( p.id ) )
				{
					pin_match = count;
					break;
				}
			}
			if( !(comp_svg.pins.get(pin_match).id.equals( p.id ) ) ) { return false; }
			// ****************************************************************************
			
			
			
			
			// ****************************************************************************
			// Look through all the lines in both staff and student SVG files.  If they are
			// horizontal lines within a range around the current pin (defined by the half-
			// vertical staring line) append them to their respective list.
			int stud_pin_x = p.x;
			int stud_pin_y = p.y;
			int staff_pin_x = comp_svg.pins.get(pin_match).x;
			int staff_pin_y = comp_svg.pins.get(pin_match).y;
			
			// Find for the staff list
			for( Line l : comp_svg.segments )
			{								
				if( (l.y1 <= (staff_pin_y+min_staff_line)) && (l.y1 >= (staff_pin_y-min_staff_line)) && (l.y1 == l.y2) && (l.x1 != l.x2) )
				{
					staff_wave.add( l );
				}				
			}			
			
			//Find for the student list
			for( Line l : this.segments )
			{								
				if( (l.y1 <= (stud_pin_y+min_stud_line)) && (l.y1 >= (stud_pin_y-min_stud_line)) && (l.y1 == l.y2) && (l.x1 != l.x2) )
				{
					stud_wave.add( l );
				}				
			}
			
			//The size of the waveforms must match
			if( staff_wave.size() != stud_wave.size() ) { return false; }
			
			// *******************************************************************************
			
			
			// *******************************************************************************
			// Sort the current staff and student waveforms.  Check if they have the same shape;
			// each bit below, at, or over their respective current midpoint.  If the shape
			// conflicts, return false for fail
			Collections.sort(staff_wave, line_comp);
			Collections.sort(stud_wave, line_comp);
			
			for( int count=0; count < staff_wave.size(); count++ )
			{
				if( (staff_wave.get(count).y1 > staff_pin_y) && (stud_wave.get(count).y1 <= stud_pin_y) )
				{
					return false;
				}
				else if( (staff_wave.get(count).y1 < staff_pin_y) && (stud_wave.get(count).y1 >= stud_pin_y) )
				{
					return false;
				}
				else if( (staff_wave.get(count).y1 == staff_pin_y) && (stud_wave.get(count).y1 != stud_pin_y) )
				{
					return false;
				}
			}
			// *********************************************************************************
						
		}
		
		// If got past all the testing and still hasn't failed, it passed
		return true;
		
// TODO: 11 lines snipped
		
	}


	
	
	/**
	 * Minimize an SVG file by eliminating zero length line segments and merging
	 * adjacent line segments.
	 * 
	 * @param originalWSVG
	 * @return minimized version of original SVG
	 */
	static WSVG minimalWSVG( final WSVG originalWSVG ) {
		final List<Line> segments = new ArrayList<Line>();
		
		for ( final Line originalLine : segments ) {
			// Ignore zero-length lines.
			if ( originalLine.x1 == originalLine.x2 && originalLine.y1 == originalLine.y2 ) {
				continue;
			}

			// Simplify checks later on by requiring the first end point to be "smaller" than the second 
			// end point(i.e. if the first end point has a smaller y-coordinate, or if it has same 
			// y-coordinate as the second end point but has a smaller x-coordinate).
			Line line = ( originalLine.x1 < originalLine.x2 || 
						( originalLine.x1 == originalLine.x2 && originalLine.y1 < originalLine.y2 ) ) ?
						new Line( originalLine.x1, originalLine.y1, originalLine.x2, originalLine.y2 ) : 
						new Line( originalLine.x2, originalLine.y2, originalLine.x1, originalLine.y1 );
			
			// Assuming that there are no overlapping line segments (intersections at single points 
			// excluded), then a line segment can only be merged with at most two other existing line 
			// segments, one at each end point. This loop searches for these two potential line 
			// segments. The line segment that has yet to be added to the minimal WSVG object is simply 
			// discarded if it can be merged with an existing line segment in the minimal WSVG object. 
			// If strictly two potential line segments can be merged with the new line segment, 
			// the first one that is found is kept and recreated as the newly merged line segment, 
			// while the second one is removed from the minimal WSVG object that is to be returned.
			int merged_pos = -1;
			for ( int i = 0; i < segments.size(); ++i ) {
				final Line l = segments.get( i );
				if ( l.y1 == l.y2 && line.y1 == line.y2 && l.y1 == line.y1 && 
					 ( l.x1 == line.x2 || l.x2 == line.x1 ) ) {
					// Handles merger of horizontal line segments.
					if ( merged_pos == -1 ) {
						segments.set( i, new Line( Math.min( l.x1, line.x1 ), l.y1, 
									  Math.max( l.x2, line.x2 ), l.y1 ) );
						line = segments.get( i );
						merged_pos = i;
					} else {
						segments.set( merged_pos, new Line( Math.min( l.x1, line.x1 ), l.y1, 
										  Math.max( l.x2, line.x2 ), l.y1 ) );
						segments.remove( i );
						break;
					}
				} else if ( l.x1 == l.x2 && line.x1 == line.x2 && l.x1 == line.x1 && 
							( l.y1 == line.y2 || l.y2 == line.y1 ) ) {
					// Handles merger of vertical line segments.
					if ( merged_pos == -1 ) {
						segments.set( i, new Line( l.x1, Math.min( l.y1, line.y1 ), l.x2, 
									  Math.max( l.y2, line.y2 ) ) );
						line = segments.get( i );
						merged_pos = i;
					} else {
						segments.set( merged_pos, new Line( l.x1, Math.min( l.y1, line.y1 ), 
									  l.x2, Math.max( l.y2, line.y2 ) ) );
						segments.remove( i );
						break;
					}
				}
			}
			
			// If required, add input line segment to minimal WSVG object.
			if ( merged_pos == -1 ) {
				segments.add(line);
			}
		}
		
		return new WSVG( originalWSVG.pins, Collections.unmodifiableList( segments ) );
	}
	
}

