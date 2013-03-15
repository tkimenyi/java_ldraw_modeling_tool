package glbrick;

import java.util.ArrayList;


public class PartSpec implements BrickSpec {
	private ArrayList<BrickSpec> children;
	private String name;
	private ColorBase colors;
	PartSpec(String name, ColorBase colors) {
		this.name = name;
		this.colors = colors;
		children = new ArrayList<BrickSpec>();
	}
	public void debugging(){
		System.out.println("part name = " + name);
	}

	void addLine(BrickSpec l) {
		//System.out.println("am I called");
		children.add(l);
	}
	public String toString() {
		String result = "";
		for (BrickSpec ln: children) {
			result += ln + "\n";
		}
		return result;
	}

	public boolean isCommment() {
		return false;
	}


	public DrawnObject toDrawnObject() 
	{
		ArrayList<DrawnObject> tempchildren = new ArrayList<DrawnObject>();
		for(BrickSpec child : children)
		{
			tempchildren.add(child.toDrawnObject());		
		}
		return new DrawnObject(tempchildren);
	}
	
	
	public ArrayList<DrawnObject> treeTester()
	{
		ArrayList<DrawnObject> ret = new ArrayList<DrawnObject>();
		for (BrickSpec l : children){
			//System.out.println(l.toString());
			ret.add(l.toDrawnObject());
		}
		return ret;
		
	}


}
 