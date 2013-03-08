package glbrick;

import java.util.ArrayList;


public interface BrickSpec {
public boolean isCommment();
	
	public String toString();
	
	public DrawnObject toDrawnObject(ArrayList<double[]> vs, double[] loc, ObjectType type, float[] color);

}









