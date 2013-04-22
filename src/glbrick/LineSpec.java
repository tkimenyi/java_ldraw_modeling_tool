package glbrick;

import java.util.ArrayList;


public class LineSpec implements BrickSpec {

	String[] lineParts;
	
	//private int c;
	private double[] one, two;
	private ArrayList<double[]> vertices= new ArrayList<double[]>();
	@SuppressWarnings("unused")
	private ColorBase colors;
	float lineColorValue;
	
	public LineSpec(String[] lineParts) {
		super();
		// TODO Auto-generated constructor stub
		System.out.println("this probably should never be called");
		
	}

	public LineSpec(String[] lineParts, ColorBase colors) { 
		
		//lineParts[0] is quad spec, so we already know that
		
		lineColorValue = Float.parseFloat(lineParts[1]);//glbrickUtilities.smartDecode(lineParts[1]);
		
		one = new double[3];
		two = new double[3];
		one[0] = Double.parseDouble(lineParts[2]);
		one[1] = Double.parseDouble(lineParts[3]);
		one[2] = Double.parseDouble(lineParts[4]);
		
		two[0] = Double.parseDouble(lineParts[5]);
		two[1] = Double.parseDouble(lineParts[6]);
		two[2] = Double.parseDouble(lineParts[7]);

		
		
		vertices.add(one);
		vertices.add(two);
		this.colors = colors;
	}


	@Override
	public boolean isCommment() {
		return false;
	}
	
	public String toString(){
		return "";//glbrickUtilities.stringer(vertices); // does what you think it does
	}

	
	public DrawnObject toDrawnObject() {
		double[] temp = new double[]{1f,1f,1f}; 
		return new DrawnObject(vertices, temp);
	}



}
