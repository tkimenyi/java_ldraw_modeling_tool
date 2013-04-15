package glbrick;

import java.awt.Font;
import java.io.File;
import javax.swing.DefaultListModel;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;


public class PartsPanel{
	private JList partsList;
	private DefaultListModel partsListModel;
	private final String partsDirectory = "ldraw/parts";
	private PartLabel currentPart;
	private final String MISSINGICON = "MISSING";

	public PartsPanel(){
		this.currentPart = null;
		this.partsListModel = new DefaultListModel();
		this.partsList = new JList(partsListModel);
		this.partsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.partsList.setCellRenderer(new PartCellRenderer());
		this.partsList.setFont(new Font("Serif", Font.BOLD, 15));
		this.loadParts();
	}

	public void loadParts(){
		File partsLocation = new File(partsDirectory);
		if(partsLocation.exists() && partsLocation.isDirectory()){
			File[] partsFiles = partsLocation.listFiles();
			for(File f : partsFiles){
				PartLabel label = new PartLabel(f.getName(), MISSINGICON);
				partsListModel.addElement(label);
			}
		}
	}

	public String getCurrentPart(){return this.currentPart.getPartFile();}

	public JScrollPane getListPane(){
		return new JScrollPane(this.partsList);
	}
	
	public JList getPartsList(){return this.partsList;}


	/*@Override
	public void valueChanged(ListSelectionEvent e) {
		Object obj = partsList.getSelectedValue();
		if(obj instanceof PartLabel){
			PartLabel label = (PartLabel)obj;
			this.currentPart = label;
		}

	}*/

	/*public static void main(String[] args) {
		JFrame frame = new JFrame("Tasks To Do");
		frame.setSize(300, 200);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		PartsPanel manager = new PartsPanel();
		Container contents = frame.getContentPane();
		contents.add(manager.getListPane(), BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);
	}*/

}