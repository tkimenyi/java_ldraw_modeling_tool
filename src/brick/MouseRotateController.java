package brick;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.threed.jpct.World;

public class MouseRotateController extends MouseAdapter {
	private BrickPanel ra;
	private World world;
	private float pX;
	private float pY;
	
	public MouseRotateController(BrickPanel renderingArea, World world) {
		this.world = world;
		//this.buffer = buffer;
		ra = renderingArea;
	}
	
    public void mouseDragged( MouseEvent e )
    {
        // here we want to rotate the gear based on the mouse dragging
    	System.out.println("Drag");
        int x = e.getX();
        int y = e.getY();
        Dimension size = e.getComponent().getSize();

        float thetaY = 1.0f*(float)Math.PI * ( (float)(x-pX)/(float)size.width );
        float thetaX = 1.0f*(float)Math.PI * ( (float)(pY-y)/(float)size.height );

        pX = x;
        pY = y;
        
        world.getCamera().rotateX( thetaX );
        world.getCamera().rotateY( thetaY );
        ra.repaint();
    }
    
    public void mousePressed( MouseEvent e )
    {
        // set the "previous" mouse location
        // this prevent the gear from jerking to the new angle
        // whenever mouseDragged gets called
    	System.out.println("Press");
        pX = e.getX();
        pY = e.getY();
    }
}
