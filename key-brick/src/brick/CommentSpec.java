package brick;

import com.threed.jpct.Matrix;

public class CommentSpec implements BrickSpec {
	
	private String[] line;
	
	public CommentSpec(String[] lineParts) {
		this.line = lineParts;
	}
	
	public String toString() {
		String result = "";
		for (String element: line) {
			result += element + " ";
		}
		return result;
	}

	@Override
	public BrickObject toBrickObject(Matrix transformation, BrickPanel world) {
		throw new UnsupportedOperationException("Comments build no bricks");
	}

	@Override
	public boolean isCommment() {
		return true;
	}
	
}
