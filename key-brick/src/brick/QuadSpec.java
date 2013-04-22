package brick;

import com.threed.jpct.*;

public class QuadSpec implements BrickSpec {
	private int c;
	private SimpleVector one, two, three, four;
	private ColorBase colors;
	
	public QuadSpec(String[] vals, ColorBase colors) {
		this.c = Integer.parseInt(vals[1]);
		one = new SimpleVector(Double.parseDouble(vals[2]), Double.parseDouble(vals[3]), Double.parseDouble(vals[4]));
		two = new SimpleVector(Double.parseDouble(vals[5]), Double.parseDouble(vals[6]), Double.parseDouble(vals[7]));
		three = new SimpleVector(Double.parseDouble(vals[8]), Double.parseDouble(vals[9]), Double.parseDouble(vals[10]));
		four = new SimpleVector(Double.parseDouble(vals[11]), Double.parseDouble(vals[12]), Double.parseDouble(vals[13]));
		this.colors = colors;
	}
	
	public String toString() {
		return "4 " + c + " " + one.x + " " + one.y + " " + one.z + " " + two.x + " " + two.y + " " + two.z + " " + three.x + " " + three.y + " " + three.z + " " + four.x + " " + four.y + " " + four.z;
	}

	@Override
	public BrickObject toBrickObject(Matrix m, BrickPanel world) {
		BrickObject quad = new BrickObject(2, world, colors, "(quad)");
		SimpleVector start = Util.matMul(m, two);
		SimpleVector end = Util.matMul(m, four);
		quad.addTriangle(start, Util.matMul(m, three), end);
		quad.addTriangle(end, Util.matMul(m,  one), start);
		quad.setColorCode(c);
		return quad;
	}

	@Override
	public boolean isCommment() {
		return false;
	}
}
