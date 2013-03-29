package brick;

import com.threed.jpct.*;

public class TriangleSpec implements BrickSpec {
	private int c;
	private SimpleVector one, two, three;
	private ColorBase colors;
	
	public TriangleSpec(String[] vals, ColorBase colors) {
		this.c = Integer.parseInt(vals[1]);
		this.one = new SimpleVector(Double.parseDouble(vals[2]), Double.parseDouble(vals[3]), Double.parseDouble(vals[4]));
		this.two = new SimpleVector(Double.parseDouble(vals[5]), Double.parseDouble(vals[6]), Double.parseDouble(vals[7]));
		this.three = new SimpleVector(Double.parseDouble(vals[8]), Double.parseDouble(vals[9]), Double.parseDouble(vals[10]));
		this.colors = colors;
	}
	
	public String toString() {
		return "3 " + c + " " + one.x + " " + one.y + " " + one.z + " " + two.x + " " + two.y + " " + two.z + " " + three.x + " " + three.y + " " + three.z;
	}

	@Override
	public BrickObject toBrickObject(Matrix m, BrickPanel world) {
		BrickObject triangle = new BrickObject(1, world, colors, "(triangle)");
		triangle.addTriangle(Util.matMul(m, one), Util.matMul(m, two), Util.matMul(m, three));
		triangle.setColorCode(c);
		return triangle;
	}

	@Override
	public boolean isCommment() {
		return false;
	}
}
