package keyframe;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import keyframe.Keyframe.NonBrick;
import keyframe.Keyframe.NonCamera;
import brick.BrickObject;
import brick.BrickPanel;
import brick.PartNotFoundException;

import com.threed.jpct.Camera;
import com.threed.jpct.Matrix;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

public class Animator {
	private BrickPanel panel;
	private World world;
	
	public Animator(BrickPanel panel){
		this.panel = panel;
		world = panel.getWorld();
	}
	
	
	public void moveBetweenFrames(Keyframe frame1, Keyframe frame2){
		moveBetweenFrames(frame1, frame2, 31);
	}
	
	//Assuming no pivot changes. That is, the pivot is only used for restoring.
	//Camera changes need to be added as well.
	//This method also cannot handle new bricks or removed bricks.
	//To remove a brick, translate it by (-1000,-1000,-1000).
	//This distance from the camera makes it not rendered.
	//Also, here are some defaults for your information:
	//		framerate: 31 frames per second
	//		seconds between two keyframes: 2
	//		frames between two keyframes: 31 fps * 2 sec = 62 frames
	//		*Technically, this counts the first keyframe as the first, and the
	//		 second keyframe as the 63rd frame (first of the next animation).
	public void moveBetweenFrames(Keyframe frame1, Keyframe frame2, int framerate){
		
		ArrayList<BrickObject> bricks = panel.getBricks();
		NonBrick[] startBricks = frame1.getBricksInScene();
		NonBrick[] endBricks = frame2.getBricksInScene();

		int steps = 62;
		float seconds = 1;
		
		if(frame2.getFramesBefore() != 0){
			//steps = frame2.getFramesBefore();
			seconds = (float)steps/(float)framerate;
		}
		System.out.println("Will take " + seconds + " seconds.");
		
		int framelength = 1000/framerate;
		
		//Step is 1 here and not 0.
		//This is because frame 0 is already being shown.
		float step = 1;
		//float timeStep = (float)seconds / (float)steps;
		//float timePassed = 0;
		
		//Because of the previous assumption, we also can assume the camera is where it should be.
//		NonCamera cstart = frame1.getCameraInfo(); 
//		Camera cam = panel.getWorld().getCamera();
//		NonCamera cend = frame2.getCameraInfo();
//		
//		
//		SimpleVector dirNorm = new SimpleVector(cend.direction);
//		float dirrot = 0;
//		if(!cstart.direction.equals(cend.direction)){
//			dirNorm.calcCross(cstart.direction).normalize();
//			float angle = cstart.direction.calcAngle(cend.direction);
//			dirrot = angle/(float)steps;
//		}
//
//		//Basically, if the normal vector is negatively oriented.
//		//Another definition is whether the start vector is to the left of the end vector.
//		//And yet another is just whether the angle should be positive or negative.
//		//BUG: the vector I'm using as the up vector isn't guaranteed to be the up vector... Lovely.
//		if(dirNorm.calcDot(cstart.side) < 0) dirrot = -dirrot;
//		
//		SimpleVector upNorm = new SimpleVector(cend.up);
//		float uprot = 0;
//		if(!cstart.up.equals(cend.up)){
//			upNorm.calcCross(cstart.up).normalize();
//			float angle = cstart.up.calcAngle(cend.up);
//			uprot = angle/(float)steps;
//		}
//		
//		if(upNorm.calcDot(cstart.direction) < 0) uprot = -uprot;
//		
//		SimpleVector sideNorm = new SimpleVector(cstart.side);
//		float siderot = 0;
//		if(!cstart.side.equals(cend.side)){
//			sideNorm.calcCross(cend.side);
//			float angle = cstart.side.calcAngle(cstart.side);
//			siderot = Math.abs(angle/(float)steps);
//		}
//		
//		if(sideNorm.calcDot(cstart.direction) < 0) siderot = -siderot;
//
//		System.out.println("Rotations: " + dirrot + ", " + uprot);// + ", " + siderot);
//		System.out.println(dirNorm + " | " + upNorm);
//		System.out.println(cstart.direction.calcAngle(cend.direction) + " | " + cend.direction.calcAngle(cstart.direction));
//		System.out.println(cstart.up.calcAngle(cend.up) + " | " + cend.up.calcAngle(cstart.up));
		
		
		while(step < steps){
			long startTime = System.currentTimeMillis();
			NonBrick start;
			NonBrick end;
			BrickObject brick;
			float progress = (float)step/(float)steps;
			for(int i = 0; i < startBricks.length; i++){
				brick = bricks.get(i);
				start = startBricks[i];
				end = endBricks[i];
				
				if(!start.rotation.equals(end.rotation)){
					Matrix inBetween = new Matrix(); 
					inBetween.interpolate(start.rotation, end.rotation, progress);
					brick.setRotationMatrix(inBetween.cloneMatrix());
				}
				
				if(!start.translation.equals(end.translation)){
					SimpleVector change = new SimpleVector(end.translation);
					change.sub(start.translation);
					change.scalarMul(1f/(float)steps);
					brick.translate(change);
				}
			}
//			cam.rotateCameraAxis(upNorm, uprot);
//			cam.rotateCameraAxis(dirNorm, dirrot);
//			cam.rotateCameraAxis(sideNorm, siderot);
			
			
			
			step++;
			//System.out.println("step");
			//this attempts to keep a uniform frame length
			while(System.currentTimeMillis() - startTime < framelength);
			panel.repaint();
		}
		for(int i = 0; i < startBricks.length; i++){
			NonBrick end;
			BrickObject brick;
			brick = bricks.get(i);
			end = endBricks[i];
			SimpleVector t = brick.getTranslation();
			t.scalarMul(-1);
			brick.translate(t);
			brick.translate(end.translation);
			brick.setRotationMatrix(end.rotation.cloneMatrix());
		}
		
		if(endBricks.length > startBricks.length){
			try {
				BrickObject temp;
				NonBrick add;
				for(int i = startBricks.length ; i < endBricks.length; i++){
					add = endBricks[i];
					temp = panel.addNewBrick(add.name);
					temp.translate(add.translation);
					temp.setRotationPivot(new SimpleVector(add.rotationPivot));
					temp.setRotationMatrix(add.rotation.cloneMatrix());
					temp.setColorCode(add.colorCode);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (PartNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		
//		System.out.println("Wanted | Acheived");
//		System.out.println(cend.direction + " | " + cam.getDirection());
//		System.out.println(cend.up + " | " + cam.getUpVector());
//		System.out.println(cend.side + " | " + cam.getSideVector());
	}
	
	public void restoreFromFrame(Keyframe frame){
		
		panel.removeAllObjects();
		NonBrick[] scene = frame.getBricksInScene();
		try {
			BrickObject temp;
			for(NonBrick brick : scene){
				temp = panel.addNewBrick(brick.name);
				temp.build();
				temp.translate(brick.translation);
				temp.setRotationPivot(new SimpleVector(brick.rotationPivot));
				temp.getRotationMatrix().matMul(brick.rotation);
				temp.setColorCode(brick.colorCode);
			}
			
			NonCamera nc = frame.getCameraInfo();
			Camera cam = new Camera();
			cam.setPosition(nc.position);
			cam.setOrientation(nc.direction, nc.up);
			world.setCameraTo(cam);
			
			panel.repaint();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (PartNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//Save a movie as a zipped up file of keyframe files.
	public static boolean saveMovieToFile(ArrayList<Keyframe> frames){
		String name = JOptionPane.showInputDialog("Name of movie: ");
		if(name == null) return false;
		name = name.split("\\.", 2)[0];
		
		boolean ret = true;
		
		File fold = new File(name);// + ".kfm");
		File out = new File(name + ".kfm");
		File[] files = new File[frames.size()];
		if(!fold.mkdir()){
			System.out.println("Couldn't make folder.");
			ret = false;
		} else {
			int i = 0;
			try {
				if(out.exists()){
					out.delete();
				}
				out.createNewFile();
				
				BufferedInputStream origin = null;
				FileOutputStream dest = new FileOutputStream(out);
				ZipOutputStream zip = new ZipOutputStream(new BufferedOutputStream(dest));
				int BUFFER = 20480;
				byte data[] = new byte[BUFFER];
				
				ZipEntry entry = null;
				FileInputStream fi = null;
				int count;
				
				String path = fold.getAbsolutePath() + System.getProperty("file.separator") + name + "_";
				for(; i < frames.size(); i++){
					boolean b = frames.get(i).saveToFile(path + i + ".kf");
					if(b){
						files[i] = new File(path + i + ".kf");
					} else {
						System.out.println(i + "failed");
						ret = false;
						break;
					}
				}
				
				for(File f : files){
					fi = new FileInputStream(f);
					origin = new BufferedInputStream(fi, BUFFER);
					entry = new ZipEntry(f.getName());
					zip.putNextEntry(entry);
					while((count = origin.read(data,0,BUFFER)) != -1){
						zip.write(data,0,count);
					}
					count = 0;
					origin.close();
				}
				zip.close();
				
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Save failed:\n" + e.getMessage());
				return false;
			} finally {
				for(File f : files){
					if(f != null && f.exists()) f.delete();
				}
				if(fold.exists()) fold.delete();
			}
		}
		return ret;
		
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<Keyframe> openMovieFromDisk(){
		ArrayList<Keyframe> frames = new ArrayList<Keyframe>();
		JFileChooser jfc = new JFileChooser();
		int chosen = jfc.showOpenDialog(null);
		if(chosen == JFileChooser.APPROVE_OPTION){
			File movie = jfc.getSelectedFile();
			ZipFile zf;
			BufferedReader br;
			try {
				zf = new ZipFile(movie);
				for(Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zf.entries(); entries.hasMoreElements();) {
					br = new BufferedReader(new InputStreamReader(zf.getInputStream(entries.nextElement())));
					frames.add(new Keyframe(br));
					br.close();
				}
				zf.close();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "File not found exception:\n" + e.getMessage());
				return null;
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Open failed:\n" + e.getMessage());
				return null;
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Keyframe exception:\n" + e.getMessage());
				return null;
			}
		} else{
			return null;
		}
		return frames;
	}
}