package glbrick;

import java.util.ArrayList;


public class PartSpec implements BrickSpec {
	private ArrayList<BrickSpec> lines;
	private String name;
	private ColorBase colors;
	PartSpec(String name, ColorBase colors) {
		this.name = name;
		this.colors = colors;
		lines = new ArrayList<BrickSpec>();
	}
	public void debugging(){
		System.out.println("part name = " + name);
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
	
	
	public ArrayList<DrawnObject> treeTester(){
		ArrayList<DrawnObject> ret = new ArrayList<DrawnObject>();
		for (BrickSpec l : lines){
			//System.out.println(l.toString());
			ret.add(l.toDrawnObject());
		}
		return ret;
		
	}
	
	
	public void addLine(CommentSpec commentSpec) {
		// does nothing
		
	}

}
 