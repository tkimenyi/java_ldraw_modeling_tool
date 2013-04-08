package glbrick;
import java.awt.*;

import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.*;

import org.lwjgl.LWJGLException;



@SuppressWarnings("serial")
public class GuInterface extends JFrame implements ActionListener{
	private GLWindow window;
	JMenuBar menuBar;
	JMenu file, edit,animate, view, help, modify;
	JButton add, undo, rotate, Redo, zoomIn, zoomOut, newFileB, openFileB, closeB, saveB, saveAllB, moveLeft, moveRight,moveUp, moveDown, up, down, left, right;
	JToolBar toolBar;
	JLabel cameraPos, cameraDirVect, editObjParam;
	JTextField y2, z2, object, x, y, z, rotateField ;
	JMenuItem newFile, viewFile, openFile, close, save, saveAll, print, exit, rename, cut, copy, selectAll, delete, translate;

	public GuInterface() {

		menuBar = new JMenuBar();
		file = new JMenu("File");
		edit = new JMenu("Edit");
		view = new JMenu("View");
		animate = new JMenu("Animate");
		help = new JMenu("Help");
		modify = new JMenu("Modify");


		toolBar = new JToolBar();
		cameraPos = new JLabel("Camera Position");
		cameraDirVect = new JLabel("Camera Direction Vector");
		editObjParam = new JLabel("Edit Object Parameters");
		up = new JButton("up");
		down = new JButton("down");
		left = new JButton("left");
		right = new JButton("right");
		rotateField = new JTextField("degree",15);
		y2 = new JTextField("Y");
		z2 = new JTextField("Z");
		x = new JTextField("X");
		y = new JTextField("Y");
		z = new JTextField("Z");
		right.setMaximumSize( right.getPreferredSize() );
		down.setMaximumSize( down.getPreferredSize() );
		up.setMaximumSize( up.getPreferredSize() );
		left.setMaximumSize( left.getPreferredSize() );
		rotateField.setMaximumSize( rotateField.getPreferredSize() );




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

		menuBar.add(file);
		menuBar.add(edit);
		menuBar.add(view);
		menuBar.add(modify);
		menuBar.add(animate);

		toolBar.add(add);
		toolBar.addSeparator();
		toolBar.add(newFileB);
		toolBar.addSeparator();
		toolBar.add(openFileB);
		toolBar.addSeparator();
		toolBar.add(saveB);
		toolBar.addSeparator();
		toolBar.add(saveAllB);
		toolBar.addSeparator();
		toolBar.add(undo);
		toolBar.addSeparator();
		toolBar.add(Redo);
		toolBar.addSeparator();
		toolBar.add(Redo);
		toolBar.addSeparator();
		toolBar.add(zoomOut);
		toolBar.addSeparator();
		toolBar.add(zoomIn);
		toolBar.addSeparator();
		toolBar.add(rotateField);
		toolBar.addSeparator();
		toolBar.add(rotate);
		toolBar.addSeparator();


		

		//JPanel panel  =  new JPanel(new BorderLayout());
		setJMenuBar(menuBar);
		add(toolBar, BorderLayout.NORTH);
		
	
		
		JPanel panel1  =  new JPanel();
		panel1.setLayout( new BoxLayout(panel1, BoxLayout.X_AXIS));
		getContentPane().add(panel1);
		moveRight.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel1.add(moveRight);
		right.setAlignmentX(LEFT_ALIGNMENT);
		panel1.add(right);
		moveLeft.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel1.add(moveLeft);
		panel1.add(left);
		moveUp.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel1.add(moveUp);
		panel1.add(up);
		moveDown.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel1.add(moveDown);
		panel1.add(down);
		panel1.setBackground(right.getBackground());
		//panel1.setBorder(new TitledBorder("ROTATE"));
		panel1.setMaximumSize(panel1.getPreferredSize());
		
		//adding action listeners
		newFile.addActionListener(this);
		newFileB.addActionListener(this);
		openFile.addActionListener(this);
		openFileB.addActionListener(this);
		moveRight.addActionListener(this);
		moveLeft.addActionListener(this);
		moveUp.addActionListener(this);
		moveDown.addActionListener(this);


		
		setTitle("CAD GUI");
		setSize(600, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
	
	public JButton createButton(String buttonName, String imgString) {
		ImageIcon icon = new ImageIcon("images/" + imgString);
		JButton tempButton = new JButton(icon);
		tempButton.setBorder(BorderFactory.createEmptyBorder());
		tempButton.setContentAreaFilled(false);
		return tempButton;
	}
	
	public void actionPerformed(ActionEvent e){
		
		if(e.getSource() == moveUp){
			//Double angle = Double.parseDouble(up.getText());
			//GLTest.rotateModel(angle);
			System.out.println("angleeee");
			
			
			
			
			window.rotateModel(100, 100);
		}
		
		if(e.getSource() == moveDown){
			Double angle = Double.parseDouble(down.getText());
			window.rotateModel(angle,0);
		}
		
		if(e.getSource() == moveRight){
			Double angle = Double.parseDouble(right.getText());
			window.rotateModel(360 -angle,0);
		}
		
		if(e.getSource() == moveLeft){
			Double angle = Double.parseDouble(left.getText());
			window.rotateModel(angle,0);
		}
		
		if(e.getSource()==newFile || e.getSource() ==newFileB){
			
			try {
				window = new GLWindow();
				window.run();
			} catch (LWJGLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			catch (FileNotFoundException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			} catch (InterruptedException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			} catch (PartNotFoundException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
			
		}

		if(e.getSource() == openFile  ||e.getSource() == openFileB ){
			final JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(GuInterface.this);
			if(returnVal ==JFileChooser.APPROVE_OPTION){
				File file = fc.getSelectedFile();
				String partName  = file.getName();
				try {
					System.out.println("here");
					window.run();
					System.out.println(partName);
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
	}
	
		
		
	public static void main(String[] args) throws InterruptedException{
		SwingUtilities.invokeLater(new Runnable (){
			public void run(){
				GuInterface gui  = new GuInterface();

				gui.setVisible(true);


			}

		});

	}


}
