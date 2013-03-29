package brick;

// TODO: The colors don't look right here.

import com.threed.jpct.*;
import java.awt.Color;

@SuppressWarnings("serial")
public class OptionalLineObject extends BrickObject {
	private SimpleVector p1, p2, control1, control2;
	private ColorBase colors;
	private BrickPanel world;
	
	public OptionalLineObject(Object3D src, BrickPanel world, ColorBase colors, SimpleVector p1, SimpleVector p2, SimpleVector control1, SimpleVector control2) {
		super(src, world, colors, "(optional edge)");
		this.p1 = p1;
		this.p2 = p2;
		this.control1 = control1;
		this.control2 = control2;
		this.world = world;
		this.colors = colors;
	}
	
	public void adjustColor() {
		int code = findColor();
		Color c = showLine() ? colors.getEdgeColor(code) : colors.getColor(code);
		setAdditionalColor(c);
	}
	
	private SimpleVector projectPoint(BrickPanel world, SimpleVector p) {
		Matrix transform = getWorldTransformation();
		return world.project3D2D(Util.matMul(transform, p));
	}
	
	public boolean showLine() {
		//These changes are to na�vely deal with the camera hating the origin.
		//Nulls happen when the projections are impossible, as such, it makes
		//sense to return false when some such projection fails.
		SimpleVector project1 = projectPoint(world, p1);
		if(project1 == null) return false;
		SimpleVector project2 = projectPoint(world, p2);
		if(project2 == null) return false;
		SimpleVector projectC1 = projectPoint(world, control1);
		if(projectC1 == null) return false;
		SimpleVector projectC2 = projectPoint(world, control2);
		if(projectC2 == null) return false;
		
		float ctrlSide1 = Util.pointSide(project1, project2, projectC1);
		float ctrlSide2 = Util.pointSide(project1, project2, projectC2);
		return (ctrlSide1 < 0 && ctrlSide2 < 0) || (ctrlSide1 > 0 && ctrlSide2 > 0);
	}
}

