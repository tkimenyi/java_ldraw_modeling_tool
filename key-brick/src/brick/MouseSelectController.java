package brick;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Interact2D;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;
import com.threed.jpct.Object3D;

public class MouseSelectController extends MouseAdapter {
	private BrickPanel ra;
	private FrameBuffer buffer;
	private World world;
	private float pad;
	
	public MouseSelectController(BrickPanel renderingArea, World world, FrameBuffer buffer, float pad) {
		this.world = world;
		this.buffer = buffer;
		ra = renderingArea;
		this.pad = pad;
	}
	//On mouse click, select an object. If Ctrl is held, add object to selected list.
	//If Ctrl is held and object is in selected list, remove it.
	//If Ctrl is not held, make that object the only selected object.
	public void mouseClicked(MouseEvent me){
		//Get where it was clicked.
		Point clicked = me.getPoint();
		//Project that into the 3D world as a unit vector!
		SimpleVector dir =
			Interact2D.reproject2D3DWS(world.getCamera(), buffer, clicked.x, clicked.y).normalize();
		//Now, I'm guessing this extends the vector til it either hits pad, or an object. 
		Object[] res = world.calcMinDistanceAndObject3D(world.getCamera().getPosition(), dir, pad);
		System.out.println("Clicked!");
		//res[0] is a float that tells distance to the found object. res[1] is the object.
		//if no object is hit, res[0] is a special value, and res[1] is null.
		if(res[1] != null){
			//Here, get the exact object that was clicked, and go up the hierarchy to the top parent.
			//This will be the BrickObject to be selected, since we only have BrickObjects.
			Object3D[] pars = ((Object3D)res[1]).getParents();
			while(!ra.isBrick(pars[0])){
				pars = pars[0].getParents();
				//System.out.println("Up one.");
			}
			if(me.isControlDown()){
				ra.addOrRemoveSelectedBrick((BrickObject)pars[0]);
			} else {
				ra.setSelectedBrick((BrickObject)pars[0]);
			}
			System.out.println("...And a hit!");
		} else {
			System.out.println("...and a miss.");
		}
		
	}
}
