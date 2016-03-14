package ece351.w.svg;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import ece351.util.Examinable;

final class Line implements Examinable {
	
	final static String LineX1 = "x1";
	final static String LineY1 = "y1";
	final static String LineX2 = "x2";
	final static String LineY2 = "y2";
	
	final int x1, x2, y1, y2;

	Line(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		assert repOk();
	}
	
	public boolean repOk() {
		// forum posts suggest that some code should be here
		return true;
	}

	@Override
	public String toString() {
		return "(" + x1 + "," + y1 + "," + x2 + "," + y2 + ")";
	}
	
	public String toXML() {
		return "<line x1=\"" + x1 + "\" x2=\"" + x2 +
				"\" y1=\"" + y1 +  "\" y2=\"" + y2 + "\" />";
	}

	public static String toXML(final int x1, final int y1, final int x2, final int y2) {
		final Line line = new Line(x1, y1, x2, y2);
		return line.toXML();
	}
	
	public static Line fromXML(final Node node) {
		final String nodeName = node.getNodeName();
		if (nodeName.equals("line")) {
			NamedNodeMap nnMap = node.getAttributes();
			if (nnMap.getNamedItem(LineX1) != null && nnMap.getNamedItem(LineY1) != null
					&& nnMap.getNamedItem(LineX2) != null && nnMap.getNamedItem(LineY2) != null) {
				final int x1, y1, x2, y2;
				x1 = Integer.parseInt(nnMap.getNamedItem(LineX1).getNodeValue());
				y1 = Integer.parseInt(nnMap.getNamedItem(LineY1).getNodeValue());
				x2 = Integer.parseInt(nnMap.getNamedItem(LineX2).getNodeValue());
				y2 = Integer.parseInt(nnMap.getNamedItem(LineY2).getNodeValue());
				return new Line(x1, y1, x2, y2);
			}
		}
		return null;
	}

	/**
	 * If we override equals() then we must override hashCode().
	 */
	@Override
	public int hashCode() {
		int hash = 17;
		hash = 31 * hash + x1;
		hash = 31 * hash + x2;
		hash = 31 * hash + y1;
		hash = 31 * hash + y2;
		return hash;
	}
	
	/**
	 * Line objects are immutable, and so may be equals.
	 */
	@Override
	public boolean equals(final Object obj) {
		//return false;
		
		// basics
		//Cast passed object to a line to work with it
		Line compLine = (Line)obj;
		
		// compare field values
		if( (compLine.x1 == this.x1) && (compLine.x2 == this.x2) )
		{
			if( (compLine.y1 == this.y1) && (compLine.y2 == this.y2) )
			{
				return true;
			}
		}
		
		return false;
		// no differences
// TODO: 11 lines snipped
	}

	/**
	 * Define in terms of equals().
	 */
	@Override
	public boolean isomorphic(final Examinable obj) {
		//return equals(obj);
		
		//Cast passed object to a line to work with it
		Line compLine = (Line)obj;
		
		//Track whether the x- and y-coordinates match
		boolean y_match = false;
		boolean x_match = false;
		
		// compare field values
		//If the x-coordinates match, in either order, then set x_match to true 
		if( (compLine.x1 == this.x1) && (compLine.x2 == this.x2) )
		{
			x_match = true;
		}
		else if( (compLine.x1 == this.x2) && (compLine.x2 == this.x1) )
		{
			x_match = true;
		}
		
		//If the y-coordinates match, in either order, then set x_match to true 
		if( (compLine.y1 == this.y1) && (compLine.y2 == this.y2) )
		{
			y_match = true;
		}
		else if( (compLine.y1 == this.y2) && (compLine.y2 == this.y1) )
		{
			y_match = true;
		}
		
		//Both x and y must match to return true
		return( y_match && x_match );
		
	}

	/**
	 * Allow geometric translations.
	 */
	@Override
	public boolean equivalent(final Examinable obj) {
		//return false;
		// basics
		//Cast passed object to a line to work with it
		Line compLine = (Line)obj;
		
		// compute deltas
		int my_x_delta = 0;			//The x-delta for this line
		int my_y_delta = 0;			//The y-delta for this line
		int comp_x_delta = 0;		//The x-delta for the passed line
		int comp_y_delta = 0;		//The y-delta for the passed line
		
		//Calculate the deltas for this line
		my_x_delta = Math.abs(this.x1 - this.x2);
		my_y_delta = Math.abs(this.y1 - this.y2);
		
		//Calculate the deltas for the passed line
		comp_x_delta = Math.abs(compLine.x1 - compLine.x2);
		comp_y_delta = Math.abs(compLine.y1 - compLine.y2);
		
		// are deltas equivalent?
		if( (my_x_delta == comp_x_delta) && (my_y_delta == comp_y_delta) )
		{
			return true;
		}
		else
		{
			return false;
		}
		
		// no important differences
// TODO: 18 lines snipped
	}
	
	
	//If the line is horizontal or vertical return its length.  If not, return 0 length.
	public int my_length()
	{
		if( (this.x1 - this.x2) != 0 ) { return( Math.abs(this.x1 - this.x2 )); }
		else if ( (this.y1 - this.y2) != 0 ) { return( Math.abs(this.y1 - this.y2) ); }
		else { return 0; }
	}
	
}
