package brick;

import com.threed.jpct.*;

public class LineSpec implements BrickSpec {
	public static final float WIDTH = 0.2f;
	
	private int c;
	private SimpleVector one, two;
	private ColorBase colors;
	
	public LineSpec(String[] vals, ColorBase colors) {
		this.c = Integer.parseInt(vals[1]);
		one = new SimpleVector(Float.parseFloat(vals[2]), Float.parseFloat(vals[3]), Float.parseFloat(vals[4]));
		two = new SimpleVector(Float.parseFloat(vals[5]), Float.parseFloat(vals[6]), Float.parseFloat(vals[7]));
		this.colors = colors;
	}
	
	public String toString() {
		return "2 " + c + " " + one.x + " " + one.y + " " + one.z + " " + two.x + " " + two.y + " " + two.z;
	}
	

	@Override
	public BrickObject toBrickObject(Matrix m, BrickPanel world) {
		SimpleVector start = Util.matMul(m, one);
		SimpleVector end = Util.matMul(m, two);
		BrickObject line = new BrickObject(Util.makeLineFrom(start, end, WIDTH), world, colors, "(edge)");
		line.setColorCode(c);
		return line;
	}

	@Override
	public boolean isCommment() {
		return false;
	}
}
