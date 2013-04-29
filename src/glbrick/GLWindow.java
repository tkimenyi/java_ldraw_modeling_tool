
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.TooManyListenersException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;

import java.nio.FloatBuffer;

import javax.swing.JFrame;
import org.lwjgl.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;

import static org.lwjgl.opengl.GL11.*;

@SuppressWarnings("serial")
public class GLWindow extends JFrame
{
	private static boolean closeRequested = false;
	private final static AtomicReference<Dimension> newCanvasSize = new AtomicReference<Dimension>();

	private String LdrawPath = "ldraw";
	
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
	double[][] rot = Matrix.identityMatrix();
	double speed = .3;
	double[] rankin = { 0, 0, 0 };
	double rex[] = { 0, 0, 0 };
	double[] theOrigin = { 0, 0, 0 };
	double[] notTheOrigin = { 0, 100, 0 };
	boolean freeCamera = true;
	double rotateSpeed = 1;
	double movementSpeed = .8;
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


	public GLWindow() throws LWJGLException{
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
		pf = new PartFactory(LdrawPath);


		glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);
		glEnable(GL_COLOR_MATERIAL);

		glDepthFunc(GL_LEQUAL);
		glEnable(GL_DEPTH_TEST);
		glShadeModel(GL_SMOOTH);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glFrustum(-1.0, 1.0, -1.0, 1.0, 1.2, 2000.0);
		glMatrixMode(GL_MODELVIEW);

		GL11.glEnable(GL11.GL_LIGHT1);
		GL11.glEnable(GL11.GL_LIGHTING);
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
	}

	public void display() throws InterruptedException, PartNotFoundException
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
		drawCrosshair(rex, white);

		updateSpeed();
		moveModel();
		glTranslated(modelloc[0], modelloc[1], modelloc[2]);
		rotateScene();
		drawObjects();


		glFlush();
	}

	public void rotateModel(double zangle, double xangle)
	{
		modelpyr[1] += zangle;
		modelpyr[0] += xangle;
	}

	public void translateModel(double x, double y, double z)
	{
		modelloc[0] += x;
		modelloc[1] += y;
		modelloc[2] += z;
	}

	public void rotateModelPart(int index, double xangle, double yangle, double zangle){

		double [][] xmatrix = {{1,0,0},{0, Math.cos(xangle), -Math.sin(xangle)}, {0, Math.sin(xangle), Math.cos(xangle)}};
		double [][] ymatrix = {{Math.cos(yangle),0,-Math.sin(yangle)},{0, 1, 0}, {Math.sin(yangle), 0, Math.cos(yangle)}};
		double [][] zmatrix = {{Math.cos(zangle),-Math.sin(zangle),0},{Math.sin(zangle), Math.cos(zangle),0}, {0, 0, 1}};

		double [][] xtransform = Matrix.matrixMult2(xmatrix, objects.get(index).getTransformation());
		double [][] ytransform = Matrix.matrixMult2(ymatrix, objects.get(index).getTransformation());
		double [][] ztransform = Matrix.matrixMult2(zmatrix, objects.get(index).getTransformation());
		double [][] transform = Matrix.matrixMult2(ytransform,xtransform);
		double [][] lastTransform = Matrix.matrixMult2(transform,ztransform);

		objects.get(index).setTransformation(lastTransform);
	}

	public void translateModelPart(int index, double x, double y, double z)
	{
		double newLocX = objects.get(index).getx() + x;
		double newLocY = objects.get(index).gety() + y;
		double newLocZ = objects.get(index).getz() + z;
		double [] loc = {newLocX, newLocY, newLocZ};

		objects.get(index).setLocation(loc);
	}

	private void drawCrosshair(double[] loc, double color[]){

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

	private void drawObjects(){
		for (DrawnObject obj : objects)
		{
			glPushMatrix();
			glTranslated(obj.getx(), obj.gety(), obj.getz());
			glRotated(180, 1, 0, 0);
			glMultMatrix(matrix_to_buffer(obj.getTransformation()));
			obj.draw();
			glPopMatrix();

		}

	}

	public static double[] buildSineTable(){
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

	public static double[] buildCosineTable(){
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

	public void updateSpeed(){
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

	public void camera() throws InterruptedException{
		if (Keyboard.isKeyDown(Keyboard.KEY_7))
		{
			System.out.println("Camera coordinates: " + X + " " + Y + " " + Z);
			System.out.println("Camera orientation: " + pitch + " " + yaw + " " + roll);
			Thread.sleep(50);
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

	public void moveModel(){
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

	private double[][] scaleTransform(double scale){
		return new double[][] { { scale, 0, 0, 0 }, { 0, scale, 0, 0 }, { 0, 0, scale, 0 }, { 0, 0, 0, 1 } };
	}

	public void addObject(String partname) throws PartNotFoundException{
		DrawnObject added = pf.getPart(partname).toDrawnObject();
		added.setTransformation(scaleTransform(.3));
		added.setParentLocation(rex);
		added.SetPartName(partname);
		objects.add(added);
	}

	public void rotateScene(){
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

	public void save(File f) throws MalformedLDrawException, IOException{
		BufferedWriter out = null;
		out = new BufferedWriter(new FileWriter(f, true));
		System.out.println("save called");
		//bad file writing code
		ArrayList<String> PLine = new ArrayList<String>(15);
		for(DrawnObject d: objects){
			System.out.println(objects.size());
			if (d.getPartName() != ""){
				PLine.add("1");
			}
			//color
			PLine.add(getColorCodeString(d.getColorArr()));
			//location
			for (double i : d.getLocation()){
				//should run exactly three times
				PLine.add(String.valueOf(i));
			}
			//transformation
			for (double[] dub: d.exportTransformation()){
				for (double d2: dub){
					PLine.add(String.valueOf(d2)); 
				}
			}
			//name
			PLine.add(d.getPartName());
			
			System.out.println("save test");
			out.write("0 Name: "+f.getName());
			for (String s : PLine){
				out.write(s + " ");
				System.out.print(s + " ");
			}
			PLine.clear();
			out.newLine();
		}
		
		if (out != null) {
			out.close();
		}
		
	}

	public String getColorCodeString(double[] color) throws FileNotFoundException, MalformedLDrawException {
		//fetches the colorcode (integer in ldraw file, string for convenience here
		//this code may not work as advertised.  Side effects may include confusion, dizziness, and hair loss.  Please consult a Dr. (or Professor) if you notice any issues.
		ArrayList<Integer> t = new ArrayList<Integer>(3);
		double tempCOL = 0.01;
		color[0] = tempCOL;
		color[1] = tempCOL;
		color[2] = tempCOL;
		for (double d: color){
			if(d <= 1 && d >= 0){
				t.add((int)(d*255.0));
			}
		}
		String hexVal = "";
		for (Integer i: t){
			String temp = Integer.toHexString(i);
			if(temp.length()<2){
				temp = "0" + temp;
			}
			hexVal = hexVal+ temp;
		}

		ColorBase CB = new ColorBase(LdrawPath);
		return CB.retrieveColorCode(hexVal, 255);

	}

	public static void main(String[] args) throws LWJGLException, InterruptedException, PartNotFoundException, FileNotFoundException{
		GLWindow window = new GLWindow();
		window.run(); 

	}

}
