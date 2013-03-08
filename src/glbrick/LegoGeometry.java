package glbrick;

public interface LegoGeometry
{

	public void draw3DQuad(double[] p1, double[] p2, float[] color);
	
	public void drawCube(double[] xyz, double size, float[] color);
	
	public void drawLine(double[] p1, double[] p2, float[] color);
	public void draw2DQuad(double[] p1, double[] p2, double[] p3, double[] p4, float[] color);
	public void drawCircle(double[] center, double radius, float[] color );

	
		
	
}
