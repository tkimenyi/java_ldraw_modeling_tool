package glbrick;

public class PartLabel {

	private String partFile;
	private String partIcon;
	public PartLabel(String partFile, String partIcon){
		this.partFile = partFile;
		this.partIcon = partIcon;
	}

	public String getIconFile(){return this.partIcon;}

	public void setPartFile(String fileName){
		this.partFile = fileName;
	}
	public String getPartFile(){return this.partFile;}
}