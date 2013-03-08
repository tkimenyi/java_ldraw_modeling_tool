package brick;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JFileChooser;

import com.threed.jpct.Matrix;

@SuppressWarnings("serial")
public class BrickPreviewWindow extends BrickPanel
								implements PropertyChangeListener {
	
	BrickObject last;
	MouseRotateObjectController mroc;
	
	public BrickPreviewWindow(JFileChooser fc, int width, int height) {
		super(width, height);
		setPreferredSize(new Dimension(width, height));
        fc.addPropertyChangeListener(this);
		buildAll();
	}
	
	
	public void setupListeners(){
		mroc = new MouseRotateObjectController(this);
		addMouseListener(mroc);
		addMouseMotionListener(mroc);
		addMouseWheelListener(mroc);
		//addKeyListener(new BrickKeyController((float)Math.PI/4, this));
	}
	
	public void addBrick(BrickObject brick){
		//if(last != null) removeAllObjects();
		last = brick;
	}
	
	public void display(String filename) throws FileNotFoundException, PartNotFoundException{
		if(filename != null){
			System.out.println("Filename: " + filename);
			PartFactory fact = new PartFactory(BrickViewer.ldrawPath);
			PartSpec model = fact.getPart(filename);
			
			BrickObject obj = model.toBrickObject(new Matrix(), this);
			obj.rotateY((float)(Math.PI/4.0f));
			obj.rotateZ((float)(Math.PI/4.0f));
			obj.setColorCode(2);
			obj.translateX(-0.1f);
			addBrick(obj);
			mroc.setObject(obj);
		}
		repaint();
	}
	@Override
	public void propertyChange(PropertyChangeEvent pce) {
        String prop = pce.getPropertyName();

        try {
	        if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(prop)) {
	            display(null);
	
	        //If a file became selected, find out which one.
	        } else if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(prop)) {
	        	if(pce.getNewValue() != null){
		        	File chosen = (File)pce.getNewValue();
		        	if(chosen.isFile()){
		        		removeAllObjects();
		        		display(chosen.getName());
		        	}
	        	}
	        }
        } catch (FileNotFoundException e) {
        	System.out.println("Somehow, that file doesn't exist.");
        } catch (PartNotFoundException e) {
        	System.out.println("Some part in this doesn't exist.");
        }
	}
}