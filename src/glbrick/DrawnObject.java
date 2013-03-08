package glbrick;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;

public class DrawnObject
{
	ObjectType type;
	public float[] color;
	ArrayList<DrawnObject> children; //Non-parts will have no children.
	double[][] transformation;
	public double[] location; // In Cartesian
	public ArrayList<double[]> vertices = new ArrayList<double[]>();

	public DrawnObject(ArrayList<double[]> vs, double[] loc, ObjectType type, float[] color)
	{
		children = new ArrayList<DrawnObject>();
		transformation = identityMatrix();
		
		vertices = vs;
		location = loc;
		this.color = color;
		this.type = type;
	}

	// Returns the location of the object in spherical coordinates, [r,theta,phi]
	
	public static double[][] identityMatrix(){
		return new double[][]{
				{1,0,0,0},
				{0,1,0,0},
				{0,0,1,0}, 
				{0,0,0,1}};
		
	}
	public double[] getSphericalCoordinates()
	{
		double[] rtp = new double[3];
		rtp[0] = Math.sqrt(location[0] * location[0] + location[1] * location[1] + location[2] * location[2]);
		rtp[1] = 0;// Math.acos(location[1]/rtp[0]);
		rtp[2] = Math.atan2(location[0], location[2]);

		return rtp;
	}

	public void setSphericalCoordiates(double[] rtp)
	{
		double r = rtp[0];
		double theta = rtp[1];
		double phi = rtp[2];

		location[2] = r * Math.cos(phi);
		location[0] = r * Math.sin(phi);

	}

	public void revolve(double theta, double phi)
	{

		double[] rtp = getSphericalCoordinates();
		rtp[1] += theta;
		rtp[2] += phi;
		setSphericalCoordiates(rtp);
	}
	public void transformVertices(){
		transformVertices(transformation);
	}
	public void transformVertices(double[][] transformation)
	{
		
		for(int i =0; i<vertices.size();i++)
		{	double[] vertex = vertices.get(i);
			vertex = matrixMult(transformation, vertex);	
			vertices.set(i, vertex);
			
		}
	}
	
	public double[] matrixMult(double[][] m, double[] v)
	{
		double[] newv = new double[v.length];	
		newv[0] = v[0]*m[0][0] + v[1]*m[0][1] + v[2]*m[0][2];
		newv[1] = v[0]*m[1][0] + v[1]*m[1][1] + v[2]*m[1][2];
		newv[2] = v[0]*m[2][0] + v[1]*m[2][1] + v[2]*m[2][2];
		return newv;
	}
	
	public double[] copyArray(double[] vertex){
		double[] newVertex = new double[vertex.length];
		for(int i =0;i<vertex.length;i++){
			newVertex[i] = vertex[i];
		}
		return newVertex;
	}

	public void draw()
	{
		if (children.size() > 0)
		{
			for (DrawnObject child : children)
			{
				child.draw();
			}
		}
		else{
			//transformVertices();
			glBegin(GL_LINE_LOOP);
			glColor3f(color[0], color[1], color[2]);
			for(double[] vertex : vertices){
				glVertex3d(vertex[0]+location[0], vertex[1] + location[1], vertex[2] + location[2]);
				
			}
			glEnd();
		}

	}

}
