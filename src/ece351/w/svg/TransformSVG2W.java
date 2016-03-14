package ece351.w.svg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.parboiled.common.ImmutableList;

import ece351.util.Debug;
import ece351.w.ast.WProgram;
import ece351.w.ast.Waveform;
import ece351.w.svg.Line;


public final class TransformSVG2W {
	
	/**
	 * Transforms an instance of WSVG to an instance of WProgram.
	 */
	public static final WProgram transform(final WSVG wsvg) {
		final List<Line> lines = new ArrayList<Line>(wsvg.segments);
		final List<Pin> pins = new ArrayList<Pin>(wsvg.pins);

		Collections.sort(lines, COMPARE_LINES);

		// Place holder for the list of waveforms in the final WProgram result.
		ImmutableList<Waveform> waveforms = ImmutableList.of();

		/*
		 * Traverse through the list of lines, each time a set of lines for a
		 * particular waveform is found, they are transformed into an instance
		 * of Waveform and placed into the result. 
		 */
		Set<Integer> setY = new HashSet<Integer>();

		final List<Line> waveform = new ArrayList<Line>(); // temporary list
		while(!lines.isEmpty()) {
			final Line line = lines.remove(0);

			if(waveform.isEmpty()) {
				waveform.add(line);
				continue;
			}

			boolean isNewWaveform = !setY.isEmpty() && (!setY.contains(line.y1) && !setY.contains(line.y2));
			
			if(isNewWaveform) { // new waveform
				waveforms = waveforms.append(transformLinesToWaveform(waveform, pins));
				waveform.clear();
				setY.clear();
			}

			waveform.add(line);
			setY.add(line.y1);
			setY.add(line.y2);
		}

		if(!waveform.isEmpty()) {
			waveforms = waveforms.append(transformLinesToWaveform(waveform, pins));
		}

		return new WProgram(waveforms);
	}

	/*
	 * Transform a list of Line to an instance of Waveform
	 */
	private static Waveform transformLinesToWaveform(final List<Line> lines, final List<Pin> pins) {
		if(lines.isEmpty()) return null;

		// Sort by the middle of two x-coordinates.
		Collections.sort(lines, COMPARE_IN_SINGLE_WAVE);

		// Place holder for the list of bits.
		ImmutableList<String> bits = ImmutableList.of();

		// The first line of the waveform.
		final Line first = lines.get(0);

		for(int i = 1; i < lines.size(); i++) {
			Line line = lines.get(i);
			int y_mid = (line.y1 + line.y2) / 2;
			
			// If a dot, skip it.
			if(line.x1 == line.x2 && line.y1 == line.y2) {
				continue;
			}

			if(first.y1 < y_mid) bits = bits.append("0");
			else if(first.y1 > y_mid) bits = bits.append("1");
		}

		// Get the corresponding id for this waveform.
		String id = "UNKNOWN";
		for(final Pin pin : pins) {
			if(pin.y == first.y1) {
				id = pin.id;
				pins.remove(pin);
				break;
			}
		}

		final Waveform result = new Waveform(bits, id);
		return result;
	}

	public final static Comparator<Line> COMPARE_IN_SINGLE_WAVE = new Comparator<Line>() {
		@Override
		public int compare(final Line l1, final Line l2) {
			if(l1.x1 < l2.x1) return -1;
			if(l1.x1 > l2.x1) return 1;
			if(l1.x2 < l2.x2) return -1;
			if(l1.x2 > l2.x2) return 1;
			return 0;
		}
	};

	public final static Comparator<Line> COMPARE_LINES = new Comparator<Line>() {
		@Override
		public int compare(final Line l1, final Line l2) {
			final double y_mid1 = (double) (l1.y1 + l1.y2) / 2.0f;
			final double y_mid2 = (double) (l2.y1 + l2.y2) / 2.0f;
			final double x_mid1 = (double) (l1.x1 + l1.x2) / 2.0f;
			final double x_mid2 = (double) (l2.x1 + l2.x2) / 2.0f;
			if (y_mid1 < y_mid2) return -1;
			if (y_mid1 > y_mid2) return 1;
			if (x_mid1 < x_mid2) return -1;
			if (x_mid1 > x_mid2) return 1;
			return 0;
		}
	};

}

