package brick;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import keyframe.Animator;
import keyframe.FramePreviewPane;
import keyframe.Keyframe;

import com.threed.jpct.SimpleVector;
import com.threed.jpct.World;

@SuppressWarnings("serial")
public class AdjustmentPane extends JFrame implements ActionListener {
	private BrickObject chosen;
	private int numBricks = 0;
	//Misleading name: Actually number of objects in total, not just bricks.
	private String lastAdded;
	private World world;
	private BrickPanel ra;
	private JTextField xpos, ypos, zpos;
	private JTextField xrot, yrot, zrot;
	private JTextField obji, objx, objy, objz, objc;
	private JButton reAddButton, addButton, objrx, objry, objrz;
	private JButton shiftx, shifty, shiftz;
	private JButton takeFrame, restoreFrame, clear, play;
	private JButton movLoad, movSave;
	private JPanel pos, rot, obj;
	private float transStep = 1f;
	private float angle = (float)Math.PI / 4;
	private FramePreviewPane fpw;
	
	
	public AdjustmentPane(BrickPanel renderingArea){
		super();
		JPanel contents = new JPanel();
		ra = renderingArea;
		world = ra.getWorld();
		
		//Camera position indicators
		
		xpos = new JTextField(5);
		xpos.addKeyListener(new FieldListener(xpos, this));
		ypos = new JTextField(5);
		ypos.addKeyListener(new FieldListener(ypos, this));
		zpos = new JTextField(5);
		zpos.addKeyListener(new FieldListener(zpos, this));
		
		//Camera direction indicators
		//That is, these three form the vector that points in the same direction as the camera
		xrot = new JTextField(5);
		xrot.addKeyListener(new FieldListener(xrot, this));
		yrot = new JTextField(5);
		yrot.addKeyListener(new FieldListener(yrot, this));
		zrot = new JTextField(5);
		zrot.addKeyListener(new FieldListener(zrot, this));
		
		//Selected object indicators (index in objs, then position)
		obji = new JTextField(5);
		obji.addKeyListener(new FieldListener(obji, this));
		objx = new JTextField(5);
		objx.addKeyListener(new FieldListener(objx, this));
		objy = new JTextField(5);
		objy.addKeyListener(new FieldListener(objy, this));
		objz = new JTextField(5);
		objz.addKeyListener(new FieldListener(objz, this));
		objc = new JTextField(3);
		objc.addKeyListener(new FieldListener(objc, this));
		
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pos = new JPanel();
		pos.setBorder(BorderFactory.createTitledBorder("Camera Position:"));
		pos.add(new JLabel("X:")); pos.add(xpos);
		pos.add(new JLabel("Y:")); pos.add(ypos);
		pos.add(new JLabel("Z:")); pos.add(zpos);
		xpos.setMaximumSize(xpos.getPreferredSize());
		ypos.setMaximumSize(ypos.getPreferredSize());
		zpos.setMaximumSize(zpos.getPreferredSize());
		pos.setLayout(new BoxLayout(pos, BoxLayout.X_AXIS));
		
		rot = new JPanel();
		rot.setBorder(BorderFactory.createTitledBorder("Camera Direction Vector:"));
		rot.add(new JLabel("X:")); rot.add(xrot);
		rot.add(new JLabel("Y:")); rot.add(yrot);
		rot.add(new JLabel("Z:")); rot.add(zrot);
		xrot.setMaximumSize(xrot.getPreferredSize());
		yrot.setMaximumSize(yrot.getPreferredSize());
		zrot.setMaximumSize(zrot.getPreferredSize());
		rot.setLayout(new BoxLayout(rot, BoxLayout.X_AXIS));
		
		obj = new JPanel();
		obj.setBorder(BorderFactory.createTitledBorder("Edit Object Parameters:"));
		obj.add(new JLabel("Object: "));	obj.add(obji);
		obj.add(new JLabel("Position X: "));	obj.add(objx);
		obj.add(new JLabel("Y: "));	obj.add(objy);
		obj.add(new JLabel("Z: ")); obj.add(objz);
		obj.add(new JLabel("Color: ")); obj.add(objc);
		obji.setMaximumSize(obji.getPreferredSize());
		objx.setMaximumSize(objx.getPreferredSize());
		objy.setMaximumSize(objy.getPreferredSize());
		objz.setMaximumSize(objz.getPreferredSize());
		objc.setMaximumSize(objc.getPreferredSize());
		obji.setName("object");
		objc.setName("color");
		obj.setLayout(new BoxLayout(obj, BoxLayout.X_AXIS));
		obj.setMaximumSize(obj.getPreferredSize());
		pos.setMinimumSize(obj.getPreferredSize());
		rot.setMinimumSize(obj.getPreferredSize());
		
		
		JPanel addPanel = new JPanel();
		reAddButton = new JButton("Redo last add");
		reAddButton.setActionCommand("readd");
		reAddButton.addActionListener(this);
		addButton = new JButton("Add new brick");
		addButton.setActionCommand("add");
		//add(addButton);
		addButton.addActionListener(this);
		addPanel.add(addButton);
		addPanel.add(reAddButton);
		addPanel.setLayout(new BoxLayout(addPanel, BoxLayout.X_AXIS));
		
		JPanel rotButtons = new JPanel();
		objrx = new JButton("Rotate X"); objry = new JButton("Rotate Y"); objrz = new JButton("Rotate Z");
		objrx.setActionCommand("xrot"); objry.setActionCommand("yrot"); objrz.setActionCommand("zrot");
		objrx.setMaximumSize(objrx.getPreferredSize());
		objry.setMaximumSize(objry.getPreferredSize());
		objrz.setMaximumSize(objrz.getPreferredSize());
		rotButtons.add(objrx); rotButtons.add(objry); rotButtons.add(objrz);
		objrx.addActionListener(this); objry.addActionListener(this); objrz.addActionListener(this);
		rotButtons.setLayout(new BoxLayout(rotButtons, BoxLayout.X_AXIS));
		
		JPanel transButtons = new JPanel();
		shiftx = new JButton("Translate X"); shifty = new JButton("Translate Y"); shiftz = new JButton("Translate Z");
		shiftx.setActionCommand("xtran"); shifty.setActionCommand("ytran"); shiftz.setActionCommand("save");//ztran");
		shiftx.setMaximumSize(shiftx.getPreferredSize());
		shifty.setMaximumSize(shifty.getPreferredSize());
		shiftz.setMaximumSize(shiftz.getPreferredSize());
		transButtons.add(shiftx); transButtons.add(shifty); transButtons.add(shiftz);
		shiftx.addActionListener(this); shifty.addActionListener(this); shiftz.addActionListener(this);
		transButtons.setLayout(new BoxLayout(transButtons, BoxLayout.X_AXIS));
		
		JPanel frameSettings = new JPanel();
		takeFrame = new JButton("Take Frame"); restoreFrame = new JButton("Restore Last Frame"); clear = new JButton("Clear");
		takeFrame.setActionCommand("take"); restoreFrame.setActionCommand("restore"); clear.setActionCommand("clear");
		frameSettings.add(takeFrame); frameSettings.add(restoreFrame); frameSettings.add(clear);
		takeFrame.addActionListener(this);
		restoreFrame.addActionListener(this);
		clear.addActionListener(this);
		
		play = new JButton("Play");
		play.setActionCommand("play");
		frameSettings.add(play);
		play.addActionListener(this);
		frameSettings.setLayout(new BoxLayout(frameSettings, BoxLayout.X_AXIS));
		
		JPanel movieSettings =  new JPanel();
		movSave = new JButton("Save Movie"); movLoad = new JButton("Load Movie");
		movSave.setActionCommand("msave"); movLoad.setActionCommand("mload");
		movieSettings.add(movSave); movieSettings.add(movLoad);
		movSave.addActionListener(this);
		movLoad.addActionListener(this);
		movieSettings.setLayout(new BoxLayout(movieSettings, BoxLayout.X_AXIS));
		
		contents.setLayout(new BoxLayout(contents, BoxLayout.Y_AXIS));
		contents.add(pos); contents.add(rot); contents.add(obj);
		contents.add(addPanel);
		contents.add(rotButtons); contents.add(transButtons); contents.add(frameSettings);
		contents.add(movieSettings);
		
		add(contents);
		
		fpw = new FramePreviewPane();
		fpw.setVisible(true);
		
	}
	
	//Has to be called AFTER it gets set visible and has its size changed.
	//Otherwise, returned values will be zero and this will break.
	public void updatePositions(){
		Point pos = getLocation();
		pos.x += getWidth() - fpw.getWidth();
		pos.y += getHeight();
		
		fpw.setLocation(pos);
	}
	
	public void update(){
		SimpleVector pos = world.getCamera().getPosition();
		numBricks = ra.numBricks();
		
		xpos.setText("" + pos.x);
		ypos.setText("" + pos.y);
		zpos.setText("" + pos.z);
		
		SimpleVector dir = world.getCamera().getDirection();
		
		xrot.setText("" + dir.x);
		yrot.setText("" + dir.y);
		zrot.setText("" + dir.z);
		
		int val = Integer.parseInt(obji.getText());
		if(numBricks > 0){
			if(val >= numBricks) val = numBricks - 1;
			
			chosen = ra.getObject(val);
			//ra.setSelectedBrick(chosen);
		
			SimpleVector objTrans = chosen.getTranslation();
		
			objx.setText("" + objTrans.x);
			objy.setText("" + objTrans.y);
			objz.setText("" + objTrans.z);
			objc.setText("" + ra.getTrueColor(chosen));
		}
	}
	public float getXPos(){
		return Float.parseFloat(xpos.getText());
	}
	public float getYPos(){
		return Float.parseFloat(ypos.getText());
	}
	public float getZPos(){
		return Float.parseFloat(zpos.getText());
	}
	public float getXRot(){
		return Float.parseFloat(xrot.getText());
	}
	public float getYRot(){
		return Float.parseFloat(yrot.getText());
	}
	public float getZRot(){
		return Float.parseFloat(zrot.getText());
	}
	public float getObjX(){
		return Float.parseFloat(objx.getText());
	}
	public float getObjY(){
		return Float.parseFloat(objy.getText());
	}
	public float getObjZ(){
		return Float.parseFloat(objz.getText());
	}
	public int getObj(){
		return Integer.parseInt(obji.getText());
	}
	
	public void updateSelectedObject(BrickObject brick){
		obji.setText("" + ra.indexOf(brick));
		objc.setText("" + ra.getTrueColor(brick));
		update();
		updateCamera();
	}
	
	public void redoLastAdd(){
		if(lastAdded != null){
			try {
				ra.addNewBrick(lastAdded);
			} catch (PartNotFoundException pnfe) {
				JOptionPane.showMessageDialog(null, "Cannot find model \"" + lastAdded + "\"");
			} catch (FileNotFoundException fnfe) {
				JOptionPane.showMessageDialog(null, "Cannot locate ldconfig.ldr");
			}
		}
	}
	
	//Badly named function. This actually applies all changes in the panel for both camera and object.
	public void updateCamera(){
		//SimpleVector newRot = new SimpleVector(getXRot(), getYRot(), getZRot());
		SimpleVector camPos = new SimpleVector(getXPos(), getYPos(), getZPos());
		world.getCamera().setPosition(camPos);
		//System.out.println("Updated Camera.");
		//world.getCamera().lookAt(orig);
		
		//First, change the position of the object.
		SimpleVector curTrans = chosen.getTranslation();
		SimpleVector posDelta = new SimpleVector(getObjX() - curTrans.x, getObjY() - curTrans.y, getObjZ() - curTrans.z);
		chosen.translate(posDelta);
		//Then change the color.
		chosen.setColorCode(Integer.parseInt(objc.getText()));
		//Now make the color change stick.
		ra.updateBrick(chosen);
		ra.repaint();
		//System.out.println("Updated Object " + getObj() +  ".");
	}
	
	private class FieldListener extends KeyAdapter{
		private JTextField val;
		private AdjustmentPane ap;
		
		FieldListener(JTextField tf, AdjustmentPane ap){
			val = tf;
			this.ap = ap;
		}
		
		public void keyPressed(KeyEvent ke) {
			if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
				ap.updateCamera();
			} else if (ke.getKeyCode() == KeyEvent.VK_UP) {
				if(val.getName() != null){
					if(val.getName().equals("object")){
						setObjectVal(1);
					} else if (val.getName().equals("color")){
						setColorVal(1);
					}
				} else if (ke.isControlDown()){
					setVal(10.0f);
				} else if (ke.isShiftDown()){
					setVal(0.1f);
				} else {
					setVal(1.0f);
				}
				ap.updateCamera();
			} else if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
				if(val.getName() != null){
					if(val.getName().equals("object")){
						setObjectVal(-1);
					} else if (val.getName().equals("color")){
						setColorVal(-1);
					}
				} else if(ke.isControlDown()){
					setVal(-10.0f);
				} else if (ke.isShiftDown()){
					setVal(-0.1f);
				} else {
					setVal(-1.0f);
				}
				ap.updateCamera();
			}
		}
		private void setVal(float f){
			String res = Float.toString(Float.parseFloat(val.getText()) + f);
			res = res.substring(0, res.length() > 5 ? 5 : res.length());
			val.setText(res);
		}
		
		private void setColorVal(int c){
			int t = Integer.parseInt(val.getText());
			if(t < 0 || (t == 0 && c < 0)){
				val.setText("0");
				return;
			} else {
				val.setText("" + (t + c));
			}
		}
		
		private void setObjectVal(int i){
			int t = Integer.parseInt(val.getText());
			if(t <= 0 && i < 0) val.setText("0");
			else if(t >= numBricks-1 && i > 0) val.setText("" + (numBricks - 1));
			else val.setText("" + (t + i));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		Boolean isCtrl = (e.getModifiers() & ActionEvent.CTRL_MASK) != 0;
		
		if(command.startsWith("xt")){
			chosen.translateX(isCtrl ? -transStep : transStep);
		} else if(command.startsWith("yt")){
			chosen.translateY(isCtrl ? -transStep : transStep);
		} else if(command.startsWith("zt")){
			chosen.translateZ(isCtrl ? -transStep : transStep);
		} else if(command.startsWith("xr")){
			chosen.rotateX(isCtrl ? -angle : angle);
		} else if(command.startsWith("yr")){
			chosen.rotateY(isCtrl ? -angle : angle);
		} else if(command.startsWith("zr")){
			chosen.rotateZ(isCtrl ? -angle : angle);
		} else if(command.equals("take")){
			fpw.addFrame(new Keyframe(ra, 16));
		} else if(command.equals("restore")){
			new Animator(ra).restoreFromFrame(fpw.getCurrentFrame());
		} else if(command.equals("clear")){
			ra.removeAllObjects();
		} else if(command.equals("play")){
			//Play a movie. That is, play all the current Keyframes in order.
			//Has to be run in a new thread so that the graphics can update.
			new Thread(new Runnable() {
	            public void run() {
	            	ArrayList<Keyframe> frames = fpw.getAllFrames();
	            	Animator ani = new Animator(ra);
	            	ani.restoreFromFrame(frames.get(0));
	            	ra.repaint();
	            	for(int i = 1; i < frames.size(); i++){
	            		ani.moveBetweenFrames(frames.get(i - 1), frames.get(i), 31);
	            	}
	            }
	        }).start();
		} else if("add".equals(command)){
			JFileChooser jfc = new JFileChooser(new File(BrickViewer.ldrawPath));
			BrickPreviewWindow bpw = new BrickPreviewWindow(jfc, 200, 200);
			bpw.setupListeners();
			jfc.setAccessory(bpw);
			FileNameExtensionFilter fnef = new FileNameExtensionFilter("LDraw model files (.dat, .ldr)", "dat", "ldr");
			jfc.setFileFilter(fnef);
			int ret = jfc.showOpenDialog(null);
			if(ret == JFileChooser.APPROVE_OPTION){
				String loc = jfc.getSelectedFile().getName();
				try {
					ra.addNewBrick(loc);
					lastAdded = loc;
				} catch (PartNotFoundException pnfe) {
					JOptionPane.showMessageDialog(null, "Cannot find model \"" + loc + "\"");
				} catch (FileNotFoundException fnfe) {
					JOptionPane.showMessageDialog(null, "Cannot locate ldconfig.ldr");
				} 
			}
		} else if("readd".equals(command)){
			redoLastAdd();
		} else if("msave".equals(command)){
			if(!Animator.saveMovieToFile(fpw.getAllFrames())){
			}
		} else if("mload".equals(command)){
			ArrayList<Keyframe> frames = Animator.openMovieFromDisk();
			if(frames != null){
				fpw.newMovie(frames);
			}
		}
		ra.repaint();
		
	}
}
