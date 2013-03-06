package glbrick;

import java.util.ArrayList;

public class QuadSpec implements BrickSpec {

	public QuadSpec(String[] lineParts) {
		super();
		// TODO Auto-generated constructor stub
	}

	public QuadSpec(String[] lineParts, ColorBase colors) { 
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
