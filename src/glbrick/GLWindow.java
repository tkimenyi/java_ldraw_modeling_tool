package glbrick;

import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.Color;
import org.lwjgl.util.glu.GLU;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL43.*;

public class GLWindow
{
	
	FloatBuffer lightpos = BufferUtils.createFloatBuffer(3);
	DoubleBuffer matrax = BufferUtils.createDoubleBuffer(16);
	PartFactory pf;
	double rotateRate = .1;
	double[] modelpyr = { 0, 0, 0 };
	double[] modelloc = { 0, 0, 0 };
	double[][] rot = DrawnObject.identityMatrix();
	double speed = .3;
	double[] rankin = { 0, 0, 0 };
	double rex[] = { 0, 0, 0 };
	boolean shit = true;
	double[] theOrigin = { 0, 0, 0 };
	double[] notTheOrigin = { 0, 100, 0 };
	boolean freeCamera = true;
	double rotateSpeed = 1;
	double movementSpeed = .8;
	boolean gridEnabled = false;
	static float[] red = { 1, 0, 0 };
	static float[] blue = { 0, 0, 1 };
	static float[] green = { 0, 1, 0 };
	static float[] yellow = { 1, 1, 0 };
	static float[] white = { 1, 1, 1 };
	double piover180 = Math.PI / 180.;
	double[] sineTable = buildSineTable();
	double[] cosineTable = buildCosineTable();
	double scalex, scaley, X, Y, Z;
	float pitch, yaw, roll;

	double[] zpos = { 0, 0, 1 };
	double[] zneg = { 0, 0, -1 };
	double[] xpos = { 1, 0, 0 };
	double[] xneg = { -1, 0, 0 };

	boolean keyPressed = false;

	ArrayList<DrawnObject> objects = new ArrayList<DrawnObject>();

	public GLWindow() throws LWJGLException
	{
		
		Display.setDisplayModeAndFullscreen(new DisplayMode(1280, 1024));
		Display.create();
	}

	void puttin(double[][] trans)
	{
		

		for (int i = 0; i < trans.length; i++)
		{
			for (int j = 0; j < trans[0].length; j++)
			{
				matrax.put(trans[j][i]);
			}
		}
		matrax.position(0);

	}

	public void run() throws InterruptedException, PartNotFoundException, FileNotFoundException
	{
		pf = new PartFactory("ldraw");
		objects.add(new DrawnObject(makeCube(), new double[] { 0, 0, 0 }, white));

		double[][] trans = { { 2, 0, 0, 0 }, { 0, 13, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		puttin(trans);

		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		glEnable(GL_COLOR_MATERIAL);
		glDepthFunc(GL_LEQUAL);
		glEnable(GL_DEPTH_TEST);
		glShadeModel(GL_SMOOTH);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glFrustum(-1.0, 1.0, -1.0, 1.0, 1.2, 2000.0);
		glMatrixMode(GL_MODELVIEW);
		glViewport(0, 0, 1280, 1024);
		while (!Display.isCloseRequested())
		{
			display();
			Display.sync(60);
			// You see this line?
			Display.update();
			// Don't you DARE remove it!
			// Doing so will lock up the entire machine!
		}
		Display.destroy();
	}

	public void handleKeyboardEvents() throws InterruptedException, PartNotFoundException
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_COMMA))
		{
			Thread.sleep(90);
			addObject("car.dat");
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_G))
		{
			gridEnabled = !gridEnabled;
			Thread.sleep(100);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_R))
		{
			reorganize(speed);
		}
	}

	void display() throws InterruptedException, PartNotFoundException
	{

		// These three lines are necessary custodial commands. Don't touch em.
		glMatrixMode(GL_MODELVIEW);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();

		camera();

		handleKeyboardEvents();

		glRotatef(roll, 0, 0, 1);
		glRotatef(pitch, 1, 0, 0);
		glRotatef(yaw, 0, 1, 0);

		glTranslated(X, Y, Z);

		if (gridEnabled)
		{
			drawGrid(30, 0, 30, yellow, 1, theOrigin);
			drawGrid(300, 0, 300, red, 7, notTheOrigin);
		}

		drawCrosshair(rex, white);

		updateSpeed();
		moveModel();

		drawCubek(0, 0, -4, red);
		

		glPushMatrix();
		glMultMatrix(matrax);
		drawCubek(-4, 0, 0, yellow);
		glPopMatrix();

		glTranslated(modelloc[0], modelloc[1], modelloc[2]);
		rotateScene();
		drawSolidCube(new double[] {2, 2, 2},4,green);
		drawObjects();
		glFlush();
	}

	void rotateModel(double angle)
	{
		modelpyr[1] += angle;
	}

	void translateModel(double x, double y, double z)
	{
		modelloc[0] += x;
		modelloc[1] += y;
		modelloc[2] += z;
	}

	void drawCrosshair(double[] loc, float color[])
	{
		
		double x, y, z;
		x = loc[0];
		y = loc[1];
		z = loc[2];
		glColor3f(color[0], color[1], color[2]);
		glBegin(GL_LINE_STRIP);
		glVertex3d(0 + x, 0 + y, 0 + z);
		glVertex3d(1 + x, 0 + y, 0 + z);
		glVertex3d(-1 + x, 0 + y, 0 + z);
		glVertex3d(0 + x, 1 + y, 0 + z);
		glVertex3d(0 + x, -1 + y, 0 + z);
		glVertex3d(1 + x, 0 + y, 0 + z);
		glVertex3d(0 + x, 0 + y, -1 + z);
		glVertex3d(0 + x, 0 + y, 1 + z);
		glVertex3d(-1 + x, 0 + y, 0 + z);
		glEnd();
	}

	void drawGrid(int x, int y, int z, float[] color, double resolution, double[] loc)
	{
		glColor3f(color[0], color[1], color[2]);
		for (double i = x * -1; i < x; i += resolution)
		{
			glBegin(GL_LINE_STRIP);
			glVertex3d(i + loc[0], y + loc[1], -z + loc[2]);
			glVertex3d(i + loc[0], y + loc[1], z + loc[2]);
			glEnd();
		}
		for (double i = z * -1; i < z; i += resolution)
		{
			glBegin(GL_LINE_STRIP);
			glVertex3d(-x + loc[0], y + loc[1], i + loc[2]);
			glVertex3d(x + loc[0], y + loc[1], i + loc[2]);
			glEnd();
		}
	}

	// This will draw a wireframe cube of a fixed size at the location (x,y,z)
	// with color {red, green, blue}
	void drawCubed(double x, double y, double z, float[] color)
	{
		glColor3f(color[0], color[1], color[2]);
		glBegin(GL_LINE_STRIP);
		glVertex3d(x, y, z);
		glVertex3d(1 + x, y, z);
		glVertex3d(1 + x, 1 + y, z);
		glVertex3d(x, 1 + y, z);

		glVertex3d(x, 1 + y, -1 + z);
		glVertex3d(x, y, -1 + z);
		glVertex3d(1 + x, y, -1 + z);
		glVertex3d(1 + x, 1 + y, -1 + z);
		glVertex3d(x, 1 + y, -1 + z);

		glVertex3d(x, 1 + y, z);
		glVertex3d(x, y, z);
		glVertex3d(x, y, -1 + z);
		glVertex3d(1 + x, y, -1 + z);
		glVertex3d(1 + x, y, z);
		glVertex3d(1 + x, 1 + y, z);
		glVertex3d(1 + x, 1 + y, -1 + z);
		glEnd();
	}

	// Currently creates a line loop for the given vertices in the DrawnObject
	void drawObjects()
	{
		for (DrawnObject obj : objects)
		{
			obj.draw();
		}

	}

	void drawCubek(float x, float y, float z, float[] color)
	{
		glColor3f(color[0], color[1], color[2]);
		glBegin(GL_LINE_STRIP);
		glVertex3f(x, y, z);
		glVertex3f(1 + x, y, z);
		glVertex3f(1 + x, 1 + y, z);
		glVertex3f(x, 1 + y, z);

		glVertex3f(x, 1 + y, -1 + z);
		glVertex3f(x, y, -1 + z);
		glVertex3f(1 + x, y, -1 + z);
		glVertex3f(1 + x, 1 + y, -1 + z);
		glVertex3f(x, 1 + y, -1 + z);

		glVertex3f(x, 1 + y, z);
		glVertex3f(x, y, z);
		glVertex3f(x, y, -1 + z);
		glVertex3f(1 + x, y, -1 + z);
		glVertex3f(1 + x, y, z);
		glVertex3f(1 + x, 1 + y, z);
		glVertex3f(1 + x, 1 + y, -1 + z);
		glEnd();

	}


	void drawSolidCube(double[] loc, double size, float[] color)
	{
		glColor3f(color[0], color[1], color[2]);
		glBegin(GL_QUADS);
		
		//Front and back faces
		glNormal3d(0, 0, -1);
		glVertex3d(loc[0], loc[1], loc[2]);
		glVertex3d(size + loc[0], loc[1], loc[2]);
		glVertex3d(size + loc[0], size + loc[1], loc[2]);
		glVertex3d(loc[0], size + loc[1], loc[2]);
		glNormal3d(0, 0, 1);
		glVertex3d(loc[0], loc[1], loc[2]+size);
		glVertex3d(size + loc[0], loc[1], loc[2]+size);
		glVertex3d(size + loc[0], size + loc[1], loc[2]+size);
		glVertex3d(loc[0], size + loc[1], loc[2]+size);
		
		//Top and bottom
		glNormal3d(0,-1,0);
		glVertex3d(loc[0],loc[1],loc[2]);
		glVertex3d(loc[0]+size,loc[1],loc[2]);
		glVertex3d(loc[0]+size,loc[1],loc[2]+size);
		glVertex3d(loc[0],loc[1],loc[2]+size);
		glNormal3d(0,1,0);
		glVertex3d(loc[0],loc[1]+size,loc[2]);
		glVertex3d(loc[0]+size,loc[1]+size,loc[2]);
		glVertex3d(loc[0]+size,loc[1]+size,loc[2]+size);
		glVertex3d(loc[0],loc[1]+size,loc[2]+size);
		
		//Left and Right
		glNormal3d(-1,0,0);
		glVertex3d(loc[0],loc[1],loc[2]);
		glVertex3d(loc[0],loc[1],loc[2]+size);
		glVertex3d(loc[0],loc[1]+size,loc[2]+size);
		glVertex3d(loc[0],loc[1]+size,loc[2]);
		glNormal3d(1, 0, 0);
		glVertex3d(loc[0]+size,loc[1],loc[2]);
		glVertex3d(loc[0]+size,loc[1],loc[2]+size);
		glVertex3d(loc[0]+size,loc[1]+size,loc[2]+size);
		glVertex3d(loc[0]+size,loc[1]+size,loc[2]);
		
		glEnd();
	}

	void drawCube(double[] xyz, double size, float[] color)
	{
		glColor3f(color[0], color[1], color[2]);
		glBegin(GL_LINE_STRIP);
		glVertex3d(xyz[0], xyz[1], xyz[2]);
		glVertex3d(size + xyz[0], xyz[1], xyz[2]);
		glVertex3d(size + xyz[0], size + xyz[1], xyz[2]);
		glVertex3d(xyz[0], size + xyz[1], xyz[2]);

		glVertex3d(xyz[0], size + xyz[1], -size + xyz[2]);
		glVertex3d(xyz[0], xyz[1], -size + xyz[2]);
		glVertex3d(size + xyz[0], xyz[1], -size + xyz[2]);
		glVertex3d(size + xyz[0], size + xyz[1], -size + xyz[2]);
		glVertex3d(xyz[0], size + xyz[1], -size + xyz[2]);

		glVertex3d(xyz[0], size + xyz[1], xyz[2]);
		glVertex3d(xyz[0], xyz[1], xyz[2]);
		glVertex3d(xyz[0], xyz[1], -size + xyz[2]);
		glVertex3d(size + xyz[0], xyz[1], -size + xyz[2]);
		glVertex3d(size + xyz[0], xyz[1], xyz[2]);
		glVertex3d(size + xyz[0], size + xyz[1], xyz[2]);
		glVertex3d(size + xyz[0], size + xyz[1], -size + xyz[2]);
		glEnd();

	}

	public static double[] buildSineTable()
	{
		double piover180 = (Math.PI / 180.0);
		double[] Table = new double[36001];
		int intdegree = 0;
		for (double degree = 0; degree < 360; degree += 0.01)
		{

			Table[intdegree] = Math.sin(piover180 * degree);

			intdegree++;
		}
		return Table;
	}

	public static double[] buildCosineTable()
	{
		double piover180 = (Math.PI / 180.0);
		double[] cosineTable = new double[36001];
		int intdegree = 0;
		for (double degree = 0; degree < 360; degree += 0.01)
		{

			cosineTable[intdegree] = Math.cos(piover180 * degree);
			intdegree++;

		}
		return cosineTable;
	}

	void updateSpeed()
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_ADD))
		{
			speed += .01;
			rotateRate += .01;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT))
		{
			speed -= .01;
			rotateRate -= .01;
		}
	}

	void reorganize(double speed)
	{
		for (DrawnObject d : objects)
		{
			double[] rtp = d.getSphericalCoordinates();
			double angle = speed / rtp[0];
			d.revolve(0, angle);
		}
	}

	void camera()

	{
		if (Mouse.isButtonDown(0))
		{
			X = (Mouse.getX() - Display.getDisplayMode().getWidth() / 2.0) / 100.0;
			Y = (Mouse.getY() - Display.getDisplayMode().getWidth() / 2.0) / 100.0;

		}
		// Does not handle roll!
		if (Keyboard.isKeyDown(Keyboard.KEY_UP))
		{
			Z += movementSpeed * Math.cos(piover180 * yaw);
			Y += movementSpeed * Math.sin(piover180 * pitch);
			X -= movementSpeed * Math.sin(piover180 * yaw);
		}
		// Does not handle roll!
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
		{
			Z -= movementSpeed * Math.cos(piover180 * yaw);
			Y -= movementSpeed * Math.sin(piover180 * pitch);
			X += movementSpeed * Math.sin(piover180 * yaw);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
		{
			yaw += rotateSpeed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
		{
			yaw -= rotateSpeed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_1))
		{
			pitch += rotateSpeed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_4))
		{
			pitch -= rotateSpeed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_I))
		{
			movementSpeed += .01;
			System.out.println("MOVEMENT SPEED = " + movementSpeed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_U))
		{
			movementSpeed -= .01;
			System.out.println("MOVEMENT SPEED = " + movementSpeed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_C))
		{
			rotateSpeed += .01;
			System.out.println("ROTATION SPEED = " + rotateSpeed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_X))
		{
			rotateSpeed -= .01;
			System.out.println("ROTATION SPEED = " + rotateSpeed);
		}
	}

	void moveModel()
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			rex[2] -= .3;
			modelloc[2] -= .3;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			rex[2] += .3;
			modelloc[2] += .3;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			rex[0] += .3;
			modelloc[0] += .3;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			rex[0] -= .3;
			modelloc[0] -= .3;
		}
	}

	ArrayList<double[]> makeCube()
	{
		ArrayList<double[]> vertices = new ArrayList<double[]>();

		vertices.add(new double[] { 0, 0, 0 });
		vertices.add(new double[] { 1, 0, 0 });
		vertices.add(new double[] { 1, 0, 1 });
		vertices.add(new double[] { 0, 0, 1 });
		vertices.add(new double[] { 0, 0, 0 });

		vertices.add(new double[] { 0, 1, 0 });
		vertices.add(new double[] { 1, 1, 0 });
		vertices.add(new double[] { 1, 1, 1 });
		vertices.add(new double[] { 0, 1, 1 });
		vertices.add(new double[] { 0, 1, 0 });

		return vertices;
	}

	void drawCube(DrawnObject beta)
	{
		glBegin(GL_LINE_STRIP);
		glColor3f(beta.color[0], beta.color[1], beta.color[2]);
		double[] loc = beta.location;
		for (double[] vertex : beta.vertices)
		{
			glVertex3d(vertex[0] + loc[0], vertex[1] + loc[1], vertex[2] + loc[2]);
		}
		glEnd();
	}

	void addObject(String partname) throws PartNotFoundException
	{
		System.out.println(objects.isEmpty());
		System.out.println(partname);
		objects.add(pf.getPart(partname).toDrawnObject());
	}

	void addCubes(float[] color) throws InterruptedException
	{

		if (Keyboard.isKeyDown(Keyboard.KEY_COMMA))
		{
			double[] loc = new double[] { rex[0], rex[1], rex[2] };
			objects.add(new DrawnObject(makeCube(), loc, color));
		}

	}

	void rotateScene()
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8))
		{
			modelpyr[0] += rotateRate;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2))
		{
			modelpyr[0] -= rotateRate;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4))
		{
			modelpyr[1] += rotateRate;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6))
		{
			modelpyr[1] -= rotateRate;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD7))
		{
			modelpyr[2] += rotateRate;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD9))
		{
			modelpyr[2] -= rotateRate;
		}
		// if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8) ||
		// Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2))
		// {
		// rot[0][0] = Math.cos(modelpyr[0]);
		// rot[0][1] = Math.sin(modelpyr[0]);
		// rot[1][0] = -Math.sin(modelpyr[0]);
		// rot[1][1] = Math.cos(modelpyr[0]);
		// try
		// {
		// Thread.sleep(90);
		// } catch (InterruptedException e)
		// {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// for (DrawnObject d : objects)
		// {
		// d.transformVertices(rot);
		// System.out.println(d.vertices.get(2)[0]);
		// }
		// }
		glRotated(modelpyr[1], 0, 1, 0);
		glRotated(modelpyr[0], 1, 0, 0);
		glRotated(modelpyr[2], 0, 0, 1);

	}

}