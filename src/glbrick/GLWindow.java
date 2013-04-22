
package glbrick;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.ArrayList;
import java.util.TooManyListenersException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

import java.nio.FloatBuffer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.lwjgl.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL11.*;

@SuppressWarnings("serial")
public class GLWindow extends JFrame
{
	private static boolean closeRequested = false;
	private final static AtomicReference<Dimension> newCanvasSize = new AtomicReference<Dimension>();
	float lightAmbient[] = { .1f, .1f, .1f, .2f };
	float lightDiffuse[] = { 0.5f, 0.5f, 0.5f, .1f};
	float lightPosition[] = { 0f, 1.0f, 0f, 1f };
	int which = 0;

	FloatBuffer lightpos = BufferUtils.createFloatBuffer(4);
	DoubleBuffer matrax = BufferUtils.createDoubleBuffer(16);
	PartFactory pf;
	double rotateRate = 1;
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
	boolean gridEnabled = true;
	static double[] red = { 1, 0, 0 };
	static double[] blue = { 0, 0, 1 };
	static double[] green = { 0, 1, 0 };
	static double[] yellow = { 1, 1, 0 };
	static double[] white = { 1, 1, 1 };
	double piover180 = Math.PI / 180.;
	double[] sineTable = buildSineTable();
	double[] cosineTable = buildCosineTable();
	double scalex, scaley;
	double pitch = 90;
	double yaw = 0;
	double roll = 0;
	double X = 0;
	double Y = -70;
	double Z = 0;

	double[] zpos = { 0, 0, 1 };
	double[] zneg = { 0, 0, -1 };
	double[] xpos = { 1, 0, 0 };
	double[] xneg = { -1, 0, 0 };

	boolean keyPressed = false;

	CopyOnWriteArrayList<DrawnObject> objects = new CopyOnWriteArrayList<DrawnObject>();
	final Canvas canvas = new Canvas();
	private GuInterface guInterface;


	public GLWindow() throws LWJGLException
	{
		super("GL Window");

		setLayout(new BorderLayout());
		guInterface = new GuInterface(this);
		setJMenuBar(guInterface.createMenuBar());
		getContentPane().add(guInterface.getToolBar(), BorderLayout.NORTH);
		
		canvas.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e)
			{ newCanvasSize.set(canvas.getSize()); }
		});
		addWindowFocusListener(new WindowAdapter() {
			@Override 
			public void windowGainedFocus(WindowEvent e)
			{canvas.requestFocusInWindow(); }
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e)
			{closeRequested = true;}
		});
		getContentPane().add(canvas, BorderLayout.CENTER);

		getContentPane().add(guInterface.getListPanels(), BorderLayout.WEST);
		setPreferredSize(new Dimension(1024, 686));
		setMinimumSize(new Dimension(800, 500));
		pack();
		Display.setParent(canvas);
		Display.setDisplayModeAndFullscreen(new DisplayMode(canvas.getWidth(), canvas.getHeight()));
		Display.create();
	}

	public static FloatBuffer vector_to_buffer(float[] vector)
	{
		FloatBuffer result = BufferUtils.createFloatBuffer(vector.length);
		for (int i = 0; i < vector.length; i++)
		{
			result.put(vector[i]);
		}
		result.position(0);

		return result;
	}

	public static DoubleBuffer matrix_to_buffer(double[][] trans)
	{
		int size = trans[0].length * trans.length;
		DoubleBuffer buffer = BufferUtils.createDoubleBuffer(size);
		for (int i = 0; i < trans.length; i++)
		{
			for (int j = 0; j < trans[0].length; j++)
			{
				buffer.put(trans[i][j]);
			}
		}
		buffer.position(0);
		return buffer;

	}

	public void run() throws InterruptedException, PartNotFoundException, FileNotFoundException
	{
		pf = new PartFactory("ldraw");


		glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);
		glEnable(GL_COLOR_MATERIAL);

		glDepthFunc(GL_LEQUAL);
		glEnable(GL_DEPTH_TEST);
		glShadeModel(GL_SMOOTH);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glFrustum(-1.0, 1.0, -1.0, 1.0, 1.2, 2000.0);
		glMatrixMode(GL_MODELVIEW);
		//glViewport(0, 0, 1280, 1024);

		//glEnable(GL_LIGHT0);
		GL11.glEnable(GL11.GL_LIGHT1);
		GL11.glEnable(GL11.GL_LIGHTING);

		//glLight(GL_LIGHT0, GL_AMBIENT, (FloatBuffer)BufferUtils.createFloatBuffer(4).put(lightAmbient).flip());
		drawCubed(1, 1, 1, red);
		drawCube(red, 0, red);
		objects.add(new DrawnObject(makeCube(), new double[] { 1, 0, 0 }, white));
		setVisible(true);
		canvas.setDropTarget(new DropTarget());
		try {
			canvas.getDropTarget().addDropTargetListener(new DropTargetListener(){

				@Override
				public void dragEnter(DropTargetDragEvent dtde) {}

				@Override
				public void dragOver(DropTargetDragEvent dtde) {}
				@Override
				public void dropActionChanged(DropTargetDragEvent dtde) {}
				@Override
				public void dragExit(DropTargetEvent dte) {}
				@Override
				public void drop(DropTargetDropEvent dtde) {
					Transferable tr = dtde.getTransferable();
					DataFlavor[] flavors = tr.getTransferDataFlavors();
					for(DataFlavor fl : flavors){
						if(fl.isFlavorTextType()){
							try {
								String partFile = (String)(tr.getTransferData(DataFlavor.stringFlavor));
								addObject(partFile);
							} catch (UnsupportedFlavorException e) {
								e.printStackTrace();
							} catch (IOException e) {
								e.printStackTrace();
							} catch (PartNotFoundException e) {
								e.printStackTrace();
							}
						}
					}
				}

			});
		} catch (TooManyListenersException e1) {
			e1.printStackTrace();
		}
		Display.setVSyncEnabled(true);
		//Display.create();

		Dimension newDim;

		while (!Display.isCloseRequested() && !closeRequested)
		{
			newDim = newCanvasSize.getAndSet(null);

			if (newDim != null)
			{
				GL11.glViewport(0, 0, newDim.width, newDim.height);
				//	              renderer.syncViewportSize();
			}
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
			display();

			Display.sync(60);
			// You see this line?
			Display.update();
			// Don't you DARE remove it!
			// Doing so will lock up the entire machine!
		}

		Display.destroy();
		dispose();
		
		//System.exit(1);
	}
	public void handleKeyboardEvents() throws InterruptedException, PartNotFoundException
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_COMMA))
		{
			Thread.sleep(90);
			addObject("3003.dat");
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_U))
		{
			translateModel(0, 0, -.4);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_J))
		{
			translateModel(0, 0, .4);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_H))
		{
			translateModel(-.4, 0, 0);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_K))
		{
			translateModel(.4, 0, 0);
		}

	}

	private void drawLights(float[] lightDiffuse, float[] lightPosition )
	{
		ByteBuffer temp = BufferUtils.createByteBuffer(16);
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, (FloatBuffer) temp.asFloatBuffer().put(lightDiffuse).flip());
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, (FloatBuffer) temp.asFloatBuffer().put(lightPosition).flip());

		//GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, (FloatBuffer) temp.asFloatBuffer().put(lightAmbient).flip());
		//GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, (FloatBuffer) temp.asFloatBuffer().put(lightPosition).flip());
	}

	void display() throws InterruptedException, PartNotFoundException
	{

		// These three lines are necessary custodial commands. Don't touch em.
		glMatrixMode(GL_MODELVIEW);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glLoadIdentity();

		camera();

		handleKeyboardEvents();

		glRotated(roll, 0, 0, 1);
		glRotated(pitch, 1, 0, 0);
		glRotated(yaw, 0, 1, 0);

		glTranslated(X, Y, Z);


		drawLights(lightDiffuse, lightPosition);

		if (gridEnabled)
		{
			drawGrid(30, 0, 30, yellow, 1, theOrigin);
			drawGrid(300, 0, 300, red, 7, notTheOrigin);
		}

		drawCrosshair(rex, white);

		updateSpeed();
		moveModel();

		drawCubek(0, 0, 0, red);



		glTranslated(modelloc[0], modelloc[1], modelloc[2]);
		rotateScene();
		drawObjects();


		glFlush();
	}

	void rotateModel(double zangle, double xangle)
	{
		modelpyr[1] += zangle;
		modelpyr[0] += xangle;
	}

	void translateModel(double x, double y, double z)
	{
		modelloc[0] += x;
		modelloc[1] += y;
		modelloc[2] += z;
	}

	void removeLastPiece()
	{
		if(objects.size()>0)
			objects.remove(objects.size() - 1);
	}

	void drawCrosshair(double[] loc, double color[])
	{

		double x, y, z;
		x = loc[0];
		y = loc[1];
		z = loc[2];
		glColor3d(color[0], color[1], color[2]);
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

	void drawGrid(int x, int y, int z, double[] color, double resolution, double[] loc)
	{
		glColor3d(color[0], color[1], color[2]);
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
	void drawCubed(double x, double y, double z, double[] color)
	{
		glColor3d(color[0], color[1], color[2]);
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
		//		Iterator<DrawnObject> iter = objects.iterator();
		for (DrawnObject obj : objects)
		{
			//			DrawnObject obj = iter.next();
			glPushMatrix();
			glTranslated(obj.getx(), obj.gety(), obj.getz());
			glRotated(180, 1, 0, 0);
			glMultMatrix(matrix_to_buffer(obj.getTransformation()));
			obj.draw();
			glPopMatrix();

		}

	}

	void drawCubek(double x, double y, double z, double[] color)
	{
		glColor3d(color[0], color[1], color[2]);
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

	void drawSolidCube(double[] loc, double size, double[] color)
	{
		glColor3d(color[0], color[1], color[2]);
		glBegin(GL_QUADS);

		// Front and back faces
		glNormal3d(0, 0, -1);
		glVertex3d(loc[0], loc[1], loc[2]);
		glVertex3d(size + loc[0], loc[1], loc[2]);
		glVertex3d(size + loc[0], size + loc[1], loc[2]);
		glVertex3d(loc[0], size + loc[1], loc[2]);
		glNormal3d(0, 0, 1);
		glVertex3d(loc[0], loc[1], loc[2] + size);
		glVertex3d(size + loc[0], loc[1], loc[2] + size);
		glVertex3d(size + loc[0], size + loc[1], loc[2] + size);
		glVertex3d(loc[0], size + loc[1], loc[2] + size);

		// Top and bottom
		glNormal3d(0, -1, 0);
		glVertex3d(loc[0], loc[1], loc[2]);
		glVertex3d(loc[0] + size, loc[1], loc[2]);
		glVertex3d(loc[0] + size, loc[1], loc[2] + size);
		glVertex3d(loc[0], loc[1], loc[2] + size);
		glNormal3d(0, 1, 0);
		glVertex3d(loc[0], loc[1] + size, loc[2]);
		glVertex3d(loc[0] + size, loc[1] + size, loc[2]);
		glVertex3d(loc[0] + size, loc[1] + size, loc[2] + size);
		glVertex3d(loc[0], loc[1] + size, loc[2] + size);

		// Left and Right
		glNormal3d(-1, 0, 0);
		glVertex3d(loc[0], loc[1], loc[2]);
		glVertex3d(loc[0], loc[1], loc[2] + size);
		glVertex3d(loc[0], loc[1] + size, loc[2] + size);
		glVertex3d(loc[0], loc[1] + size, loc[2]);
		glNormal3d(1, 0, 0);
		glVertex3d(loc[0] + size, loc[1], loc[2]);
		glVertex3d(loc[0] + size, loc[1], loc[2] + size);
		glVertex3d(loc[0] + size, loc[1] + size, loc[2] + size);
		glVertex3d(loc[0] + size, loc[1] + size, loc[2]);

		glEnd();
	}

	void drawCube(double[] xyz, double size, double[] color)
	{
		glColor3d(color[0], color[1], color[2]);
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

	public void zoomIn(){
		Z += movementSpeed * Math.cos(piover180 * yaw);
		Y += movementSpeed * Math.sin(piover180 * pitch);
		X -= movementSpeed * Math.sin(piover180 * yaw);
	}

	public void zoomOut(){
		Z -= movementSpeed * Math.cos(piover180 * yaw);
		Y -= movementSpeed * Math.sin(piover180 * pitch);
		X += movementSpeed * Math.sin(piover180 * yaw);
	}

	void camera() throws InterruptedException
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_7))
		{
			System.out.println("Camera coordinates: " + X + " " + Y + " " + Z);
			System.out.println("Camera orientation: " + pitch + " " + yaw + " " + roll);
			Thread.sleep(50);
		}
		if (Mouse.isButtonDown(0))
		{
			// X = (Mouse.getX() - Display.getDisplayMode().getWidth() / 2.0) /
			// 100.0;
			// Y = (Mouse.getY() - Display.getDisplayMode().getWidth() / 2.0) /
			// 100.0;
			removeLastPiece();
			Thread.sleep(100);
		}

		// Does not handle roll!
		if (Keyboard.isKeyDown(Keyboard.KEY_UP))
		{
			zoomIn();

		}
		// Does not handle roll!
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
		{
			zoomOut();
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
		if (Keyboard.isKeyDown(Keyboard.KEY_Y))
		{
			movementSpeed -= .01;
			System.out.println("MOVEMENT SPEED = " + movementSpeed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_C))
		{
			
			System.out.println("ROTATION SPEED = " + rotateSpeed);
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_X))
		{
			
			System.out.println("ROTATION SPEED = " + rotateSpeed);
		}
	}

	void moveModel()
	{
		if (Keyboard.isKeyDown(Keyboard.KEY_W))
		{
			rex[2] -= .3;
			lightPosition[2]= (float)rex[2];
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			rex[2] += .3;
			lightPosition[2]=(float)rex[2];
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D))
		{
			rex[0] += .3;
			lightPosition[0]=(float)rex[1];
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A))
		{
			rex[0] -= .3;
			lightPosition[0]=(float)rex[1];
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Z))
		{
			rex[1] -= .3;
			lightPosition[1] = (float)rex[1];
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_X))
		{
			rex[1] += .3;
			lightPosition[1] = (float)rex[1];

		}

		if (Keyboard.isKeyDown(Keyboard.KEY_B)){
			lightPosition[3]+=.1;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_N)){
			lightPosition[3]-=.1;
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
		glColor3d(beta.getRed(), beta.getGreen(), beta.getBlue());
		double[] loc = beta.getLocation();
		for (double[] vertex : beta.getVertices())
		{
			glVertex3d(vertex[0] + loc[0], vertex[1] + loc[1], vertex[2] + loc[2]);
		}
		glEnd();
	}

	double[][] scaleTransform(double scale)
	{
		return new double[][] { { scale, 0, 0, 0 }, { 0, scale, 0, 0 }, { 0, 0, scale, 0 }, { 0, 0, 0, 1 } };
	}

	void addObject(String partname) throws PartNotFoundException
	{
		DrawnObject added = pf.getPart(partname).toDrawnObject();
		added.setTransformation(scaleTransform(.3));
		added.setParentLocation(rex);
		objects.add(added);
	}

	void addCubes(double[] color) throws InterruptedException
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
		glRotated(modelpyr[1], 0, 1, 0);
		glRotated(modelpyr[0], 1, 0, 0);
		glRotated(modelpyr[2], 0, 0, 1);

	}
	
	public static void main(String[] args) throws LWJGLException, InterruptedException, PartNotFoundException, FileNotFoundException
	{
		GLWindow window = new GLWindow();
		window.run(); 
		
	}

}