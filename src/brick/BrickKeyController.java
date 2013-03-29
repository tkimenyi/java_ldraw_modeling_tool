package brick;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BrickKeyController extends KeyAdapter {
	private float angle;
	private BrickPanel renderingArea;
	
	public BrickKeyController(float angle, BrickPanel renderingArea) {
		this.angle = angle;
		this.renderingArea = renderingArea;
	}
	
	@Override
	//Group movements is based on the root of the selection.
	//While this works as it should, grouping has unwanted side-effects.
	//I'm working on reversing these side-effects.
	public void keyPressed(KeyEvent ke) {
		BrickObject target =  
			renderingArea.getSelectedBricks().isEmpty()
			? null
			: renderingArea.getSelectedBricks().get(0).getBrick();
		float mult = 1.0f;
		if(ke.isControlDown()) mult = 10.0f;
		else if(ke.isShiftDown()) mult = 0.1f;
		
		if (ke.getKeyCode() == KeyEvent.VK_U) {
			//for(BrickColorPair targ : renderingArea.getSelectedBricks()){
				/*target = targ.getBrick();
				SimpleVector oldPivot = new SimpleVector(target.getRotationPivot());
				SimpleVector oldTC = new SimpleVector(target.getTransformedCenter());
				SimpleVector newPivot = new SimpleVector(renderingArea.getSelectionPivot());
				newPivot.sub(oldTC);
				System.out.println("Dump:\n\tOldPivot:    " + target.getRotationPivot() +
								   "\n\tTransCenter: " + target.getTransformedCenter() +
								   "\n\tSelectPivot: " + newPivot +
								   "\n\tDiff:        " + oldTC);
				
				
				target.setRotationPivot(newPivot);*/
				//target.setRotationPivot(oldPivot);
			//}
			target.rotateX(-angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_I) {
			target.rotateX(angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_J) {
			target.rotateY(-angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_K) {
			target.rotateY(angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_M) {
			target.rotateZ(-angle);
		} else if (ke.getKeyCode() == KeyEvent.VK_COMMA) {
			target.rotateZ(angle);
		} else if (ke.getKeyCode() == 107) {
			target.scale(1.1f);
		} else if (ke.getKeyCode() == 109) {
			target.scale(1 / 1.1f);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD8) {
			target.translate(0f, -mult, 0f);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD2) {
			target.translate(0f, mult, 0f);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD4) {
			target.translate(0f, 0f, mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD6) {
			target.translate(0f, 0f, -mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD7) {
			target.translate(0f, -mult, mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD9) {
			target.translate(0f, -mult, -mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD1) {
			target.translate(0f, mult, mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD3) {
			target.translate(0f, mult, -mult);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD0) {
			target.translate(mult, 0f, 0f);
		} else if (ke.getKeyCode() == KeyEvent.VK_NUMPAD5) {
			target.translate(-mult, 0f, 0f);
		} else if (ke.getKeyCode() == KeyEvent.VK_ASTERISK) {
			target.setColorCode(renderingArea.getSelectedBricks().get(0).getColorCode() + 1);
			renderingArea.getSelectedBricks().set(0, new BrickColorPair(target));
			System.out.println("Asterisk!");
//		} else {
//			System.out.println(ke.getKeyChar() + ", " + ke.getKeyCode());
//			System.out.println("Not " + KeyEvent.VK_ASTERISK);
		}
		renderingArea.repaint();
		//System.out.println();
	}

//	private SimpleVector getCentralPoint(SimpleVector[] centers) {
//		if(centers.length == 0) return new SimpleVector();
//		if(centers.length == 1) return centers[0];
//		//SimpleVector res = pivots[0];
//		float x = 0; float y = 0; float z = 0;
//		for(SimpleVector p : centers){
//			x += p.x;
//			y += p.y;
//			z += p.z;
//		}
//		
//		SimpleVector res = new SimpleVector(x, y, z);
//		res.scalarMul(1.0f/(float)centers.length);
//		return res;
//		
//	}
	
	
}
