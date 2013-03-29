package keyframe;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import brick.BrickPanel;

@SuppressWarnings("serial")
public class FramePreviewPane extends JFrame implements ActionListener {
	private JPanel content;
	private JPanel topButtons, ordering, saving, information;
	private ArrayList<Keyframe> frames;
	private JButton first, prev, next, last;
	private JButton swapLeft, swapRight, delete, duplicate;
	private JButton saveFrame, addFrame;
	private JTextField before;
	private JCheckBox sudden;
	private BrickPanel preview;
	private int curFrame = -1;

	public FramePreviewPane(){
		
		frames = new ArrayList<Keyframe>();
		content = new JPanel();
		add(content);
		
		first = new JButton("First"); first.setActionCommand("first");
		first.addActionListener(this);
		prev = new JButton("Previous"); prev.setActionCommand("prev");
		prev.addActionListener(this);
		next = new JButton("Next"); next.setActionCommand("next");
		next.addActionListener(this);
		last = new JButton("Last"); last.setActionCommand("last");
		last.addActionListener(this);
		
		topButtons = new JPanel();
		topButtons.setLayout(new BoxLayout(topButtons, BoxLayout.X_AXIS));
		topButtons.add(first); topButtons.add(prev);
		topButtons.add(next); topButtons.add(last);
		first.setEnabled(false); prev.setEnabled(false);
		next.setEnabled(false); last.setEnabled(false);
		
		swapLeft = new JButton("Swap left"); swapLeft.setActionCommand("left");
		swapLeft.addActionListener(this);
		swapRight = new JButton("Swap right"); swapRight.setActionCommand("right");
		swapRight.addActionListener(this);
		delete = new JButton("Delete"); delete.setActionCommand("delete");
		delete.addActionListener(this);
		duplicate = new JButton("Duplicate"); duplicate.setActionCommand("duplicate");
		duplicate.addActionListener(this);
		
		ordering = new JPanel();
		ordering.add(swapLeft); ordering.add(swapRight);
		ordering.add(delete); ordering.add(duplicate);
		ordering.setLayout(new BoxLayout(ordering, BoxLayout.X_AXIS));
		
		saveFrame = new JButton("Save Frame"); saveFrame.setActionCommand("save_frame");
		saveFrame.addActionListener(this);
		addFrame = new JButton("Open Frame"); addFrame.setActionCommand("add_frame");
		addFrame.addActionListener(this);
		
		saving = new JPanel();
		saving.add(saveFrame); saving.add(addFrame);
		saving.setLayout(new BoxLayout(saving, BoxLayout.X_AXIS));
		
		
		
		
		
		information = new JPanel();
		//information.setBorder(BorderFactory.createTitledBorder("Frame Settings:"));
		//GridLayout infoLayout = new GridLayout(2, );
		//information.setLayout(infoLayout);
		
		sudden = new JCheckBox("Sudden");
		JPanel frBefore = new JPanel();
		frBefore.setLayout(new BoxLayout(frBefore, BoxLayout.X_AXIS));
		before = new JTextField(5);
		frBefore.add(new JLabel("Frames before: "));
		frBefore.add(before);
		JPanel informationLeft = new JPanel();
		informationLeft.setBorder(BorderFactory.createTitledBorder("Frame Settings:"));
		informationLeft.add(frBefore);
		informationLeft.add(sudden);
		JPanel informationRight = new JPanel();
		informationLeft.setLayout(new BoxLayout(informationLeft, BoxLayout.Y_AXIS));
		informationRight.setLayout(new BoxLayout(informationRight, BoxLayout.Y_AXIS));
		JPanel informationTopRight = new JPanel();
		informationTopRight.setBorder(BorderFactory.createTitledBorder("Movie Settings:"));
		JPanel informationBotRight = new JPanel();
		informationBotRight.setBorder(BorderFactory.createTitledBorder("Brick Settings:"));
		informationRight.add(informationTopRight);
		informationRight.add(informationBotRight);
		
		information.setLayout(new BoxLayout(information, BoxLayout.X_AXIS));
		information.add(informationLeft);
		information.add(informationRight);
		
		//calculated this in one run, since the ordering to get button sizes is frustrating.
		int size = 95*4;
		//Same as before, the height of the buttons turns out to be 26;
		int buttonHeight = 26;
		int infoHeight = 200;
		
		information.setPreferredSize(new Dimension(size, infoHeight));
		information.setMaximumSize(information.getPreferredSize());
		before.setMaximumSize(before.getPreferredSize());
		preview = new BrickPanel(size, size);
		preview.buildAll();
		content.add(preview);
		
		content.add(topButtons);
		content.add(ordering);
		content.add(saving);
		content.add(information);
		content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
		setPreferredSize(new Dimension(size + 10, size + buttonHeight*2  + infoHeight + 10));
		setMinimumSize(getPreferredSize());
		setMaximumSize(getPreferredSize());
		setVisible(true);
		change();
		//int size = first.getWidth() + last.getWidth() + next.getWidth() + prev.getWidth();
		System.out.println("SIZE IS " + information.getHeight());
		//sizingFix();
		
	}
	
	public void sizingFix(){
//		int buttonWidth = max(new int[]{first.getWidth(), prev.getWidth(), next.getWidth(), last.getWidth(),
//										swapLeft.getWidth(), swapRight.getWidth(), delete.getWidth(), duplicate.getWidth()},
//										0, 0);
		//System.out.println(buttonWidth); 95.
		int buttonWidth = 95;
		Dimension size = new Dimension(buttonWidth, first.getHeight());
		enforceSize(first, size); 
		enforceSize(prev, size);
		enforceSize(next, size);
		enforceSize(last, size);
		enforceSize(swapLeft, size);
		enforceSize(swapRight, size);
		enforceSize(delete, size);
		enforceSize(duplicate, size);
	}
	
	private void enforceSize(JComponent panel, Dimension size){
		panel.setPreferredSize(size); panel.setMaximumSize(size);
		panel.setMinimumSize(size);
	}
	
//	private int max(int[] widths, int place, int cur){
//		if(place == widths.length) return cur;
//		return Math.max(cur, max(widths, place + 1, widths[place]));
//	}
	
	public BrickPanel getPreviewPanel(){
		return preview;
	}
	
	private void next(){
		curFrame++;
		change();
	}
	
	private void prev(){
		curFrame--;
		change();
	}
	
	private void first(){
		curFrame = 0;
		change();
	}
	
	public void toStart(){
		first();
	}
	
	public void addFrame(Keyframe kf){
		frames.add(kf);
		last();
	}
	
	public Keyframe getCurrentFrame(){
		return frames.get(curFrame);
	}
	
	public int getCurrentFrameIndex(){
		return curFrame;
	}
	
	public ArrayList<Keyframe> getAllFrames(){
		return frames;
	}
	
	//I find this funny, because it's faster by a tiny, tiny bit than setting it directly.
	//Also, this sets the current frame to be the last one
	private void last(){
		curFrame = Integer.MAX_VALUE;
		change();
	}
	
	public void newMovie(ArrayList<Keyframe> frames){
		this.frames = frames;
		change();
	}
	
	private void change(){
		if(frames.size() > 1){
			swapRight.setEnabled(true);
			next.setEnabled(true);
			last.setEnabled(true);
			swapLeft.setEnabled(true);
			first.setEnabled(true);
			prev.setEnabled(true);
		}
		if(curFrame >= frames.size() - 1){
			curFrame = frames.size() - 1;
			next.setEnabled(false);
			last.setEnabled(false);
			swapRight.setEnabled(false);
		}
		if(curFrame <= 0){
			curFrame = 0;
			first.setEnabled(false);
			prev.setEnabled(false);
			swapLeft.setEnabled(false);
		}
		if(frames.size() == 0){
			delete.setEnabled(false);
			duplicate.setEnabled(false);
			preview.removeAllObjects();
			preview.repaint();
		} else{
			delete.setEnabled(true);
			duplicate.setEnabled(true);
			new Animator(preview).restoreFromFrame(frames.get(curFrame));
		}
	}
	
	private void saveFrame(){
		frames.get(curFrame).saveToFile("frame_" + curFrame);
	}
	
	private void addFrame(){
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter fnef = new FileNameExtensionFilter("KeyFrame frame files (.kf)", "kf");
		fc.setFileFilter(fnef);
		int ret = fc.showOpenDialog(null);
		if(ret == JFileChooser.APPROVE_OPTION){
			try {
				Keyframe kf = new Keyframe(fc.getSelectedFile());
				addFrame(kf);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		String command = arg0.getActionCommand();
		if(command.equals("first")){
			first();
		} else if(command.equals("next")){
			next();
		} else if(command.equals("prev")){
			prev();
		} else if(command.equals("last")){
			last();
		} else if(command.equals("save_frame")){
			saveFrame();
		} else if(command.equals("add_frame")){
			addFrame();
		} else if(command.equals("left")){
			swapLeft();
		} else if(command.equals("right")){
			swapRight();
		} else if(command.equals("delete")){
			deleteFrame();
		} else if(command.equals("duplicate")){
			duplicateFrame();
		}
	}

	private void duplicateFrame() {
		frames.add(curFrame + 1, getCurrentFrame().clone());
		change();
	}

	private void deleteFrame() {
		frames.remove(curFrame);
		change();
	}

	private void swapRight() {
		Keyframe mid = getCurrentFrame();
		frames.set(curFrame, frames.get(curFrame + 1));
		frames.set(curFrame + 1, mid);
		curFrame++;
		change();
	}

	private void swapLeft() {
		Keyframe mid = getCurrentFrame();
		frames.set(curFrame, frames.get(curFrame - 1));
		frames.set(curFrame - 1, mid);
		curFrame--;
		change();
	}

}
