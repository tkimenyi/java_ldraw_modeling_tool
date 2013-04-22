package glbrick;
import java.awt.*;

import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class GuInterface implements ActionListener, ListSelectionListener{
	private GLWindow window;
	JMenuBar menuBar;
	JMenu file;
	JButton add, zoomIn, zoomOut,  saveAllB, moveLeft, moveRight,moveUp, moveDown;
	JToolBar toolBar;
	JTextField up, down, left, right;
	JMenuItem close, translate;

	JComboBox choices;
	JLabel warningLabel;
	JTextArea warnings;
	JScrollPane warningsPane;

	private PartsPanel allPartsPanel;
	private AddedPartsPanel partsBin;
	private JButton partAdderButton;
	private PartLabel currentSelectedPart;
	private JPanel partsListPanel;
	private JRadioButton translateButton, rotateButton;


	public GuInterface(GLWindow window) {
		this.window = window;
		createAllButtons();
		createPartsPanel();
		addAllActionListeners();

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
		this.partsListPanel = new JPanel();
		GridBagLayout gridLayout = new GridBagLayout();
		this.partsListPanel.setLayout(gridLayout);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.gridheight = 4;
		constraints.gridwidth = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;

		this.partsBin = new AddedPartsPanel();
		JPanel addedParts = new JPanel();
		TitledBorder addedPartsBorder = BorderFactory.createTitledBorder("Added Parts");
		addedPartsBorder.setTitleJustification(TitledBorder.LEADING);
		addedParts.setBorder(addedPartsBorder);
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


		gridLayout.setConstraints(adderPanel, constraints);
		this.partsListPanel.add(adderPanel);
		constraints.gridx = 0;
		constraints.gridy = 5;
		constraints.gridheight = 2;
		constraints.gridwidth = 1;
		constraints.weightx = 1;
		constraints.weighty = 1;
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridheight = GridBagConstraints.REMAINDER;


		gridLayout.setConstraints(addedParts, constraints);
		this.partsListPanel.add(addedParts);
	}

	public JPanel getListPanels(){return this.partsListPanel;}

	public void addAllActionListeners(){
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
		close = new JMenuItem("Close", new ImageIcon("images/exit.png"));
		close.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(1);			
			}			
		});
		file.add(close);
		menu.add(file);
		return menu;
	}

	public JPanel getControlPanel(){
		JPanel controlPanel  =  new JPanel();

		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
		moveRight.setAlignmentX(Component.LEFT_ALIGNMENT);
		controlPanel.add(moveRight);
		moveLeft.setAlignmentX(Component.LEFT_ALIGNMENT);
		controlPanel.add(moveLeft);
		moveUp.setAlignmentX(Component.LEFT_ALIGNMENT);
		controlPanel.add(moveUp);
		moveDown.setAlignmentX(Component.LEFT_ALIGNMENT);
		controlPanel.add(moveDown);
		controlPanel.add(zoomIn);
		controlPanel.add(zoomOut);
		controlPanel.setMaximumSize(controlPanel.getPreferredSize());
		return controlPanel;
	}

	public JButton createButton(String buttonName, String imgString) {
		ImageIcon icon = new ImageIcon("images/" + imgString);
		JButton tempButton = new JButton(icon);
		tempButton.setBorder(BorderFactory.createEmptyBorder());
		tempButton.setContentAreaFilled(false);
		return tempButton;
	}

	public void createAllButtons(){
		zoomIn = createButton("zoomIn","Zoomin.png");
		zoomOut = createButton("zoomOut","Zoomout.png");
		moveUp = createButton("moveUp","moveup.png");
		moveDown = createButton("moveDown","movedown.png");
		moveLeft = createButton("moveLeft","moveleft.png");
		moveRight = createButton("moveRight","moveright.png");
		translateButton = new JRadioButton("Translate");
		rotateButton = new JRadioButton("Rotate");
		ButtonGroup bgroup = new ButtonGroup();
		bgroup.add(translateButton);
		bgroup.add(rotateButton);

	}

	public JToolBar getToolBar(){
		JToolBar controlToolBar = new JToolBar();
		controlToolBar.add(zoomOut);
		controlToolBar.addSeparator();
		controlToolBar.add(zoomIn);
		controlToolBar.addSeparator();
		controlToolBar.add(moveUp);
		controlToolBar.addSeparator();
		controlToolBar.add(moveDown);
		controlToolBar.addSeparator();
		controlToolBar.add(moveLeft);
		controlToolBar.addSeparator();
		controlToolBar.add(moveRight);
		controlToolBar.add(rotateButton);
		controlToolBar.add(translateButton);
		return controlToolBar;
	}

	public void actionPerformed(ActionEvent e){
		double angle = 15;
		double distance = 2;
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
			if(rotateButton.isSelected()){
				window.rotateModel(0, angle);
			}else{
				window.translateModel(0.0, distance, 0.0);
			}
		}

		else if(e.getSource() == moveDown){
			if(rotateButton.isSelected()){
				window.rotateModel(angle,0);
			}else{
				window.translateModel(0.0, -distance, 0.0);
			}
		}

		else if(e.getSource() == moveRight){
			if(rotateButton.isSelected()){
				window.rotateModel(360-angle,0);
			}
			else{
				window.translateModel(distance, 0.0, 0.0);
			}
		}

		else if(e.getSource() == moveLeft){
			if(rotateButton.isSelected()){
				window.rotateModel(angle,0);
			}
			else{
				window.translateModel(-distance, 0.0, 0.0);
			}
		}

		if(e.getSource() == zoomIn){
			window.zoomIn();
		}
		if(e.getSource() == zoomOut){
			window.zoomOut();
		}
	}

}
