package glbrick;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.lwjgl.LWJGLException;

public class GLWindowTest
{
	public static void main(String[] args) throws LWJGLException, InterruptedException, PartNotFoundException, MalformedLDrawException, IOException
	{
		GLWindow window = new GLWindow();
		window.run();
	}
}
