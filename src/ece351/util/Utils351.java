package ece351.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

// TODO: remove class Utils351
public final class Utils351 {

	private Utils351() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Read input file to String.
	 * @param inputFileName
	 * @return
	 */
	public static String readFile(final String inputFileName) {
		try {
			final File f = new File(inputFileName);
			final long length = f.length();
			if (length < 1000000) {
				final FileReader fr = new FileReader(inputFileName);
				final BufferedReader br = new BufferedReader(fr);
				final char[] c = new char[(int)length];
				br.read(c);
				return new String(c);
			} else {
				throw new RuntimeException("File is too big! " + inputFileName);
			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	/**
	 * Read non-blank lines from file into a list of strings.
	 * @param inputFileName
	 * @return
	 */
	// TODO: remove these methods from Utils351
	public static List<String> readFileLines(final String inputFileName) {
		try {
			
			final BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFileName));
			final List<String> lines = new ArrayList<String>();
			
			String line = null;
			while ((line= bufferedReader.readLine()) != null) {
				if (line.trim().length() > 0) {
					lines.add(line);
				}
			}
			bufferedReader.close();
			
			return lines;
			
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static File[] files(final String dirName, final String regex) {
		final File dir = new File(dirName);
		assert dir.isDirectory() : "not a directory: " + dir;
		final Pattern p = Pattern.compile(regex);
		final File[] result = dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(final File f, final String s) {
				final boolean result = p.matcher(s).matches();
				return result;
			}
		});
		assert result != null;
		Arrays.sort(result);
		return result;

	}
}
