package brick;

//Disposable, hopefully low-memory pair class.
//Used primarily to keep track of a brick's previous color before selection.
public class BrickColorPair {
	private final BrickObject brick;
	private final int colorCode;
	
	public BrickColorPair(BrickObject brick){
		this.brick = brick;
		colorCode = brick.findColor();
		//System.out.println("Made new brick with color code " + colorCode);
	}
	
	//Version to use when forcefully overwriting the color.
	public BrickColorPair(BrickObject brick, int color){
		this.brick = brick;
		colorCode = color;
		//System.out.println("Made new brick with color code " + colorCode);
	}

	public BrickObject getBrick() {
		return brick;
	}

	public int getColorCode() {
		return colorCode;
	}
	
	//Hash the same way as the bricks.
	//Except if the brick is null...
	public int hashCode(){
		if(brick == null) return 0;
		return brick.hashCode();
	}
	
	//Base equality on the brick's equals method.
	//If this is just memory location, then it still works as desired.
	public boolean equals(Object other){
		if(other instanceof BrickColorPair){
			if(brick.equals(((BrickColorPair)other).getBrick())) return true;
			return false;
		} if(other instanceof BrickObject){
			//Wanted to use this for easier contains() calls... Doesn't work.
			//contains() checks the class for me. Leaving this anyway.
			if(brick.equals((BrickObject)other)) return true;
			return false;
		} else {
			return false;
		}
	}

}
