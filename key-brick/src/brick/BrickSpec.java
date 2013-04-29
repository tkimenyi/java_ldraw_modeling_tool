package brick;

import com.threed.jpct.*;

public interface BrickSpec {
	public boolean isCommment();
	public String toString();
	public BrickObject toBrickObject(Matrix transformation, BrickPanel world);
}
