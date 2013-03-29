package glbrick;
import java.awt.*;

import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import org.lwjgl.LWJGLException;



public class GuInterface extends JFrame implements ActionListener{
	private GLWindow window;
	JMenuBar menuBar;
	JMenu file, edit,animate, view, help, modify;
	JButton add, undo, rotate, Redo, zoomIn, zoomOut, newFileB, openFileB, closeB, saveB, saveAllB, moveLeft, moveRight,moveUp, moveDown;
	JToolBar toolBar;
	JLabel cameraPos, cameraDirVect, editObjParam;
	JTextField up, down, left, right, y2, z2, object, x, y, z, rotateField ;
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
		up = new JTextField("up",30);
		down = new JTextField("down",30);
		left = new JTextField("left",30);
		right = new JTextField("right",30);
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




		add = new JButton(new ImageIcon("images/Add.png"));
		undo = new JButton(new ImageIcon("images/Undo.png"));
		Redo = new JButton(new ImageIcon("images/Redo.png"));
		zoomIn = new JButton(new ImageIcon("images/Zoomin.png"));
		zoomOut = new JButton(new ImageIcon("images/Zoomout.png"));
		newFileB = new JButton( new ImageIcon("images/New.png"));
		openFileB = new JButton( new ImageIcon("images/Folder.png"));
		saveB = new JButton(new ImageIcon("images/Save.png") );
		saveAllB = new JButton( new ImageIcon("images/Saveall.png") );
		moveUp =new JButton( new ImageIcon("images/moveup.png") );
		moveDown = new JButton( new ImageIcon("images/movedown.png") );
		moveLeft = new JButton( new ImageIcon("images/moveleft.png") );
		moveRight = new JButton( new ImageIcon("images/moveright.png") );
		rotate = new JButton( new ImageIcon("images/rotate.png"));



		removesomecode(add);
		removesomecode(newFileB);
		removesomecode(openFileB);
		removesomecode(saveB);
		removesomecode(saveAllB);
		removesomecode(undo);
		removesomecode(Redo);
		removesomecode(zoomIn);
		removesomecode(zoomOut);
		removesomecode(moveRight);
		removesomecode(moveLeft);
		removesomecode(moveDown);
		removesomecode(moveUp);



		newFile = new JMenuItem("New", new ImageIcon("images/New.png"));
		newFile.setMnemonic(KeyEvent.VK_B);
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
		
		file.add(newFile);
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

		toolBar.add(newFileB);
		toolBar.addSeparator();
		toolBar.add(openFileB);
		toolBar.addSeparator();
		toolBar.add(saveB);
		toolBar.addSeparator();
		toolBar.add(saveAllB);
		toolBar.addSeparator();
		toolBar.add(add);
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


		

		JPanel panel  =  new JPanel(new BorderLayout());
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
	
	public void removesomecode(JButton button) {
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setContentAreaFilled(false);
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
			GLTest.rotateModel(angle);
		}
		
		if(e.getSource() == moveRight){
			Double angle = Double.parseDouble(right.getText());
			GLTest.rotateModel(360 -angle);
		}
		
		if(e.getSource() == moveLeft){
			Double angle = Double.parseDouble(left.getText());
			GLTest.rotateModel(angle);
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
					GLTest.main(new String[]{});
					System.out.println(partName);
					GLTest.addObject(partName);
					
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
