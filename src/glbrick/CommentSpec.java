package glbrick;

public class CommentSpec implements BrickSpec{

	String[] lineParts;
	public CommentSpec(String[] lineParts) 
	{
		this.lineParts = lineParts;
	}

	@Override
	public boolean isCommment()
	{
			return true;
	}

	@Override
	public DrawnObject toDrawnObject()
	{
		return new DrawnObject();
	}

}
