package brick;

import java.awt.Color;
import java.awt.Graphics;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.JPanel;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.IRenderer;
import com.threed.jpct.Interact2D;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

@SuppressWarnings("serial")
public class BrickPanel extends JPanel {
	private World world;
	private FrameBuffer buffer;
	private ArrayList<BrickObject> bricks;
	private ArrayList<BrickColorPair> selected;
	private AdjustmentPane ap;
	public final int selectColor = 7;
	
	
	public BrickPanel() {
		this(800, 600);
	}
	
	public BrickPanel(int width, int height) {
		super();
		world = new World();
		world.setAmbientLight(0, 0, 0);
		buffer = new FrameBuffer(width, height, FrameBuffer.SAMPLINGMODE_NORMAL);
		bricks = new ArrayList<BrickObject>();
		selected = new ArrayList<BrickColorPair>();
	}
	
	public ArrayList<BrickObject> getBricks(){
		return bricks;
	}
	

	public void setupDefaultListeners(){
		MouseRotateController mrc = new MouseRotateController(this, world);
		
		addMouseListener(new MouseSelectController(this, world, buffer, 10000));
		addMouseListener(mrc);
		addMouseMotionListener(mrc);
		
	}

	public BrickObject addNewBrick(String fileLoc) throws PartNotFoundException, FileNotFoundException {
		PartFactory fact = new PartFactory(BrickViewer.ldrawPath);
		PartSpec model = fact.getPart(fileLoc);
		BrickObject obj = model.toBrickObject(new Matrix(), this);
		addBrick(obj);
		return obj;
	}
	
	//Add an Object3D to the world (component/child of a full BrickObject).
	//Also needs to be called on the BrickObjects themselves.
	public void addObject(BrickObject obj) {
		world.addObject(obj);
		obj.setCulling(Object3D.CULLING_DISABLED);
		obj.compile();
	}
	
	public void removeObject(BrickObject obj){
		world.removeObject(obj);
		
	}
	
	public World getWorld(){
		return world;
	}
	
	public void setAdjustmentPane(AdjustmentPane ap){
		this.ap = ap;
		
	}
	
	public boolean isBrick(Object3D o){
		return o instanceof BrickObject ? bricks.contains(o) : false;
	}
	
	public boolean isTopBrick(Object3D o){
		return o instanceof BrickObject ? (bricks.contains(o) && !((BrickObject)o).isGrouped()) : false;
	}
	
	public int indexOf(BrickObject brick){
		return bricks.indexOf(brick);
	}
	
	//Add a completed BrickObject to the ArrayList.
	//Provides for easier tracking of full objects.
	public void addBrick(BrickObject brick){
		bricks.add(brick);
	}
	
	public int numBricks(){
		return bricks.size();
	}
	
	public void removeAllObjects(){
		world.removeAllObjects();
		selected.clear();
		bricks.clear();
	}
	
	//Get the color that the brick was born with or that it was changed to,
	//but never get the selection color unless that's it.
	//Returns -1 if the brick doesn't exist.
	public int getTrueColor(BrickObject obj){
		//Normally this line always returns false. Not true here.
		if(!selected.contains(new BrickColorPair(obj))){
			if(!bricks.contains(obj)) return -1;
			return obj.findColor();
		}
		return selected.get(selected.indexOf(new BrickColorPair(obj))).getColorCode();
	}
	
	public int getTrueColor(int index){
		if(index >= bricks.size() || index < 0) return -1;
		return getTrueColor(bricks.get(index));
	}
	
	//Change it from some number of selected bricks to only one.
	//Also edits colors here now.
	public void setSelectedBrick(BrickObject b){
		//Need first brick: Selection hierarchy makes this one the "root" of what's chosen.
		if(!selected.isEmpty()){
			//Backwards so that it doesn't change the root each time.
			for(int i = selected.size() - 1; i >= 0; i--){
				deselectBrick(selected.get(i).getBrick());
			}
		}
		selected.add(new BrickColorPair(b));
		b.setColorCode(selectColor);
		repaint();
		//ap.updateSelectedObject(b);
	}
	
	//Call it when the color of a brick has changed.
	public void updateBrick(BrickObject obj){
		BrickColorPair pair = new BrickColorPair(obj);
		if(selected.contains(pair))
			selected.set(selected.indexOf(pair), new BrickColorPair(obj, getTrueColor(obj)));
	}
	
	//Set from the information pane.
	//Precondition: 0 <= i < bricks.size()
	public void setSelectedBrick(int i){
		setSelectedBrick(bricks.get(i));
	}
	
	public void deselectBrick(BrickObject b){
		//Then remove.
		System.out.println("Contains");
		//If this is the only selected object, it's not its own parent.
		if(selected.size() > 1){
			BrickObject base = selected.get(0).getBrick();
			if(base.equals(b)){
				//If we're trying to deselect the base brick.
				BrickObject newBase = selected.get(1).getBrick();
				//!newBase.getRotationMatrix().matMul(base.getRotationMatrix().cloneMatrix().invert());
				base.removeChild(newBase);
				
				newBase.getRotationMatrix().matMul(base.getRotationMatrix());
				//b.getTranslationMatrix().matMul(base.getTranslationMatrix().cloneMatrix().invert());
				newBase.getTranslationMatrix().matMul(base.getWorldTransformation());
				
				for(int i = 2; i < selected.size(); i++){
					BrickObject brick = selected.get(i).getBrick();
					base.removeChild(brick);
					brick.getRotationMatrix().matMul(base.getRotationMatrix());
					//b.getTranslationMatrix().matMul(base.getTranslationMatrix().cloneMatrix().invert());
					brick.getTranslationMatrix().matMul(base.getWorldTransformation());
					newBase.addChild(brick);
					brick.getRotationMatrix().matMul(newBase.getRotationMatrix().invert());
					//b.getTranslationMatrix().matMul(base.getTranslationMatrix().cloneMatrix().invert());
					brick.getTranslationMatrix().matMul(newBase.getWorldTransformation().invert());
				}
				
			} else {
				base.removeChild(b);
				b.getRotationMatrix().matMul(base.getRotationMatrix());
				//b.getTranslationMatrix().matMul(base.getTranslationMatrix().cloneMatrix().invert());
				b.getTranslationMatrix().matMul(base.getWorldTransformation());
			}
		}
		//This is a weird line. We're basically just wanting the original color and this gets it
		BrickColorPair pair = new BrickColorPair(b);
		b.setColorCode(selected.get(selected.indexOf(pair)).getColorCode());
		selected.remove(pair);
	}
	
	public void selectBrick(BrickObject b){
		//Then add.
		
		selected.add(new BrickColorPair(b));
		System.out.println("Does not contain");
		b.setColorCode(selectColor);
		//"If we're not about to try and set a brick to be its own parent"
		if(selected.size() > 1){
			//System.out.println("Before Adding: " + b.getTranslation());
			//System.out.println("Before Adding: " + b.getCenter());
			BrickObject base = selected.get(0).getBrick();
			SimpleVector trans = new SimpleVector(base.getTranslation());
			trans.scalarMul(-1);
			//Matrix origRot = b.getRotationMatrix();
			//SimpleVector origCenter = b.getCenter();
			base.addChild(b);
			b.getRotationMatrix().matMul(base.getRotationMatrix().invert());
			//b.getTranslationMatrix().matMul(base.getTranslationMatrix().cloneMatrix().invert());
			b.getTranslationMatrix().matMul(base.getWorldTransformation().invert());
			//b.translate(trans);
			//SimpleVector newCenter = b.getCenter();
			//System.out.println("Orig Center: " + origCenter);
			//System.out.println("New Center: " + newCenter);
			//b.getCenter();
			//b.set(base.getInverseWorldTransformation());
			//System.out.println("After adding: " + b.getTranslation());
			//System.out.println("After adding: " + b.getCenter());
			//base.translate(trans);
		}
	}
	
	//Add a new brick to the selection or remove it from the selection if it's already selected.
	public void addOrRemoveSelectedBrick(BrickObject b){
		BrickColorPair pair = new BrickColorPair(b);
		if(!selected.contains(pair)){
			//Then add.
			System.out.println("Does not contain");
			selectBrick(b);
		} else {
			//Then remove.
			System.out.println("Contains");
			deselectBrick(b);
		}
	}
	
	public ArrayList<BrickColorPair> getSelectedBricks(){
		return selected;
	}
	
	public BrickObject getMostRecent() {
		return bricks.get(bricks.size() - 1);
	}
	
	public BrickObject getObject(int index){
		return bricks.get(index >= bricks.size() ? bricks.size() - 1 : index);
	}
	
	public void buildAll() {
		world.buildAllObjects();
		//Camera cam = world.getCamera();
//		world.getCamera().setPosition(0, -50, 0);
//		world.getCamera().lookAt(new SimpleVector(0, 1, 0));//getMostRecent().getTransformedCenter());
		world.getCamera().setPosition(-100.0f, 0, 0);
		world.getCamera().lookAt(new SimpleVector(1f, 0, 0));//getMostRecent().getTransformedCenter());
		
	}
	
	public SimpleVector project3D2D(SimpleVector point3D) {
		return Interact2D.project3D2D(world.getCamera(), buffer, point3D);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void paintComponent(Graphics g) {
		if(ap != null) ap.update();
		//This reeeeally makes Java unhappy.
		for (Enumeration<BrickObject> bricks = world.getObjects(); bricks.hasMoreElements();) {
			bricks.nextElement().adjustColor();
		}
		buffer.clear(Color.BLUE);
		world.renderScene(buffer);
		world.draw(buffer);
		buffer.update();
		buffer.display(g);
	}
	
	public void dispose() {
		buffer.disableRenderer(IRenderer.RENDERER_OPENGL);
		buffer.dispose();
	}
}
