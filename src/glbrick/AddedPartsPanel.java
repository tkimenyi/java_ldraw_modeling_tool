package glbrick;

import java.awt.Font;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class AddedPartsPanel implements ListSelectionListener{
	private JList partsList;
	private DefaultListModel partsListModel;
	private PartLabel currentPart;

	public AddedPartsPanel(){
		this.currentPart = null;
		this.partsListModel = new DefaultListModel();
		this.partsList = new JList(partsListModel);
		this.partsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.partsList.setCellRenderer(new PartCellRenderer());
		this.partsList.setFont(new Font("Serif", Font.BOLD, 12));
	}


	public String getCurrentPart(){return this.currentPart.getPartFile();}

	public JScrollPane getListPane(){
		return new JScrollPane(this.partsList);
	}
	
	public void addPart(PartLabel part){
		this.partsListModel.addElement(part);
	}

	public JList getPartsList(){return this.partsList;}


	@Override
	public void valueChanged(ListSelectionEvent e) {
		Object obj = this.partsList.getSelectedValue();
		if(obj instanceof PartLabel){
			PartLabel label = (PartLabel)obj;
			this.currentPart = label;
		}
		
	}
}