package glbrick;

import java.awt.Font;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragGestureRecognizer;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.DefaultListModel;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class PartsPanel implements DragGestureListener, ListSelectionListener, DragSourceListener{
	private JList partsList;
	private DefaultListModel partsListModel;
	private final String partsDirectory = "ldraw/parts";
	private PartLabel currentPart;
	private final String MISSINGICON = "MISSING";
	private DragSource dragSource;

	@SuppressWarnings("unused")
	public PartsPanel(){
		this.currentPart = null;
		this.partsListModel = new DefaultListModel();
		this.partsList = new JList(partsListModel);
		this.partsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.partsList.setCellRenderer(new PartCellRenderer());
		this.partsList.setFont(new Font("Serif", Font.BOLD, 10));
		this.partsList.setFixedCellWidth(200);
		this.dragSource = new DragSource();
		DragGestureRecognizer drg = this.dragSource.createDefaultDragGestureRecognizer(this.partsList, DnDConstants.ACTION_COPY, this);
		@SuppressWarnings("rawtypes")
		SwingWorker loader = new SwingWorker(){

			@Override
			protected Object doInBackground() throws Exception {
				loadParts();
				//loadTestParts();
				return null;
			}
			
			
		};
		loader.execute();
		
		
	}

	public void loadParts(){
		File partsLocation = new File(partsDirectory);
		if(partsLocation.exists() && partsLocation.isDirectory()){
			File[] partsFiles = partsLocation.listFiles();
			for(File f : partsFiles){
				if(!f.isDirectory()){
					String partName = f.getName();
					FileReader reader;
					BufferedReader buffer;
					try {
						reader = new FileReader(f.getAbsoluteFile());
						buffer = new BufferedReader(reader);
						String tempName = buffer.readLine();
						String[] nameComponents = tempName.split("[ \t\n\r]");
						tempName = "";
						for(String str:nameComponents){
							if(str.length() > 1){
								tempName += " " + str;
							}
						}
						tempName = tempName.trim();
						if(tempName.length() != 0){
							partName = tempName;
						}
						reader.close();
						buffer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

					PartLabel label = new PartLabel(partName, f.getName(), MISSINGICON);
					partsListModel.addElement(label);
				}
			}
		}
	}
	
	
	public void loadTestParts(){
		File partsLocation = new File(partsDirectory);
		if(partsLocation.exists() && partsLocation.isDirectory()){
			File f = new File(partsDirectory, "../models/car.dat");
					System.out.println(f.getAbsoluteFile());
					String partName = f.getName();
					FileReader reader;
					BufferedReader buffer;
					try {
						reader = new FileReader(f.getAbsoluteFile());
						buffer = new BufferedReader(reader);
						String tempName = buffer.readLine();
						String[] nameComponents = tempName.split("[ \t\n\r]");
						tempName = "";
						for(String str:nameComponents){
							if(str.length() > 1){
								tempName += " " + str;
							}
						}
						tempName = tempName.trim();
						if(tempName.length() != 0){
							partName = tempName;
						}
						reader.close();
						buffer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

					PartLabel label = new PartLabel(partName, f.getName(), MISSINGICON);
					partsListModel.addElement(label);
			
		}
	}

	public String getCurrentPart(){return this.currentPart.getPartFile();}

	public JScrollPane getListPane(){
		JScrollPane res = new JScrollPane();
		res.getViewport().setView(partsList);
		return res;
	}

	public JList getPartsList(){return this.partsList;}

	@Override
	public void dragGestureRecognized(DragGestureEvent dge) {
		Object label = this.partsList.getSelectedValue();
		if(label != null){
			this.currentPart = (PartLabel)label;
			StringSelection transferable = new StringSelection(this.currentPart.getPartFile());
//			System.out.println("current part file: " + this.currentPart.getPartFile());
			this.dragSource.startDrag(dge, DragSource.DefaultCopyDrop, transferable, this);
		}
	}


	@Override
	public void valueChanged(ListSelectionEvent e) {
		Object obj = partsList.getSelectedValue();
		if(obj instanceof PartLabel){
			PartLabel label = (PartLabel)obj;
			this.currentPart = label;
		}

	}
	
	public int getPartsCount(){return this.partsListModel.getSize();}

	@Override
	public void dragEnter(DragSourceDragEvent dsde) {}

	@Override
	public void dragOver(DragSourceDragEvent dsde) {}

	@Override
	public void dropActionChanged(DragSourceDragEvent dsde) {}

	@Override
	public void dragExit(DragSourceEvent dse) {}

	@Override
	public void dragDropEnd(DragSourceDropEvent dsde) {}

}