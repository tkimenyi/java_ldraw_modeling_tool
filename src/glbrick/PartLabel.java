package glbrick;

public class PartLabel {
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