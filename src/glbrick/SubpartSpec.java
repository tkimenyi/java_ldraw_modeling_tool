package glbrick;

import java.util.ArrayList;



public class SubpartSpec implements BrickSpec {
	
	

	String[] lineParts;
	//private int c;
	private double[] loc = {0,0,0};
	private double[][] trans = { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };
	private ArrayList<double[]> vertices;
	@SuppressWarnings("unused")
	private ColorBase colors;
	float lineColorValue;
	String partName = "";
	private PartSpec part;
	
	

	public SubpartSpec(String[] lineParts) {
		super();
		// TODO Auto-generated constructor stub
	}
	public String toString(){
		return "";//glbrickUtilities.stringer(vertices); // does what you think it does
	}
	
	public SubpartSpec(String[] lineParts, PartFactory partFactory) throws PartNotFoundException 
	{
		this.lineParts = lineParts;

		lineColorValue = Float.parseFloat(lineParts[1]);

		loc[0] = Double.parseDouble(lineParts[2]);
		loc[1] = Double.parseDouble(lineParts[3]);
		loc[2] = Double.parseDouble(lineParts[4]);

		for (int i = 5; i <= 13; i++)
		{
			trans[(i-5) / 3][(i-5) % 3] = Double.parseDouble(lineParts[i]);
		}
		

		partName = lineParts[14];
		
		this.part = partFactory.getPart(this.partName);
		
		//1 colour x y z a b c d e f g h i part.dat
		
	}
	

	@Override
	public boolean isCommment() {
		return false;
	}

	public DrawnObject toDrawnObject() 
	{
		//something about lineColorValue;
		float[] temp = new float[]{1f,1f,1f}; 
		DrawnObject tempmodel = part.toDrawnObject();
		tempmodel.setLocation(loc);
		tempmodel.setTransformation(trans);
		return tempmodel;
		
		
		

	}



}
