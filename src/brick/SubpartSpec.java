package brick;

import com.threed.jpct.*;

public class SubpartSpec implements BrickSpec {
	private int col;
	private Matrix m;
	private PartSpec subpart;
	private String filename;
	
	public SubpartSpec(String[] vals, PartFactory pf) throws PartNotFoundException {
		this.col = Integer.parseInt(vals[1]);
		m = new Matrix();
		m.setColumn(0, Float.parseFloat(vals[5]), Float.parseFloat(vals[6]), Float.parseFloat(vals[7]), Float.parseFloat(vals[2]));
		m.setColumn(1, Float.parseFloat(vals[8]), Float.parseFloat(vals[9]), Float.parseFloat(vals[10]), Float.parseFloat(vals[3]));
		m.setColumn(2, Float.parseFloat(vals[11]), Float.parseFloat(vals[12]), Float.parseFloat(vals[13]), Float.parseFloat(vals[4]));
		m.setColumn(3, 0, 0, 0, 1);
		this.filename = vals[14];
		
		this.subpart = pf.getPart(this.filename);
	}
	
	public String toString() {
		return "1 " + col + " " + m.get(3, 0) + " " + m.get(3, 1) + " " + m.get(3, 2) + " " + m.get(0, 0) + " " + m.get(1, 0) + " " + m.get(2, 0) + " " + m.get(0, 1) + " " + m.get(1, 1) + " " + m.get(2, 1) + " " + m.get(0, 2) + " " + m.get(1, 2) + " " + m.get(2, 2) + " " + filename;
	}

	@Override
	public BrickObject toBrickObject(Matrix transformation, BrickPanel world) {
		BrickObject result = subpart.toBrickObject(Util.matMul(transformation, m), world);
		result.setColorCode(col);
		return result;
	}

	@Override
	public boolean isCommment() {
		return false;
	}
}

