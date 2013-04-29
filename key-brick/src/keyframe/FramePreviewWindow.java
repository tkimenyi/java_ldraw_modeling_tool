package keyframe;

import java.awt.Dimension;
import java.io.FileNotFoundException;

import brick.BrickObject;
import brick.BrickPanel;
import brick.BrickViewer;
import brick.MouseRotateObjectController;
import brick.PartFactory;
import brick.PartNotFoundException;
import brick.PartSpec;

import com.threed.jpct.Matrix;

@SuppressWarnings("serial")
public class FramePreviewWindow extends BrickPanel {
	BrickObject last;
	MouseRotateObjectController mroc;
	FramePreviewPane pane;
	
	public FramePreviewWindow(FramePreviewPane fpp, int width, int height) {
		super(width, height);
		pane = fpp;
		setPreferredSize(new Dimension(width, height));
		buildAll();
	}
	
	//Figure out which listeners are needed.
	public void setupListeners(){
//		mroc = new MouseRotateObjectController(this);
//		addMouseListener(mroc);
//		addMouseMotionListener(mroc);
//		addMouseWheelListener(mroc);
		//addKeyListener(new BrickKeyController((float)Math.PI/4, this));
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
}
