package glbrick;
import java.awt.*;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;



public class GuiInterface extends JFrame{
	JMenuBar menuBar;
	JMenu file, edit,animate, view, help, modify;
	JButton add, undo, Redo, zoomIn, zoomOut, newFileB, openFileB, closeB, saveB, saveAllB;
	JToolBar toolBar;
	JLabel cameraPos, cameraDirVect, editObjParam;
	JTextField x1, y1, z1, x2, y2, z2, object, x3, y3, z3 ;
	JMenuItem newFile, viewFile, openFile, close, save, saveAll, print, exit, rename, cut, copy, selectAll, delete, rotate, translate;
	
	public GuiInterface() {

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
		x1 = new JTextField("X");
		y1 = new JTextField("Y");
		z1 = new JTextField("Z");
		x1 = new JTextField("X");
		y2 = new JTextField("Y");
		z2 = new JTextField("Z");
		x3 = new JTextField("X");
		y3 = new JTextField("Y");
		z3 = new JTextField("Z");
		
		

		
		add = new JButton(new ImageIcon("images/Add.png"));
		undo = new JButton(new ImageIcon("images/Undo.png"));
		Redo = new JButton(new ImageIcon("images/Redo.png"));
		zoomIn = new JButton(new ImageIcon("images/Zoomin.png"));
		zoomOut = new JButton(new ImageIcon("images/Zoomout.png"));
		newFileB = new JButton( new ImageIcon("images/New.png"));
		openFileB = new JButton( new ImageIcon("images/Folder.png"));
		saveB = new JButton(new ImageIcon("images/Save.png") );
		saveAllB = new JButton( new ImageIcon("images/Saveall.png") );
		
		add.setBorder(BorderFactory.createEmptyBorder());
		add.setContentAreaFilled(false);
		newFileB.setBorder(BorderFactory.createEmptyBorder());
		newFileB.setContentAreaFilled(false);
		openFileB.setBorder(BorderFactory.createEmptyBorder());
		openFileB.setContentAreaFilled(false);
		saveB.setBorder(BorderFactory.createEmptyBorder());
		saveB.setContentAreaFilled(false);
		saveAllB.setBorder(BorderFactory.createEmptyBorder());
		saveAllB.setContentAreaFilled(false);
		undo.setBorder(BorderFactory.createEmptyBorder());
		undo.setContentAreaFilled(false);
		Redo.setBorder(BorderFactory.createEmptyBorder());
		Redo.setContentAreaFilled(false);
		zoomIn.setBorder(BorderFactory.createEmptyBorder());
		zoomIn.setContentAreaFilled(false);
		zoomOut.setBorder(BorderFactory.createEmptyBorder());
		zoomOut.setContentAreaFilled(false);
		

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
		rotate = new JMenuItem("Rotate");
		translate = new JMenuItem("Translate");
		viewFile = new JMenuItem("View Parts");
		
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
		
		
		
		JPanel panel  =  new JPanel(new BorderLayout());
		getContentPane().add(panel);
		setJMenuBar(menuBar);
		add(toolBar, BorderLayout.NORTH);
		//panel.add(cameraPos);
		//panel.add(x1);
		//panel.add(y1);
		//panel.add(z1);
       	//panel.add(cameraDirVect, BorderLayout.CENTER);
		//panel.add(editObjParam, BorderLayout.CENTER);
		//panel.setLayout();
		setTitle("CAD GUI");
		setSize(600, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		viewFile.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				try {
					GLTest.main(new String[]{});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			
		});
		
	}
	
	public static void main(String[] args) throws InterruptedException{
		SwingUtilities.invokeLater(new Runnable (){
			public void run(){
				GuiInterface gui  = new GuiInterface();

				gui.setVisible(true);
				
				
			}
			
		});
			
	}
	
}
