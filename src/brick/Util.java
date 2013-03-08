package brick;

import java.io.File;
import java.util.ArrayList;

import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;

public class Util {
	private Util() {throw new UnsupportedOperationException("No Util objects");}
	
	public static SimpleVector matMul(Matrix m, SimpleVector v) {
		SimpleVector result = new SimpleVector(v);
		result.matMul(m);
		return result;
	}
	
	public static Matrix matMul(Matrix left, Matrix right) {
		Matrix result = new Matrix(right);
		result.matMul(left);
		return result;
	}

	public static String javafy(String s) {
		return "\"" + s.replace("\\", "\\\\").replace("\n", "\\n").replace("\t", "\\t").replace("\"", "\\\"") + "\"";
	}
	
	public static String fixPath(String s) {
		return s.replace('\\', File.separatorChar);
	}
	

	public static Object3D makeLineFrom(SimpleVector start, SimpleVector end, float width) {
		SimpleVector diff = end.calcSub(start); 
		Object3D line = new Object3D(Primitives.getCylinder(6, width, start.distance(end) / (2 * width)));
		line.rotateZ((float)Math.PI/2);
		line.rotateY((float)Math.PI/2);
		line.setRotationMatrix(Util.matMul(diff.getRotationMatrix(), line.getRotationMatrix()));
		line.translate((start.x + end.x) / 2, (start.y + end.y) / 2, (start.z + end.z) / 2); 
		return line;
	}
	
	public static float pointSide(SimpleVector p1, SimpleVector p2, SimpleVector query) {
		return (p2.x - p1.x) * (query.y - p1.y) - (p2.y - p1.y) * (query.x - p1.x);
	}
	
	
	public static SimpleVector findCenter(ArrayList<BrickColorPair> list){
		System.out.println("Called Find Center.");
		float x = 0; float y = 0; float z = 0;
		SimpleVector center;
		for(BrickColorPair obj : list){
			center = obj.getBrick().getTransformedCenter();
			x += center.x; y += center.y; z += center.z;
		}
		return new SimpleVector(x/list.size(), y/list.size(), z/list.size());
		
		
	}
}
