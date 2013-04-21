package glbrick;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.opengl.GL12.*;
//import static org.lwjgl.opengl.GL13.*;
//import static org.lwjgl.opengl.GL20.*;

public class DrawnObject
{
	private boolean comment = false;
	private double[] color = { 1, 1, 1 }; // Default is white
	private ArrayList<DrawnObject> children = new ArrayList<DrawnObject>(); // Non-parts will have no children.
	private double[][] transformation = identityMatrix();
	private double[] location = { 0, 0, 0 }; // In Cartesian
	private ArrayList<double[]> vertices = new ArrayList<double[]>();

	//This is only called by the CommentSpec.toDrawnObject() method.
	public DrawnObject()
	{
		comment = true;
	}
	public double getRed(){
		return color[0];
	}
	public double getGreen(){
		return color[1];
	}public double getBlue(){
		return color[2];
	}
	public double[] doubleCopy(double[] d){
		double[] n = new double[d.length];
		for (int i =0; i < d.length; i++){
			n[i] = d[i];
		}
		return n;
	}
	
	public double[][] twoDArrayCopy(double[][] d){
		double[][] n = new double[d.length][d[0].length];
		for (int i =0; i < d.length; i++){
			n[i] = doubleCopy(d[i]);
		}
		return n;
	}
	
	public ArrayList<double[]> doubleArrListCopy(ArrayList<double[]> d){
		ArrayList<double[]> n = new ArrayList<double[]>();
		for (double[] i: d){
			n.add(i);		}
		return n;
	}
	
	public double[] getLocation(){
		return doubleCopy(location);
	}
	public ArrayList<double[]> getVertices(){
		return doubleArrListCopy(vertices);
	}
	
	//Constructor for a general DrawnObject that requires all of the fields set. This one will not be called except internally (I think)
	//However, I don't want to make it private just yet.
	private DrawnObject(ArrayList<double[]> vertices, double[] location, double[][] transformation, double[] color, ArrayList<DrawnObject> children)
	{
		this.children = children;
		this.transformation = transformation;
		this.vertices = vertices;
		transformVertices(this.transformation);
		this.location = location;
		this.color = color;
	}

	public DrawnObject(ArrayList<DrawnObject> children)
	{
		this(new ArrayList<double[]>(), new double[] { 0, 0, 0 }, identityMatrix(), new double[] { 1, 1, 1 }, children);
	}

	// constructor for non-linetype 1 specs
	public DrawnObject(ArrayList<double[]> vertices, double[] color)
	{
		this(vertices, new double[] { 0, 0, 0 }, identityMatrix(), color, new ArrayList<DrawnObject>());
		System.out.print("color: ");
		for (double d : color){
			System.out.print(d + " ");
		}
		System.out.print("\n");
		
	}

	// constructor for testing purposes
	public DrawnObject(ArrayList<double[]> vertices, double[] location, double[] color)
	{
		this(vertices, location, identityMatrix(), color, new ArrayList<DrawnObject>());
	}

	// constructor for linetype 1's
	public DrawnObject(double[] location, double[][] transformation, double[] color, ArrayList<DrawnObject> children)
	{
		this(new ArrayList<double[]>(), location, transformation, color, children);
	}

	public void setTransformation(double[][] trans)
	{
		for (int i = 0; i < trans.length; i++)
		{
			for (int j = 0; j < trans[0].length; j++)
			{
				transformation[i][j] = trans[i][j];
			}
		}
		transformALL(transformation);

	}

	public void setParentLocation(double[] loc)
	{
		location[0] = loc[0];
		location[1] = loc[1];
		location[2] = loc[2];
	}

	public void setLocation(double[] loc)
	{
		setParentLocation(loc);
		for (DrawnObject child : children)
		{
			child.setLocation(loc);
		}
	}

	
	public void transformALL(double[][] trans)
	{
		transformVertices(trans);

		for (DrawnObject d : children)
		{
			d.transformVertices(trans);
		}
	}

	public void move(double[] vector)
	{
		location[0] += vector[0];
		location[1] += vector[1];
		location[2] += vector[2];
	}

	

	public static double[][] identityMatrix()
	{
		return new double[][] { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };

	}


	// This method will change later because of how rendering is currently handled. The transformations should not modify the set of vertices.
	public void transformVertices(double[][] transformation)
	{

		for (int i = 0; i < vertices.size(); i++)
		{
			double[] vertex = vertices.get(i);
			vertex = matrixMult(transformation, vertex);
			vertices.set(i, vertex);

		}

	}

	public double[] matrixMult(double[][] m, double[] v)
	{
		double[] newv = new double[v.length];
		newv[0] = v[0] * m[0][0] + v[1] * m[0][1] + v[2] * m[0][2];
		newv[1] = v[0] * m[1][0] + v[1] * m[1][1] + v[2] * m[1][2];
		newv[2] = v[0] * m[2][0] + v[1] * m[2][1] + v[2] * m[2][2];
		return newv;
	}


	public void draw()
	{
		if (comment)
			return;

		if (children.size() > 0)
		{
			for (DrawnObject child : children)
			{
				child.draw();
			}
		} else
		{
			glColor3d(color[0], color[1], color[2]);
			int type = vertices.size();
			if (type == 2)
			{
				glBegin(GL_LINE);
			}
			if (type == 3)
			{
				glBegin(GL_TRIANGLES);
			}
			if (type == 4)
			{
				glBegin(GL_QUADS);
			}
			if(type == 3 || type ==4)
			{
				double[] v1 = vertices.get(0);
				double[] v2 = vertices.get(1);
				double[] v3 = vertices.get(2);
				
				double[] shit = subtract(v2,v1);
				double[] gyrocopter = subtract(v3,v2);
				
				double[] normal = cross_product(gyrocopter, shit);
				normal = normalize(normal);
				glNormal3d(normal[0], normal[1], normal[2]);
			}
			
			for (double[] vertex : vertices)
			{
				glVertex3d(vertex[0] + location[0], vertex[1] + location[1], vertex[2] + location[2]);

			}
			glEnd();

		}
	}


	public double[][] getTransformation()
	{
		return twoDArrayCopy(transformation);
	}

	public double getx()
	{
		return location[0];
	}

	public double gety()
	{
		return location[1];
	}

	public double getz()
	{
		return location[2];
	}

	public String toString()
	{
		return "Location: " + location[0] + ", " + location[1] + ", " + location[2];
	}

	public boolean isComment()
	{
		return comment;
	}
	
	// v1 - v2
	public double[] subtract(double[] v1, double[] v2)
	{
		double[] result = new double[v1.length];
		for (int i = 0; i < result.length; i++)
		{
			result[i] = v1[i] - v2[i];
		}
		return result;
	}

	public double magnitude(double[] v)
	{
		double result = 0;
		for (double i : v)
		{
			result += i * i;
		}
		return Math.sqrt(result);
	}

	public double[] normalize(double[] v)
	{
		double[] result = new double[v.length];
		result[0] = v[0] / magnitude(v);
		result[1] = v[1] / magnitude(v);
		result[2] = v[2] / magnitude(v);
		return result;
	}

	public double[] cross_product(double[] v1, double[] v2)
	{
		double[] result = new double[v1.length];

		result[0] = v1[1] * v2[2] - v1[2] * v2[1];
		result[1] = v1[2] * v2[0] - v1[0] * v2[2];
		result[2] = v1[0] * v2[1] - v1[1] * v2[0];

		return result;

	}
}