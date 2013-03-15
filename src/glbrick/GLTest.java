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
//import static org.lwjgl.opengl.GL12.*;
//import static org.lwjgl.opengl.GL13.*;
//import static org.lwjgl.opengl.GL20.*;

public class GLTest
{
	static PartFactory pf;
	static double rotateRate = .1;
	static double[] modelpyr = { 0, 0, 0 };
	static double[] modelloc = { 0, 0, 0 };
	static double[][] rot = DrawnObject.identityMatrix();
	static double speed = .3;
	static double[] rankin = { 0, 0, 0 };
	static double rex[] = { 0, 0, 0 };
	static boolean shit = true;
	static double[] theOrigin = { 0, 0, 0 };
	static double[] notTheOrigin = { 0, 100, 0 };
	static boolean freeCamera = true;
	static double rotateSpeed = 1;
	static double movementSpeed = .8;
	static boolean gridEnabled = false;
	static float[] red = { 1, 0, 0 };
	static float[] blue = { 0, 0, 1 };
	static float[] green = { 0, 1, 0 };
	static float[] yellow = { 1, 1, 0 };
	static float[] white = { 1, 1, 1 };
	static double piover180 = Math.PI / 180.;
	static double[] sineTable = buildSineTable();
	static double[] cosineTable = buildCosineTable();
	static double scalex, scaley, X, Y, Z;
	static float pitch, yaw, roll;

	static double[] zpos = { 0, 0, 1 };
	static double[] zneg = { 0, 0, -1 };
	static double[] xpos = { 1, 0, 0 };
	static double[] xneg = { -1, 0, 0 };

	static boolean keyPressed = false;

	static ArrayList<DrawnObject> objects = new ArrayList<DrawnObject>();

	public static void main(String[] args) throws InterruptedException, FileNotFoundException, PartNotFoundException
	{

		pf = new PartFactory("ldraw");
		objects.add(new DrawnObject(makeCube(), new double[] { 0, 0, 0 }, red));
		try
		{
			Display.setDisplayModeAndFullscreen(new DisplayMode(1280, 1024));
			Display.create();

		} catch (Exception e)
		{
		}

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

	static void updateSpeed()
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

	static void reorganize(double speed)
	{
		for (DrawnObject d : objects)
		{
			double[] rtp = d.getSphericalCoordinates();
			double angle = speed / rtp[0];
			d.revolve(0, angle);
		}
	}

	static void camera()

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

	static void moveshit()
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

	static ArrayList<double[]> makeCube()
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

	static void drawCube(DrawnObject beta)
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

	static void addObject(String partname) throws PartNotFoundException
	{
			objects.add(pf.getPart(partname).toDrawnObject());
	}

	static void addCubes(float[] color) throws InterruptedException
	{

		if (Keyboard.isKeyDown(Keyboard.KEY_COMMA))
		{
			double[] loc = new double[] { rex[0], rex[1], rex[2] };
			objects.add(new DrawnObject(makeCube(), loc, color));
		}

	}

	static void rotateScene()
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
//		if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8) || Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2))
//		{
//			rot[0][0] = Math.cos(modelpyr[0]);
//			rot[0][1] = Math.sin(modelpyr[0]);
//			rot[1][0] = -Math.sin(modelpyr[0]);
//			rot[1][1] = Math.cos(modelpyr[0]);
//			try
//			{
//				Thread.sleep(90);
//			} catch (InterruptedException e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			for (DrawnObject d : objects)
//			{
//				d.transformVertices(rot);
//				System.out.println(d.vertices.get(2)[0]);
//			}
//		}
		glRotated(modelpyr[1], 0, 1, 0);
		glRotated(modelpyr[0], 1, 0, 0);
		glRotated(modelpyr[2], 0, 0, 1);
		
	}

	static void display() throws InterruptedException, PartNotFoundException
	{

		// These three lines are necessary custodial commands. Don't touch em.
		glMatrixMode(GL_MODELVIEW);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();
		//
		//
		// BEGIN CAMERA MOVEMENT CODE
		camera();
		// END CAMERA MOVEMENT CODE
		//
		//
		//addCubes(white);
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
		moveshit();

		

		drawCubek(0, 0, -4, red);
		drawCubek(4, 0, 0, blue);
		drawCubek(0, 0, 4, green);
		drawCubek(-4, 0, 0, yellow);
		
		glTranslated(modelloc[0],modelloc[1],modelloc[2]);
		drawObjects();

		glFlush();
	}

	static void rotateModel(double angle)
	{
		modelpyr[1] += angle;
	}

	static void translateModel(double x, double y, double z)
	{
		modelloc[0] += x;
		modelloc[1] += y;
		modelloc[2] += z;
	}
	

	static void drawCrosshair(double[] loc, float color[])
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

	static void drawSineWave()
	{

		glColor3f(0.0f, 0f, 1.0f);
		glBegin(GL_LINE_STRIP);
		for (int i = 0; i < 36000; i += 1)
		{
			glVertex3d(i / 3600.0, sineTable[i], 0);

		}
		glEnd();
	}

	static void drawCosineWave()
	{
		glColor3f(0.0f, .5f, 0);
		glBegin(GL_LINE_STRIP);
		for (int i = 0; i < 36000; i += 1)
		{
			glVertex3d(i / 3600.0, cosineTable[i], 0);

		}
		glEnd();
	}

	static void drawGrid(int x, int y, int z, float[] color, double resolution, double[] loc)
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
	static void drawCubed(double x, double y, double z, float[] color)
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
	static void drawObjects()
	{
		rotateScene();
		for (DrawnObject obj : objects)
		{
			obj.draw();
		}

	}

	static double sine(double angle)
	{
		if (angle < 0)
		{
			return sineTable[(int) (((angle * -1) % 360) * 100)] * -1;
		} else
		{
			return sineTable[(int) ((angle % 360) * 100)];
		}
	}

	static double cosine(double angle)
	{
		return cosineTable[Math.abs((int) ((angle % 360) * 100))];
	}

	static void drawCubek(float x, float y, float z, float[] color)
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

	static void drawCube(double[] xyz, double size, float[] color)
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

}
