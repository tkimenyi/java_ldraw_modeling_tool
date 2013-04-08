package glbrick;

import java.io.File;
import java.util.ArrayList;

public class glbrickUtilities {
	
	public static float smartDecode(String s){
		float ret;
		if (s.length() >1 && s.contains("x")){
			ret = (float) Integer.decode(s);
		}

		else{
			ret = Float.parseFloat(s);
		}
		return ret;
	}
	
	
	public static String stringer(ArrayList<double[]> vertices){
		
		String printstr = "";
		for(double[] d : vertices){
			for(double e : d){
				printstr += e + " ";
			}
		}
		return printstr;
	}
	
	public static String fixPath(String s) {
		return s.replace('\\', File.separatorChar);
	}

}
