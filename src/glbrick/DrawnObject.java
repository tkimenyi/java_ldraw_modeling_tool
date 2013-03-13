package glbrick;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.opengl.GL12.*;
//import static org.lwjgl.opengl.GL13.*;
//import static org.lwjgl.opengl.GL20.*;

public class DrawnObject
{

	//these should probably not be public...
	//This code should be more commeneted, especially how these parts interact
	//arraylist of children is clever, or so it appeared.  Maybe there should be a leaf for the bootom of the arraylist.
	//evidently this is a prime candiate for unit testing....
	public float[] color;
	ArrayList<DrawnObject> children; // Non-parts will have no children.
	double[][] transformation;//this should probably be final as well...
	public double[] location; // In Cartesian
	public ArrayList<double[]> vertices = new ArrayList<double[]>();

	public DrawnObject(ArrayList<double[]> vertices, double[] location, double[][] transformation, float[] color, ArrayList<DrawnObject> children)
	{
		this.children = children;
		this.transformation = transformation;
		this.vertices = vertices;
		this.location = location;
		this.color = color;
	}

	// constructor for non linetype 1 specs
	public DrawnObject(ArrayList<double[]> vertices, float[] color)
	{
		this(vertices, new double[] { 0, 0, 0 }, identityMatrix(), color, new ArrayList<DrawnObject>());
	}

	// constructor for testing purposes
	public DrawnObject(ArrayList<double[]> vertices, double[] location, float[] color)
	{
		this(vertices, location, identityMatrix(), color, new ArrayList<DrawnObject>());
	}

	// constructor for linetype 1's
	public DrawnObject(double[] location, double[][] transformation, float[] color, ArrayList<DrawnObject> children)
	{
		this(new ArrayList<double[]>(), location, transformation, color, children);
	}

	public static double[][] identityMatrix()
	{
		return new double[][] { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };

	}

	// Returns the location of the object in spherical coordinates,
	// [r,theta,phi]
	// These three are -------------------------------------------------------
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
		double theta = rtp[1];//this has a compiler warning.  Ferrer suspects a bug.
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

	// ---------------methods used to perform a rendering test (the spiral galaxy test).
	public void transformVertices()
	{
		transformVertices(transformation);
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
		//maybe make this static since is does nothing with the data structure.
		//or a utility object
		//could also think about creating a matrix class...  oh wait.  there's a matrix class, but it's empty...
		//apparently there are other empty classes.
		//also, legoGeometry  isn't necessary, all the methods could be static
		//similarily, objecttype should probably go away....
		double[] newv = new double[v.length];
		newv[0] = v[0] * m[0][0] + v[1] * m[0][1] + v[2] * m[0][2];
		newv[1] = v[0] * m[1][0] + v[1] * m[1][1] + v[2] * m[1][2];
		newv[2] = v[0] * m[2][0] + v[1] * m[2][1] + v[2] * m[2][2];
		return newv;
	}

	public double[] copyArray(double[] vertex)
	{
		double[] newVertex = new double[vertex.length];
		for (int i = 0; i < vertex.length; i++)
		{
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
		} else
		{
			glBegin(GL_LINE_LOOP);
			glColor3f(color[0], color[1], color[2]);
			for (double[] vertex : vertices)
			{
				glVertex3d(vertex[0] + location[0], vertex[1] + location[1], vertex[2] + location[2]);

			}
			glEnd();
		}

	}

}
