package keyframe;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import brick.BrickObject;
import brick.BrickPanel;

import com.threed.jpct.Camera;
import com.threed.jpct.Matrix;
import com.threed.jpct.SimpleVector;


//Class to handle keyframes of the animation.
public class Keyframe {
	private NonCamera nCam;
	private NonBrick[] nBricks;
	private int framesBefore;
	
	//I want to be able to make an empty Keyframe, but not let others do so.
	private Keyframe(){}
	
	public Keyframe(BrickPanel panel, int framesBefore){
		this.framesBefore = framesBefore;
		ArrayList<BrickObject> bricks = panel.getBricks();
		nCam = new NonCamera(panel.getWorld().getCamera());
		nBricks = new NonBrick[bricks.size()];
		for(int i = 0; i < nBricks.length; i++){
			nBricks[i] = new NonBrick(bricks.get(i), panel);
		}
	}
	
	public Keyframe(BufferedReader source) throws Exception {
		String line = source.readLine();
		if(line == null) throw new Exception("Empty file.");
		if(!line.equals("SETTINGS")){
			throw new Exception("Not a keyframe file.");
		}
		framesBefore = Integer.parseInt(source.readLine());
		
		line = source.readLine();
		if(!line.equals("CAMERA")){
			throw new Exception("No camera information in file.");
		}else{
			nCam = parseCamera(new String[]{line,  source.readLine(), source.readLine(), source.readLine(), source.readLine()});
		}
		
		ArrayList<NonBrick> bricks = new ArrayList<Keyframe.NonBrick>();
		
		line = source.readLine();
		while(line.equals("BRICK")){
			NonBrick nb = parseBrick(
					new String[]{line, source.readLine(), source.readLine(), source.readLine(), source.readLine(), 
								 	   source.readLine(), source.readLine(), source.readLine(), source.readLine(),
								 	   source.readLine(), source.readLine()});
			
			bricks.add(nb);
			line = source.readLine();
		}
		
		//Because toArray() is dumb...
		nBricks = new NonBrick[bricks.size()];
		for(int b = 0; b < nBricks.length; b++){
			nBricks[b] = bricks.get(b);
		}
		source.close();
	}
	
	public Keyframe(File f) throws Exception{
		this(new BufferedReader(new FileReader(f)));
	}
	
	public NonBrick[] getBricksInScene(){
		return nBricks;
	}
	
	public NonCamera getCameraInfo(){
		return nCam;
	}
	
	public int getFramesBefore(){
		return framesBefore;
	}
	
	//Everything you wanted to know about a brick without actually having a brick. 
	class NonBrick{
		public SimpleVector translation;
		public SimpleVector rotationPivot;
		public Matrix rotation;
		public String name;
		//Color code might be off for selected bricks. Remove this when it's fixed.
		public int colorCode;
		
		public NonBrick(NonBrick n){
			translation = new SimpleVector(n.translation);
			rotationPivot = new SimpleVector(n.rotationPivot);
			rotation = n.rotation.cloneMatrix();
			colorCode = n.colorCode;
			name = n.name;
		}
		
		public NonBrick(BrickObject obj, BrickPanel panel){
			translation = new SimpleVector(obj.getTranslation());
			rotationPivot = new SimpleVector(obj.getRotationPivot());
			rotation = obj.getRotationMatrix().cloneMatrix();
			colorCode = panel.getTrueColor(obj);
			name = obj.getName();
		}
		
		public NonBrick(String name, int colorCode, SimpleVector translation,
						SimpleVector rotationPivot, Matrix rotation){
			this.translation = translation;
			this.rotationPivot = rotationPivot;
			this.rotation = rotation;
			this.colorCode = colorCode;
			this.name = name;
		}
	}
	
	//Everything needed to restore a camera.
	class NonCamera{
		public SimpleVector position;
		public SimpleVector direction;
		public SimpleVector up;
		public SimpleVector side;
		
		public NonCamera(NonCamera cam){
			position = new SimpleVector(cam.position);
			direction = new SimpleVector(cam.direction);
			up = new SimpleVector(cam.up);
			side = new SimpleVector(cam.side);
		}
		
		public NonCamera(Camera cam){
			position = new SimpleVector(cam.getPosition());
			direction = new SimpleVector(cam.getDirection());
			up = new SimpleVector(cam.getUpVector());
			side = new SimpleVector(cam.getSideVector());
		}
		
		public NonCamera(SimpleVector position, SimpleVector direction, SimpleVector up, SimpleVector side){
			this.position = position;
			this.direction = direction;
			this.up = up;
			this.side = side;
		}
	}
	
	public String toString(){
		StringBuilder output = new StringBuilder();
		output.append("SETTINGS\n");
		output.append(framesBefore + "\n");
		output.append("CAMERA\n");
		output.append(getCameraInfo().position + "\n");
		output.append(getCameraInfo().direction + "\n");
		output.append(getCameraInfo().up + "\n");
		output.append(getCameraInfo().side + "\n");
		for(NonBrick brick : getBricksInScene()){
			output.append("BRICK\n");
			output.append(brick.name + "\n");
			output.append(brick.colorCode + "\n");
			output.append(brick.translation + "\n");
			output.append(brick.rotationPivot + "\n");
			output.append(brick.rotation);
		}
		output.append("END");
		return output.toString();
	}
	
	//Takes a string representation of a NonBrick object and returns a NonBrick object.
	//Format of the input needs to match exactly the output of calling toString() on a 
	//NonBrick instance.
	public NonBrick parseBrick(String[] data){
		if(!data[0].equals("BRICK")){
			System.out.println("Bad brick info");
			for(String s : data){
				System.out.print(s + " | ");
			}
			System.out.println();
			return null;
		}
		return new NonBrick(data[1], Integer.parseInt(data[2]),	parseVector(data[3]),
							parseVector(data[4]), parseMatrix(new String[]{data[6], data[7], data[8], data[9]}));
	}
	
	//Takes a string representation of a NonCamera object and returns a NonCamera instance.
	//Format of the input needs to match exactly the output of calling toString() on a 
	//NonCamera instance.
	public NonCamera parseCamera(String[] data){
		if(!data[0].equals("CAMERA")){
			System.out.println("Bad camera info");
			for(String s : data){
				System.out.print(s + " | ");
			}
			System.out.println();
			return null;
		}
		return new NonCamera(parseVector(data[1]), parseVector(data[2]), parseVector(data[3]), parseVector(data[4]));
	}
	
	//Takes a string of the form (x,y,z) and returns a vector representing it.
	//x, y, and z can be any valid floats, and no spaces should be present 
	public static SimpleVector parseVector(String val){
		String[] vals = val.substring(1).split("\\,");
		if(vals.length != 3){
			System.out.println("Bad vector info");
			for(String s : vals){
				System.out.print(s + " | ");
			}
			System.out.println();
			return null;
		}
		return new SimpleVector(Float.parseFloat(vals[0]), Float.parseFloat(vals[1]),
								Float.parseFloat(vals[2].substring(0, vals[2].length() - 1)));
	}
	
	//Takes a string of the weird form
	//	1.0	0.0	0.0	0.0
	//	0.0	1.0	0.0	0.0
	//	0.0	0.0	1.0	0.0
	//	0.0	0.0	0.0	1.0
	//and returns the Matrix that it represents.
	//These are the middle lines of calling toString() on a Matrix instance.
	//These EXCLUDE the opening and closing parentheses. Whitespace gets trimmed,
	//so beginning tabs are optional, and inner whitespace can be tabs or spaces.
	public static Matrix parseMatrix(String[] lines){
		if(lines.length != 4){
			System.out.println("Bad Matrix info");
			for(String s : lines){
				System.out.print(s + " | ");
			}
			System.out.println();
			return null;
		}
		float[] dump = new float[16];
		for(int i = 0; i < 4; i++){
			String[] vals = lines[i].trim().split("\\s");
			for(int j = 0; j < 4; j++){
				dump[i * 4 + j] = Float.parseFloat(vals[j]);
			}
		}
		Matrix out = new Matrix();
		out.setDump(dump);
		return out;
	}
	
	public boolean saveToFile(String filename){
		File f = new File(filename.endsWith(".kf") ? filename : filename + ".kf");
		try {
			BufferedWriter bf = new BufferedWriter(new FileWriter(f));
			bf.write(toString());
			bf.close();
			return true;
		} catch (IOException e) {
			System.err.println(filename + " failed to save.");
			return false;
		}
	}
	
	public Keyframe clone(){
		Keyframe out = new Keyframe();
		out.nCam = new NonCamera(nCam);
		out.nBricks = new NonBrick[nBricks.length];
		for(int i = 0; i < nBricks.length; i++){
			out.nBricks[i] = new NonBrick(nBricks[i]);
		}
		out.framesBefore = framesBefore;
		return out;
	}
}
