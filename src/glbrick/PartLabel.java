package glbrick;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class PartLabel extends JLabel{
	private String partName;
	private String partFile;
	private String partIcon;
	private final String ALPHA = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
	public PartLabel(String partName, String partFile, String partIcon){
		//removes ugly characters in parts bin
		if(!ALPHA.contains(String.valueOf(partName.charAt(0)))){
			partName = partName.substring(1);
		}
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