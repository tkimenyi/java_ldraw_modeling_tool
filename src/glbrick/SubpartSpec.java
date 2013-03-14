package glbrick;

import java.util.ArrayList;



public class SubpartSpec implements BrickSpec {
	
	
String[] lineParts;
	
	//private int c;
	private double[] loc, row1, row2, row3;
	private ArrayList<double[]> vertices;
	@SuppressWarnings("unused")
	private ColorBase colors;
	float lineColorValue;
	String partName = "";
	private PartSpec subpart;
	
	

	public SubpartSpec(String[] lineParts) {
		super();
		// TODO Auto-generated constructor stub
	}
	public String toString(){
		return glbrickUtilities.stringer(vertices); // does what you think it does
	}
	
	public SubpartSpec(String[] lineParts, PartFactory partFactory) throws PartNotFoundException {
		this.lineParts = lineParts;
		
		loc = new double[3];
		row1 = new double[3];
		row2 = new double[3];
		row3 = new double[3];
		
		lineColorValue = glbrickUtilities.smartDecode(lineParts[1]);
		
		loc[0] = Double.parseDouble(lineParts[2]);
		loc[1] = Double.parseDouble(lineParts[3]);
		loc[2] = Double.parseDouble(lineParts[4]);
		
		row1[0] = Double.parseDouble(lineParts[5]);
		row1[1] = Double.parseDouble(lineParts[6]);
		row1[2] = Double.parseDouble(lineParts[7]);
		
		row2[0] = Double.parseDouble(lineParts[8]);
		row2[1] = Double.parseDouble(lineParts[9]);
		row2[2] = Double.parseDouble(lineParts[10]);
		
		row3[0] = Double.parseDouble(lineParts[11]);
		row3[1] = Double.parseDouble(lineParts[12]);
		row3[2] = Double.parseDouble(lineParts[13]);
		
		partName = lineParts[14];
		this.subpart = partFactory.getPart(this.partName);
		
		//1 colour x y z a b c d e f g h i part.dat
		
	}
	

	@Override
	public boolean isCommment() {
		return false;
	}


	public DrawnObject toDrawnObject(float[] color) {
		float[] temp = new float[]{1f,1f,1f}; 
		subpart.toDrawnObject();
		return new DrawnObject(vertices, temp);

	}
	public DrawnObject toDrawnObject() {
		//something about lineColorValue;
		float[] temp = new float[]{1f,1f,1f}; 
		subpart.toDrawnObject();
		return new DrawnObject(vertices, temp);

	}



}
