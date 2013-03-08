package brick;

import java.util.*;

import com.threed.jpct.*;

public class PartSpec implements BrickSpec {
	private ArrayList<BrickSpec> lines;
	private String name;
	private ColorBase colors;
	
	PartSpec(String name, ColorBase colors) {
		this.name = name;
		this.colors = colors;
		lines = new ArrayList<BrickSpec>();
	}
	
	void addLine(BrickSpec l) {
		lines.add(l);
	}
	
	public String getName() {return name;}
	
	public String toString() {
		String result = "";
		for (BrickSpec ln: lines) {
			result += ln + "\n";
		}
		return result;
	}
	
	public BrickObject toBrickObject(Matrix transformation, BrickPanel world) {
		BrickObject result = new BrickObject(0, world, colors, name);
		for (BrickSpec l: lines) {
			if (!l.isCommment()) {
				result.addChild(l.toBrickObject(transformation, world));
			}
		}
		return result;
	}

	@Override
	public boolean isCommment() {
		return false;
	}
}

