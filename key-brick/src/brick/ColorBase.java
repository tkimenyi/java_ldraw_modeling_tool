package brick;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * 
 * @author Gabriel Ferrer
 *
 * Represents the table of available LDraw colors.  These color definitions are found in $(LDRAW)/ldconfig.ldr
 */

public class ColorBase {
	public final static String configFile = "ldconfig.ldr";
	
	private Map<Integer,Color> colorEncodings, color2Edge;
	
	public ColorBase(String ldrawPath) throws FileNotFoundException {
		colorEncodings = new HashMap<Integer,Color>();
		color2Edge = new HashMap<Integer,Color>();
		
		File config = new File(ldrawPath + File.separator + "ldconfig.ldr");
		Scanner s = new Scanner(config);
		Map<Integer,Integer> edgeTemps = new LinkedHashMap<Integer,Integer>();
		while (s.hasNextLine()) {
			String line = s.nextLine();
			try {
				String[] params = line.split("\\s+");
				if (params.length > 1 && params[1].equals("!COLOUR")) {
					int code = getColorCode(params);
					int alpha = hasAlpha(params) ? getAlpha(params) : 255;
					colorEncodings.put(code, getMainColor(params, alpha));
					recordEdgeColor(code, params, color2Edge, edgeTemps);
				}
			} catch (MalformedLDrawException e) {
				System.out.println("Line \"" + line + "\" contains no color information");
			} catch (NumberFormatException e) {
				System.out.println("Line \"" + line + "\" has bad numbers");
			}
		}
		
		for (int colorCode: edgeTemps.keySet()) {
			color2Edge.put(colorCode, colorEncodings.get(edgeTemps.get(colorCode)));
		}
	}
	
	private static int getColorCode(String[] params) throws MalformedLDrawException {
		for (int i = 0; i < params.length - 1; ++i) {
			if (params[i].toUpperCase().equals("CODE")) {
				return Integer.parseInt(params[i+1]);
			}
		}
		throw new MalformedLDrawException();
	}
	
	private static Color getMainColor(String[] params, int alpha) throws MalformedLDrawException {
		for (int i = 0; i < params.length - 1; ++i) {
			if (params[i].toUpperCase().equals("VALUE")) {
				return decodeColorString(params[i+1], alpha);
			}
		}
		throw new MalformedLDrawException();
	}
	
	private static void recordEdgeColor(int code, String[] params, Map<Integer,Color> edgeColors, Map<Integer,Integer> tempEdges) throws MalformedLDrawException {
		for (int i = 0; i < params.length - 1; ++i) {
			if (params[i].toUpperCase().equals("EDGE")) {
				String color = params[i+1];
				if (color.length() == 7) {
					edgeColors.put(code, decodeColorString(color, 255));
				} else {
					tempEdges.put(code, Integer.parseInt(color));
				}
				return;
			}
		}
		throw new MalformedLDrawException();
	}
	
	private static Color decodeColorString(String color, int alpha) throws MalformedLDrawException {
		if (color.length() != 7) {
			throw new MalformedLDrawException();
		}
		int red = Integer.parseInt(color.substring(1, 3), 16);
		int green = Integer.parseInt(color.substring(3, 5), 16);
		int blue = Integer.parseInt(color.substring(5, 7), 16);
		return new Color(red, green, blue, alpha);
	}
	
	private static boolean hasAlpha(String[] params) {
		for (int i = 0; i < params.length - 1; ++i) {
			if (params[i].toUpperCase().equals("ALPHA")) {
				return true;
			}
		}
		return false;
	}
	
	private static int getAlpha(String[] params) {
		for (int i = 0; i < params.length - 1; ++i) {
			if (params[i].toUpperCase().equals("ALPHA")) {
				return Integer.parseInt(params[i+1]);
			}
		}
		throw new IllegalStateException("Internal error: Called ColorBase.getAlpha() without checking hasAlpha()");
	}
	
	public Color getColor(int color) {
		return colorEncodings.get(color);
	}
	
	public Color getEdgeColor(int color) {
		return color2Edge.get(color);
	}

}
