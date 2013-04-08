package glbrick;

import java.io.FileNotFoundException;

import org.lwjgl.LWJGLException;

public class GLWindowTest
{
	public static void main(String[] args) throws LWJGLException, InterruptedException, PartNotFoundException, FileNotFoundException
	{
		GLWindow window = new GLWindow();
		window.run();
	}
}
