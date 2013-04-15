package glbrick;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class PartLabel extends JLabel{
	private String partName;
	private String partFile;
	private String partIcon;
	public PartLabel(String partName, String partFile, String partIcon){
		this.partFile = partFile;
		this.partIcon = partIcon;
		this.partName = partName;
	}

	public String getIconFile(){return this.partIcon;}

	public void setPartFile(String fileName){
		this.partFile = fileName;
	}
	public String getPartFile(){return this.partFile;}
	
	public String getPartName(){return this.partName;}
	
}