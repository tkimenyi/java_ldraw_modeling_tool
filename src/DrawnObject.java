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
	Matrix transformation;
	public double[] location; // In Cartesian
	public ArrayList<int[]> vertices = new ArrayList<int[]>();

	public DrawnObject(ArrayList<int[]> vs, double[] loc, ObjectType type, float[] color)
	{
		children = new ArrayList<DrawnObject>();
		transformation = new Matrix();
		vertices = vs;
		location = loc;
		this.color = color;
		this.type = type;
	}

	// Returns the location of the object in spherical coordinates, [r,theta,phi]
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
			glBegin(GL_LINE_LOOP);
			glColor3f(color[0], color[1], color[2]);
			for(int[] vertex : vertices){
				glVertex3d(vertex[0], vertex[1], vertex[2]);
				
			}
			glEnd();
		}

	}

}
