package brick;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JComponent;

import com.threed.jpct.Matrix;

public class MouseRotateObjectController extends MouseAdapter {
	private BrickObject target;
	private JComponent ra;
	private Matrix origRot;
	private float pZ;
	private float pY;
	
	public MouseRotateObjectController(JComponent renderingArea) {
		ra = renderingArea;
	}
	
	public MouseRotateObjectController(JComponent renderingArea, BrickObject obj) {
		ra = renderingArea;
		setObject(obj);
	}
	
	public void setObject(BrickObject obj){
		this.target = obj;
	}
	
    public void mouseDragged(MouseEvent e){
	    if(target != null){
	    	
	    	int z = e.getX();
	        int y = e.getY();
	        Dimension size = e.getComponent().getSize();
	        
	        //Buffer value to change dragging speed
	        float buff = 1.0f;
	        float thetaY = buff*(float)Math.PI*((float)(pZ-z)/(float)size.width);
	        float thetaZ = buff*(float)Math.PI*((float)(y-pY)/(float)size.height);
	
	        pZ = z;
	        pY = y;
	        
	        target.rotateZ( thetaZ );
	        target.rotateY( thetaY );
	        ra.repaint();
	        
    	}
    }
    
    public void mousePressed(MouseEvent e) {
        // Sets location for start of dragging.
    	if(target != null){
	    	origRot = target.getRotationMatrix().cloneMatrix(); 
	        pZ = e.getX();
	        pY = e.getY();
    	}
    }
    
    public void mouseReleased(MouseEvent e){
    	if(target != null){
    		target.setRotationMatrix(origRot.cloneMatrix());
    		ra.repaint();
    	}
    }
    
    public void mouseWheelMoved(MouseWheelEvent mwe){
    	if(target != null){
	    	int clicks = mwe.getWheelRotation();
	    	if(clicks < 0){
	    		target.scale(-(1.1f/1)*clicks);
	    		System.out.println((1/1.1f)*clicks);
	    	} else {
	    		target.scale((1/1.1f)*clicks);
	    		System.out.println((1/1.1f)*clicks);
	    	}
	    	ra.repaint();
    	}
    }
}
