package glbrick;
import java.awt.*;

import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.lwjgl.LWJGLException;

@SuppressWarnings("serial")
public class GuInterface extends JFrame implements ActionListener, ListSelectionListener{
	private GLWindow window;
	JMenuBar menuBar;
	JMenu file, edit,animate, view, help, modify;
	JButton add, undo, rotate, Redo, zoomIn, zoomOut, newFileB, openFileB, closeB, saveB, saveAllB, moveLeft, moveRight,moveUp, moveDown;
	JToolBar toolBar;
	JTextField up, down, left, right;
	JMenuItem newFile, viewFile, openFile, close, save, saveAll, print, exit, rename, cut, copy, selectAll, delete, translate;
	final String [] choicesList = {"rotate","translate"};
	JComboBox choices;
	JLabel warningLabel;
	JTextArea warnings;
	JScrollPane warningsPane;
	
	private PartsPanel allPartsPanel;
	private AddedPartsPanel partsBin;
	private JButton partAdderButton;
	private PartLabel currentSelectedPart;
	
	public GuInterface() {
		createAllButtons();
		menuBar = createMenuBar();
		toolBar = createToolBar();
		setJMenuBar(menuBar);
		add(toolBar, BorderLayout.NORTH);
		createControlPanel();
		createPartsPanel();
		addAllActionListeners();
		createWarningsArea();
		setTitle("CAD GUI");
		setSize(800, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}	
	@Override
	public void valueChanged(ListSelectionEvent e) {
		this.partAdderButton.setEnabled(true);
		Object obj = this.allPartsPanel.getPartsList().getSelectedValue();
		if(obj instanceof PartLabel){
			this.currentSelectedPart = (PartLabel)obj;
		}		
	}

	private void createPartsPanel(){
		this.partsBin = new AddedPartsPanel();
		JPanel addedParts = new JPanel();
		TitledBorder addedPartsBorder = BorderFactory.createTitledBorder("Added Parts");
		addedPartsBorder.setTitleJustification(TitledBorder.LEADING);
		addedParts.setBorder(addedPartsBorder);
		addedParts.setLayout(new GridLayout(1, 1));
		addedParts.add(this.partsBin.getListPane());
		
		this.currentSelectedPart = null;
		this.allPartsPanel = new PartsPanel();
		this.allPartsPanel.getPartsList().addListSelectionListener(this);
		this.partAdderButton = new JButton("Add part");
		this.partAdderButton.addActionListener(this);
		this.partAdderButton.setEnabled(false);
		JPanel adderPanel = new JPanel();
		adderPanel.setLayout(new BoxLayout(adderPanel, BoxLayout.Y_AXIS));
		TitledBorder adderBorder = BorderFactory.createTitledBorder("Select a part to add");
		adderBorder.setTitleJustification(TitledBorder.LEADING);
		adderPanel.setBorder(adderBorder);
		this.partAdderButton.setAlignmentY(Component.CENTER_ALIGNMENT);
		adderPanel.add(this.partAdderButton);

		JScrollPane partsPane = this.allPartsPanel.getListPane();
		adderPanel.add(partsPane);
		add(adderPanel, BorderLayout.WEST);
		add(addedParts, BorderLayout.EAST);
	}
	
	public void addAllActionListeners(){
		newFile.addActionListener(this);
		newFileB.addActionListener(this);
		openFile.addActionListener(this);
		openFileB.addActionListener(this);
		moveRight.addActionListener(this);
		moveLeft.addActionListener(this);
		moveUp.addActionListener(this);
		moveDown.addActionListener(this);
		zoomIn.addActionListener(this);
		zoomOut.addActionListener(this);
	}
	
	public JMenuBar createMenuBar(){
		JMenuBar menu = new JMenuBar();
		file = new JMenu("File");
		edit = new JMenu("Edit");
		view = new JMenu("View");
		animate = new JMenu("Animate");
		help = new JMenu("Help");
		modify = new JMenu("Modify");
		
		newFile = new JMenuItem("New", new ImageIcon("images/New.png"));
		newFile.setMnemonic(KeyEvent.VK_B);
		file.add(newFile);
		openFile = new JMenuItem("Open", new ImageIcon("images/Folder.png"));
		close = new JMenuItem("Close", new ImageIcon("images/exit.png"));
		save = new JMenuItem("Save As",new ImageIcon("images/Save.png") );
		saveAll = new JMenuItem("Save All", new ImageIcon("images/Saveall.png") );
		print = new JMenuItem("Print", new ImageIcon("images/print.png"));
		rename = new JMenuItem("Rename", new ImageIcon("images/rename.png"));
		cut = new JMenuItem("Cut", new ImageIcon("images/Cut.png"));
		copy = new JMenuItem("Copy", new ImageIcon("images/Copy.png"));
		selectAll = new JMenuItem("Select All", new ImageIcon("images/selectall.png"));
		delete = new JMenuItem("delete", new ImageIcon("images/exit.png"));
		exit  = new JMenuItem("Exit", new ImageIcon("images/exit.png"));
		translate = new JMenuItem("Translate");
		viewFile = new JMenuItem("View Part");
		choices = new JComboBox(choicesList);
		
		file.add(openFile);
		file.add(save);
		file.add(saveAll);
		file.add(rename);
		file.add(print);
		file.add(close);
		file.add(rotate);
		file.add(translate);

		edit.add(cut);
		edit.add(copy);
		edit.add(selectAll);
		edit.add(delete);
		view.add(viewFile);

		menu.add(file);
		menu.add(edit);
		menu.add(view);
		menu.add(modify);
		menu.add(animate);
		return menu;
	}
	
	public void createWarningsArea(){
		JPanel warningPanel = new JPanel();
		warningLabel= new JLabel("Warnings");
		warnings = new JTextArea();
		warningsPane = new JScrollPane(warnings);
		warnings.setRows(10);
		warnings.setLineWrap(true);
		warningPanel.setLayout(new BorderLayout());
		warningPanel.add(warningLabel, BorderLayout.NORTH);
		warningPanel.add(warningsPane, BorderLayout.CENTER);
		add(warningPanel, BorderLayout.SOUTH);
	}
	
	public void createControlPanel(){
		JPanel controlPanel  =  new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
		getContentPane().add(controlPanel, BorderLayout.CENTER);
		moveRight.setAlignmentX(Component.LEFT_ALIGNMENT);
		controlPanel.add(moveRight);
		right.setAlignmentX(LEFT_ALIGNMENT);
		controlPanel.add(right);
		moveLeft.setAlignmentX(Component.LEFT_ALIGNMENT);
		controlPanel.add(moveLeft);
		controlPanel.add(left);
		moveUp.setAlignmentX(Component.LEFT_ALIGNMENT);
		controlPanel.add(moveUp);
		controlPanel.add(up);
		moveDown.setAlignmentX(Component.LEFT_ALIGNMENT);
		controlPanel.add(moveDown);
		controlPanel.add(down);
		controlPanel.setBackground(right.getBackground());
		controlPanel.setMaximumSize(controlPanel.getPreferredSize());
		
	}
	
	public JButton createButton(String buttonName, String imgString) {
		ImageIcon icon = new ImageIcon("images/" + imgString);
		JButton tempButton = new JButton(icon);
		tempButton.setBorder(BorderFactory.createEmptyBorder());
		tempButton.setContentAreaFilled(false);
		return tempButton;
	}
	
	public void createAllButtons(){
		add = createButton("add", "Add.png");
		undo = createButton("undo","Undo.png");
		Redo = createButton("Redo","Redo.png");
		zoomIn = createButton("zoomIn","Zoomin.png");
		zoomOut = createButton("zoomOut","Zoomout.png");
		newFileB = createButton("newFileB","New.png");
		openFileB = createButton("openFileB","Folder.png");
		saveB = createButton("saveB","Save.png");
		saveAllB = createButton("saveAllB","Saveall.png");
		moveUp = createButton("moveUp","moveup.png");
		moveDown = createButton("moveDown","movedown.png");
		moveLeft = createButton("moveLeft","moveleft.png");
		moveRight = createButton("moveRight","moveright.png");
		rotate = createButton("rotate","rotate.png");
	}
	
	public JToolBar createToolBar(){
		JToolBar controlToolBar = new JToolBar();
		up = new JTextField("0",30);
		down = new JTextField("0",30);
		left = new JTextField("0",30);
		right = new JTextField("0",30);
		right.setMaximumSize( right.getPreferredSize() );
		down.setMaximumSize( down.getPreferredSize() );
		up.setMaximumSize( up.getPreferredSize() );
		left.setMaximumSize( left.getPreferredSize() );
		controlToolBar.add(add);
		controlToolBar.addSeparator();
		controlToolBar.add(newFileB);
		controlToolBar.addSeparator();
		controlToolBar.add(openFileB);
		controlToolBar.addSeparator();
		controlToolBar.add(saveB);
		controlToolBar.addSeparator();
		controlToolBar.add(saveAllB);
		controlToolBar.addSeparator();
		controlToolBar.add(undo);
		controlToolBar.addSeparator();
		controlToolBar.add(Redo);
		controlToolBar.addSeparator();
		controlToolBar.add(Redo);
		controlToolBar.addSeparator();
		controlToolBar.add(zoomOut);
		controlToolBar.addSeparator();
		controlToolBar.add(zoomIn);
		controlToolBar.addSeparator();
		controlToolBar.add(rotate);
		controlToolBar.addSeparator();
		controlToolBar.add(choices);
		controlToolBar.addSeparator();
		
		return controlToolBar;
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == partAdderButton){
			try {
				try {
					Thread.sleep(90);
				} catch (InterruptedException e1) {
					System.out.println("exception in thread");
				}
				window.addObject(currentSelectedPart.getPartFile());
				partsBin.addPart(currentSelectedPart);
			} catch (PartNotFoundException e1) {
//				e1.printStackTrace();
			}
		}
		
		if(e.getSource() == moveUp){
			Double upPos = Double.parseDouble(up.getText());
			if(choices.getSelectedIndex()==0){
				window.rotateModel(0, upPos);
			}else{
				window.translateModel(0.0, upPos, 0.0);
			}
		}
		
		else if(e.getSource() == moveDown){
			Double downPos = Double.parseDouble(down.getText());
			if(choices.getSelectedIndex()==0){
				window.rotateModel(downPos,0);
			}else{
				window.translateModel(0.0, -downPos, 0.0);
			}
		}
		
		else if(e.getSource() == moveRight){
			Double rightPos = Double.parseDouble(right.getText());
			if(choices.getSelectedIndex()==0){
				window.rotateModel(360-rightPos,0);
			}
			else{
				window.translateModel(rightPos, 0.0, 0.0);
			}
		}
		
		else if(e.getSource() == moveLeft){
			Double leftPos = Double.parseDouble(left.getText());
			if(choices.getSelectedIndex()==0){
				window.rotateModel(leftPos,0);
			}
			else{
				window.translateModel(-leftPos, 0.0, 0.0);
			}
		}
		
		if(e.getSource()==newFile || e.getSource() ==newFileB){
			MyWindow viewWindow = new MyWindow();
			Thread viewWindowThread = new Thread(viewWindow);
			viewWindowThread.start();
		}

		if(e.getSource() == openFile  ||e.getSource() == openFileB ){
			final JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(GuInterface.this);
			if(returnVal ==JFileChooser.APPROVE_OPTION){
				File file = fc.getSelectedFile();
				String partName  = file.getName();
				try {
					window.run();
					window.addObject(partName);
				} catch (PartNotFoundException ex) {
					ex.printStackTrace();
				} catch (FileNotFoundException ex) {
					ex.printStackTrace();
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		}
		
		if(e.getSource() == zoomIn){
			window.zoomIn();
		}
		if(e.getSource() == zoomOut){
			window.zoomOut();
		}
	}
	
	private final class MyWindow extends Thread{
		private MyWindow(){
		}
	    public void run() {
	    	try {
	    		window = new GLWindow();
	    		window.run();
			} catch (LWJGLException e1) {
				e1.printStackTrace();
			}
			catch (FileNotFoundException ex) {
				ex.printStackTrace();
				warnings.append("The specified file was not found");
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			} catch (PartNotFoundException ex) {
				ex.printStackTrace();
				warnings.append("The specified part was not found");
			}
	    }
	}
	
	public static void main(String[] args) throws InterruptedException{
		SwingUtilities.invokeLater(new Runnable (){
			public void run(){
				GuInterface gui  = new GuInterface();
				System.out.println("hitt");
				gui.setVisible(true);
			}
		});
	}
}
