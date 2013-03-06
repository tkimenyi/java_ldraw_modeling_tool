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
	
	public double[][] identityMatrix(){
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
		for (double[] vertex : vertices)
		{
			vertex[0] = vertex[0]*transformation[0][0] + vertex[1]*transformation[0][1] + vertex[2]*transformation[0][2];
			vertex[1] = vertex[0]*transformation[1][0] + vertex[1]*transformation[1][1] + vertex[2]*transformation[1][2];
			vertex[2] = vertex[0]*transformation[2][0] + vertex[1]*transformation[2][1] + vertex[2]*transformation[2][2];
		}
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
			transformVertices();
			glBegin(GL_LINE_LOOP);
			glColor3f(color[0], color[1], color[2]);
			for(double[] vertex : vertices){
				glVertex3d(vertex[0], vertex[1], vertex[2]);
				
			}
			glEnd();
		}

	}

}
