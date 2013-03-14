package glbrick;

import java.util.ArrayList;


public class QuadSpec implements BrickSpec {

	String[] lineParts;
	
	//private int c;
	private double[] one, two, three, four;
	private ArrayList<double[]> vertices = new ArrayList<double[]>();
	@SuppressWarnings("unused")
	private ColorBase colors;
	float lineColorValue;
	
	public QuadSpec(String[] lineParts) {
		super();
		// TODO Auto-generated constructor stub
		System.out.println("this probably should never be called");
		
	}

	public QuadSpec(String[] lineParts, ColorBase colors) { 
		System.out.println("quadspec called");
		
		//lineParts[0] is quad spec, so we already know that
		
		//System.out.println(lineParts[1].);
		
			lineColorValue = glbrickUtilities.smartDecode(lineParts[1]);
		
		
		//lineColorValue = Float.parseFloat(lineParts[1]);
		
		one = new double[3];
		two = new double[3];
		three = new double[3];
		four = new double[3];
		
		one[0] = Double.parseDouble(lineParts[2]);
		one[1] = Double.parseDouble(lineParts[3]);
		one[2] = Double.parseDouble(lineParts[4]);
		
		two[0] = Double.parseDouble(lineParts[5]);
		two[1] = Double.parseDouble(lineParts[6]);
		two[2] = Double.parseDouble(lineParts[7]);
		
		three[0] = Double.parseDouble(lineParts[8]);
		three[1] = Double.parseDouble(lineParts[9]);
		three[2] = Double.parseDouble(lineParts[10]);
		
		four[0] = Double.parseDouble(lineParts[11]);
		four[1] = Double.parseDouble(lineParts[12]);
		four[2] = Double.parseDouble(lineParts[13]);
		
		
		vertices.add(one);
		vertices.add(two);
		vertices.add(three);
		vertices.add(four);
		this.colors = colors;
	}


	@Override
	public boolean isCommment() {
		return false;
	}
	
	public String toString(){
		return glbrickUtilities.stringer(vertices); // does what you think it does
	}

	
	public DrawnObject toDrawnObject() {
		float[] temp = new float[]{1f,1f,1f}; 
		return new DrawnObject(vertices, temp);
	}
	



}
