package glbrick;

import java.util.ArrayList;

public class TriangleSpec implements BrickSpec {

	public TriangleSpec(String[] lineParts) {
		super();
		// TODO Auto-generated constructor stub
	}

	public TriangleSpec(String[] lineParts, ColorBase colors) { 
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isCommment() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DrawnObject toDrawnObject(ArrayList<double[]> vs, double[] loc,
			ObjectType type, float[] color) {
		// TODO Auto-generated method stub
		return null;
	}

}
