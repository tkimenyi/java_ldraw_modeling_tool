package brick;


import com.threed.jpct.*;

public class OptionalLineSpec implements BrickSpec {
	private int c;
	private SimpleVector one, two, control1, control2;
	private ColorBase colors;
	
	public OptionalLineSpec(String[] vals, ColorBase colors) {
		this.c = Integer.parseInt(vals[1]);
		one = new SimpleVector(Float.parseFloat(vals[2]), Float.parseFloat(vals[3]), Float.parseFloat(vals[4]));
		two = new SimpleVector(Float.parseFloat(vals[5]), Float.parseFloat(vals[6]), Float.parseFloat(vals[7]));
		control1 = new SimpleVector(Float.parseFloat(vals[8]), Float.parseFloat(vals[9]), Float.parseFloat(vals[10]));
		control2 = new SimpleVector(Float.parseFloat(vals[11]), Float.parseFloat(vals[12]), Float.parseFloat(vals[13]));
		this.colors = colors;
	}
	
	public String toString() {
		return "5 " + c + " " + one.x + " " + one.y + " " + one.z + " " + two.x + " " + two.y + " " + two.z + " " + control1.x + " " + control1.y + " " + control1.z + " " + control2.x + " " + control2.y + " " + control2.z;
	}
	
	@Override
	public BrickObject toBrickObject(Matrix m, BrickPanel world) {
		SimpleVector start = Util.matMul(m, one);
		SimpleVector end = Util.matMul(m, two);
		SimpleVector ctrl1 = Util.matMul(m, control1);
		SimpleVector ctrl2 = Util.matMul(m, control2);
		Object3D line = Util.makeLineFrom(start, end, LineSpec.WIDTH);
		BrickObject obj = new OptionalLineObject(line, world, colors, start, end, ctrl1, ctrl2);
		obj.setColorCode(c);
		return obj;
	}

	@Override
	public boolean isCommment() {
		return false;
	}
}

