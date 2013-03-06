package glbrick;

import java.util.ArrayList;

public class SubpartSpec implements BrickSpec {

	public SubpartSpec(String[] lineParts) {
		super();
		// TODO Auto-generated constructor stub
	}

	public SubpartSpec(String[] lineParts, PartFactory partFactory) {
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
