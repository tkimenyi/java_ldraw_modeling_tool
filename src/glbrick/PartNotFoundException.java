package glbrick;

@SuppressWarnings("serial")
public class PartNotFoundException extends Exception {
	private String name;
	
	public PartNotFoundException(String partName) {
		super("Brick part '" + partName + "' not available");
		this.name = partName;
	}
	
	public String getPartName() {return name;}
}
