package glbrick;

import java.io.File;

public class brickUtilities {
	
	public static String javafy(String s) {
		return "\"" + s.replace("\\", "\\\\").replace("\n", "\\n").replace("\t", "\\t").replace("\"", "\\\"") + "\"";
	}
	
	public static String fixPath(String s) {
		return s.replace('\\', File.separatorChar);
	}

}
