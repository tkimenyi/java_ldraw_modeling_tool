package glbrick;

import java.util.ArrayList;


public class OldPartSpec implements BrickSpec {
	private ArrayList<BrickSpec> lines;
	private String name;
	private ColorBase colors;
	OldPartSpec(String name, ColorBase colors) {
		this.name = name;
		this.colors = colors;
		lines = new ArrayList<BrickSpec>();
	}
	public void debugging(){
		System.out.println("something worked");
	}

	void addLine(BrickSpec l) {
		//System.out.println("am I called");
		lines.add(l);
	}
	public String toString() {
		String result = "";
		for (BrickSpec ln: lines) {
			result += ln + "\n";
		}
		return result;
	}

	public boolean isCommment() {
		return false;
	}

	/*public DrawnObject toDrawnObject(ArrayList<double[]> transformation) {
		//willing to bet this constructor isn't so good
		return null;
	}*/

	public DrawnObject toDrawnObject() {
		//willing to bet this constructor isn't so good
		return null;
	}
	public void addLine(CommentSpec commentSpec) {
		// does nothing
		
	}

}
 